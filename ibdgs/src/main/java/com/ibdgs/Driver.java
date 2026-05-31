package com.ibdgs;

public class Driver {
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType;
    private String address;
    private String birthdate;

    public Driver(String driverID, String name, int experienceYears,
                  String licenseType, String address, String birthdate) {

        if (!validateDriverID(driverID)) {
            throw new IllegalArgumentException("Invalid Driver ID");
        }
        if (!validateAddress(address)) {
            throw new IllegalArgumentException("Invalid Address");
        }
        if (!validateBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid Birthdate");
        }

        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    // D1: 10 chars, first two digits 2-9, last two uppercase A-Z, 2+ special chars between positions 3-8
    public static boolean validateDriverID(String id) {
        if (id == null || id.length() != 10) return false;
        if (!Character.isDigit(id.charAt(0)) || id.charAt(0) < '2') return false;
        if (!Character.isDigit(id.charAt(1)) || id.charAt(1) < '2') return false;
        if (!Character.isUpperCase(id.charAt(8))) return false;
        if (!Character.isUpperCase(id.charAt(9))) return false;

        int specialCount = 0;
        for (int i = 2; i < 8; i++) {
            if (!Character.isLetterOrDigit(id.charAt(i))) specialCount++;
        }
        return specialCount >= 2;
    }

    // D2: format must be Street Number|Street Name|City|State|Country
    public static boolean validateAddress(String address) {
        if (address == null) return false;
        String[] parts = address.split("\\|");
        if (parts.length != 5) return false;
        for (String part : parts) {
            if (part.trim().isEmpty()) return false;
        }
        return true;
    }

    // D3: must match DD-MM-YYYY
    public static boolean validateBirthdate(String date) {
        return date != null && date.matches("\\d{2}-\\d{2}-\\d{4}");
    }

    // D4: license cannot be changed if experience > 10 years
    public void updateLicense(String newLicense) {
        if (experienceYears > 10) {
            throw new IllegalArgumentException("Cannot change license");
        }
        this.licenseType = newLicense;
    }

    // D5: driverID and name are immutable
    public void setDriverID(String id) {
        throw new IllegalArgumentException("Driver ID is immutable");
    }

    public void setName(String name) {
        throw new IllegalArgumentException("Name is immutable");
    }

    public void setAddress(String address) {
        if (!validateAddress(address)) {
            throw new IllegalArgumentException("Invalid Address");
        }
        this.address = address;
    }

    public void setBirthdate(String birthdate) {
        if (!validateBirthdate(birthdate)) {
            throw new IllegalArgumentException("Invalid Birthdate");
        }
        this.birthdate = birthdate;
    }

    public void setExperienceYears(int years) {
        this.experienceYears = years;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getDriverID() { return driverID; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getLicenseType() { return licenseType; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }
}
