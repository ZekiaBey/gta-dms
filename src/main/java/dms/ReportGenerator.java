package dms;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class: ReportGenerator
 * Purpose: Calculates the top-N most dangerous (wanted) characters based on weighted score.
 * Author: Zekia Beyene
 * Date: October 2025
 */
public class ReportGenerator {

    /**
     * Calculates a numeric "danger" score using weighted attributes.
     * Formula: wantedLevel * 100 + bountyCents/100 + max(0, -reputation)
     */
    private int score(Character c) {
        int wl = c.getWantedLevel();
        int bounty = c.getBountyCents() / 100;
        int badRep = Math.max(0, -c.getReputation());
        return wl * 100 + bounty + badRep;
    }

    /**
     * Returns a list of top-N ThreatEntries sorted by danger score.
     */
    public List<ThreatEntry> topN(int n, List<Character> characters) {
        return characters.stream()
                .map(c -> new ThreatEntry(c, score(c)))
                .sorted()
                .limit(Math.max(n, 0))
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of ThreatEntries into a printable CSV format.
     */
    public String toCSV(List<ThreatEntry> entries) {
        String header = "id,handle,server,occupation,wantedLevel,bountyCents,reputation,score";
        String body = entries.stream()
                .map(te -> {
                    Character c = te.getCharacter();
                    return String.join(",",
                            String.valueOf(c.getId()),
                            c.getHandle(),
                            c.getServer().name(),
                            c.getOccupation(),
                            String.valueOf(c.getWantedLevel()),
                            String.valueOf(c.getBountyCents()),
                            String.valueOf(c.getReputation()),
                            String.valueOf(te.getThreatScore()));
                })
                .collect(Collectors.joining(System.lineSeparator()));
        return header + System.lineSeparator() + body;
    }
}
