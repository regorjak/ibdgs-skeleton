package com.ibdgs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

// integration tests for BusRepository - uses a TXT tile each time
public class BusIntegrationTest {

    private static final String TEST_FILE = "data/test-buses.txt";

    @BeforeEach
    void setup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE));
    }

    @Test
    void validBusStoredCorrectly() {
        BusRepository repo = new BusRepository(TEST_FILE);
        Bus b = new Bus("11111111", 50, 85.0, "Diesel");
        repo.add(b);

        // reload from the file to make sure it actually saved
        BusRepository reloaded = new BusRepository(TEST_FILE);
        assertEquals(1, reloaded.count());
        Bus retrieved = reloaded.retrieve("11111111");
        assertNotNull(retrieved);
        assertEquals(50, retrieved.getCapacity());
        assertEquals("Diesel", retrieved.getFuelType());
    }

    @Test
    void invalidBusRejected() {
        BusRepository repo = new BusRepository(TEST_FILE);

        // bad ID should be caught at construction of variable
        assertThrows(IllegalArgumentException.class, () ->
                new Bus("ABCDEFGH", 40, 80.0, "Diesel"));

        // adding the same ID twice should fail as it doesnt allow for duplicates
        repo.add(new Bus("22222222", 40, 80.0, "Diesel"));
        assertThrows(IllegalArgumentException.class, () ->
                repo.add(new Bus("22222222", 30, 50.0, "Hybrid")));
        assertEquals(1, repo.count());
    }

    @Test
    void updatesPersistedCorrectly() {
        BusRepository repo = new BusRepository(TEST_FILE);
        repo.add(new Bus("33333333", 50, 80.0, "Diesel"));

        // decreasing capacity and changing fuel type should work fine
        repo.update(new Bus("33333333", 40, 65.0, "Hybrid"));

        // trying to increase capacity should throw (B2)
        assertThrows(IllegalArgumentException.class, () ->
                repo.update(new Bus("33333333", 99, 65.0, "Hybrid")));

        // reload and check the valid update actually saved
        BusRepository reloaded = new BusRepository(TEST_FILE);
        Bus retrieved = reloaded.retrieve("33333333");
        assertNotNull(retrieved);
        assertEquals(40, retrieved.getCapacity());
        assertEquals("Hybrid", retrieved.getFuelType());
    }

    @Test
    void recordCountsUpdatedCorrectly() {
        BusRepository repo = new BusRepository(TEST_FILE);
        assertEquals(0, repo.count());

        repo.add(new Bus("44444444", 40, 80.0, "Diesel"));
        assertEquals(1, repo.count());

        repo.add(new Bus("55555555", 30, 60.0, "Electricity"));
        assertEquals(2, repo.count());

        // reload and make sure count is still right
        BusRepository reloaded = new BusRepository(TEST_FILE);
        assertEquals(2, reloaded.count());
    }
}