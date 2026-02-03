package com.dungeonGame.roomContents;

import com.dungeonGame.GameUtils;
import com.dungeonGame.logic.PositionDataHolder;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;

import java.util.Random;

/**
 * Handles teleportation actions in the Dungeon Game, allowing the player
 * to move to a random location on the map.
 */
public class TeleportationActions {

   /**
    * Uses the Teleportation Spell to move the player to a random valid location
    * on the current level.
    *
    * <ul>
    *     <li><b>Valid Locations:</b> The target cell must:
    *         <ul>
    *             <li>Be within the map boundaries.</li>
    *             <li>Not be a wall ('W').</li>
    *             <li>Not be an entrance ('E') or exit ('X').</li>
    *             <li>Not be the player's current position.</li>
    *         </ul>
    *     </li>
    *     <li><b>Consequences:</b> The player's position is updated to a new random location.
    *         A message confirms the teleportation.</li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link GameUtils#delayPrint(String)}</li>
    *     <li>{@link PositionDataHolder#setCurrentPosition(int, int)}</li>
    * </ul>
    *
    * @param pm the player's position manager
    */
   public void useTeleportationSpell(PositionDataHolder pm) {
      GameUtils.delayPrint("You activate the Teleportation Spell...");

      Random random = new Random();
      char[][] currentLevel = DungeonMap.getCurrentLevel();
      int levelSize = currentLevel.length;
      int newX, newY;

      // Find a valid location for teleportation
      do {
         newX = random.nextInt(levelSize);
         newY = random.nextInt(levelSize);
      } while ((currentLevel[newX][newY] == 'W' || currentLevel[newX][newY] == 'X' || currentLevel[newX][newY] == 'E')
              || (newX == pm.getPlayerX() && newY == pm.getPlayerY()));

      // Update the player's position
      pm.setCurrentPosition(newX, newY);
      GameUtils.delayPrint("The spell teleports you to a new location!");
   }

}
