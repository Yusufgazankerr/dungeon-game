package com.dungeonGameTest;

import com.dungeonGame.GameUtils;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class GameUtilsTest {

   @Test
   void testClearConsole() {
      // Testing console clearing isn't straightforward; assume manual validation.
      assertDoesNotThrow(GameUtils::clearConsole, "Clearing the console should not throw exceptions");
   }

   @Test
   void testDelayPrint() {
      assertDoesNotThrow(() -> GameUtils.delayPrint("Testing delay print"));
   }

   @Test
   void testWaitForEnter() {
      ByteArrayInputStream input = new ByteArrayInputStream("\n".getBytes());
      System.setIn(input);
      assertDoesNotThrow(GameUtils::waitForEnter, "Waiting for Enter should not throw exceptions");
   }
}
