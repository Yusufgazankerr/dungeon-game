package com.dungeonGameTest;

import com.dungeonGame.logic.InventoryManager;
import com.dungeonGame.logic.MovementLogic;
import com.dungeonGame.logic.PlayerDataHolder;
import com.dungeonGame.logic.PositionDataHolder;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovementLogicTest {
   MovementLogic movementLogic;
   PositionDataHolder position;
   PlayerDataHolder player;
   InventoryManager inventoryManager;

   @BeforeEach
   void setup() {
      // Initialize DungeonMap levels
      DungeonMap.levels = new char[3][][];
      DungeonMap.levels[0] = DungeonMap.LevelFactory.createLevel1();
      DungeonMap.levels[1] = DungeonMap.LevelFactory.createLevel2();
      DungeonMap.levels[2] = DungeonMap.LevelFactory.createLevel3();

      // Set the current level
      DungeonMap.currentLevel = DungeonMap.getLevel(1);
      DungeonMap.currentLevelNumber = 1;

      // Find the entrance ('E') in the map
      char[][] level = DungeonMap.getLevel(1);
      int entranceX = -1, entranceY = -1;

      for (int i = 0; i < level.length; i++) {
         for (int j = 0; j < level[i].length; j++) {
            if (level[i][j] == 'E') {
               entranceX = i;
               entranceY = j;
               break;
            }
         }
         if (entranceX != -1) break; // Exit outer loop once entrance is found
      }

      if (entranceX == -1 || entranceY == -1) {
         throw new IllegalStateException("Entrance ('E') not found in the map!");
      }

      // Initialize the position and player at the entrance
      movementLogic = new MovementLogic();
      position = new PositionDataHolder(level);
      position.setCurrentPosition(entranceX, entranceY); // Start at the entrance
      player = new PlayerDataHolder();
   }


   @Test
   void testValidMove() {
      assertTrue(movementLogic.handleMovement(position, player, "up", inventoryManager), "Player should move up");
   }

   @Test
   void testInvalidMove() {
      assertFalse(movementLogic.handleMovement(position, player, "left", inventoryManager), "Player should not move into a wall");
   }

   @Test
   void testPowerPointDeductionOnMove() {
      movementLogic.handleMovement(position, player, "up", inventoryManager);
      assertEquals(97, player.getPowerPoints(), "Power points should decrease by 3 after a valid move");
   }
}

