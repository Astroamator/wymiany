/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import pl.currency.exchange.validators.ValidPesel;

/**
 *
 * @author rober
 */
@Data
public class CreateSubAccountDto {

    @ValidPesel(message = "{pesel.validpesel}")
    private String pesel;

    @NotNull(message = "{currency.notnull}")
    @Size(min = 3, max = 3, message = "{currency.size}")
    private String currency;

}
