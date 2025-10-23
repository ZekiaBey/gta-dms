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

    public boolean add(Character c) {
        if (!c.validate().isEmpty()) return false;
        if (findById(c.getId()).isPresent() || findByHandle(c.getHandle()).isPresent())
            return false;
        characters.add(c);
        return true;
    }

    public boolean remove(int id) {
        return characters.removeIf(c -> c.getId() == id);
    }

    public boolean update(Character updated) {
        Optional<Character> existing = findById(updated.getId());
        if (existing.isEmpty()) return false;
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

    public Optional<Character> findById(int id) {
        return characters.stream().filter(c -> c.getId() == id).findFirst();
    }

    public Optional<Character> findByHandle(String handle) {
        return characters.stream().filter(c -> c.getHandle().equalsIgnoreCase(handle)).findFirst();
    }

    public List<Character> getAll() { return characters; }

    public String toCsv() {
        return characters.stream()
                .map(c -> String.join(",", String.valueOf(c.getId()), c.getHandle(),
                        c.getServer().name(), c.getOccupation(), String.valueOf(c.getWantedLevel()),
                        String.valueOf(c.getBountyCents()), String.valueOf(c.getReputation()),
                        String.valueOf(c.isActive())))
                .collect(Collectors.joining("\n"));
    }
}
