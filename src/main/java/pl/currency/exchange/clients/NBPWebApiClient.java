/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.clients;

import pl.currency.exchange.clients.model.Table;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author rober
 */
@FeignClient(value = "nbpwebapiclient", url = "http://api.nbp.pl/api/")
public interface NBPWebApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/exchangerates/rates/{table}/{code}/")
    public Table getRateByTableAndCode(@PathVariable("table") String table, @PathVariable("code") String code);

}
