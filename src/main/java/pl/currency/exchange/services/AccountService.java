/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.services;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.currency.exchange.clients.model.Rate;
import pl.currency.exchange.dtos.CreateAccountDto;
import pl.currency.exchange.dtos.CreateSubAccountDto;
import pl.currency.exchange.dtos.ExchangeDto;
import pl.currency.exchange.entities.Account;
import pl.currency.exchange.entities.SubAccount;
import pl.currency.exchange.exceptions.AccountDoNotExistsException;
import pl.currency.exchange.exceptions.AccountExistsException;
import pl.currency.exchange.exceptions.CurrencyNotFoundException;
import pl.currency.exchange.exceptions.ExchangeRateNotFoundException;
import pl.currency.exchange.exceptions.InsufficientAmountException;
import pl.currency.exchange.exceptions.SubAccountExistsException;
import pl.currency.exchange.exceptions.SubAccountNotFoundException;
import pl.currency.exchange.repositories.AccountRepository;

/**
 *
 * @author rober
 */
@Slf4j
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountAssembler accountAssembler;

    @Autowired
    private CurrencyExchangeService currencyExchangeService;

    public ResponseEntity<Account> createAccount(CreateAccountDto createAccountDto) {
        if (accountRepository.findByPesel(createAccountDto.getPesel()).isPresent()) {
            throw new AccountExistsException();
        }
        Account newAccount = accountAssembler.createAccountFromDto(createAccountDto);
        accountRepository.save(newAccount);

        return new ResponseEntity(newAccount, HttpStatus.CREATED);
    }

    public ResponseEntity<Account> getAccount(String pesel) {
        Optional<Account> accountOptional = accountRepository.findByPesel(pesel);
        return ResponseEntity.of(accountOptional);
    }

    public ResponseEntity<Account> createSubAccount(CreateSubAccountDto createSubAccountDto) {
        Account account = accountRepository.findByPesel(createSubAccountDto.getPesel())
                .orElseThrow(AccountDoNotExistsException::new);

        Currency currency = getCurrency(createSubAccountDto.getCurrency())
                .orElseThrow(() -> new CurrencyNotFoundException(createSubAccountDto.getCurrency()));

        Rate rate = currencyExchangeService.getExternalCurrencyRate(currency)
                .orElseThrow(() -> new ExchangeRateNotFoundException(currency.getCurrencyCode()));

        if (hasSubAccountByCurrency(account, currency)) {
            throw new SubAccountExistsException(currency.getCurrencyCode());
        }

        SubAccount subAccount = accountAssembler.createSubaccount(currency, BigDecimal.ZERO);
        account.getSubAccounts().add(subAccount);
        accountRepository.save(account);
        return new ResponseEntity(account, HttpStatus.CREATED);

    }

    public ResponseEntity<Account> exchange(ExchangeDto exchangeDto) {
        Account account = accountRepository.findByPesel(exchangeDto.getPesel())
                .orElseThrow(AccountDoNotExistsException::new);

        Currency currencyFrom = getCurrency(exchangeDto.getFrom())
                .orElseThrow(() -> new CurrencyNotFoundException(exchangeDto.getFrom()));

        Currency currencyTo = getCurrency(exchangeDto.getTo())
                .orElseThrow(() -> new CurrencyNotFoundException(exchangeDto.getFrom()));

        SubAccount from = findSubAccountByCurrency(account, currencyFrom)
                .orElseThrow(() -> new SubAccountNotFoundException(exchangeDto.getFrom()));

        SubAccount to = findSubAccountByCurrency(account, currencyTo)
                .orElseThrow(() -> new SubAccountNotFoundException(exchangeDto.getTo()));

        BigDecimal amountToExchange = exchangeDto.getAmount();
        if (from.getBalance().compareTo(amountToExchange) == -1) {
            throw new InsufficientAmountException();
        }
        BigDecimal exchangedAmount = currencyExchangeService.exchange(from.getCurrency(), to.getCurrency(), amountToExchange);

        from.setBalance(from.getBalance().subtract(amountToExchange));
        to.setBalance(to.getBalance().add(exchangedAmount));

        accountRepository.save(account);
        return ResponseEntity.ok(account);
    }

    private Optional<SubAccount> findSubAccountByCurrency(Account account, Currency currency) {
        return account.getSubAccounts()
                .stream()
                .filter(sa -> sa.getCurrency().equals(currency))
                .findFirst();
    }

    private boolean hasSubAccountByCurrency(Account account, Currency currency) {
        return account.getSubAccounts()
                .stream()
                .anyMatch(s -> s.getCurrency().equals(currency));
    }

    private Optional<Currency> getCurrency(String currencyString) {
        Optional<Currency> optionalCurrency = Optional.empty();
        try {
            optionalCurrency = Optional.of(Currency.getInstance(currencyString));
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        return optionalCurrency;
    }

}
