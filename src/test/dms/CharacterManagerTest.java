package dms;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CharacterManager (Phase 2)
 * - Verifies add, update, remove, findById/findByHandle, isActive, file open (CSV),
 *   and ReportGenerator.topN/toCsv.
 * - Uses temp CSV to satisfy the “open a file” rubric item.
 */
public class CharacterManagerTest {

    private CharacterManager manager;

    @BeforeEach
    void setup() {
        manager = new CharacterManager();
    }

    // ---------- helpers ----------

    private Character mk(int id, String handle, Server server, String occ,
                         int wl, int bounty, int rep, boolean active) {
        return new Character(id, handle, server, occ, wl, bounty, rep, active);
    }

    // ---------- core tests ----------

    @Test
    void testAddCharacter() {
        Character c = mk(1, "Doofnita", Server.NA, "Troll", 3, 500, 80, true);
        assertTrue(manager.add(c), "manager.add() should return true when adding new");
        assertEquals(1, manager.getAll().size(), "Should have one character after add");
        assertEquals("Doofnita", manager.findById(1).get().getHandle());
    }

    @Test
    void testUpdateCharacter() {
        Character c = mk(2, "AnitaBath", Server.NA, "Catfish", 5, 1000, 95, true);
        manager.add(c);
        c.setReputation(99);
        assertTrue(manager.update(c));
        assertEquals(99, manager.findById(2).get().getReputation());
    }

    @Test
    void testRemoveCharacter() {
        Character c = mk(3, "LouLou", Server.NA, "Gangster", 2, 200, 40, true);
        manager.add(c);
        assertTrue(manager.remove(3));
        assertTrue(manager.findById(3).isEmpty(), "Character should be removed");
    }

    @Test
    void testFindByHandle() {
        Character c = mk(4, "Madoogin", Server.NA, "Streamer", 1, 300, 60, true);
        manager.add(c);
        assertTrue(manager.findByHandle("Madoogin").isPresent());
    }

    @Test
    void testIsActiveStatus() {
        Character c = mk(5, "InactivePlayer", Server.NA, "NPC", 0, 0, 0, false);
        manager.add(c);
        assertFalse(manager.findById(5).get().isActive());
    }

    @Test
    void testLoadFromCsvAndToCsv() throws IOException {
        // --- create a temp CSV (this is the “open a file” verification) ---
        Path tmp = Files.createTempFile("characters", ".csv");
        Files.write(tmp, List.of(
                "1,Doofnita,NA,Troll,3,500,80,true",
                "2,AnitaBath,NA,Catfish,5,1000,95,true"
        ));

        // Open & read the file, load characters
        for (String line : Files.readAllLines(tmp)) {
            Character c = Character.fromCsv(line);
            if (c != null) manager.add(c);
        }
        assertEquals(2, manager.getAll().size(), "Should load 2 records from CSV");

        // Export a Top-N report to CSV and verify something was written
        ReportGenerator rg = new ReportGenerator();
        Path out = Files.createTempFile("characters_out", ".csv");
        List<ThreatEntry> top = rg.topN(1, manager.getAll());
        String csv = rg.toCsv(top);
        Files.writeString(out, csv);
        assertTrue(Files.size(out) > 0, "Exported CSV should not be empty");
    }

    @Test
    void testReportGeneratorTopN() {
        Character c1 = mk(10, "Anita",   Server.NA, "Catfish", 5, 1000, 90, true);
        Character c2 = mk(11, "LouLou",  Server.NA, "Gangster",3,  500, 60, true);
        Character c3 = mk(12, "Doofnita",Server.NA, "Troll",   1,  200, 40, true);

        List<Character> list = Arrays.asList(c1, c2, c3);
        ReportGenerator rg = new ReportGenerator();
        List<ThreatEntry> top = rg.topN(2, list);

        assertEquals(2, top.size(), "Should return top 2 characters by threat score");
        assertTrue(top.get(0).getThreatScore() >= top.get(1).getThreatScore());
    }
}


