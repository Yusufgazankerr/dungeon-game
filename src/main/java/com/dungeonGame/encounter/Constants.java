package com.dungeonGame.encounter;

/**
 * A utility class that defines constants used in encounters within the Dungeon Game.
 * Includes random scientific openers and riddles with answers for interactive gameplay.
 */
public class Constants {

   /**
    * Array of scientific openers used during the Mad Scientist encounter.
    * Selection Pool:
    * <ul>
    *   <li>{@code "Behold! The quantum entanglement paradox of hyper-space atoms!"}</li>
    *   <li>{@code "Ah, yes! The bifurcating nuclei of the plasmonic resonance are upon us!"}</li>
    *   <li>{@code "Aha! My flux capacitor is in perfect harmony with the neutrino wave!"}</li>
    *   <li>{@code "Did you know that photons can polarize to infinity under an antimatter ray?"}</li>
    *   <li>{@code "Ah, I’ve perfected the infinite vacuum instability of antimatter vortices!"}</li>
    *   <li>{@code "Behold my latest experiment! Transdimensional ionic bonding in action!"}</li>
    * </ul>
    */
   static final String[] SCIENCE_OPENERS = {
           "Behold! The quantum entanglement paradox of hyper-space atoms!",
           "Ah, yes! The bifurcating nuclei of the plasmonic resonance are upon us!",
           "Aha! My flux capacitor is in perfect harmony with the neutrino wave!",
           "Did you know that photons can polarize to infinity under an antimatter ray?",
           "Ah, I’ve perfected the infinite vacuum instability of antimatter vortices!",
           "Behold my latest experiment! Transdimensional ionic bonding in action!"
   };

   /**
    * Array of riddles and their corresponding answers used during encounters.
    * Each entry is an array where:
    * <ul>
    *   <li>Index 0: The riddle question as a {@code String}</li>
    *   <li>Index 1: The riddle answer as a {@code String}</li>
    * </ul>
    * Selection Pool:
    * <ul>
    *   <li>{@code {"What has to be broken before you can use it?", "egg"}}</li>
    *   <li>{@code {"I’m tall when I’m young, and I’m short when I’m old. What am I?", "candle"}}</li>
    *   <li>{@code {"What has hands but can’t clap?", "clock"}}</li>
    *   <li>{@code {"What can you catch but not throw?", "cold"}}</li>
    *   <li>{@code {"What has a head, a tail, is brown, and has no legs?", "penny"}}</li>
    *   <li>{@code {"I’m light as a feather, yet the strongest man can’t hold me for long. What am I?", "breath"}}</li>
    *   <li>{@code {"What comes down but never goes up?", "rain"}}</li>
    *   <li>{@code {"What has many keys but can’t open a single lock?", "piano"}}</li>
    *   <li>{@code {"What has one eye but can’t see?", "needle"}}</li>
    *   <li>{@code {"What has roots as nobody sees, is taller than trees, up, up it goes, and yet it never grows?", "mountain"}}</li>
    * </ul>
    */
   static final String[][] RIDDLES = {
           {"What has to be broken before you can use it?", "egg"},
           {"I’m tall when I’m young, and I’m short when I’m old. What am I?", "candle"},
           {"What has hands but can’t clap?", "clock"},
           {"What can you catch but not throw?", "cold"},
           {"What has a head, a tail, is brown, and has no legs?", "penny"},
           {"I’m light as a feather, yet the strongest man can’t hold me for long. What am I?", "breath"},
           {"What comes down but never goes up?", "rain"},
           {"What has many keys but can’t open a single lock?", "piano"},
           {"What has one eye but can’t see?", "needle"},
           {"What has roots as nobody sees, is taller than trees, up, up it goes, and yet it never grows?", "mountain"}
   };


}
