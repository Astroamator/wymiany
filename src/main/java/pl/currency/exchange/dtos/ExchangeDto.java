/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.dtos;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.Data;
import pl.currency.exchange.validators.ValidPesel;

/**
 *
 * @author rober
 */
@Data
public class ExchangeDto {

    @ValidPesel(message = "{pesel.validpesel}")
    private String pesel;
    
    @NotNull(message = "{from.notnull}")
    private String from;

    @NotNull(message = "{to.notnull}")
    private String to;

    @DecimalMin(value = "0.0", inclusive = false, message = "{amount.min}")
    private BigDecimal amount;

}
