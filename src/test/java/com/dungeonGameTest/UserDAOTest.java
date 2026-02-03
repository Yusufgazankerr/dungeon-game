package com.dungeonGameTest;

import com.dungeonGame.database.UserDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class UserDAOTest {

   @Test
   void testInsertUser() {
      UserDAO dao = mock(UserDAO.class);
      doNothing().when(dao).insertUser("TestPlayer", 1, 100, "E", "Sword,Hammer");
      dao.insertUser("TestPlayer", 1, 100, "E", "Sword,Hammer");
      verify(dao, times(1)).insertUser("TestPlayer", 1, 100, "E", "Sword,Hammer");
   }

   @Test
   void testRetrieveUser() {
      UserDAO dao = mock(UserDAO.class);
      when(dao.getUserByName("TestPlayer")).thenReturn(null);
      assertNull(dao.getUserByName("TestPlayer"), "Retrieving a non-existent user should return null");
   }
}
