README.txt  

Project Title: GTA-DMS (Game Character Data Management System) Phase 2
Author: Zekia Beyene  
Course: CEN 3024C Software Development I  
Date: 10/20/2025  


Project Description

This console-based Java application manages GTA RP character data.

The system allows you to:
- Add, update, remove, and search characters
- Load data from a CSV text file
- Generate a Top-N Most Wanted report
- Validate inputs and prevent duplicates
- Run automated JUnit tests to verify functionality


Instructions on running the program

1. Open the project in IntelliJ IDEA or any Java IDE.
2. Run the main entry file:
 src/dms/Main.java
3. OR launch using the executable JAR file:
 java -jar gta-dms.jar

How to Run Unit Tests

1. Locate the test file:
 src/test/dms/CharacterManagerTest.java
2. Right-click the file and choose:
 "Run 'CharacterManagerTest'"
3. Verify that all seven tests pass (green checkmarks).
 One test can be modified temporarily to show a failing case for your demo video.

Data File

File: characters.txt 
Contains at least 20 character records with these fields:
id, handle, server, occupation, wantedLevel, bountyCents, reputation, active

Project Structure

.idea/
out/
src/
 ├── main/java/dms/
 │ ├── Main.java
 │ ├── Character.java
 │ ├── CharacterManager.java
 │ ├── ReportGenerator.java
 │ ├── Server.java
 │ └── ThreatEntry.java
 └── test/java/dms/
 └── CharacterManagerTest.java
characters.txt
README.txt
gta-dms.iml


   
