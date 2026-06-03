package com.ibdgs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// handles storing and loading buses from a txt file
// each line is: busID|capacity|fuelLevel|fuelType seperation by '|'
public class BusRepository {

    private final String filePath;
    private final List<Bus> buses = new ArrayList<>();

    public BusRepository(String filePath) {
        this.filePath = filePath;
        load();
    }

    // adds a new bus - throws exception if the ID already exists (B1)
    public void add(Bus bus) {
        if (bus == null) throw new IllegalArgumentException("Bus cannot be null");
        if (retrieve(bus.getBusID()) != null) {
            throw new IllegalArgumentException("A bus with that ID already exists");
        }
        buses.add(bus);
        save();
    }

    // updates an existing bus - capacity cant increase (B2)
    public void update(Bus updated) {
        if (updated == null) throw new IllegalArgumentException("Bus cannot be null");
        Bus existing = retrieve(updated.getBusID());
        if (existing == null) {
            throw new IllegalArgumentException("No bus found with that ID");
        }
        // B2 - make sure capacity isnt going up
        if (!existing.isValidCapacityUpdate(updated.getCapacity())) {
            throw new IllegalArgumentException("Cannot increase capacity during update");
        }
        existing.setCapacity(updated.getCapacity());
        existing.setFuelLevel(updated.getFuelLevel());
        existing.setFuelType(updated.getFuelType());
        save();
    }

    // finds a bus by ID, returns null if not found
    public Bus retrieve(String busID) {
        for (Bus b : buses) {
            if (b.getBusID().equals(busID)) return b;
        }
        return null;
    }

    // returns how many buses are stored in the system 
    public int count() {
        return buses.size();
    }

    // reads buses from the txt file into memory
    private void load() {
        buses.clear();
        if (!Files.exists(Paths.get(filePath))) return;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("\\|");
                if (parts.length != 4) continue;
                String busID = parts[0];
                int capacity = Integer.parseInt(parts[1]);
                double fuelLevel = Double.parseDouble(parts[2]);
                String fuelType = parts[3];
                buses.add(new Bus(busID, capacity, fuelLevel, fuelType));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read file: " + filePath, e);
        }
    }

    // writes all buses back to the txt file
    private void save() {
        try {
            java.nio.file.Path path = Paths.get(filePath);
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            List<String> lines = new ArrayList<>();
            for (Bus b : buses) lines.add(b.toString());
            Files.write(path, lines);
        } catch (IOException e) {
            throw new RuntimeException("Could not write file: " + filePath, e);
        }
    }
}