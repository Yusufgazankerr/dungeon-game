package com.dungeonGameTest;

import com.dungeonGame.logic.PlayerDataHolder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDataHolderTest {

   @Test
   void testAddInventory() {
      PlayerDataHolder player = new PlayerDataHolder();
      player.getInventory().add("Sword");
      assertTrue(player.getInventory().contains("Sword"), "Item should be added to inventory");
   }

   @Test
   void testRemoveInventory() {
      PlayerDataHolder player = new PlayerDataHolder();
      player.getInventory().add("Shield");
      player.getInventory().remove("Shield");
      assertFalse(player.getInventory().contains("Shield"), "Item should be removed from inventory");
   }

   @Test
   void testSetPowerPoints() {
      PlayerDataHolder player = new PlayerDataHolder();
      player.setPowerPoints(50);
      assertEquals(50, player.getPowerPoints(), "Power points should be updated correctly");
   }

   @Test
   void testPlayerIsAlive() {
      PlayerDataHolder player = new PlayerDataHolder();
      player.setPowerPoints(0);
      assertFalse(player.isAlive(), "Player should be dead when power points are zero or less");
   }
}
