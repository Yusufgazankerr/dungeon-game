package com.dungeonGame.manualTests;

import com.dungeonGame.logic.PlayerDataHolder;
import com.dungeonGame.encounter.Encounters;
import com.dungeonGame.logic.InventoryManager;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A manual test class for simulating the Lost Explorer encounter in the Dungeon Game.
 *
 * <p>
 * This class sets up a mock scenario where a player interacts with the Lost Explorer encounter.
 * It allows for testing the player's choices and their effects on inventory and power points.
 * </p>
 */
public class LostExplorerEncounterTest {

   /**
    * The main method simulates the Lost Explorer encounter with a predefined player state.
    *
    * <ul>
    *     <li><b>Setup:</b>
    *         <ul>
    *             <li>Player is initialized with a specific inventory (Hammer, Freeze Spell, and Teleportation Spell).</li>
    *             <li>Player's power points are set to 80.</li>
    *             <li>Simulated user input is used to choose an action during the encounter.</li>
    *         </ul>
    *     </li>
    *     <li><b>Options:</b>
    *         <ul>
    *             <li>Use Hammer and Freeze Spell to subdue the explorer.</li>
    *             <li>Use Teleportation Spell to escape.</li>
    *             <li>Fight the explorer.</li>
    *         </ul>
    *     </li>
    *     <li><b>Test Outputs:</b>
    *         <ul>
    *             <li>Displays the player's inventory after the encounter.</li>
    *             <li>Displays the player's power points after the encounter.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link PlayerDataHolder#setPowerPoints(int)}</li>
    *     <li>{@link InventoryManager#addItem(PlayerDataHolder, String)}</li>
    *     <li>{@link Encounters.LostExplorerEncounterManager#lostExplorerEncounter(PlayerDataHolder)}</li>
    * </ul>
    */
   public static void main(String[] args) {

      InventoryManager inventoryManager = new InventoryManager();
      Encounters.LostExplorerEncounterManager lostExplorerEncounterManager = new Encounters.LostExplorerEncounterManager();

      // Setup player scenario
      PlayerDataHolder player = new PlayerDataHolder();
      inventoryManager.addItem(player, "Hammer");
      inventoryManager.addItem(player, "Freeze Spell");
      inventoryManager.addItem(player, "Teleportation Spell");

      // Set power points
      player.setPowerPoints(80);

      // Simulated user input for choosing an action
      String simulatedInput = "1\n"; // Simulates choosing to fight
      InputStream is = new ByteArrayInputStream(simulatedInput.getBytes());
      System.setIn(is);

      // Execute the Lost Explorer encounter
      lostExplorerEncounterManager.lostExplorerEncounter(player);

      // Test outputs
      System.out.println("Inventory after encounter: " + player.getInventory());
      System.out.println("Power points after encounter: " + player.getPowerPoints());

   }

}
