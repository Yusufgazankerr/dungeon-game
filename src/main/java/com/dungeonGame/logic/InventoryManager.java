package com.dungeonGame.logic;

import com.dungeonGame.GameUtils;
import com.dungeonGame.roomContents.TeleportationActions;

import java.util.List;
import java.util.Random;

/**
 * Manages the player's inventory, including adding, removing, and using items.
 * <p>
 * This class also includes methods for random item generation and relic-specific logic.
 * </p>
 */
public class InventoryManager {

   /**
    * Adds an item to the player's inventory if it is not already present.
    *
    * @param player   the player's data, including inventory
    * @param itemName the name of the item to add
    */
   public void addItem(PlayerDataHolder player, String itemName) {
      player.getInventory().add(itemName);
   }

   /**
    * Checks if the player's inventory contains a specific item.
    *
    * @param player   the player's data, including inventory
    * @param itemName the name of the item to check
    * @return {@code true} if the item exists in the inventory, {@code false} otherwise
    */
   public boolean hasItem(PlayerDataHolder player, String itemName) {
      return player.getInventory().contains(itemName);
   }

   /**
    * Removes a specific item from the player's inventory if it exists.
    *
    * @param player   the player's data, including inventory
    * @param itemName the name of the item to remove
    * @return {@code true} if the item was removed, {@code false} otherwise
    */
   public boolean useItem(PlayerDataHolder player, String itemName) {
      return player.getInventory().remove(itemName);
   }

   /**
    * Clears all items from the player's inventory.
    *
    * @param player the player's data, including inventory
    */
   public void clearInventory(PlayerDataHolder player) {
      player.getInventory().clear();
   }

   /**
    * Generates and adds a specified number of random non-potion items to the player's inventory.
    *
    * <p>This method uses a default {@link Random} instance to generate items.</p>
    *
    * <ul>
    *     <li>{@link #giveRandomItems(PlayerDataHolder, int, Random)}</li>
    * </ul>
    *
    *
    * @param player the player's data, including inventory
    * @param amount the number of random items to generate
    */
   public void giveRandomItems(PlayerDataHolder player, int amount) {
      giveRandomItems(player, amount, new Random());
   }

   /**
    * Generates and adds a specified number of random non-potion items to the player's inventory.
    * A custom {@link Random} instance can be provided for testing or customization.
    * <p>
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link #addItem(PlayerDataHolder, String)}</li>
    *     <li>{@link #hasItem(PlayerDataHolder, String)}</li>
    *     <li>{@link GameUtils#delayPrint(String)}</li>
    * </ul>
    *
    * @param player the player's data, including inventory
    * @param amount the number of random items to generate
    * @param random the {@link Random} instance to use for item generation
    */
   public void giveRandomItems(PlayerDataHolder player, int amount, Random random) {
      List<String> nonPotionItems = List.of("Teleportation Spell", "Freeze Spell", "Cake", "Sandwich", "Hammer");
      int itemsAdded = 0;
      while (itemsAdded < amount) {
         String randomItem = nonPotionItems.get(random.nextInt(nonPotionItems.size()));
         if (!hasItem(player, randomItem)) {
            addItem(player, randomItem);
            GameUtils.delayPrint("You received: " + randomItem);
            itemsAdded++;
         }
      }
   }

   /**
    * Handles the usage of specific inventory items and applies their effects.
    *
    * <ul>
    *     <li><b>Supported Items:</b>
    *         <ul>
    *             <li>{@code "Teleportation Spell"} - Teleports the player to a random location.</li>
    *             <li>{@code "Cake"} - Restores 3 power points.</li>
    *             <li>{@code "Sandwich"} - Restores 5 power points.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link #useItem(PlayerDataHolder, String)}</li>
    *     <li>{@link TeleportationActions#useTeleportationSpell(PositionDataHolder)}</li>
    *     <li>{@link PowerPointManager#addPowerPoints(PlayerDataHolder, int)}</li>
    * </ul>
    *
    * @param selectedItem the name of the item to use
    * @param player       the player's data, including inventory
    * @param pm           the player's position manager
    */
   public void handleInventoryItemUse(String selectedItem, PlayerDataHolder player, PositionDataHolder pm) {

      TeleportationActions teleportationActions = new TeleportationActions();
      PowerPointManager pointManager = new PowerPointManager();

      switch (selectedItem) {
         case "Teleportation Spell":
            if (useItem(player, "Teleportation Spell")) {
               GameUtils.delayPrint("You used the Teleportation Spell!");
               teleportationActions.useTeleportationSpell(pm);
            } else {
               GameUtils.delayPrint("You don't have a Teleportation Spell anymore.");
            }
            break;
         case "Cake (+3 Power Points)":
            if (useItem(player, "Cake")) {
               GameUtils.delayPrint("You ate the Cake and gained 3 Power Points!");
               pointManager.addPowerPoints(player, 3);
            } else {
               GameUtils.delayPrint("You don't have a Cake anymore.");
            }
            break;
         case "Sandwich (+5 Power Points)":
            if (useItem(player, "Sandwich")) {
               GameUtils.delayPrint("You ate the Sandwich and gained 5 Power Points!");
               pointManager.addPowerPoints(player, 5);
            } else {
               GameUtils.delayPrint("You don't have a Sandwich anymore.");
            }
            break;
         default:
            // no default action
            break;
      }
   }

   /**
    * An inner class that handles logic related to the Relic, a unique item in the game.
    */
   public static class RelicManager {

      /**
       * Tracks whether the player has found the relic. Initially set to {@code false}.
       */
      private static boolean relicFound = false;

      /**
       * Checks if the relic has been found.
       *
       * @return {@code true} if the relic has been found, {@code false} otherwise
       */
      public static boolean isRelicFound() {
         return relicFound;
      }

      /**
       * Marks the relic as found, ensuring it will not appear again in the game.
       */
      public static void markRelicFound() {
         relicFound = true;
      }

   }

}
