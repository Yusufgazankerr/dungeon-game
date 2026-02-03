package com.dungeonGameTest;

import com.dungeonGame.encounter.EncounterManager;
import com.dungeonGame.encounter.Encounters;
import com.dungeonGame.logic.InventoryManager;
import com.dungeonGame.logic.PlayerDataHolder;
import com.dungeonGame.logic.PositionDataHolder;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuardianEncounterTest {

   EncounterManager encounterManager;
   EncounterManager.EncounterState encounterState;
   PositionDataHolder position;
   PlayerDataHolder player;
   InventoryManager inventoryManager;

   @BeforeEach
   void setup() {
      System.setProperty("test.mode", "true"); // Enable test mode

      // Initialize DungeonMap levels
      DungeonMap.levels = new char[3][][];
      DungeonMap.levels[0] = DungeonMap.LevelFactory.createLevel1();
      DungeonMap.levels[1] = DungeonMap.LevelFactory.createLevel2();
      DungeonMap.levels[2] = DungeonMap.LevelFactory.createLevel3();

      // Set current level
      DungeonMap.currentLevel = DungeonMap.getLevel(3);
      DungeonMap.currentLevelNumber = 3;

      // Initialize EncounterManager and assign encounters
      encounterManager = new EncounterManager();
      encounterManager.assignEncounters(DungeonMap.getCurrentLevel());

      // Access EncounterState
      encounterState = encounterManager.getEncounterState();

      // Initialize PositionDataHolder and PlayerDataHolder
      position = new PositionDataHolder(DungeonMap.getCurrentLevel());
      position.setCurrentPosition(2, 3);// Valid position for Guardian
      inventoryManager = new InventoryManager();
      player = new PlayerDataHolder();
      player.setPowerPoints(100);
   }

   @Test
   void testGuardianEncounterWithRelic() {
      // Setup player with Relic
      player.getInventory().add("Relic");

      // Simulate moving to Guardian Room
      position.setCurrentPosition(encounterState.getGuardianRoomX(), encounterState.getGuardianRoomY());
      encounterManager.checkForEncounters(position, player, inventoryManager);

      // Validate Relic was used
      assertFalse(player.getInventory().contains("Relic"), "Relic should be used during the Guardian encounter");

      // Validate Guardian is defeated (game continues)
      assertTrue(player.isAlive(), "Player should survive when defeating the Guardian with Relic");
   }

   @Test
   void testGuardianEncounterWithTeleportationSpell() {
      // Create a mock InventoryManager
      InventoryManager inventoryManagerMock = Mockito.mock(InventoryManager.class);

      // Setup player with Teleportation Spell
      player.getInventory().add("Teleportation Spell");

      // Stub the `hasItem` and `useItem` methods
      Mockito.when(inventoryManagerMock.hasItem(player, "Teleportation Spell")).thenReturn(true);
      Mockito.when(inventoryManagerMock.useItem(player, "Teleportation Spell")).thenReturn(true);

      // Simulate user input for a Teleportation Spell option
      ByteArrayInputStream simulatedInput = new ByteArrayInputStream("1\n".getBytes());
      System.setIn(simulatedInput);

      // Simulate moving to Guardian Room and trigger encounter
      position.setCurrentPosition(encounterState.getGuardianRoomX(), encounterState.getGuardianRoomY());
      Encounters.GuardianEncounterManager.guardianEncounter(player, inventoryManagerMock);

      // Verify `useItem` was called
      Mockito.verify(inventoryManagerMock).useItem(player, "Teleportation Spell");

      // Assert player is still alive
      assertTrue(player.isAlive(), "Player should survive by fleeing with Teleportation Spell");

      // Restore System.in
      System.setIn(System.in);
   }

   @Test
   void testGuardianEncounterWithoutOptions() {
      // Setup player without Relic or Teleportation Spell
      int initialPowerPoints = player.getPowerPoints();
      initialPowerPoints = 100;
      // Simulate moving to Guardian Room
      position.setCurrentPosition(encounterState.getGuardianRoomX(), encounterState.getGuardianRoomY());
      encounterManager.checkForEncounters(position, player, inventoryManager);

      // Validate game ends if a player cannot defeat or escape Guardian
      assertFalse(player.isAlive(), "Player should lose the game when facing the Guardian without options");
   }

}
