package dms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Class: Main
 * Purpose: Console entry point for the Game Character DMS.
 * - Menu for CRUD + search + Top-N
 * - Safe input handling (won’t crash on bad input)
 * - Early duplicate checks for Add
 * - Validates ID exists first for Update/Remove (per instructor feedback)
 * - Loads CSV by reading a file and using Character.fromCsv
 * Author: Zekia Bey
 * Date: October 2025
 */
public class Main {

    private static final Scanner IN = new Scanner(System.in);
    private static final CharacterManager manager = new CharacterManager();
    private static final ReportGenerator reporter = new ReportGenerator();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = readInt("Choose: ", 0, 7);
            switch (choice) {
                case 1 -> loadCsv();
                case 2 -> addCharacter();
                case 3 -> updateCharacter();
                case 4 -> removeCharacter();
                case 5 -> listActive();
                case 6 -> search();
                case 7 -> topN();
                case 0 -> { System.out.println("Goodbye!"); return; }
            }
        }
    }

    // --------------------- MENU ACTIONS ---------------------

    private static void loadCsv() {
        System.out.print("CSV path: ");
        String path = IN.nextLine().trim();
        int added = 0, skipped = 0;

        try {
            for (String line : Files.readAllLines(Path.of(path))) {
                if (line.isBlank()) continue;
                Character c = Character.fromCsv(line);
                if (c == null) { skipped++; continue; }
                if (manager.add(c)) added++; else skipped++;
            }
            System.out.printf("Loaded: %d added, %d skipped.%n", added, skipped);
        } catch (IOException ex) {
            System.out.println("Error reading file: " + ex.getMessage());
        }
    }

    private static void addCharacter() {
        System.out.println("=== Add Character ===");

        // ID first – reject early if duplicate
        int id = readInt("id: ", 1, Integer.MAX_VALUE);
        if (manager.findById(id).isPresent()) {
            System.out.println("Add failed (duplicate id).");
            return;
        }

        // Handle next – also reject early if duplicate
        String handle = readNonEmpty("handle: ");
        if (manager.findByHandle(handle).isPresent()) {
            System.out.println("Add failed (duplicate handle).");
            return;
        }

        Server server = readServer("server (NA/EU/AS): ");
        String occupation = readNonEmpty("occupation: ");
        int wl = readInt("wantedLevel (0..6): ", 0, 6);
        int bounty = readInt("bountyCents (>=0): ", 0, Integer.MAX_VALUE);
        int rep = readInt("reputation (-100..100): ", -100, 100);
        boolean active = readBoolean("active (true/false): ");

        Character c = new Character(id, handle, server, occupation, wl, bounty, rep, active);
        if (manager.add(c)) {
            System.out.println("Add successful.");
        } else {
            System.out.println("Add failed (validation/duplicate).");
        }
    }

    private static void updateCharacter() {
        System.out.println("=== Update Character ===");

        // Validate the ID FIRST (addresses instructor feedback)
        int id = readInt("Enter existing id to update: ", 1, Integer.MAX_VALUE);
        Optional<Character> existingOpt = manager.findById(id);
        if (existingOpt.isEmpty()) {
            System.out.println("Update failed (id not found).");
            return;
        }
        Character existing = existingOpt.get();
        System.out.println("Found: " + existing);

        // Prompt for new values
        String handle = readNonEmpty("new handle: ");

        // Prevent handle collision with some *other* character
        Optional<Character> byHandle = manager.findByHandle(handle);
        if (byHandle.isPresent() && byHandle.get().getId() != id) {
            System.out.println("Update failed (handle belongs to another id).");
            return;
        }

        Server server = readServer("new server (NA/EU/AS): ");
        String occupation = readNonEmpty("new occupation: ");
        int wl = readInt("new wantedLevel (0..6): ", 0, 6);
        int bounty = readInt("new bountyCents (>=0): ", 0, Integer.MAX_VALUE);
        int rep = readInt("new reputation (-100..100): ", -100, 100);
        boolean active = readBoolean("new active (true/false): ");

        Character updated = new Character(id, handle, server, occupation, wl, bounty, rep, active);
        if (manager.update(updated)) {
            System.out.println("Update successful.");
        } else {
            System.out.println("Update failed.");
        }
    }

    private static void removeCharacter() {
        System.out.println("=== Remove Character ===");
        int id = readInt("id to remove: ", 1, Integer.MAX_VALUE);
        if (manager.remove(id)) {
            System.out.println("Removed.");
        } else {
            System.out.println("Nothing removed (id not found).");
        }
    }

    private static void listActive() {
        System.out.println("=== Active Characters ===");
        List<Character> all = manager.getAll();
        if (all.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        all.stream().filter(Character::isActive).forEach(System.out::println);
    }

    private static void search() {
        System.out.println("=== Search ===");
        System.out.println("(1) by id   (2) by handle");
        int c = readInt("choose: ", 1, 2);
        if (c == 1) {
            int id = readInt("id: ", 1, Integer.MAX_VALUE);
            manager.findById(id).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("No match")
            );
        } else {
            String h = readNonEmpty("handle: ");
            manager.findByHandle(h).ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("No match")
            );
        }
    }

    private static void topN() {
        System.out.println("=== Top-N Most Wanted ===");
        int n = readInt("N: ", 1, Integer.MAX_VALUE);
        List<ThreatEntry> top = reporter.topN(n, manager.getAll());
        if (top.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        top.forEach(System.out::println);
    }

    // --------------------- INPUT HELPERS ---------------------

    private static void printMenu() {
        System.out.println();
        System.out.println("=== Game Character DMS ===");
        System.out.println("(1) Load CSV");
        System.out.println("(2) Add");
        System.out.println("(3) Update");
        System.out.println("(4) Remove (Archive)");
        System.out.println("(5) List Active");
        System.out.println("(6) Search");
        System.out.println("(7) Top-N Most Wanted");
        System.out.println("(0) Exit");
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine().trim();
            try {
                int v = Integer.parseInt(s);
                if (v < min || v > max) {
                    System.out.printf("Enter a number in range [%d..%d].%n", min, max);
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine().trim();
            if (!s.isBlank()) return s;
            System.out.println("Value required.");
        }
    }

    private static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine().trim().toLowerCase();
            if (s.equals("true") || s.equals("t") || s.equals("yes") || s.equals("y")) return true;
            if (s.equals("false") || s.equals("f") || s.equals("no") || s.equals("n")) return false;
            System.out.println("Please enter true/false (or y/n).");
        }
    }

    private static Server readServer(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = IN.nextLine().trim().toUpperCase();
            try {
                return Server.valueOf(s); // NA, EU, AS
            } catch (IllegalArgumentException e) {
                System.out.println("Please enter one of: NA, EU, AS");
            }
        }
    }
}

