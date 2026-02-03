package com.dungeonGame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing SQLite database connections.
 * Provides a single method to establish a connection to the database.
 */
public class SQLiteConnection {

   /**
    * Establishes a connection to the SQLite database.
    *
    * @return a {@link Connection} object to interact with the database, or {@code null} if the connection fails
    */
   public Connection connect() {
      Connection conn = null;
      try {
         String URL = "jdbc:sqlite:game.db";
         conn = DriverManager.getConnection(URL);
         System.out.println("Connection to SQLite has been established.");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return conn;
   }

}