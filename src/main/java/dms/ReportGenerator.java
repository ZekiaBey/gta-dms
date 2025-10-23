package dms;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class: ReportGenerator
 * Purpose: Calculates the top-N most dangerous (wanted) characters based on weighted score.
 * Author: Zekia Beyene
 * Date: October 2025
 */
public class ReportGenerator {
    private int score(Character c) {
        int wl = c.getWantedLevel();
        int bounty = c.getBountyCents() / 100;
        int badRep = Math.max(0, -c.getReputation());
        return wl * 100 + bounty + badRep;
    }

    public List<ThreatEntry> topN(int n, List<Character> list) {
        return list.stream()
                .map(c -> new ThreatEntry(c, score(c)))
                .sorted(Comparator.comparingInt(ThreatEntry::getThreatScore).reversed())
                .limit(Math.max(n, 0))
                .collect(Collectors.toList());
    }

    public String toCsv(List<ThreatEntry> entries) {
        String header = "id,handle,server,occupation,wantedLevel,bountyCents,reputation,score";
        String body = entries.stream().map(te -> {
            Character c = te.getCharacter();
            return String.join(",", String.valueOf(c.getId()), c.getHandle(), c.getServer().name(),
                    c.getOccupation(), String.valueOf(c.getWantedLevel()),
                    String.valueOf(c.getBountyCents()), String.valueOf(c.getReputation()),
                    String.valueOf(te.getThreatScore()));
        }).collect(Collectors.joining("\n"));
        return header + "\n" + body;
    }
}

