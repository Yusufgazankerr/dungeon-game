package com.dungeonGame.logic;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds the player's data, including name, power points, current room,
 * and inventory. This class is used to manage and update the player's state
 * during the game.
 */
public class PlayerDataHolder {

   /**
    * The player's name.
    */
   private String name;

   /**
    * The player's current power points.
    */
   private int powerPoints;

   /**
    * The name of the room where the player is currently located.
    */
   private String currentRoom;

   /**
    * The player's inventory, containing the items they have collected.
    */
   private Set<String> inventory;

   /**
    * Initializes a new player with a default inventory and 100 power points.
    */
   public PlayerDataHolder() {
      this.inventory = new HashSet<>();
      this.powerPoints = 100;
   }

   //Getters and Setters for the player's data.

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getPowerPoints() {
      return powerPoints;
   }

   /**
    * Updates the player's current power points.
    *
    * <p><b>Consequences:</b> Setting power points to zero or below may result
    * in the player losing the game.</p>
    *
    * @param powerPoints the new power points to set
    */
   public void setPowerPoints(int powerPoints) {
      this.powerPoints = powerPoints;
   }

   public String getCurrentRoom() {
      return currentRoom;
   }

   public void setCurrentRoom(String currentRoom) {
      this.currentRoom = currentRoom;
   }

   public Set<String> getInventory() {
      return inventory;
   }

   public void setInventory(Set<String> inventory) {
      this.inventory = inventory;
   }

   /**
    * Checks whether the player is alive.
    *
    * <ul>
    *     <li><b>Alive Condition:</b> The player is considered alive if their power points are greater than zero.</li>
    *     <li><b>Consequences:</b> If the player's power points are zero or below, the game ends, and the player is considered dead.</li>
    * </ul>
    *
    * @return {@code true} if the player's power points are greater than zero, {@code false} otherwise
    */
   public boolean isAlive() {
      return powerPoints > 0;
   }

   public void setInventory(String inventory) {
      this.inventory = new HashSet<>(Set.of(inventory.split(",")));
   }
}