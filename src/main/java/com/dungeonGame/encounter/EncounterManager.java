package com.dungeonGame.encounter;

import com.dungeonGame.logic.InventoryManager;
import com.dungeonGame.logic.PositionDataHolder;
import com.dungeonGame.logic.PlayerDataHolder;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;

import java.util.Random;

public class EncounterManager {

   private final Encounters.LostExplorerEncounterManager lostExplorerEncounterManager = new Encounters.LostExplorerEncounterManager();

   /**
    * Manages the state of encounters in the dungeon.
    *
    * <p>The {@link EncounterState} object tracks the following:</p>
    * <ul>
    *     <li>Coordinates for encounters such as traps, Mad Scientists, Lost Explorers, and Guardians.</li>
    *     <li>Status flags indicating whether an encounter has occurred or been completed.</li>
    * </ul>
    *
    * <p>This static instance is shared across all levels to ensure consistent tracking of encounter states.</p>
    */
   private static final EncounterState encounterState = new EncounterState();

   /**
    * Retrieves the current state of encounters in the dungeon.
    *
    * <p>The {@link EncounterState} object provides details about:</p>
    * <ul>
    *     <li>The coordinates of active encounters such as traps, Mad Scientists, Lost Explorers, and Guardians.</li>
    *     <li>The status flags indicating whether specific encounters have occurred or been completed.</li>
    * </ul>
    *
    * @return the {@link EncounterState} object containing the current encounter state
    */
   public EncounterState getEncounterState() {
      return encounterState;
   }

   /**
    * Assigns encounters to specific rooms in the current dungeon level.
    *
    * <ul>
    *     <li><b>Encounters Assigned:</b>
    *         <ul>
    *             <li>Trap: Always assigned.</li>
    *             <li>Mad Scientist: Always assigned.</li>
    *             <li>Lost Explorer: Assigned only in Level 2.</li>
    *             <li>Guardian: Assigned only in Level 3.</li>
    *         </ul>
    *     </li>
    *     <li><b>Assignment Rules:</b>
    *         <ul>
    *             <li>Encounters cannot overlap with each other or non-walkable areas like walls ('W').</li>
    *             <li>The Guardian is placed in a walkable room (' ') in Level 3.</li>
    *         </ul>
    *     </li>
    *     <li><b>Reset States:</b>
    *         <ul>
    *             <li>Resets encounter states (e.g., trap encountered, Mad Scientist encountered).</li>
    *             <li>Resets the Guardian's completion state.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link #getValidEncounterRoom(char[][], Random)}</li>
    *     <li>{@link DungeonMap#getCurrentLevelNumber()}</li>
    *     <li>{@link EncounterState#setTrapRoom(int, int)}</li>
    *     <li>{@link EncounterState#setMadScientistRoom(int, int)}</li>
    *     <li>{@link EncounterState#setExplorerRoom(int, int)}</li>
    *     <li>{@link EncounterState#setGuardianRoom(int, int)}</li>
    * </ul>
    *
    * @param level the current dungeon level represented as a 2D character array
    */
   public void assignEncounters(char[][] level) {
      Random random = new Random();
      int[] trap = getValidEncounterRoom(level, random);
      encounterState.setTrapRoom(trap[0], trap[1]);

      int[] madScientist;
      do {
         madScientist = getValidEncounterRoom(level, random);
      } while (madScientist[0] == encounterState.getTrapRoomX() && madScientist[1] == encounterState.getTrapRoomY());
      encounterState.setMadScientistRoom(madScientist[0], madScientist[1]);

      // Debugging
      //System.out.println("Final Trap Room: (" + encounterState.getTrapRoomX() + ", " + encounterState.getTrapRoomY() + ")");
      //System.out.println("Final Mad Scientist Room: (" + encounterState.getMadScientistRoomX() + ", " + encounterState.getMadScientistRoomY() + ")");

      if (DungeonMap.currentLevel != null && DungeonMap.getCurrentLevelNumber() == 2) {
         int[] explorer;
         do {
            explorer = getValidEncounterRoom(level, random);
         } while ((explorer[0] == encounterState.getTrapRoomX() && explorer[1] == encounterState.getTrapRoomY()) ||
                 (explorer[0] == encounterState.getMadScientistRoomX() && explorer[1] == encounterState.getMadScientistRoomY()));
         encounterState.setExplorerRoom(explorer[0], explorer[1]);
      }

      if (DungeonMap.getCurrentLevelNumber() == 3) {
         int[] guardian;
         do {
            guardian = getValidEncounterRoom(level, random);
         } while ((guardian[0] == encounterState.getTrapRoomX() && guardian[1] == encounterState.getTrapRoomY()) ||
                 (guardian[0] == encounterState.getMadScientistRoomX() && guardian[1] == encounterState.getMadScientistRoomY()) ||
                 level[guardian[0]][guardian[1]] != ' '); // Ensure the room is walkable
         encounterState.setGuardianRoom(guardian[0], guardian[1]);
         //System.out.println("Assigned Guardian Room: (" + guardian[0] + ", " + guardian[1] + ")");
      }


      encounterState.setTrapEncountered(false);
      encounterState.setMadScientistEncountered(false);
      if (DungeonMap.currentLevel != null && DungeonMap.getCurrentLevelNumber() == 2) {
         encounterState.setExplorerEncountered(false);
      }
      EncounterState.setGuardianEncounterCompleted(false);
   }

   /**
    * Finds a valid room for placing an encounter within the current dungeon level.
    *
    * <ul>
    *     <li><b>Valid Room Conditions:</b>
    *         <ul>
    *             <li>The room must be within the boundaries of the map.</li>
    *             <li>The room must not be a wall ('W').</li>
    *             <li>The room must not be an entrance ('E') or an exit ('X').</li>
    *         </ul>
    *     </li>
    *     <li><b>Constraints:</b>
    *         <ul>
    *             <li>Limits the number of attempts to find a valid room to avoid infinite loops.</li>
    *             <li>Throws an {@link IllegalStateException} if no valid room is found after 100 attempts.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link #isValidEncounterRoom(char[][], int, int)}</li>
    * </ul>
    *
    * @param level the current dungeon level represented as a 2D character array
    * @param random a {@link Random} instance used to generate random positions
    * @return an array containing the x and y coordinates of a valid room
    * @throws IllegalStateException if no valid room is found after 100 attempts
    */
   private int[] getValidEncounterRoom(char[][] level, Random random) {
      int attempts = 0; // Limit attempts to avoid infinite loops
      int x, y;

      do {
         x = random.nextInt(level.length);
         y = random.nextInt(level[0].length);
         attempts++;
         //System.out.println("Checking Guardian Room (" + x + ", " + y + "): " + level[x][y] + " -> " + (isValidEncounterRoom(level, x, y) ? "VALID" : "INVALID"));
         if (attempts > 100) {
            throw new IllegalStateException("Failed to find a valid room for Guardian after 100 attempts.");
         }
      } while (!isValidEncounterRoom(level, x, y));

      return new int[]{x, y};
   }

   /**
    * Determines if a specific room in the dungeon is valid for placing an encounter.
    *
    * <ul>
    *     <li><b>Valid Room Conditions:</b>
    *         <ul>
    *             <li>The room must not be a wall ('W').</li>
    *             <li>The room must not be an entrance ('E').</li>
    *             <li>The room must not be an exit ('X').</li>
    *         </ul>
    *     </li>
    *     <li><b>Usage:</b> Used by the {@link #getValidEncounterRoom(char[][], Random)} method to validate candidate rooms.</li>
    * </ul>
    *
    * @param level the current dungeon level represented as a 2D character array
    * @param x the x-coordinate of the room
    * @param y the y-coordinate of the room
    * @return {@code true} if the room is valid for placing an encounter, {@code false} otherwise
    */
   private boolean isValidEncounterRoom(char[][] level, int x, int y) {
      //System.out.println("Checking room (" + x + ", " + y + "): " + level[x][y] + " -> " + (isValid ? "VALID" : "INVALID"));
      return level[x][y] != 'W' && level[x][y] != 'X' && level[x][y] != 'E';
   }

   /**
    * Checks whether the player has entered a room with an encounter
    * and triggers the appropriate encounter.
    *
    * @param pm the player's position manager
    * @param player the player's data holder
    */
   public void checkForEncounters(PositionDataHolder pm, PlayerDataHolder player, InventoryManager inventoryManager) {

      // Trap encounter once per level
      if (!encounterState.isTrapEncountered() &&
              pm.getPlayerX() == encounterState.getTrapRoomX() && pm.getPlayerY() == encounterState.getTrapRoomY()) {
         System.out.println("Triggering Trap Encounter");
         Encounters.trap(player);
         encounterState.setTrapEncountered(true);
      }

      // Mad Scientist encounter once per level
      if (!encounterState.isMadScientistEncountered() &&
              pm.getPlayerX() == encounterState.getMadScientistRoomX() && pm.getPlayerY() == encounterState.getMadScientistRoomY()) {
         Encounters.madScientist(player);
         encounterState.setMadScientistEncountered(true);
      }

      // Lost Explorer only once in level 2
      if (DungeonMap.getCurrentLevelNumber() == 2 &&
              !encounterState.isExplorerEncountered() &&
              pm.getPlayerX() == encounterState.getExplorerRoomX() && pm.getPlayerY() == encounterState.getExplorerRoomY()) {
         lostExplorerEncounterManager.lostExplorerEncounter(player);
         encounterState.setExplorerEncountered(true);
      }

      // Guardian encounter in level 3:
      if (DungeonMap.getCurrentLevelNumber() == 3 &&
              !encounterState.isGuardianEncounterCompleted() && // If completed, no more Guardian
              pm.getPlayerX() == encounterState.getGuardianRoomX() && pm.getPlayerY() == encounterState.getGuardianRoomY()) {
         // Guardian can happen multiple times if a player chooses to flee
         // Only if a player wins (Encounters code sets pm.setGuardianEncounterCompleted(true)) does it stop.
         Encounters.GuardianEncounterManager.guardianEncounter(player, inventoryManager);
         // If guardianEncounterCompleted is set to true inside guardianEncounter, next time won't trigger.
      }
      System.out.println("Player position: (" + pm.getPlayerX() + ", " + pm.getPlayerY() + ")");
      System.out.println("Trap Room: (" + encounterState.getTrapRoomX() + ", " + encounterState.getTrapRoomY() + "), Encountered: " + encounterState.isTrapEncountered());
      System.out.println("Mad Scientist Room: (" + encounterState.getMadScientistRoomX() + ", " + encounterState.getMadScientistRoomY() + "), Encountered: " + encounterState.isMadScientistEncountered());

   }

   /**
    * Inner class to manage the state of encounters, including their locations
    * and whether they have been encountered.
    * <p>
    * Includes getter and setter methods for tracking the state and coordinates of
    * encounters, such as traps, Mad Scientists, Lost Explorers, and the Guardian.
    * <p>
    * These methods allow checking and updating the occurrence status and the
    * positions of each encounter.</p>
    */
   public static class EncounterState {

      private boolean trapEncountered;
      private boolean madScientistEncountered;
      private boolean explorerEncountered;
      private static boolean guardianEncounterCompleted;

      private int trapRoomX;
      private int trapRoomY;
      private int madScientistRoomX;
      private int madScientistRoomY;
      private int explorerRoomX;
      private int explorerRoomY;
      private int guardianRoomX;
      private int guardianRoomY;

      /**
       * Gets whether the trap encounter has occurred.
       *
       * @return {@code true} if the trap encounter has occurred, {@code false} otherwise
       */
      public boolean isTrapEncountered() { return trapEncountered; }

      /**
       * Sets whether the trap encounter has occurred.
       *
       * @param encountered {@code true} if the trap encounter has occurred
       */
      public void setTrapEncountered(boolean encountered) { trapEncountered = encountered; }

      /*
       * General note: The following getter and setter methods provide access to the
       * state and coordinates of various encounters. They are used to query or update
       * the current game state dynamically.
       */

      public boolean isMadScientistEncountered() { return madScientistEncountered; }
      public void setMadScientistEncountered(boolean encountered) { madScientistEncountered = encountered; }

      public boolean isExplorerEncountered() { return explorerEncountered; }
      public void setExplorerEncountered(boolean encountered) { explorerEncountered = encountered; }

      public boolean isGuardianEncounterCompleted() { return guardianEncounterCompleted; }
      public static void setGuardianEncounterCompleted(boolean completed) { guardianEncounterCompleted = completed; }

      public int getTrapRoomX() { return trapRoomX; }
      public int getTrapRoomY() { return trapRoomY; }
      public void setTrapRoom(int x, int y) {
         trapRoomX = x;
         trapRoomY = y;
      }

      public int getMadScientistRoomX() { return madScientistRoomX; }
      public int getMadScientistRoomY() { return madScientistRoomY; }
      public void setMadScientistRoom(int x, int y) { madScientistRoomX = x; madScientistRoomY = y; }

      public int getExplorerRoomX() { return explorerRoomX; }
      public int getExplorerRoomY() { return explorerRoomY; }
      public void setExplorerRoom(int x, int y) {
         this.explorerRoomX = x;
         this.explorerRoomY = y;
      }

      public int getGuardianRoomX() { return guardianRoomX; }
      public int getGuardianRoomY() { return guardianRoomY; }
      public void setGuardianRoom(int x, int y) {
         this.guardianRoomX = x;
         this.guardianRoomY = y;
      }

   }

}
