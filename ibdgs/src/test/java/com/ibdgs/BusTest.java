package com.ibdgs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    // B1 - busID must be exactly 8 digits

    @Test
    void validBusID() {
        assertTrue(Bus.validateBusID("10234567"));
    }

    @Test
    void busIDWithLetter() {
        assertFalse(Bus.validateBusID("1234567A"));
    }

    @Test
    void busIDTooShort() {
        assertFalse(Bus.validateBusID("1234567"));
    }

    // B2 - capacity cant increase during an update

    @Test
    void capacityDecreaseAllowed() {
        Bus bus = new Bus("10000001", 50, 80.0, "Diesel");
        assertTrue(bus.isValidCapacityUpdate(40));
    }

    @Test
    void capacityIncreaseNotAllowed() {
        Bus bus = new Bus("10000002", 40, 80.0, "Diesel");
        assertFalse(bus.isValidCapacityUpdate(60));
    }

    @Test
    void capacityUnchangedAllowed() {
        // staying the same isnt an increase so it should pass
        Bus bus = new Bus("10000003", 50, 80.0, "Diesel");
        assertTrue(bus.isValidCapacityUpdate(50));
    }

    // B3 - drivers over 50 cant drive buses with 50+ seats

    @Test
    void youngDriverLargeBusAllowed() {
        Bus bus = new Bus("20000001", 60, 80.0, "Diesel");
        assertTrue(bus.allowsDriverAge(40));
    }

    @Test
    void oldDriverLargeBusNotAllowed() {
        Bus bus = new Bus("20000002", 50, 80.0, "Diesel");
        assertFalse(bus.allowsDriverAge(55));
    }

    @Test
    void boundaryDriverAge50Allowed() {
        // rule is "older than 50" so age 50 should still be fine
        Bus bus = new Bus("20000003", 50, 80.0, "Diesel");
        assertTrue(bus.allowsDriverAge(50));
    }

    // B4 - need at least 5 years experience for electric buses

    @Test
    void experiencedDriverElectricAllowed() {
        Bus bus = new Bus("30000001", 40, 80.0, "Electricity");
        assertTrue(bus.allowsExperience(8));
    }

    @Test
    void inexperiencedDriverElectricNotAllowed() {
        Bus bus = new Bus("30000002", 40, 80.0, "Electricity");
        assertFalse(bus.allowsExperience(3));
    }

    @Test
    void boundaryExactly5YearsAllowed() {
        // exactly 5 years should be enough
        Bus bus = new Bus("30000003", 40, 80.0, "Electricity");
        assertTrue(bus.allowsExperience(5));
    }

    // B5 - electric and hybrid buses need Heavy or PublicTransport licence

    @Test
    void heavyLicenceElectricAllowed() {
        Bus bus = new Bus("40000001", 40, 80.0, "Electricity");
        assertTrue(bus.allowsLicence("Heavy"));
    }

    @Test
    void lightLicenceHybridNotAllowed() {
        Bus bus = new Bus("40000002", 40, 80.0, "Hybrid");
        assertFalse(bus.allowsLicence("Light"));
    }

    @Test
    void licenceRestrictionDoesNotApplyToDiesel() {
        // B5 only applies to electric and hybrid, diesel is fine with any licence
        Bus bus = new Bus("40000003", 40, 80.0, "Diesel");
        assertTrue(bus.allowsLicence("Light"));
    }
}