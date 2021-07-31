/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.currency.exchange.clients.NBPWebApiClient;
import pl.currency.exchange.clients.model.Rate;
import pl.currency.exchange.exceptions.CurrencyNotFoundException;

/**
 *
 * @author rober
 */
@Slf4j
@Service
public class CurrencyExchangeService {

    @Autowired
    private NBPWebApiClient nbpWebApiClient;

    public BigDecimal exchange(Currency from, Currency to, BigDecimal amount) {
        Rate fromRate = getRate(from)
                .orElseThrow(() -> new CurrencyNotFoundException(from.getCurrencyCode()));

        Rate toRate = getRate(to)
                .orElseThrow(() -> new CurrencyNotFoundException(to.getCurrencyCode()));

        float exchangeRate = fromRate.getBid() / toRate.getAsk();
        return amount.multiply(BigDecimal.valueOf(exchangeRate), new MathContext(to.getDefaultFractionDigits()));
    }

    private Optional<Rate> getRate(Currency currency) {
        Optional<Rate> rate = Optional.empty();
        switch (currency.getCurrencyCode()) {
            case "PLN":
                rate = getDefaultCurrencyRate();
                break;
            default:
                rate = getExternalCurrencyRate(currency);
                break;
        }
        return rate;
    }

    public Optional<Rate> getExternalCurrencyRate(Currency currency) {
        Optional<Rate> rate = Optional.empty();
        try {
            rate = nbpWebApiClient.getRateByTableAndCode("C", currency.getCurrencyCode())
                    .getRates()
                    .stream()
                    .findFirst();
        } catch (Exception ex) {
            log.warn(String.format("Nie znaleziono kursu wymiany dla waluty: ", currency.getCurrencyCode()));
        }
        return rate;
    }

    private Optional<Rate> getDefaultCurrencyRate() {
        Rate rate = Rate.builder()
                .ask(1)
                .bid(1)
                .effectiveDate(LocalDate.now())
                .build();
        return Optional.of(rate);
    }

}
