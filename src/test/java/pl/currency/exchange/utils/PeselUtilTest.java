/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rober
 */
public class PeselUtilTest {

    @Test
    public void isValid_InvalidPesel_ShouldReturnFalse() {
        assertFalse(PeselUtil.isValid("12345678901"));
    }

    @Test
    public void isValid_ValidPesel_ShouldReturnTrue() {
        assertTrue(PeselUtil.isValid("18260590061"));
    }

    @Test
    public void isValid_InvalidPeselChars_ShouldReturnFalse() {
        assertFalse(PeselUtil.isValid("ASC123ASC12"));

    }

    @Test
    public void getBirthDate_ValidPeselAfter1900_ShouldReturnValidDate() {
        LocalDate result = PeselUtil.getBirthDateFromPesel("02070803628").orElse(LocalDate.now());
        LocalDate birthDate = LocalDate.of(1902, 7, 8);
        long daysBetween = ChronoUnit.DAYS.between(birthDate, result);
        assertEquals(0, daysBetween);
    }

    @Test
    public void getBirthDate_ValidPeselAfter2000_ShouldReturnValidDate() {
        LocalDate result = PeselUtil.getBirthDateFromPesel("08210113914").orElse(LocalDate.now());
        LocalDate birthDate = LocalDate.of(2008, 1, 1);
        long daysBetween = ChronoUnit.DAYS.between(birthDate, result);
        assertEquals(0, daysBetween);
    }

    @Test
    public void isAdult_NotAdultPesel_ShouldReturnFalse() {
        assertFalse(PeselUtil.isAdult("15210487697"));
    }

    @Test
    public void isAdult_AdultPesel_ShouldReturnTrue() {
        assertTrue(PeselUtil.isAdult("90010453374"));
    }

}
