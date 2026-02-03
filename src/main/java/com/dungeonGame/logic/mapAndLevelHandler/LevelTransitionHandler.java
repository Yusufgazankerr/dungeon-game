package com.dungeonGame.logic.mapAndLevelHandler;

import com.dungeonGame.GameUtils;
import com.dungeonGame.encounter.EncounterManager;
import com.dungeonGame.logic.*;

/**
 * Handles transitions between dungeon levels and manages level-related game states.
 *<p>
 * This class provides methods to initialize levels, transition between them,
 * check exit conditions, and handle game-over scenarios.
 * </p>
 */
public class LevelTransitionHandler {

   DisplayManager displayManager = new DisplayManager();
   PlayerDataHolder player = new PlayerDataHolder();
   //EncounterManager encounterManager = new EncounterManager();

   /**
    * Checks if the player is on an exit tile.
    *
    * @param pm the player's position manager
    * @return {@code true} if the player is on an exit tile, {@code false} otherwise
    */
   public boolean isOnExit(PositionDataHolder pm) {
      return DungeonMap.currentLevel[pm.getPlayerX()][pm.getPlayerY()] == 'X';
   }

   /**
    * Handles the transition to the next level when the player reaches an exit.
    *
    * <ul>
    *     <li><b>Success Condition:</b> The player reaches the next level or completes the game if on the final level.</li>
    *     <li><b>Failure Condition:</b> None, as this method assumes, the player has successfully reached the exit.</li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link DungeonMap#setCurrentLevel(PositionDataHolder, char[][], int, LevelTransitionHandler)}</li>
    *     <li>{@link GameUtils#clearConsole()}</li>
    *     <li>{@link GameUtils#printHeader(String)}</li>
    *     <li>{@link DisplayManager#displayCurrentRoom(PositionDataHolder, PlayerDataHolder)}</li>
    * </ul>
    *
    * @param player the player's data
    * @param pm the player's position manager
    * @return {@code true} if the game continues, {@code false} if the game ends
    */
   public boolean handleLevelTransition(PlayerDataHolder player, PositionDataHolder pm) {
      GameUtils.printHeader("Level Exit");
      GameUtils.delayPrint("You found the exit! Moving to the next level...\n");

      int nextLevel = findNextLevel(DungeonMap.currentLevel);
      if (nextLevel == -1) {
         GameUtils.delayPrint("Congratulations! You have completed the game!\n");
         return false; // End the game
      } else {
         char[][] newLevel = DungeonMap.getLevel(nextLevel);
         DungeonMap.setCurrentLevel(pm, newLevel, nextLevel, this);

         GameUtils.clearConsole();
         GameUtils.printHeader("Welcome to Level " + nextLevel);
         displayManager.displayCurrentRoom(pm, player);
         return true; // Continue the game
      }
   }

   /**
    * Handles the game-over scenario when the player's power points drop to zero.
    * <ul>
    *     <li><b>Consequence:</b> The game ends, and the player is presented with a game-over message.</li>
    * </ul>
    */
   public static void handleGameOver() {
      GameUtils.printHeader("Game Over");
      GameUtils.delayPrint("You have run out of power points. Better luck next time!\n");
   }

   /**
    * Determines the next level to transition to based on the current level.
    *
    * @param currentLevel the current level's layout
    * @return the number of the next level, or {@code -1} if there is no next level
    */
   private int findNextLevel(char[][] currentLevel) {
      if (currentLevel == DungeonMap.getLevel(1)) {
         return 2;
      } else if (currentLevel == DungeonMap.getLevel(2)) {
         return 3;
      }
      return -1; // No next level
   }

   /**
    * Initializes the player's position to the entrance ('E') of the current level.
    *
    * @param pm the player's position manager
    * @throws IllegalStateException if no entrance ('E') is found in the level
    */
   public void initializePlayerPosition(PositionDataHolder pm) {
      for (int i = 0; i < DungeonMap.currentLevel.length; i++) {
         for (int j = 0; j < DungeonMap.currentLevel[i].length; j++) {
            if (DungeonMap.currentLevel[i][j] == 'E') {
               pm.setCurrentPosition(i, j);
               return;
            }
         }
      }
      throw new IllegalStateException("Entrance ('E') not found in the provided level.");
   }

   /**
    * Initializes the game by setting up the first level and assigning encounters.
    *
    * <ul>
    *     <li><b>Initial State:</b> Player starts with 100 power points at the entrance of Level 1.</li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link DungeonMap#setCurrentLevel(PositionDataHolder, char[][], int, LevelTransitionHandler)}</li>
    *     <li>{@link #initializePlayerPosition(PositionDataHolder)}</li>
    *     <li>{@link EncounterManager#assignEncounters(char[][])}</li>
    * </ul>
    *
    * @param initialLevel the layout of the first level
    * @return an array containing the initialized {@link PlayerDataHolder} and {@link PositionDataHolder}
    */
   public Object[] initializeGame(char[][] initialLevel) {

      PositionDataHolder pm = new PositionDataHolder(initialLevel);

      DungeonMap.setCurrentLevel(pm, initialLevel, 1, this);
      initializePlayerPosition(pm);
      //encounterManager.assignEncounters(initialLevel);

      GameUtils.clearConsole();
      GameUtils.printHeader("Welcome to the Dungeon Game!");
      GameUtils.delayPrint("You start at the entrance of Level 1 with 100 power points");
      displayManager.displayCurrentRoom(pm, player);

      return new Object[]{player, pm};
   }

   /**
    * Retrieves the layout of a level based on its number.
    *
    * <ul>
    *     <li><b>Valid Input:</b> Levels 1 to 3.</li>
    *     <li><b>Error Handling:</b> Throws {@link IllegalArgumentException} for invalid level numbers.</li>
    * </ul>
    *
    * @param levelNumber the number of the level to retrieve
    * @return the layout of the specified level
    * @throws IllegalArgumentException if the level number is invalid
    */
   public char[][] getLevelByNumber(int levelNumber) {
      if (levelNumber < 1 || levelNumber > 3) {
         throw new IllegalArgumentException("Invalid level number. Levels range from 1 to 3.");
      }
      return DungeonMap.getLevel(levelNumber);
   }

   /**
    * Directly sets the current level's layout and level number.
    *
    * <p>This method bypasses initialization logic such as setting the player's
    * position or assigning encounters. It is intended for cases where the
    * level state needs to be set manually without triggering other level-related
    * processes.</p>
    *
    * @param newLevel the layout of the new level as a 2D character array
    * @param levelNumber the number of the new level
    */
   public void setCurrentLevelDirectly(char[][] newLevel, int levelNumber) {
      DungeonMap.currentLevel = newLevel;
      DungeonMap.currentLevelNumber = levelNumber;
   }
}