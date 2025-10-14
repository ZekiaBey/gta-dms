package dms;

import java.util.ArrayList;
import java.util.List;

/**
 * Class: Character
 * Purpose: Represents a game character record within the database management system.
 * Handles data validation and CSV import formatting.
 * Author: Zekia Beyene
 * Date: October 2025
 */
public class Character {
    private int id;
    private String handle;
    private Server server;
    private String occupation;
    private int wantedLevel;
    private int bountyCents;
    private int reputation;
    private boolean active;

    // Constructor for all fields
    public Character(int id, String handle, Server server, String occupation,
                     int wantedLevel, int bountyCents, int reputation, boolean active) {
        this.id = id;
        this.handle = handle;
        this.server = server;
        this.occupation = occupation;
        this.wantedLevel = wantedLevel;
        this.bountyCents = bountyCents;
        this.reputation = reputation;
        this.active = active;
    }

    /**
     * Validates the character fields according to project rubric.
     * Ensures numeric ranges and required fields are met.
     */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (id <= 0) errors.add("id must be > 0");
        if (handle == null || handle.trim().isEmpty()) errors.add("handle is required");
        if (server == null) errors.add("server must be NA/EU/AS");
        if (wantedLevel < 0 || wantedLevel > 6) errors.add("wantedLevel must be 0..6");
        if (bountyCents < 0) errors.add("bountyCents must be >= 0");
        if (reputation < -100 || reputation > 100) errors.add("reputation must be -100..100");
        return errors;
    }

    /**
     * Static helper method to create a Character object from a CSV line.
     * Expected order: id,handle,server,occupation,wantedLevel,bountyCents,reputation,active
     */
    public static Character fromCsv(String line) {
        String[] p = line.split(",");
        if (p.length < 8) return null;
        int id = Integer.parseInt(p[0].trim());
        String handle = p[1].trim();
        Server server = Server.valueOf(p[2].trim().toUpperCase());
        String occ = p[3].trim();
        int wl = Integer.parseInt(p[4].trim());
        int bounty = Integer.parseInt(p[5].trim());
        int rep = Integer.parseInt(p[6].trim());
        boolean active = Boolean.parseBoolean(p[7].trim());
        return new Character(id, handle, server, occ, wl, bounty, rep, active);
    }

    // --- Getters and Setters (void setters for simplicity) ---
    public int getId() { return id; }
    public String getHandle() { return handle; }
    public Server getServer() { return server; }
    public String getOccupation() { return occupation; }
    public int getWantedLevel() { return wantedLevel; }
    public int getBountyCents() { return bountyCents; }
    public int getReputation() { return reputation; }
    public boolean isActive() { return active; }

    public void setHandle(String handle) { this.handle = handle; }
    public void setServer(Server server) { this.server = server; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public void setWantedLevel(int wantedLevel) { this.wantedLevel = wantedLevel; }
    public void setBountyCents(int bountyCents) { this.bountyCents = bountyCents; }
    public void setReputation(int reputation) { this.reputation = reputation; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return String.format("%4d %-12s %s %-8s WL=%d bounty=%d rep=%d %s",
                id, handle, server, occupation, wantedLevel, bountyCents, reputation,
                active ? "active" : "inactive");
    }
}

