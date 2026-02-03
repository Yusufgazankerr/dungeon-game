package com.dungeonGame.logic;

import com.dungeonGame.GameUtils;
import com.dungeonGame.encounter.EncounterManager;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;

/**
 * Handles player movement within the dungeon. Processes user commands to move
 * the player, checks for valid moves, and updates the game state accordingly.
 */
public class MovementLogic {

   PowerPointManager pointManager = new PowerPointManager();
   SearchActions searchActions = new SearchActions();
   DisplayManager displayManager = new DisplayManager();
   EncounterManager encounterManager = new EncounterManager();
   //EncounterManager.EncounterState encounterState = encounterManager.getEncounterState();

   /**
    * Processes the player's movement input and updates their position if the move is valid.
    *
    * <ul>
    *     <li><b>Options:</b>
    *         <ul>
    *             <li>{@code "up"}, {@code "move up"}, {@code "go up"} - Move up.</li>
    *             <li>{@code "down"}, {@code "move down"}, {@code "go down"} - Move down.</li>
    *             <li>{@code "left"}, {@code "move left"}, {@code "go left"} - Move left.</li>
    *             <li>{@code "right"}, {@code "move right"}, {@code "go right"} - Move right.</li>
    *             <li>{@code "look around"}, {@code "search"}, {@code "observe"} - Look around the room.</li>
    *             <li>{@code "inventory"}, {@code "bag"}, {@code "open inventory"} - Open the inventory.</li>
    *         </ul>
    *     </li>
    *     <li><b>Consequences:</b>
    *         <ul>
    *             <li>If the move is invalid, a message is displayed, and no changes are made.</li>
    *             <li>If valid, updates the player's position and deducts 5 power points.</li>
    *             <li>Triggers encounters if the player moves into an encounter room.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link GameUtils#clearConsole()}</li>
    *     <li>{@link SearchActions#lookAround(PositionDataHolder, PlayerDataHolder)}</li>
    *     <li>{@link DisplayManager#displayCurrentRoom(PositionDataHolder, PlayerDataHolder)}</li>
    *     <li>{@link DisplayManager#displayInventory(PlayerDataHolder, PositionDataHolder)}</li>
    *     <li>{@link #isValidMove(int, int)}</li>
    *     <li>{@link PowerPointManager#deductPowerPoints(PlayerDataHolder, int)}</li>
    *     <li>{@link EncounterManager#checkForEncounters(PositionDataHolder, PlayerDataHolder, InventoryManager)}</li>
    * </ul>
    *
    * @param pm        the player's position manager
    * @param player    the player's data, including inventory and power points
    * @param userInput the player's movement command
    * @return {@code true} if the movement was valid, {@code false} otherwise
    */
   public boolean handleMovement(PositionDataHolder pm, PlayerDataHolder player, String userInput, InventoryManager inventoryManager) {

      int newX = pm.getPlayerX();
      int newY = pm.getPlayerY();

      switch (userInput.toLowerCase()) {
         case "up":
         case "move up":
         case "move forward":
         case "forward":
         case "go up":
         case "upwards":
         case "move upwards":
            newX--;
            break;

         case "down":
         case "go down":
         case "below":
         case "go below":
         case "move below":
         case "behind":
         case "go behind":
            newX++;
            break;

         case "left":
         case "go left":
         case "move left":
            newY--;
            break;

         case "right":
         case "go right":
         case "move right":
            newY++;
            break;

         case "look around":
         case "search":
         case "around":
         case "look":
         case "observe":
            GameUtils.clearConsole();
            searchActions.lookAround(pm, player);
            displayManager.displayCurrentRoom(pm, player);
            return false;

         case "look inventory":
         case "inventory":
         case "bag":
         case "open inventory":
         case "open bag":
            GameUtils.clearConsole();
            displayManager.displayInventory(player, pm);
            displayManager.displayCurrentRoom(pm, player);
            return false;

         default:
            GameUtils.delayPrint("Invalid input!");
            return false;
      }

      if (!isValidMove(newX, newY)) {
         return false;
      }

      pm.setCurrentPosition(newX, newY);
      GameUtils.delayPrint("Player moved " + userInput + ".");
      pointManager.deductPowerPoints(player, 3);
      encounterManager.checkForEncounters(pm, player, inventoryManager);
      displayManager.displayCurrentRoom(pm, player);
      return true;
   }

   /**
    * Checks whether a move is valid based on the current dungeon layout.
    *
    * <ul>
    *     <li><b>Valid Moves:</b> The target cell must be within the map bounds and not a wall ('W').</li>
    * </ul>
    *
    * <p><b>Consequences:</b>
    * <ul>
    *     <li>If the move is outside the map, a warning is displayed.</li>
    *     <li>If the move targets a wall, a warning is displayed.</li>
    * </ul></p>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link DungeonMap#getCurrentLevel()}</li>
    *     <li>{@link GameUtils#delayPrint(String)}</li>
    * </ul>
    *
    * @param newX the target x-coordinate
    * @param newY the target y-coordinate
    * @return {@code true} if the move is valid, {@code false} otherwise
    */
   private boolean isValidMove(int newX, int newY) {
      char[][] level = DungeonMap.getCurrentLevel();
      if (newX < 0 || newX >= level.length || newY < 0 || newY >= level[0].length) {
         GameUtils.delayPrint("You cannot move outside the map!");
         return false;
      }
      if (level[newX][newY] == 'W') {
         GameUtils.delayPrint("You cannot move through a wall!");
         return false;
      }
      return true;
   }

}
