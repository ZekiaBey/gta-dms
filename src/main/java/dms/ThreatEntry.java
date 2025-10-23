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

    public ThreatEntry(Character c, int score) {
        this.character = c;
        this.threatScore = score;
    }

    public Character getCharacter() { return character; }
    public int getThreatScore() { return threatScore; }

    @Override
    public int compareTo(ThreatEntry o) {
        return Integer.compare(o.threatScore, this.threatScore);
    }

    @Override
    public String toString() {
        return String.format("%d %s WL=%d Bounty=%d Rep=%d Score=%d",
                character.getId(), character.getHandle(), character.getWantedLevel(),
                character.getBountyCents(), character.getReputation(), threatScore);
    }
}
