package com.dungeonGame;

import com.dungeonGame.database.UserDAO;
import com.dungeonGame.logic.InventoryManager;
import com.dungeonGame.logic.PositionDataHolder;
import com.dungeonGame.logic.MovementLogic;
import com.dungeonGame.logic.PlayerDataHolder;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;
import com.dungeonGame.logic.mapAndLevelHandler.LevelTransitionHandler;

import java.util.Scanner;
import java.util.Set;

public class Main {

   /**
    * The entry point of the Dungeon Game application.
    *
    * <ul>
    *     <li><b>Initial Setup:</b>
    *         <ul>
    *             <li>Creates the users table in the database if it does not exist.</li>
    *             <li>Prompts the user for their name and loads existing player data if available.</li>
    *             <li>Initializes a new game if no player data exists.</li>
    *         </ul>
    *     </li>
    *     <li><b>Gameplay:</b>
    *         <ul>
    *             <li>Starts the main game loop where the player can interact with the dungeon.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <p><b>Internal Method Calls:</b></p>
    * <ul>
    *     <li>{@link UserDAO#createNewTable()}</li>
    *     <li>{@link UserDAO#getUserByName(String)}</li>
    *     <li>{@link LevelTransitionHandler#initializeGame(char[][])}</li>
    * </ul>
    */
   public static void main(String[] args) {

      DungeonMap.initialize();

      GameLoop gameLoop = new GameLoop();
      LevelTransitionHandler levelTransitionHandler = new LevelTransitionHandler();
      UserDAO userDAO = new UserDAO();

      Scanner scanner = new Scanner(System.in);

      // Create users table
      userDAO.createNewTable();

      // Ask the user for their name
      System.out.print("Enter your name: ");
      String playerName = scanner.nextLine();

      // Load user data if exists, otherwise initialize new game
      PlayerDataHolder player = userDAO.getUserByName(playerName);
      PositionDataHolder pm;
      if (player == null) {
         Object[] initResults = levelTransitionHandler.initializeGame(DungeonMap.getLevel(1));
         player = (PlayerDataHolder) initResults[0];
         pm = (PositionDataHolder) initResults[1];
         player.setName(playerName);
         userDAO.insertUser(player.getName(), DungeonMap.getCurrentLevelNumber(), player.getPowerPoints(), player.getCurrentRoom(), convertSetToString(player.getInventory()));
      } else {
         int currentLevelNumber = DungeonMap.getCurrentLevelNumber();
         char[][] currentLevel = levelTransitionHandler.getLevelByNumber(currentLevelNumber);
         pm = new PositionDataHolder(currentLevel);
         // Set a valid starting position
         pm.setCurrentPosition(1, 1); // Example starting position, adjust as needed
      }

      InventoryManager inventoryManager = new InventoryManager();
      gameLoop.runGameLoop(player, pm, inventoryManager);
   }

   /**
    * Converts a set of strings into a comma-separated string.
    *
    * <ul>
    *     <li><b>Usage:</b> This method is used to serialize the player's inventory for database storage.</li>
    * </ul>
    *
    * @param set the set of strings to convert
    * @return a comma-separated string representing the set contents
    */
   private static String convertSetToString(Set<String> set) {
      return String.join(",", set);
   }

   /**
    * Inner class responsible for running the main game loop.
    */
   public static class GameLoop {


      MovementLogic movementLogic = new MovementLogic();
      LevelTransitionHandler levelTransitionHandler = new LevelTransitionHandler();
      UserDAO userDAO = new UserDAO();

      /**
       * Executes the main game loop where the player can interact with the dungeon.
       *
       * <ul>
       *     <li><b>Options:</b> The player can:
       *         <ul>
       *             <li>Move in different directions (up, down, left, right).</li>
       *             <li>Search the surroundings or open their inventory.</li>
       *             <li>Exit the game, saving progress.</li>
       *         </ul>
       *     </li>
       *     <li><b>Consequences:</b>
       *         <ul>
       *             <li>Invalid moves display an error message.</li>
       *             <li>Encounters and level transitions are handled as the player progresses.</li>
       *         </ul>
       *     </li>
       * </ul>
       *
       * <b>Internal Method Calls:</b>
       * <ul>
       *     <li>{@link MovementLogic#handleMovement(PositionDataHolder, PlayerDataHolder, String, com.dungeonGame.logic.InventoryManager)}</li>
       *     <li>{@link LevelTransitionHandler#isOnExit(PositionDataHolder)}</li>
       *     <li>{@link UserDAO#updateUser(String, int, int, String, String)}</li>
       *     <li>{@link LevelTransitionHandler#handleGameOver()}</li>
       * </ul>
       *
       * @param player  the player's data, including inventory and power points
       * @param pm      the player's position manager
       */
      private void runGameLoop(PlayerDataHolder player, PositionDataHolder pm, InventoryManager inventoryManager) {
         Scanner scanner = new Scanner(System.in);
         boolean gameRunning = true;

         while (gameRunning && player.isAlive()) {
            GameUtils.delayPrint("\nEnter your move(up, down, left, right, look around, inventory, exit): ");
            String userInput = scanner.nextLine().toLowerCase();

            GameUtils.clearConsole();

            if (userInput.equals("exit")) {
               userDAO.updateUser(player.getName(), DungeonMap.getCurrentLevelNumber(), player.getPowerPoints(), player.getCurrentRoom(), convertSetToString(player.getInventory()));
               System.out.println("Game saved. Exiting...");
               gameRunning = false;
               continue;
            }

            if (movementLogic.handleMovement(pm, player, userInput, inventoryManager)) {
               if (levelTransitionHandler.isOnExit(pm)) {
                  gameRunning = levelTransitionHandler.handleLevelTransition(player, pm);
               }
            }

            if (!player.isAlive()) {
               LevelTransitionHandler.handleGameOver();
               gameRunning = false;
            }
         }

         GameUtils.delayPrint("Thank you for playing the Dungeon Game!\n");
         scanner.close();
      }

   }

}