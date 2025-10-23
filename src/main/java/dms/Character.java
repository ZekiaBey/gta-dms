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

    /** Validate all fields and return a list of issues (empty = valid). */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (id <= 0) errors.add("Invalid ID");
        if (handle == null || handle.isBlank()) errors.add("Handle required");
        if (server == null) errors.add("Server required");
        if (wantedLevel < 0) errors.add("Wanted level cannot be negative");
        if (bountyCents < 0) errors.add("Bounty cannot be negative");
        return errors;
    }

    public static Character fromCsv(String line) {
        try {
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
        } catch (Exception e) {
            System.err.println("Failed to parse line: " + line);
            return null;
        }
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public String getHandle() { return handle; }
    public Server getServer() { return server; }
    public String getOccupation() { return occupation; }
    public int getWantedLevel() { return wantedLevel; }
    public int getBountyCents() { return bountyCents; }
    public int getReputation() { return reputation; }
    public boolean isActive() { return active; }

    public void setHandle(String h) { this.handle = h; }
    public void setServer(Server s) { this.server = s; }
    public void setOccupation(String o) { this.occupation = o; }
    public void setWantedLevel(int w) { this.wantedLevel = w; }
    public void setBountyCents(int b) { this.bountyCents = b; }
    public void setReputation(int r) { this.reputation = r; }
    public void setActive(boolean a) { this.active = a; }

    @Override
    public String toString() {
        return String.format("%d %-10s %-2s WL=%d Bounty=%d Rep=%d %s",
                id, handle, server, wantedLevel, bountyCents, reputation,
                active ? "Active" : "Inactive");
    }
}

