package com.ibdgs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists Driver records to a delimited TXT file (one driver per line).
 * Field separator is ';' so the address (which uses '|' internally) is preserved.
 * Loads from file on construction; saves on every add/update.
 */
public class DriverRepository {

    private final String dataFilePath;
    private final List<Driver> drivers;

    public DriverRepository(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        this.drivers = new ArrayList<>();
        loadFromFile();
    }

    // D1 uniqueness check enforced here
    public void add(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        for (Driver existing : drivers) {
            if (existing.getDriverID().equals(driver.getDriverID())) {
                throw new IllegalArgumentException("Driver ID already exists: " + driver.getDriverID());
            }
        }
        drivers.add(driver);
        saveToFile();
    }

    public Driver retrieve(String driverID) {
        for (Driver d : drivers) {
            if (d.getDriverID().equals(driverID)) {
                return d;
            }
        }
        return null;
    }

    public List<Driver> retrieveAll() {
        return new ArrayList<>(drivers);
    }

    // D5 reinforced here: name change rejected, driverID match is required
    public void update(Driver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        for (int i = 0; i < drivers.size(); i++) {
            Driver existing = drivers.get(i);
            if (existing.getDriverID().equals(driver.getDriverID())) {
                if (!existing.getName().equals(driver.getName())) {
                    throw new IllegalArgumentException("Name cannot be modified");
                }
                drivers.set(i, driver);
                saveToFile();
                return;
            }
        }
        throw new IllegalArgumentException("Driver not found: " + driver.getDriverID());
    }

    public int count() {
        return drivers.size();
    }

    private void loadFromFile() {
        Path path = Paths.get(dataFilePath);
        if (!Files.exists(path)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                String[] f = line.split(";", -1);
                if (f.length != 6) continue;
                try {
                    Driver d = new Driver(f[0], f[1], Integer.parseInt(f[2]), f[3], f[4], f[5]);
                    drivers.add(d);
                } catch (Exception ignored) {
                    // skip malformed line
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load drivers from " + dataFilePath, e);
        }
    }

    private void saveToFile() {
        Path path = Paths.get(dataFilePath);
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (Driver d : drivers) {
                    writer.write(String.format("%s;%s;%d;%s;%s;%s",
                            d.getDriverID(),
                            d.getName(),
                            d.getExperienceYears(),
                            d.getLicenseType(),
                            d.getAddress(),
                            d.getBirthdate()));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save drivers to " + dataFilePath, e);
        }
    }
}
