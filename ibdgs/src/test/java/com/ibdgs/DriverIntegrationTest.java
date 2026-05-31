package com.ibdgs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ibdgs.DriverRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for DriverRepository using a real TXT file.
 * Each test starts with a clean file and tears down afterwards.
 */
public class DriverIntegrationTest {

    private static final String TEST_FILE = "data/test-drivers.txt";
    private static final String VALID_ADDRESS = "123|Main St|Melbourne|VIC|Australia";

    @BeforeEach
    void setup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    void validDriverStoredCorrectly() {
        DriverRepository repo = new DriverRepository(TEST_FILE);
        Driver d = new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000");
        repo.add(d);

        DriverRepository reloaded = new DriverRepository(TEST_FILE);
        assertEquals(1, reloaded.count());
        Driver retrieved = reloaded.retrieve("23!!@@AAAB");
        assertNotNull(retrieved);
        assertEquals("John", retrieved.getName());
        assertEquals("Light", retrieved.getLicenseType());
    }

    @Test
    void invalidDriverRejected() {
        DriverRepository repo = new DriverRepository(TEST_FILE);

        // invalid ID format rejected at construction
        assertThrows(IllegalArgumentException.class, () ->
                new Driver("invalid", "John", 5, "Light", VALID_ADDRESS, "01-01-2000"));

        // duplicate ID rejected at add
        repo.add(new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000"));
        assertThrows(IllegalArgumentException.class, () ->
                repo.add(new Driver("23!!@@AAAB", "Jane", 7, "Heavy", VALID_ADDRESS, "01-01-1990")));
        assertEquals(1, repo.count());
    }

    @Test
    void updatesPersistedCorrectly() {
        DriverRepository repo = new DriverRepository(TEST_FILE);
        Driver d = new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000");
        repo.add(d);

        d.updateLicense("Heavy");
        repo.update(d);

        DriverRepository reloaded = new DriverRepository(TEST_FILE);
        Driver retrieved = reloaded.retrieve("23!!@@AAAB");
        assertNotNull(retrieved);
        assertEquals("Heavy", retrieved.getLicenseType());
    }

    @Test
    void recordCountsUpdatedCorrectly() {
        DriverRepository repo = new DriverRepository(TEST_FILE);
        assertEquals(0, repo.count());

        repo.add(new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000"));
        assertEquals(1, repo.count());

        repo.add(new Driver("34##$$BBBC", "Jane", 7, "Heavy", VALID_ADDRESS, "01-01-1990"));
        assertEquals(2, repo.count());

        DriverRepository reloaded = new DriverRepository(TEST_FILE);
        assertEquals(2, reloaded.count());
    }
}
