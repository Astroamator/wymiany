/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import pl.currency.exchange.utils.PeselUtil;

/**
 *
 * @author rober
 */
public class PeselValidator implements ConstraintValidator<ValidPesel, String> {

    @Override
    public boolean isValid(String t, ConstraintValidatorContext cvc) {
        return t != null && PeselUtil.isValid(t);
    }

}
