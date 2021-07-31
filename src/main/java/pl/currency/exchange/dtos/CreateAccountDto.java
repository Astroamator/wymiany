/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.dtos;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import pl.currency.exchange.validators.ValidPesel;
import pl.currency.exchange.validators.AdultPerson;

/**
 *
 * @author rober
 */
@Data
public class CreateAccountDto {

    @NotBlank(message = "{firstname.notblank}")
    private String firstName;

    @NotBlank(message = "{lastname.notblank}")
    private String lastName;

    @ValidPesel(message = "{pesel.validpesel}")
    @AdultPerson(message = "{pesel.adult}")
    private String pesel;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 19, fraction = 2)
    private BigDecimal balance;

}
