# Advanced OOP 6 — Dungeon Game

A console-based dungeon crawler written in Java. The player explores a three-level dungeon, manages power points and inventory, encounters randomized events, and can save/load progress using a local SQLite database.

## Overview
The game starts by prompting for a player name, loads existing progress from a local SQLite database if available, and places the player into a grid-based dungeon. The player navigates room-to-room, discovers items, triggers encounters, and advances through dungeon levels by finding exits.

## Features
- Three dungeon levels with entrances (`E`), exits (`X`), walls (`W`), and walkable rooms.
- Player movement, room searching, and inventory management.
- Randomized encounters including traps, mad scientists, a lost explorer (level 2), and a guardian boss (level 3).
- Items and potions, including a relic required for late-game progression.
- Persistent save data using SQLite (`game.db`).
- Unit testing with JUnit and Mockito.

## Controls
The game accepts text-based commands (case-insensitive), including:
- Movement: `up`, `down`, `left`, `right` (and variants such as `go up`)
- Search: `look around`, `search`
- Inventory: `inventory`, `open inventory`
- Exit and save: `exit`

## Requirements
- Java 23 (as configured in `pom.xml`)
- Maven

## Running the Game
Recommended via an IDE (e.g. IntelliJ IDEA):
- Run `com.dungeonGame.Main`

From the command line:
1. Build and test:
   ```bash
   mvn test

   ## Notes
This project was developed as part of an Advanced Object-Oriented Programming module.
