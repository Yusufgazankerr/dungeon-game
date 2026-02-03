package com.dungeonGame.logic;

import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;

/**
 * Manages the player's position within the dungeon.
 * <p>
 * This class tracks the player's current coordinates and ensures that any movement updates
 * stay within valid bounds.
 * </p>
 */
public class PositionDataHolder {

   /**
    * The x-coordinate of the player's current position.
    */
   private int playerX;

   /**
    * The y-coordinate of the player's current position.
    */
   private int playerY;

   /**
    * Initializes the player's position manager with the given level layout.
    *
    * <p>
    * <b>Default Behavior:</b> The player's position is not explicitly set during initialization.
    * Position updates must be handled externally after creating this object.
    * </p>
    *
    * @param initialLevel the layout of the level where the player starts
    */
   public PositionDataHolder(char[][] initialLevel) {
      DungeonMap.currentLevel = initialLevel;
      DungeonMap.currentLevelNumber = 1;
   }

   // Getters

   public int getPlayerX() {
      return playerX;
   }
   public int getPlayerY() {
      return playerY;
   }

   /**
    * Updates the player's position on the dungeon map.
    *
    * <ul>
    *     <li><b>Valid Conditions:</b>
    *         <ul>
    *             <li>The coordinates must be within the boundaries of the map.</li>
    *             <li>The target cell must not be a wall ('W').</li>
    *         </ul>
    *     </li>
    *     <li><b>Consequences:</b> Throws an {@link IllegalArgumentException} if the move is invalid.</li>
    * </ul>
    *
    * @param x the new x-coordinate
    * @param y the new y-coordinate
    * @throws IllegalArgumentException if the coordinates are out of bounds or target a wall
    */
   public void setCurrentPosition(int x, int y) {
      if (x < 0 || x >= DungeonMap.currentLevel.length || y < 0 || y >= DungeonMap.currentLevel[0].length) {
         throw new IllegalArgumentException("Invalid position: The coordinates are outside the map boundaries.");
      }
      if (DungeonMap.currentLevel[x][y] == 'W') {
         throw new IllegalArgumentException("Invalid position: You cannot set the position to a wall ('W').");
      }
      this.playerX = x;
      this.playerY = y;
   }

}
