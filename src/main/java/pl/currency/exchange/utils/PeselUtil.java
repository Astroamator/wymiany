/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author rober
 */
@Slf4j
public class PeselUtil {

    // wagi dla cyfr
    private static final int[] weight = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

    // 0 - lata 1900, 1 - lata 2000, 2 - lata 2100, 3 - lata 2200, 4 - lata 1800
    private static final int[] centuries = {1900, 2000, 2100, 2200, 1800};

    public static boolean isValid(String peselString) {
        boolean result = false;
        try {
            if (hasCorrectFormat(peselString)) {
                int[] peselDigits = convertStringToIntArray(peselString);
                int sum = 0;
                for (int i = 0; i < 10; i++) {
                    sum += peselDigits[i] * weight[i];
                }
                int modulo = sum % 10;
                int lastDigit = 10 - modulo;
                result = (lastDigit == peselDigits[10]);
            }
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return result;
    }

    public static Optional<LocalDate> getBirthDateFromPesel(String peselString) {
        Optional<LocalDate> resultDate = Optional.empty();
        try {
            if (isValid(peselString)) {
                int year = Integer.parseInt(peselString.substring(0, 2));
                int month = Integer.parseInt(peselString.substring(2, 4));
                int day = Integer.parseInt(peselString.substring(4, 6));

                int century = month / 20;
                month %= 20;
                year += centuries[century];
                resultDate = Optional.of(LocalDate.of(year, month, day));
            }
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return resultDate;
    }

    public static boolean isAdult(String peselString) {
        LocalDate birthDate = PeselUtil.getBirthDateFromPesel(peselString).orElse(LocalDate.now());
        return (ChronoUnit.YEARS.between(birthDate, LocalDate.now())) >= 18;
    }

    private static int[] convertStringToIntArray(String inputString) {
        int[] result = new int[inputString.length()];
        for (int i = 0; i < inputString.length(); i++) {
            result[i] = Character.getNumericValue(inputString.charAt(i));
        }
        return result;
    }

    private static boolean hasCorrectFormat(String peselString) {
        return peselString.matches("[0-9]{11}");
    }

}
