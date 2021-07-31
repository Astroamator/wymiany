/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.controllers;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.currency.exchange.dtos.CreateAccountDto;
import pl.currency.exchange.dtos.CreateSubAccountDto;
import pl.currency.exchange.dtos.ExchangeDto;
import pl.currency.exchange.services.AccountService;
import pl.currency.exchange.validators.ValidPesel;

/**
 *
 * @author rober
 */
@RestController
@RequestMapping("accounts")
public class AccountsController {

    @Autowired
    private AccountService accountService;

    @Operation(
            summary = "Załóż konto",
            description = "Metoda tworzy konto wraz z subkontami walutowymi w PLN i USD"
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAccount(@Valid @RequestBody CreateAccountDto createAccountDto) {
        return accountService.createAccount(createAccountDto);
    }

    @Operation(
            summary = "Pobierz informacje o koncie",
            description = "Metoda zwraca informacje o koncie wraz z utworzonymi subkontami"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{pesel}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAccount(@PathVariable @ValidPesel(message = "{pesel.validpesel}") String pesel) {
        return accountService.getAccount(pesel);
    }

    @Operation(
            summary = "Wymień walutę",
            description = "Metoda wymienia kwotę w walucie subkonta źródłowego na kwotę w walucie subkonta docelowego"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/exchange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> exchange(@Valid @RequestBody ExchangeDto exchangeAccountRequestDto) {
        return accountService.exchange(exchangeAccountRequestDto);
    }

    @Operation(
            summary = "Utwórz subkonto walutowe",
            description = "Metoda tworzy subkonto walutowe w ramach konta głównego"
    )
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/subaccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSubAccount(@Valid @RequestBody CreateSubAccountDto createSubAccountDto) {
        return accountService.createSubAccount(createSubAccountDto);
    }

}
