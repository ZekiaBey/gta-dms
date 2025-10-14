package dms;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class: CharacterManager
 * Purpose: Central controller for managing Character CRUD operations and searches.
 * Author: Zekia Beyene
 * Date: October 2025
 */
public class CharacterManager {
    private final List<Character> characters = new ArrayList<>();

    // --- CRUD OPERATIONS ---

    /**
     * Adds a character after validating and checking for duplicates.
     */
    public boolean add(Character c) {
        List<String> errors = c.validate();
        if (!errors.isEmpty()) return false;
        if (findById(c.getId()).isPresent() || findByHandle(c.getHandle()).isPresent())
            return false;
        characters.add(c);
        return true;
    }

    /**
     * Updates an existing character if valid and not duplicate.
     */
    public boolean update(Character updated) {
        List<String> errors = updated.validate();
        if (!errors.isEmpty()) return false;
        Optional<Character> existing = findById(updated.getId());
        if (existing.isEmpty()) return false;

        // Prevent handle conflicts
        if (findByHandle(updated.getHandle())
                .filter(c -> c.getId() != updated.getId())
                .isPresent()) return false;

        Character c = existing.get();
        c.setHandle(updated.getHandle());
        c.setServer(updated.getServer());
        c.setOccupation(updated.getOccupation());
        c.setWantedLevel(updated.getWantedLevel());
        c.setBountyCents(updated.getBountyCents());
        c.setReputation(updated.getReputation());
        c.setActive(updated.isActive());
        return true;
    }

    /**
     * Archives a character (logical delete) by marking inactive.
     */
    public boolean archive(int id) {
        Optional<Character> c = findById(id);
        if (c.isEmpty()) return false;
        c.get().setActive(false);
        return true;
    }

    // --- SEARCH OPERATIONS ---

    public Optional<Character> findById(int id) {
        return characters.stream().filter(ch -> ch.getId() == id).findFirst();
    }

    public Optional<Character> findByHandle(String handle) {
        String h = handle == null ? "" : handle.trim().toLowerCase();
        return characters.stream().filter(ch -> ch.getHandle().toLowerCase().equals(h)).findFirst();
    }

    public List<Character> listActive() {
        return characters.stream()
                .filter(Character::isActive)
                .sorted(Comparator.comparing(Character::getId))
                .collect(Collectors.toList());
    }

    /**
     * Searches active characters by handle, server, or occupation (case-insensitive).
     */
    public List<Character> search(String q) {
        String s = q == null ? "" : q.trim().toLowerCase();
        return characters.stream()
                .filter(Character::isActive)
                .filter(c ->
                        c.getHandle().toLowerCase().contains(s) ||
                                c.getServer().name().toLowerCase().contains(s) ||
                                c.getOccupation().toLowerCase().contains(s))
                .sorted(Comparator.comparing(Character::getId))
                .collect(Collectors.toList());
    }

    public int size() { return characters.size(); }

    // --- FILE IMPORT ---

    /**
     * Loads characters from a CSV file. Skips invalid or duplicate lines.
     * @return Status messages of import results.
     */
    public List<String> loadFromFile(String path) {
        List<String> messages = new ArrayList<>();
        int added = 0, skipped = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                Character c = Character.fromCsv(line);
                if (c == null || !add(c)) skipped++; else added++;
            }
            messages.add("Loaded: " + added + " added, " + skipped + " skipped.");
        } catch (Exception ex) {
            messages.add("Error reading file: " + ex.getMessage());
        }
        return messages;
    }
}
