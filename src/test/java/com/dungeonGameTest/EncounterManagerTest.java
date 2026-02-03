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

import static org.junit.jupiter.api.Assertions.*;

class EncounterManagerTest {
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
   void testEncountersAssignedToValidRooms() {
      // Validate Trap Room
      char[][] currentLevel = DungeonMap.getCurrentLevel();
      int trapX = encounterState.getTrapRoomX();
      int trapY = encounterState.getTrapRoomY();
      System.out.println("Assigned Trap Room: (" + trapX + ", " + trapY + ")");
      assertTrue(currentLevel[trapX][trapY] == ' ', "Trap should be assigned to a valid room");

      // Validate Mad Scientist Room
      int madScientistX = encounterState.getMadScientistRoomX();
      int madScientistY = encounterState.getMadScientistRoomY();
      assertTrue(currentLevel[madScientistX][madScientistY] == ' ', "Mad Scientist should be assigned to a valid room");
   }

   @Test
   void testTrapEncounter() {
      // Simulate player moving to Trap Room
      position.setCurrentPosition(encounterState.getTrapRoomX(), encounterState.getTrapRoomY());
      int initialPowerPoints = player.getPowerPoints();
      encounterManager.checkForEncounters(position, player, inventoryManager);

      // Validate Trap triggered
      assertTrue(player.getPowerPoints() < initialPowerPoints, "Player should lose power points in Trap encounter");
   }

   @Test
   void testMadScientistEncounterWithIncorrectAnswer() {
      // Simulate user providing an incorrect answer
      ByteArrayInputStream simulatedInput = new ByteArrayInputStream("incorrect\n".getBytes());
      System.setIn(simulatedInput);

      // Simulate player moving to Mad Scientist Room
      position.setCurrentPosition(encounterState.getMadScientistRoomX(), encounterState.getMadScientistRoomY());
      int initialPowerPoints = player.getPowerPoints();
      encounterManager.checkForEncounters(position, player, inventoryManager);

      // Validate power points are deducted
      assertTrue(player.getPowerPoints() < initialPowerPoints, "Mad Scientist encounter should deduct power for incorrect answer");

      // Restore System.in
      System.setIn(System.in);
   }

   @Test
   void testLostExplorerEncounterWithHammerAndFreezeSpell() {
      // Setup player with Hammer and Freeze Spell
      player.getInventory().add("Hammer");
      player.getInventory().add("Freeze Spell");

      // Simulate moving to Lost Explorer Room
      position.setCurrentPosition(encounterState.getExplorerRoomX(), encounterState.getExplorerRoomY());
      encounterManager.checkForEncounters(position, player, inventoryManager);

      // Validate both items were used
      assertFalse(player.getInventory().contains("Hammer"), "Hammer should be used during the encounter");
      assertFalse(player.getInventory().contains("Freeze Spell"), "Freeze Spell should be used during the encounter");

      // Validate power points remain unchanged (no fight occurred)
      assertEquals(100, player.getPowerPoints(), "Power points should not change when using Hammer and Freeze Spell");
   }

   @Test
   void testLostExplorerEncounterWithTeleportationSpell() {
      // Setup player with Teleportation Spell
      player.getInventory().add("Teleportation Spell");

      // Simulate moving to Lost Explorer Room
      position.setCurrentPosition(encounterState.getExplorerRoomX(), encounterState.getExplorerRoomY());
      encounterManager.checkForEncounters(position, player, inventoryManager);

      // Validate Teleportation Spell was used
      assertFalse(player.getInventory().contains("Teleportation Spell"), "Teleportation Spell should be used during the encounter");

      // Validate power points remain unchanged (no fight occurred)
      assertEquals(100, player.getPowerPoints(), "Power points should not change when using Teleportation Spell");
   }

   @Test
   void testLostExplorerEncounterWithFight() {
      // Setup player without items
      int initialPowerPoints = player.getPowerPoints();

      // Simulate moving to Lost Explorer Room
      position.setCurrentPosition(encounterState.getExplorerRoomX(), encounterState.getExplorerRoomY());
      encounterManager.checkForEncounters(position, player, inventoryManager);

      // Validate power points are deducted if a player fights
      assertTrue(player.getPowerPoints() < initialPowerPoints, "Player should lose power points when fighting the Lost Explorer");
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

   @Test
   void testUseItem() {


      // Setup player with Teleportation Spell
      player.getInventory().add("Teleportation Spell");

      // Use the item
      boolean wasUsed = inventoryManager.useItem(player, "Teleportation Spell");

      // Validate the item was used
      assertTrue(wasUsed, "Teleportation Spell should be successfully used");
      assertFalse(player.getInventory().contains("Teleportation Spell"), "Teleportation Spell should be removed from inventory");
   }


}


