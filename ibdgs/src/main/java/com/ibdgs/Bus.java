package com.ibdgs;

// Bus class - stores bus info and checks the bus-related conditions
public class Bus {

    private String busID;
    private int capacity;
    private double fuelLevel; // Really don't know why we have this...
    private String fuelType; // Diesel, Hybrid or Electricity

    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        if (!validateBusID(busID)) {
            throw new IllegalArgumentException("Invalid Bus ID");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (!isValidFuelType(fuelType)) {
            throw new IllegalArgumentException("Invalid fuel type");
        }
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    // B1 - busID has to be exactly 8 digits
    public static boolean validateBusID(String id) {
        if (id == null || id.length() != 8) return false;
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    //  B2 -  checks capacity doesnt increase during an update
    public boolean isValidCapacityUpdate(int newCapacity) {
        return newCapacity <= this.capacity;
    }

    // B3 - drivers over 50 cant drive buses with 50+ seats
    public boolean allowsDriverAge(int driverAge) {
        return !(driverAge > 50 && this.capacity >= 50);
    }

    // B4 - need at least 5 years experience to drive an electric bus
    public boolean allowsExperience(int experienceYears) {
        return !("Electricity".equals(this.fuelType) && experienceYears < 5);
    }

    // B5 - only Heavy or PublicTransport licences can drive electric or hybrid
    public boolean allowsLicence(String licenceType) {
        if ("Electricity".equals(fuelType) || "Hybrid".equals(fuelType)) {
            return "Heavy".equals(licenceType) || "PublicTransport".equals(licenceType);
        }
        return true;
    }

    private static boolean isValidFuelType(String ft) {
        return "Diesel".equals(ft) || "Hybrid".equals(ft) || "Electricity".equals(ft);
    }

    public String getBusID() { return busID; }
    public int getCapacity() { return capacity; }
    public double getFuelLevel() { return fuelLevel; }
    public String getFuelType() { return fuelType; }

    public void setCapacity(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = capacity;
    }
    // Var isn't used but assignment states so 
    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setFuelType(String fuelType) {
        if (!isValidFuelType(fuelType)) throw new IllegalArgumentException("Invalid fuel type");
        this.fuelType = fuelType;
    }

    // formats bus record on one line for ease of storage in TXT file
    @Override
    public String toString() {
        return busID+"|"+capacity+"|"+fuelLevel+"|"+fuelType;
    }
}