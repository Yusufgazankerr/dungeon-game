package com.dungeonGame.logic.mapAndLevelHandler;

import com.dungeonGame.encounter.EncounterManager;
import com.dungeonGame.logic.PositionDataHolder;

/**
 * Manages the dungeon map and its levels in the Dungeon Game.
 * <p>
 * The dungeon consists of three levels, each represented as a 2D character array.
 * The class provides methods to retrieve specific levels and initialize the map layout.
 * </p>
 */
public class DungeonMap {

   static EncounterManager encounterManager = new EncounterManager();

   /**
    * Array containing the layouts for all dungeon levels.
    * Each level is a 2D character array.
    * <ul>
    *     <li>{@code 'W'} - Wall</li>
    *     <li>{@code ' '} - Walkable room</li>
    *     <li>{@code 'E'} - Entrance</li>
    *     <li>{@code 'X'} - Exit</li>
    * </ul>
    */
   public static char[][][] levels;

   /**
    * Tracks the number of the currently active level.
    * Level numbering starts at 1.
    */
   public static int currentLevelNumber;

   /**
    * Stores the map of the currently active level.
    */
   public static char[][] currentLevel;


   /**
    * Private constructor to prevent instantiation of the {@code DungeonMap} utility class.
    *
    * <ul>
    *     <li><b>Purpose:</b> Ensures that the class is used only as a utility and cannot be instantiated.</li>
    *     <li><b>Behavior:</b> Throws an {@link UnsupportedOperationException} if instantiation is attempted.</li>
    * </ul>
    *
    * @throws UnsupportedOperationException if an attempt is made to instantiate this utility class
    */
   public DungeonMap() {
      // Prevent instantiation
      throw new UnsupportedOperationException("Utility class DungeonMap cannot be instantiated.");
   }

   /**
    * Initializes the dungeon map by creating layouts for all levels.
    * The layouts are generated using the {@link LevelFactory} inner class.
    */
   public static void initialize() {
      // Initialize the dungeon levels using LevelFactory
      levels = new char[3][][];
      levels[0] = LevelFactory.createLevel1();
      levels[1] = LevelFactory.createLevel2();
      levels[2] = LevelFactory.createLevel3();

      currentLevel = levels[0];
      currentLevelNumber = 1;
      encounterManager.assignEncounters(currentLevel);

   }

   /**
    * Retrieves the layout of a specified dungeon level.
    *
    * <ul>
    *     <li><b>Valid Input:</b> Levels 1 through 3.</li>
    *     <li><b>Error Handling:</b> Throws {@link IllegalArgumentException} for invalid level numbers.</li>
    * </ul>
    *
    * @param levelNumber the number of the level to retrieve
    * @return the 2D character array representing the level's layout
    * @throws IllegalArgumentException if the level number is not in the range 1 to 3
    */
   public static char[][] getLevel(int levelNumber) {
      // Retrieve a specific level
      if (levelNumber < 1 || levelNumber > 3) {
         throw new IllegalArgumentException("Invalid level number. Levels range from 1 to 3.");
      }
      return levels[levelNumber - 1];
   }

   /**
    * Retrieves the layout of the current level.
    *
    * @return the 2D character array representing the current level's layout
    */
   public static char[][] getCurrentLevel() { return currentLevel; }

   /**
    * Sets the current level to a new layout and level number.
    * <p>
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link EncounterManager#assignEncounters(char[][])}</li>
    * </ul>
    *  @param pm the player's position manager
    *
    * @param newLevel               the new level's layout
    * @param levelNumber            the number of the new level
    */
   public static void setCurrentLevel(PositionDataHolder pm, char[][] newLevel, int levelNumber, LevelTransitionHandler levelTransitionHandler) {
      currentLevel = newLevel;
      currentLevelNumber = levelNumber;
      levelTransitionHandler.setCurrentLevelDirectly(newLevel, levelNumber);
      System.out.println("Initializing Level: " + levelNumber);

      // Initialize player position after setting the new level
      levelTransitionHandler.initializePlayerPosition(pm);

      if (levelNumber > 1) {
         encounterManager.assignEncounters(newLevel);
      }
   }

   /**
    * Retrieves the number of the current level.
    *
    * @return the number of the current level
    */
   public static int getCurrentLevelNumber() { return currentLevelNumber; }

   /**
    * Inner class responsible for creating the layouts of each dungeon level.
    *
    * <p>Each level is represented as a 2D character array. This class
    * provides methods to define the static layout for each level.</p>
    */
   public static class LevelFactory {

      /**
       * Creates the layout for Level 1.
       * <ul>
       *     <li>{@code 'E'} - Entrance in the bottom-left corner.</li>
       *     <li>{@code 'X'} - Exit in the top-right corner.</li>
       *     <li>{@code 'W'} - Walls surrounding and dividing rooms.</li>
       * </ul>
       *
       * @return a 2D character array representing Level 1
       */
      public static char[][] createLevel1() {
         return new char[][]{
                 {'W', 'W', 'W', 'W', 'W'},
                 {'W', ' ', ' ', ' ', 'W'},
                 {'W', ' ', 'W', ' ', 'W'},
                 {'W', ' ', 'W', 'X', 'W'},
                 {'W', 'E', 'W', 'W', 'W'}
         };
      }

      /**
       * Creates the layout for Level 2.
       * <ul>
       *     <li>{@code 'E'} - Entrance in the top-left corner.</li>
       *     <li>{@code 'X'} - Exit in the top-right corner.</li>
       *     <li>{@code 'W'} - Walls dividing rooms.</li>
       * </ul>
       *
       * @return a 2D character array representing Level 2
       */
      public static char[][] createLevel2() {
         return new char[][]{
                 {'W', 'W', 'W', 'W', 'W'},
                 {'W', 'E', ' ', ' ', 'W'},
                 {'W', ' ', 'W', ' ', 'W'},
                 {'W', ' ', ' ', ' ', 'X'},
                 {'W', 'W', ' ', ' ', 'W'}
         };
      }

      /**
       * Creates the layout for Level 3.
       * <ul>
       *     <li>{@code 'E'} - Entrance near the bottom-center corner.</li>
       *     <li>{@code 'X'} - Exit in the top-right corner.</li>
       *     <li>{@code 'W'} - Walls forming challenging paths.</li>
       * </ul>
       *
       * @return a 2D character array representing Level 3
       */
      public static char[][] createLevel3() {
         return new char[][]{
                 {'W', 'W', ' ', ' ', 'X'},
                 {'W', ' ', ' ', 'W', ' '},
                 {'W', ' ', ' ', ' ', ' '},
                 {'W', ' ', 'E', 'W', 'W'},
                 {'W', 'W', 'W', 'W', 'W'}
         };
      }

   }

}
