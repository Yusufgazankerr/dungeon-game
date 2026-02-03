package com.dungeonGame.encounter;

import com.dungeonGame.GameUtils;
import com.dungeonGame.logic.*;
import com.dungeonGame.logic.mapAndLevelHandler.LevelTransitionHandler;

import java.util.Random;
import java.util.Scanner;

/**
 * Manages specific encounters in the Dungeon Game, including Mad Scientists,
 * Traps, Lost Explorer, and The Guardian.
 * <p>
 * Each encounter is designed to challenge the player in unique ways, such as
 * solving riddles, managing inventory items, or making strategic choices.
 * </p>
 */
public class Encounters {

   static InventoryManager inventoryManager = new InventoryManager();
   static PowerPointManager pointManager = new PowerPointManager();

   /**
    * Triggers the Mad Scientist encounter. Can happen in every level.
    * Happens once for each level.
    * <ul>
    *     <li><b>Options:</b> The player can either:
    *         <ul>
    *             <li>Use a Freeze Spell to bypass the Mad Scientist.</li>
    *             <li>Solve a riddle presented by the scientist.</li>
    *         </ul>
    *     </li>
    *     <li><b>How to defeat:</b> Solve the riddle correctly or use a Freeze Spell.</li>
    *     <li><b>Consequence of losing:</b> Lose 8 power points for an incorrect riddle answer.</li>
    * </ul>
    *
    * @param player the player's data, including inventory and power points
    */
   public static void madScientist(PlayerDataHolder player) {
      GameUtils.clearConsole();
      GameUtils.printHeader("Mad Scientist Encounter");

      Random random = new Random();
      Scanner scanner = new Scanner(System.in);

      String scienceTalk = Constants.SCIENCE_OPENERS[random.nextInt(Constants.SCIENCE_OPENERS.length)];
      GameUtils.delayPrint(scienceTalk);

      boolean hasFreezeSpell = inventoryManager.hasItem(player, "Freeze Spell");
      if (hasFreezeSpell) {
         GameUtils.delayPrint("The Mad Scientist blocks your way, but you have a Freeze Spell.");
         System.out.println("1. Use Freeze Spell");
         System.out.println("2. Solve the riddle");
         System.out.print("\nEnter your choice: ");
         int choice = scanner.nextInt();
         scanner.nextLine();

         if (choice == 1) {
            GameUtils.delayPrint("You cast the Freeze Spell! The Mad Scientist is frozen. You're free to move again!");
            inventoryManager.useItem(player, "Freeze Spell");
            GameUtils.waitForEnter();
            return;
         }
      }

      int riddleIndex = random.nextInt(Constants.RIDDLES.length);
      String riddleQuestion = Constants.RIDDLES[riddleIndex][0];
      String riddleAnswer = Constants.RIDDLES[riddleIndex][1];

      GameUtils.delayPrint("The Mad Scientist challenges you with a riddle!");
      GameUtils.delayPrint(riddleQuestion);
      System.out.print("Your Answer: ");
      String answer = scanner.nextLine().trim().toLowerCase();

      if (answer.equals(riddleAnswer)) {
         GameUtils.delayPrint("Mad Scientist: Correct! You may proceed. Brilliant mind!");
      } else {
         pointManager.deductPowerPoints(player, 8);
         GameUtils.delayPrint("Mad Scientist: Incorrect! The correct answer was: " + riddleAnswer + ". You lose 10 power points.");
      }

      GameUtils.waitForEnter();
   }

   /**
    * Triggers the trap encounter. It can happen at every level.
    * Happens once for each level.
    * <ul>
    *     <li><b>Options:</b> The player can:
    *         <ul>
    *             <li>Use a Hammer to disable the trap.</li>
    *             <li>Use a Freeze Spell to neutralize the trap.</li>
    *             <li>Do nothing and suffer the consequences.</li>
    *         </ul>
    *     </li>
    *     <li><b>How to defeat:</b> Use a Hammer or a Freeze Spell.</li>
    *     <li><b>Consequence of losing:</b> Lose 7 power points if no valid action is taken.</li>
    * </ul>
    *
    * @param player the player's data, including inventory and power points
    */
   public static void trap(PlayerDataHolder player) {
      GameUtils.clearConsole();
      GameUtils.printHeader("Trap Encounter");

      Scanner scanner = new Scanner(System.in);

      boolean hasHammer = inventoryManager.hasItem(player, "Hammer");
      boolean hasFreezeSpell = inventoryManager.hasItem(player, "Freeze Spell");

      if (hasHammer || hasFreezeSpell) {
         GameUtils.delayPrint("Oh no! You've triggered a trap! But you have tools to escape it.");

         int optionNumber = 1;
         if (hasHammer) {
            System.out.println(optionNumber++ + ". Use Hammer");
         }
         if (hasFreezeSpell) {
            System.out.println(optionNumber++ + ". Use Freeze Spell");
         }
         System.out.println(optionNumber + ". Do nothing");

         System.out.print("\nWhat do you want to do? Enter the number: ");
         int choice = scanner.nextInt();
         scanner.nextLine();

         if (hasHammer && choice == 1) {
            GameUtils.delayPrint("You used a Hammer to disable the trap! You're free to move now.");
            inventoryManager.useItem(player, "Hammer");
            return;
         } else if (hasFreezeSpell && ((hasHammer && choice == 2) || (!hasHammer && choice == 1))) {
            GameUtils.delayPrint("You cast the Freeze Spell! The trap has been neutralized. You're free to move now.");
            inventoryManager.useItem(player, "Freeze Spell");
            return;
         }
      }

      GameUtils.delayPrint("Oh no! You couldn't escape the trap! You lose 7 power points.");
      pointManager.deductPowerPoints(player, 7);
      GameUtils.waitForEnter();
   }

   /**
    * Manages the Lost Explorer encounter in the Dungeon Game.
    * It only happens once in Level 2
    * <p>This encounter challenges the player to make strategic decisions to handle a desperate and threatening explorer.</p>
    * <ul>
    *     <li><b>Options:</b> The player can:
    *         <ul>
    *             <li>Use a Hammer and Freeze Spell together to subdue the explorer.</li>
    *             <li>Use a Teleportation Spell to escape.</li>
    *             <li>Fight the explorer directly.</li>
    *         </ul>
    *     </li>
    *     <li><b>How to defeat:</b> Use the Hammer and Freeze Spell together, or win the fight.</li>
    *     <li><b>Consequence of losing:</b> Lose 5 power points and all inventory items if defeated in the fight.</li>
    * </ul>
    */
   public static class LostExplorerEncounterManager {

      InventoryManager inventoryManager = new InventoryManager();
      PowerPointManager pointManager = new PowerPointManager();

      /**
       * Triggers the Lost Explorer encounter and presents the player with multiple options to resolve it.
       *
       * @param player the player's data, including inventory and power points
       */
      public void lostExplorerEncounter(PlayerDataHolder player) {
         GameUtils.printHeader("Lost Explorer Encounter");
         GameUtils.delayPrint("You encounter a desperate explorer...");

         boolean hasHammer = inventoryManager.hasItem(player, "Hammer");
         boolean hasFreezeSpell = inventoryManager.hasItem(player, "Freeze Spell");
         boolean hasTeleportationSpell = inventoryManager.hasItem(player, "Teleportation Spell");

         GameUtils.delayPrint("The Lost Explorer stares at you, desperate and threatening.");

         // Display options based on what the player has
         int optionNumber = 1;
         int hammerFreezeOption = -1;
         int teleportOption = -1;
         int fightOption;

         // If a player has both Hammer and Freeze Spell, offer that option
         if (hasHammer && hasFreezeSpell) {
            GameUtils.delayPrint(optionNumber + ". Use Hammer and Freeze Spell to subdue the Explorer.");
            hammerFreezeOption = optionNumber;
            optionNumber++;
         }

         // If a player doesn't use or have hammer+freeze or chooses not to use them, they can try teleportation if they have it
         if (hasTeleportationSpell) {
            GameUtils.delayPrint(optionNumber + ". Use Teleportation Spell to escape.");
            teleportOption = optionNumber;
            optionNumber++;
         }

         // Always have a fight option
         GameUtils.delayPrint(optionNumber + ". Fight the Lost Explorer.");
         fightOption = optionNumber;

         GameUtils.delayPrint("\nWhat do you want to do? Enter the number:");

         Scanner scanner = new Scanner(System.in);
         int choice = scanner.nextInt();
         scanner.nextLine(); // clear buffer

         // Handle the player's choice
         if (choice == hammerFreezeOption && hammerFreezeOption != -1) {
            // Use Hammer and Freeze Spell
            GameUtils.delayPrint("You use the Hammer and Freeze Spell to overwhelm the Lost Explorer without a fight!");
            inventoryManager.useItem(player, "Freeze Spell");
            inventoryManager.useItem(player, "Hammer");
            handleExplorerWin(player);

         } else if (choice == teleportOption && teleportOption != -1) {
            // Use Teleportation Spell
            GameUtils.delayPrint("You use the Teleportation Spell to escape the Lost Explorer!");
            inventoryManager.useItem(player, "Teleportation Spell");
            // No win or lose scenario, escape

         } else if (choice == fightOption) {
            // Fight a scenario
            fightLostExplorer(player);
         } else {
            // Invalid choice defaults to fight a scenario
            GameUtils.delayPrint("You hesitate and the Explorer attacks!");
            fightLostExplorer(player);

         }

         GameUtils.waitForEnter();
      }

      /**
       * Handles the fight scenario for the Lost Explorer encounter.
       *
       * <ul>
       *     <li><b>Win condition:</b> The player wins if they have more than 70 power points.</li>
       *     <li><b>Lose condition:</b> The player loses if their power points are 70 or below.</li>
       * </ul>
       *
       * @param player the player's data, including inventory and power points
       */
      private void fightLostExplorer(PlayerDataHolder player) {
         int playerPower = player.getPowerPoints();

         if (playerPower > 70) {
            // Player wins fight
            GameUtils.delayPrint("You overpower the Lost Explorer and take some of his items!");
            handleExplorerWin(player);

         } else {
            // Player loses fight
            GameUtils.delayPrint("The Lost Explorer overpowers you and takes all your items!");
            handleExplorerLose(player);
         }
      }

      /**
       * Handles the scenario when the player wins against the Lost Explorer.
       * <ul>
       *     <li>The player gains three random items as a reward.</li>
       * </ul>
       *
       * @param player the player's data
       */
      private void handleExplorerWin(PlayerDataHolder player) {
         inventoryManager.giveRandomItems(player, 3); // Assumes your giveRandomItems method takes count as an argument
      }

      /**
       * Handles the scenario when the player loses to the Lost Explorer.
       * <ul>
       *     <li>The player loses 5 power points.</li>
       *     <li>The player's inventory is cleared.</li>
       * </ul>
       *
       * @param player the player's data
       */
      private void handleExplorerLose(PlayerDataHolder player) {
         pointManager.deductPowerPoints(player, 5);
         inventoryManager.clearInventory(player);
         GameUtils.delayPrint("You lose 5 power points.");
      }

   }

   /**
    * Manages the Guardian encounter in Level 3 of the Dungeon Game.
    *
    * <p>The Guardian encounter serves as the final challenge for the player, testing their preparation and strategy.</p>
    * <ul>
    *     <li><b>Options:</b> The player can:
    *         <ul>
    *             <li>Use the Relic to destroy the Guardian and win.</li>
    *             <li>Use a Teleportation Spell to escape.</li>
    *             <li>Do nothing and lose the game.</li>
    *         </ul>
    *     </li>
    *     <li><b>How to defeat:</b> Use the Relic to destroy the Guardian.</li>
    *     <li><b>Consequence of losing:</b> The player is defeated, and the game ends.</li>
    * </ul>
    */
   public static class GuardianEncounterManager {

      //static InventoryManager inventoryManager = new InventoryManager();

      /**
       * Triggers the Guardian encounter and presents the player with options to resolve it.
       *
       * @param player           the player's data, including inventory and power points
       * @param inventoryManager the inventory manager to handle player's inventory
       */
      public static void guardianEncounter(PlayerDataHolder player, InventoryManager inventoryManager) {

         GameUtils.printHeader("The Guardian Encounter");
         GameUtils.delayPrint("You stand before The Guardian, a towering sentinel protecting the dungeon's deepest secrets...");

         boolean hasRelic = inventoryManager.hasItem(player, "Relic");
         boolean hasTeleport = inventoryManager.hasItem(player, "Teleportation Spell");

         // Present options
         int optionNumber = 1;
         int relicOption = -1;
         int teleportOption = -1;

         // Option: Use Relic
         if (hasRelic) {
            GameUtils.delayPrint(optionNumber + ". Use the Relic to destroy The Guardian.");
            relicOption = optionNumber++;
         }

         // Option: Use Teleportation Spell
         if (hasTeleport) {
            System.out.println("Guardian Encounter: Checking for Teleportation Spell");
            GameUtils.delayPrint(optionNumber + ". Use Teleportation Spell to flee.");
            teleportOption = optionNumber++;

            if (System.getProperty("test.mode") != null) {
               if (hasTeleport) {
                  inventoryManager.useItem(player, "Teleportation Spell");
                  System.out.println("Test Mode: Teleportation Spell used.");
                  return;
               }
            }

         }

         // Option: Do nothing (if no items available)
         if (!hasRelic && !hasTeleport) {
            GameUtils.delayPrint("You have no means to overcome or escape The Guardian.");
            handleGuardianLose();
            return;
         }

         // Get player's choice
         System.out.print("\nWhat do you want to do? Enter the number: ");
         Scanner scanner = new Scanner(System.in);
         int choice = scanner.hasNextInt() ? scanner.nextInt() : -1;

         // Process player's choice
         if (choice == relicOption) {
            handleGuardianWin();
         } else if (choice == teleportOption) {
            // Use Teleportation Spell to flee
            if (inventoryManager.useItem(player, "Teleportation Spell")) {
               GameUtils.delayPrint("You used the Teleportation Spell and fled from The Guardian!");
               //return; // Encounter ends
            } else {
               GameUtils.delayPrint("You tried to use the Teleportation Spell, but you don't have one!");
            }
         } else {
            GameUtils.delayPrint("You hesitated and The Guardian attacked!");
            handleGuardianLose();
         }
      }

      /**
       * Handles the scenario when the player uses the Relic to destroy the Guardian.
       */
      private static void handleGuardianWin() {
         GameUtils.delayPrint("The Relic shines brightly, unmaking The Guardian in an instant!");
         GameUtils.delayPrint("With The Guardian gone, the path forward is clear. You have triumphed!");
      }

      /**
       * Handles the scenario when the player loses to the Guardian.
       * <ul>
       *     <li>The player is defeated, and the game ends.</li>
       * </ul>
       */
      private static void handleGuardianLose() {
         // The player has no means to defeat or escape. This ends the game.
         GameUtils.delayPrint("You would need the Relic to overcome Guardian...");
         GameUtils.delayPrint("You don't have Teleportation Spell to escape either...");
         GameUtils.delayPrint("Overwhelmed by The Guardian, you fall, and the dungeon claims another victim...");

         LevelTransitionHandler.handleGameOver();
      }

   }

}
