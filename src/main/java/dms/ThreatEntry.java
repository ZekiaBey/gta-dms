package dms;

/**
 * Class: ThreatEntry
 * Purpose: Represents a ranked entry for a "most wanted" report.
 * Comparable by threatScore to enable sorting.
 * Author: Zekia Beyene
 * Date: October 2025
 */
public class ThreatEntry implements Comparable<ThreatEntry> {
    private final Character character;
    private final int threatScore;

    public ThreatEntry(Character character, int threatScore) {
        this.character = character;
        this.threatScore = threatScore;
    }

    public Character getCharacter() { return character; }
    public int getThreatScore() { return threatScore; }

    @Override
    public int compareTo(ThreatEntry o) {
        // Sort descending by threatScore
        return Integer.compare(o.threatScore, this.threatScore);
    }

    @Override
    public String toString() {
        return String.format("%5d %-12s WL=%d bounty=%d rep=%d score=%d",
                character.getId(),
                character.getHandle(),
                character.getWantedLevel(),
                character.getBountyCents(),
                character.getReputation(),
                threatScore);
    }
}
