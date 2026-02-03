package com.dungeonGame;

import java.util.Scanner;

/**
 * Utility class for common game-related operations, such as printing with delay,
 * clearing the console, and displaying headers.
 */
public class GameUtils {

   /**
    * Default delay in milliseconds for the {@code delayPrint} method.
    */
   public static final int DEFAULT_PRINT_DELAY = 15;

   /**
    * Clears the console screen. Works for both Windows and Unix-based systems.
    * Also prints an error message if the console cannot be cleared.
    */
   public static void clearConsole() {
      try {
         if (System.getProperty("os.name").contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
         } else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
         }
      } catch (Exception e) {
         System.out.println("Could not clear the console.");
      }
   }

   /**
    * Prints a message to the console with a default character delay.
    * This method uses the default delay specified by the constant {@code DEFAULT_PRINT_DELAY}.
    * It delegates to the {@link #delayPrint(String, int)} method.
    *
    * @param message the message to print
    */
   public static void delayPrint(String message) {
      delayPrint(message, DEFAULT_PRINT_DELAY);
   }

   /**
    * Prints a message to the console with a specified character delay.
    *
    * @param message the message to print
    * @param delay the delay in milliseconds between each character
    */
   public static void delayPrint(String message, int delay) {
      for (char c : message.toCharArray()) {
         System.out.print(c);
         try {
            Thread.sleep(DEFAULT_PRINT_DELAY);
         } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
         }
      }
      System.out.println();
   }

   /**
    * Prints a formatted header with separators to the console.
    *
    * @param header the header text to display
    */
   public static void printHeader(String header) {
      System.out.println("==========================================");
      System.out.println(header.toUpperCase());
      System.out.println("==========================================");
   }

   /**
    * Waits for the user to press the Enter key before proceeding.
    * This method checks if the {@link Scanner} has a next line available and consumes it.
    */
   public static void waitForEnter() {
      if (System.getProperty("test.mode") != null) {
         return; // Skip waiting in test mode
      }

      Scanner scanner = new Scanner(System.in);
      System.out.println("Press Enter to continue...");
      if (scanner.hasNextLine()) {
         scanner.nextLine();
      }
   }

}
