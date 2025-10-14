package dms;

import java.util.List;
import java.util.Scanner;

/**
 * Class: Main
 * Purpose: Entry point for the console-based Game Character DMS project.
 * Implements the menu system and connects all other classes.
 * Author: Zekia Beyene
 * Date: October 2025
 */
public class Main {
    private static final Scanner IN = new Scanner(System.in);
    private static final CharacterManager manager = new CharacterManager();
    private static final ReportGenerator reporter = new ReportGenerator();

    public static void main(String[] args) {
        // Seed with demo data for easier testing
        manager.add(new Character(1, "GhostRider", Server.NA, "Gang", 3, 25000, 80, true));
        manager.add(new Character(2, "CaptainRay", Server.EU, "Cop", 5, 91000, 10, true));

        boolean running = true;
        while (running) {
            System.out.println("\n=== Game Character DMS ===");
            System.out.println("(1) Load CSV");
            System.out.println("(2) Add");
            System.out.println("(3) Update");
            System.out.println("(4) Remove (Archive)");
            System.out.println("(5) List Active");
            System.out.println("(6) Search");
            System.out.println("(7) Top-N Most Wanted");
            System.out.println("(0) Exit");
            System.out.print("Choose: ");
            String choice = IN.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> doLoad();
                    case "2" -> doAdd();
                    case "3" -> doUpdate();
                    case "4" -> doArchive();
                    case "5" -> doList();
                    case "6" -> doSearch();
                    case "7" -> doTopN();
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    // --- MENU METHODS ---

    private static void doLoad() {
        System.out.print("CSV path: ");
        String path = IN.nextLine();
        manager.loadFromFile(path).forEach(System.out::println);
    }

    private static void doAdd() {
        Character c = readCharacter(false);
        boolean ok = manager.add(c);
        System.out.println(ok ? "Added." : "Add failed (validation/duplicate).");
    }

    private static void doUpdate() {
        Character c = readCharacter(true);
        boolean ok = manager.update(c);
        System.out.println(ok ? "Updated." : "Update failed (not found/validation/duplicate).");
    }

    private static void doArchive() {
        System.out.print("Enter ID to archive: ");
        int id = Integer.parseInt(IN.nextLine());
        boolean ok = manager.archive(id);
        System.out.println(ok ? "Archived." : "Archive failed (not found).");
    }

    private static void doList() {
        manager.listActive().forEach(System.out::println);
        System.out.println("Total: " + manager.size());
    }

    private static void doSearch() {
        System.out.print("Search text: ");
        String q = IN.nextLine();
        List<Character> res = manager.search(q);
        res.forEach(System.out::println);
        System.out.println("Matches: " + res.size());
    }

    private static void doTopN() {
        System.out.print("N: ");
        int n = Integer.parseInt(IN.nextLine());
        var entries = reporter.topN(n, manager.listActive());
        System.out.println(reporter.toCSV(entries));
    }

    /**
     * Helper method to build a Character from user input.
     */
    private static Character readCharacter(boolean forUpdate) {
        System.out.print("id: ");
        int id = Integer.parseInt(IN.nextLine());
        System.out.print("handle: ");
        String handle = IN.nextLine().trim();
        System.out.print("server (NA/EU/AS): ");
        Server server = Server.valueOf(IN.nextLine().trim().toUpperCase());
        System.out.print("occupation: ");
        String occ = IN.nextLine().trim();
        System.out.print("wantedLevel (0..6): ");
        int wl = Integer.parseInt(IN.nextLine());
        System.out.print("bountyCents: ");
        int bounty = Integer.parseInt(IN.nextLine());
        System.out.print("reputation (-100..100): ");
        int rep = Integer.parseInt(IN.nextLine());
        boolean active = true;
        if (forUpdate) {
            System.out.print("active (true/false): ");
            active = Boolean.parseBoolean(IN.nextLine());
        }
        return new Character(id, handle, server, occ, wl, bounty, rep, active);
    }
}

