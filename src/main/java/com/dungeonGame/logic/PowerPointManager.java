package com.dungeonGame.logic;

/**
 * Manages the player's power points, including adding and deducting points.
 *
 * <p>
 * Power points represent the player's health in the game.
 * Maintaining a positive power point balance is crucial to surviving and progressing in the dungeon.
 * </p>
 */
public class PowerPointManager {

   /**
    * Adds a specified number of power points to the player.
    *
    * <ul>
    *     <li><b>Valid Use:</b> The amount can be any non-negative integer.</li>
    *     <li><b>Consequences:</b> Increases the player's power points.</li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link PlayerDataHolder#setPowerPoints(int)}</li>
    * </ul>
    *
    * @param player the player's data
    * @param amount the number of power points to add
    */
   public void addPowerPoints(PlayerDataHolder player, int amount) {
      int newAmount = player.getPowerPoints() + amount;
      player.setPowerPoints(newAmount);
   }

   /**
    * Deducts a specified number of power points from the player.
    *
    * <ul>
    *     <li><b>Valid Use:</b> The amount can be any non-negative integer.</li>
    *     <li><b>Consequences:</b>
    *         <ul>
    *             <li>If the resulting power points are less than zero, they are capped at zero.</li>
    *             <li>Setting power points to zero may result in the player losing the game.</li>
    *         </ul>
    *     </li>
    * </ul>
    *
    * <b>Internal Method Calls:</b>
    * <ul>
    *     <li>{@link PlayerDataHolder#setPowerPoints(int)}</li>
    * </ul>
    *
    * @param player the player's data
    * @param amount the number of power points to deduct
    */
   public void deductPowerPoints(PlayerDataHolder player, int amount) {
      int newAmount = player.getPowerPoints() - amount;
      if (newAmount < 0) {
         newAmount = 0;
      }
      player.setPowerPoints(newAmount);
   }

}
