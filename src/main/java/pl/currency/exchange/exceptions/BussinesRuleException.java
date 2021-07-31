/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.exceptions;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author rober
 */
@Getter
@Setter
public class BussinesRuleException extends RuntimeException {

    private Object[] objects;
    
    public BussinesRuleException(String string, Object[] objects) {
        super(string);
        this.objects = objects;
    }
        
}
