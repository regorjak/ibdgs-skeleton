

import org.junit.jupiter.api.Test;

import com.ibdgs.Driver;

import static org.junit.jupiter.api.Assertions.*;

public class DriverTest {

    private static final String VALID_ADDRESS = "123|Main St|Melbourne|VIC|Australia";

    // D1 - Driver ID Rules
    @Test
    void validDriverID() {
        assertTrue(Driver.validateDriverID("23!!@@AAAB"));
    }

    @Test
    void invalidShortID() {
        assertFalse(Driver.validateDriverID("123"));
    }

    @Test
    void invalidFirstDigit() {
        assertFalse(Driver.validateDriverID("13!!@@AAAB"));
    }

    @Test
    void invalidLastCharLowercase() {
        assertFalse(Driver.validateDriverID("23!!@@AABb"));
    }

    @Test
    void lessThanTwoSpecialChars() {
        assertFalse(Driver.validateDriverID("23!56789AB"));
    }

    @Test
    void edgeCaseExactlyTwoSpecialChars() {
        assertTrue(Driver.validateDriverID("23!!6789AB"));
    }

    @Test
    void emptyStringID() {
        assertFalse(Driver.validateDriverID(""));
    }

    @Test
    void nullID() {
        assertFalse(Driver.validateDriverID(null));
    }

    // D2 - Address Format
    @Test
    void validAddress() {
        assertTrue(Driver.validateAddress("123|Main St|Melbourne|VIC|Australia"));
    }

    @Test
    void invalidAddressMissingParts() {
        assertFalse(Driver.validateAddress("123|Main St|Melbourne"));
    }

    @Test
    void invalidAddressEmptyPart() {
        assertFalse(Driver.validateAddress("123|Main St||VIC|Australia"));
    }

    // D3 - Birthdate Format
    @Test
    void validBirthdate() {
        assertTrue(Driver.validateBirthdate("01-01-2000"));
    }

    @Test
    void invalidBirthdateFormat() {
        assertFalse(Driver.validateBirthdate("2000-01-01"));
    }

    @Test
    void nullBirthdate() {
        assertFalse(Driver.validateBirthdate(null));
    }

    // D4 - License Update Restriction
    @Test
    void licenseUpdateAllowedUnder10Years() {
        Driver d = new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000");
        assertDoesNotThrow(() -> d.updateLicense("Heavy"));
    }

    @Test
    void licenseUpdateBlockedOver10Years() {
        Driver d = new Driver("23!!@@AAAB", "John", 15, "Light", VALID_ADDRESS, "01-01-2000");
        assertThrows(Exception.class, () -> d.updateLicense("Heavy"));
    }

    @Test
    void licenseUpdateAllowedAt10YearsBoundary() {
        Driver d = new Driver("23!!@@AABB", "Tom", 10, "Light", VALID_ADDRESS, "01-01-2000");
        assertDoesNotThrow(() -> d.updateLicense("Heavy"));
    }

    @Test
    void licenseUpdateBlockedAt11YearsBoundary() {
        Driver d = new Driver("23!!@@AABB", "Tom", 11, "Light", VALID_ADDRESS, "01-01-2000");
        assertThrows(Exception.class, () -> d.updateLicense("Heavy"));
    }

    @Test
    void licenseUpdateBlockedAtLargeExperience() {
        Driver d = new Driver("23!!@@AABB", "Tom", 50, "Light", VALID_ADDRESS, "01-01-2000");
        assertThrows(Exception.class, () -> d.updateLicense("Heavy"));
    }

    // D5 - Immutable Fields
    @Test
    void cannotChangeDriverID() {
        Driver d = new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000");
        assertThrows(Exception.class, () -> d.setDriverID("99##$$ZZZZ"));
    }

    @Test
    void cannotChangeName() {
        Driver d = new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000");
        assertThrows(Exception.class, () -> d.setName("Jane"));
    }

    @Test
    void originalFieldsPreservedAfterFailedSet() {
        Driver d = new Driver("23!!@@AAAB", "John", 5, "Light", VALID_ADDRESS, "01-01-2000");
        assertThrows(Exception.class, () -> d.setDriverID("99##$$ZZZZ"));
        assertThrows(Exception.class, () -> d.setName("Jane"));
        assertEquals("23!!@@AAAB", d.getDriverID());
        assertEquals("John", d.getName());
    }

    // Sanity check
    @Test
    void validFullObject() {
        Driver d = new Driver(
            "23!!@@AABB",
            "Alice",
            5,
            "Light",
            VALID_ADDRESS,
            "01-01-2000"
        );
        assertNotNull(d);
    }
}
