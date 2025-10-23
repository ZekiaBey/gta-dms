package dms;

import java.util.Optional;

/**
 * Enum: Server
 * Purpose: Represents which game server a character belongs to.
 * Values: NA (North America), EU (Europe), AS (Asia)
 * Author: Zekia Beyene
 * Date: October 2025
 */
public enum Server {
    NA, EU, AS;

    public static Optional<Server> tryParse(String s) {
        if (s == null) return Optional.empty();
        switch (s.trim().toUpperCase()) {
            case "NA": return Optional.of(NA);
            case "EU": return Optional.of(EU);
            case "AS": return Optional.of(AS);
            default: return Optional.empty();
        }
    }
}

