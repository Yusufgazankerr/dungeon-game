package com.dungeonGame.logic;

import com.dungeonGame.GameUtils;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;
import com.dungeonGame.roomContents.PotionActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Handles player search actions in the dungeon, allowing players to look
 * around and discover items or interact with their environment.
 */
public class SearchActions {

   InventoryManager inventoryManager = new InventoryManager();
   PotionActions potionActions = new PotionActions();

   /**
    * Allows the player to search the current room for items or special objects.
    *
    * <ul>
    *     <li><b>Items Found:</b>
    *         <ul>
    *             <li>{@code "Teleportation Spell"} - Allows teleportation.</li>
    *             <li>{@code "Freeze Spell"} - Freezes enemies.</li>
    *             <li>{@code "Cake"} - Restores 3 power points.</li>
    *             <li>{@code "Sandwich"} - Restores 5 power points.</li>
    *             <li>{@code "Hammer"} - Disables traps.</li>
    *             <li>{@code "Potion"} - Applies a random effect.</li>
    *             <li>{@code "Relic"} (Level 3 only) - Required to defeat the Guardian.</li>
    *         </ul>
    *     </li>
    *     <li><b>Options:</b> Players can pick up an item or ignore them.</li>
    *     <li><b>Consequences:</b>
    *         <ul>
    *             <li>If an item is picked up, it is added to the player's inventory.</li>
    *             <li>Drinking a potion immediately applies its effect.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link GameUtils#clearConsole()}</li>
    *     <li>{@link GameUtils#printHeader(String)}</li>
    *     <li>{@link GameUtils#delayPrint(String)}</li>
    *     <li>{@link PotionActions#drinkPotion(PositionDataHolder, PlayerDataHolder, String)}</li>
    *     <li>{@link InventoryManager#addItem(PlayerDataHolder, String)}</li>
    *     <li>{@link InventoryManager.RelicManager#markRelicFound()}</li>
    *     <li>{@link GameUtils#waitForEnter()}</li>
    * </ul>
    *
    * @param pm     the player's position manager
    * @param player the player's data, including inventory and power points
    */
   public void lookAround(PositionDataHolder pm, PlayerDataHolder player) {

      GameUtils.clearConsole();
      GameUtils.printHeader("Looking Around");

      List<String> baseContents = List.of("Teleportation Spell", "Freeze Spell", "Cake", "Sandwich", "Hammer", "Potion");
      List<String> contents = new ArrayList<>(baseContents);

      // If we are in level 3 and relic not found yet, include "Relic"
      if (DungeonMap.getCurrentLevelNumber() == 3 && !InventoryManager.RelicManager.isRelicFound()) {
         contents.add("Relic");
      }

      Random random = new Random();
      int itemCount = random.nextInt(2) + 1;
      List<String> foundContents = new ArrayList<>();
      while (foundContents.size() < itemCount) {
         String item = contents.get(random.nextInt(contents.size()));
         if (!foundContents.contains(item)) {
            foundContents.add(item);
         }
      }

      GameUtils.delayPrint("You look around and find:");
      for (int i = 0; i < foundContents.size(); i++) {
         String found = foundContents.get(i);
         if (found.equals("Potion")) {
            GameUtils.delayPrint((i + 1) + ". A mysterious potion");
         } else if (found.equals("Relic")) {
            // Special item displayed once
            GameUtils.delayPrint((i + 1) + ". A strange glowing Relic");
         } else {
            GameUtils.delayPrint((i + 1) + ". " + found);
         }
      }
      GameUtils.delayPrint((foundContents.size() + 1) + ". Ignore");

      System.out.print("\nWhat do you want to pick? Enter the number: ");
      Scanner scanner = new Scanner(System.in);
      int choice = scanner.nextInt();
      scanner.nextLine();

      if (choice > 0 && choice <= foundContents.size()) {
         String selectedItem = foundContents.get(choice - 1);

         if (selectedItem.equals("Potion")) {
            // Handle random potion drinking
            String potionType = potionActions.getRandomPotion();
            potionActions.drinkPotion(pm, player, potionType);
         } else if (selectedItem.equals("Relic")) {
            // Player picks up the Relic
            inventoryManager.addItem(player, "Relic");
            InventoryManager.RelicManager.markRelicFound();
            GameUtils.delayPrint("You carefully pick up the Relic. It hums with ancient power...");
         } else if (inventoryManager.hasItem(player, selectedItem)) {
            GameUtils.delayPrint("You already have " + selectedItem + ". You leave it behind.");
         } else {
            inventoryManager.addItem(player, selectedItem);
            GameUtils.delayPrint(selectedItem + " has been added to your inventory.");
         }
      } else {
         GameUtils.delayPrint("You decided to leave the items untouched.");
      }

      GameUtils.waitForEnter();
   }

}
