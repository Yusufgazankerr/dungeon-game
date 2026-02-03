package com.dungeonGame.database;

import com.dungeonGame.logic.PlayerDataHolder;
import com.dungeonGame.logic.PositionDataHolder;
import com.dungeonGame.logic.mapAndLevelHandler.DungeonMap;
import com.dungeonGame.logic.mapAndLevelHandler.LevelTransitionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Data Access Object (DAO) class for managing user data in the SQLite database.
 * Provides methods to create, insert, update, and retrieve user data.
 */
public class UserDAO {

   SQLiteConnection sqliteConnection = new SQLiteConnection();
   LevelTransitionHandler levelTransitionHandler = new LevelTransitionHandler();

   /**
    * Creates the "users" table in the database if it does not already exist.
    * The table includes the following fields:
    * <ul>
    *   <li>{@code id} - INTEGER, Primary Key, Auto-Incremented</li>
    *   <li>{@code name} - TEXT, Not Null</li>
    *   <li>{@code current_level} - INTEGER</li>
    *   <li>{@code power_points} - INTEGER</li>
    *   <li>{@code current_room} - TEXT</li>
    *   <li>{@code inventory} - TEXT</li>
    * </ul>
    */
   public void createNewTable() {
      String sql = """
              CREATE TABLE IF NOT EXISTS users (
               id INTEGER PRIMARY KEY AUTOINCREMENT,
               name TEXT NOT NULL,
               current_level INTEGER,
               power_points INTEGER,
               current_room TEXT,
               inventory TEXT
              );""";

      try (Connection conn = sqliteConnection.connect();
           PreparedStatement stmt = conn.prepareStatement(sql)) {
         stmt.execute();
         //System.out.println("Table 'users' has been created.");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   /**
    * Inserts a new user into the database.
    *
    * @param name the name of the user
    * @param currentLevelNumber the current level the user is on
    * @param powerPoints the user's current power points
    * @param currentRoom the user's current room
    * @param inventory the user's inventory as a comma-separated string
    */
   public void insertUser(String name, int currentLevelNumber, int powerPoints, String currentRoom, String inventory) {
      String sql = "INSERT INTO users(name, current_level, power_points, current_room, inventory) VALUES(?,?,?,?,?)";

      try (Connection conn = sqliteConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, name);
         pstmt.setInt(2, currentLevelNumber);
         pstmt.setInt(3, powerPoints);
         pstmt.setString(4, currentRoom);
         pstmt.setString(5, inventory);
         pstmt.executeUpdate();
         System.out.println("User has been inserted.");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   /**
    * Updates an existing user's data in the database.
    *
    * @param name the name of the user
    * @param currentLevelNumber the current level the user is on
    * @param powerPoints the user's current power points
    * @param currentRoom the user's current room
    * @param inventory the user's inventory as a comma-separated string
    */
   public void updateUser(String name, int currentLevelNumber, int powerPoints, String currentRoom, String inventory) {
      String sql = "UPDATE users SET current_level = ?, power_points = ?, current_room = ?, inventory = ? WHERE name = ?";

      try (Connection conn = sqliteConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setInt(1, currentLevelNumber);
         pstmt.setInt(2, powerPoints);
         pstmt.setString(3, currentRoom);
         pstmt.setString(4, inventory);
         pstmt.setString(5, name);
         pstmt.executeUpdate();
         System.out.println("User has been updated.");
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
   }

   /**
    * Retrieves a user's data from the database by their name.
    *
    * @param name the name of the user
    * @return a {@link PlayerDataHolder} object containing the user's data, or {@code null} if no user is found
    */
   public PlayerDataHolder getUserByName(String name) {
      String sql = "SELECT * FROM users WHERE name = ?";
      PlayerDataHolder player = null;

      try (Connection conn = sqliteConnection.connect();
           PreparedStatement pstmt = conn.prepareStatement(sql)) {
         pstmt.setString(1, name);
         ResultSet rs = pstmt.executeQuery();

         if (rs.next()) {
            player = new PlayerDataHolder();
            player.setName(rs.getString("name"));
            int currentLevelNumber = rs.getInt("current_level");
            //System.out.println("Retrieved level number: " + currentLevelNumber); // Debug statement
            if (currentLevelNumber < 1 || currentLevelNumber > 3) {
               System.out.println("Invalid level number retrieved, setting to default level 1.");
               currentLevelNumber = 1; // Set to default level 1 if invalid
            }
            char[][] currentLevel = levelTransitionHandler.getLevelByNumber(currentLevelNumber);
            PositionDataHolder pm = new PositionDataHolder(currentLevel);
            DungeonMap.setCurrentLevel(pm, currentLevel, currentLevelNumber, levelTransitionHandler);
            player.setPowerPoints(rs.getInt("power_points"));
            player.setCurrentRoom(rs.getString("current_room"));
            player.setInventory(Collections.singleton(rs.getString("inventory")));
         } else {
            //System.out.println("No user found with name: " + name); // Debug statement
         }
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }

      return player;
   }

}