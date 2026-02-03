package com.dungeonGame.logic;

import com.dungeonGame.GameUtils;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Handles the display of game-related information, including the current room,
 * the player's location on the map, and their inventory.
 *
 * <p>This class includes methods to present dynamic game data to the player,
 * providing a clear interface for interacting with the dungeon environment.</p>
 */
public class DisplayManager {

   InventoryManager inventoryManager = new InventoryManager();

   /**
    * Displays information about the current room, including surroundings,
    * the player's location, and power points.
    *
    * <ul>
    *     <li><b>Details Shown:</b>
    *         <ul>
    *             <li>The player's current level and room coordinates.</li>
    *             <li>Descriptions of the surroundings (e.g., walls, exits).</li>
    *             <li>The player's current power points.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link GameUtils#clearConsole()}</li>
    *     <li>{@link GameUtils#printHeader(String)}</li>
    *     <li>{@link GameUtils#delayPrint(String)}</li>
    *     <li>{@link #checkCompactSurrounding(String, int, int)}</li>
    * </ul>
    *
    * @param pm     the player's position manager
    * @param player the player's data, including inventory and power points
    */
   public void displayCurrentRoom(PositionDataHolder pm, PlayerDataHolder player) {
      GameUtils.clearConsole();
      GameUtils.printHeader("Current Room");

      int px = pm.getPlayerX();
      int py = pm.getPlayerY();
      int levelNum = DungeonMap.getCurrentLevelNumber();

      char row = (char) ('A' + px);
      int column = py + 1;

      GameUtils.delayPrint("You are in Level " + levelNum + " Room " + row + column + ".");
      GameUtils.delayPrint("You have a long way to go.");

      String surroundings = "Surroundings: " +
              checkCompactSurrounding("left", px, py - 1) + ", " +
              checkCompactSurrounding("right", px, py + 1) + ", " +
              checkCompactSurrounding("ahead", px - 1, py) + ", " +
              checkCompactSurrounding("behind", px + 1, py);

      GameUtils.delayPrint(surroundings);
      GameUtils.delayPrint("Current Power Points: " + player.getPowerPoints());
      GameUtils.delayPrint("What do you want to do?");
   }

   /**
    * Displays the current level map with the player's location marked.
    *
    * <ul>
    *     <li><b>Details Shown:</b>
    *         <ul>
    *             <li>Walls, entrances, exits, and other rooms.</li>
    *             <li>The player's current position marked as {@code [P]}.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link GameUtils#printHeader(String)}</li>
    * </ul>
    *
    * @param pm the player's position manager
    */
   public void displayMapWithPlayerLocation(PositionDataHolder pm) {
      GameUtils.printHeader("Current Level Map");
      char[][] level = DungeonMap.getCurrentLevel();
      for (int i = 0; i < level.length; i++) {
         for (int j = 0; j < level[i].length; j++) {
            if (i == pm.getPlayerX() && j == pm.getPlayerY()) {
               System.out.print("[P] ");
            } else {
               System.out.print(level[i][j] + " ");
            }
         }
         System.out.println();
      }
      //GameUtils.waitForEnter();
   }

   /**
    * Provides a concise description of the surroundings relative to the player's position.
    *
    * @param direction the direction being checked (e.g., "left", "right")
    * @param x         the x-coordinate of the location to check
    * @param y         the y-coordinate of the location to check
    * @return a string description of the surroundings in the specified direction
    */
   private String checkCompactSurrounding(String direction, int x, int y) {
      char[][] level = DungeonMap.getCurrentLevel();
      if (x < 0 || x >= level.length || y < 0 || y >= level[0].length) {
         return "Wall to the " + direction;
      }

      char cell = level[x][y];
      return switch (cell) {
         case 'W' -> "Wall to the " + direction;
         case 'X' -> "Exit to the " + direction;
         case 'E' -> "Entrance to the " + direction;
         default -> "Room to the " + direction;
      };
   }

   /**
    * Displays the player's inventory, including usable items, and provides options to use them.
    *
    * <ul>
    *     <li><b>Details Shown:</b>
    *         <ul>
    *             <li>All items in the player's inventory.</li>
    *             <li>Options for using specific items, such as potions or spells.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link GameUtils#clearConsole()}</li>
    *     <li>{@link GameUtils#printHeader(String)}</li>
    *     <li>{@link GameUtils#delayPrint(String)}</li>
    *     <li>{@link GameUtils#waitForEnter()}</li>
    *     <li>{@link InventoryManager#handleInventoryItemUse(String, PlayerDataHolder, PositionDataHolder)}</li>
    * </ul>
    *
    * @param player the player's data, including inventory
    * @param pm     the player's position manager
    */
   public void displayInventory(PlayerDataHolder player, PositionDataHolder pm) {
      GameUtils.clearConsole();
      GameUtils.printHeader("Inventory");

      Set<String> inventory = player.getInventory();
      if (inventory.isEmpty()) {
         GameUtils.delayPrint("Your inventory is empty.");
         GameUtils.waitForEnter();
         return;
      }

      GameUtils.delayPrint("Your Items:");
      for (String item : inventory) {
         GameUtils.delayPrint("- " + item);
      }

      List<String> usableItems = new ArrayList<>();
      boolean hasTeleport = inventoryManager.hasItem(player, "Teleportation Spell");
      boolean hasCake = inventoryManager.hasItem(player, "Cake");
      boolean hasSandwich = inventoryManager.hasItem(player, "Sandwich");

      if (hasTeleport) {
         usableItems.add("Teleportation Spell");
      }
      if (hasCake) {
         usableItems.add("Cake (+3 Power Points)");
      }
      if (hasSandwich) {
         usableItems.add("Sandwich (+5 Power Points)");
      }

      if (!usableItems.isEmpty()) {
         GameUtils.delayPrint("\nYou have usable items. Do you want to use one?");
         for (int i = 0; i < usableItems.size(); i++) {
            GameUtils.delayPrint((i + 1) + ". " + usableItems.get(i));
         }
         GameUtils.delayPrint((usableItems.size() + 1) + ". Exit Inventory");

         System.out.print("\nChoose an item to use (Enter the number): ");
         Scanner scanner = new Scanner(System.in);
         int choice = scanner.nextInt();
         scanner.nextLine(); // Clear buffer

         if (choice > 0 && choice <= usableItems.size()) {
            String selectedItem = usableItems.get(choice - 1);
            inventoryManager.handleInventoryItemUse(selectedItem, player, pm);
         } else {
            GameUtils.delayPrint("Exiting inventory.");
         }
      } else {
         GameUtils.delayPrint("\nYou have no usable items.");
      }

      GameUtils.waitForEnter();
   }

}
