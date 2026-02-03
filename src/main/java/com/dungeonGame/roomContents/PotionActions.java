package com.dungeonGame.roomContents;

import com.dungeonGame.GameUtils;
import com.dungeonGame.logic.DisplayManager;
import com.dungeonGame.logic.PositionDataHolder;
import com.dungeonGame.logic.PlayerDataHolder;
import com.dungeonGame.logic.PowerPointManager;

import java.util.Random;

/**
 * Handles the effects of potions in the Dungeon Game.
 *
 * <p>
 * Potions can have both positive and negative effects on the player,
 * ranging from power point changes to enhancing visibility.
 * </p>
 */
public class PotionActions {

   PowerPointManager pointManager = new PowerPointManager();
   DisplayManager displayManager = new DisplayManager();

   /**
    * List of available potions and their effects:
    *
    * <ul>
    *     <li>{@code "Sleeping Potion"} - Reduces power points by 5.</li>
    *     <li>{@code "Health Potion"} - Increases power points by 5.</li>
    *     <li>{@code "Vision Potion"} - Enhances visibility by showing the player's current map layout.
    *     </li>
    * </ul>
    */
   static final String[] POTIONS = {"Sleeping Potion", "Health Potion", "Vision Potion"};

   /**
    * Applies the effect of a specific potion to the player.
    *
    * <ul>
    *     <li><b>Potions and Effects:</b>
    *         <ul>
    *             <li>{@code "Sleeping Potion"} - Decreases power points by 5.</li>
    *             <li>{@code "Health Potion"} - Increases power points by 5.</li>
    *             <li>{@code "Vision Potion"} - Displays the player's current map location.</li>
    *         </ul>
    *     </li>
    *     <li><b>Consequences:</b> The player's power points or visibility are updated based on the potion's effect.</li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link GameUtils#delayPrint(String)}</li>
    *     <li>{@link PowerPointManager#deductPowerPoints(PlayerDataHolder, int)}</li>
    *     <li>{@link PowerPointManager#addPowerPoints(PlayerDataHolder, int)}</li>
    *     <li>{@link DisplayManager#displayMapWithPlayerLocation(PositionDataHolder)}</li>
    * </ul>
    *
    * @param pm         the player's position manager
    * @param player     the player's data, including inventory and power points
    * @param potionType the type of potion to apply
    */
   public void drinkPotion(PositionDataHolder pm, PlayerDataHolder player, String potionType) {
      switch (potionType) {
         case "Sleeping Potion":
            GameUtils.delayPrint("You drink the Sleeping Potion. You feel drowsy and lose 5 power points.");
            pointManager.deductPowerPoints(player, 5);
            break;
         case "Health Potion":
            GameUtils.delayPrint("You drink the Health Potion. You feel rejuvenated and gain 5 power points!");
            pointManager.addPowerPoints(player, 5);
            break;
         case "Vision Potion":
            GameUtils.delayPrint("You drink the Vision Potion. Your surroundings become clearer...");
            displayManager.displayMapWithPlayerLocation(pm);
            break;
         default:
            GameUtils.delayPrint("The potion has no effect...");
            break;
      }
   }

   /**
    * Selects a random potion from the available options.
    *
    * <ul>
    *     <li><b>Options:</b> {@code "Sleeping Potion"}, {@code "Health Potion"}, {@code "Vision Potion"}.</li>
    *     <li><b>Usage:</b> Used when a player encounters a generic potion in the game.</li>
    * </ul>
    *
    * @return the name of a randomly selected potion
    */
   public String getRandomPotion() {
      Random random = new Random();
      return POTIONS[random.nextInt(POTIONS.length)];
   }

}
