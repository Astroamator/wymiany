/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.clients.model;

import java.util.List;
import lombok.Data;

/**
 *
 * @author rober
 */
@Data
public class Table {
    private String table;
    private String currency;
    private String code;
    private List<Rate> rates;
}
