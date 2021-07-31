/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.services;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;
import pl.currency.exchange.dtos.CreateAccountDto;
import pl.currency.exchange.entities.Account;
import pl.currency.exchange.entities.SubAccount;

/**
 *
 * @author rober
 */
@Component
public class AccountAssembler {

    public Account createAccountFromDto(CreateAccountDto createAccountDto) {
        Set<SubAccount> subAccounts = new HashSet<>();
        subAccounts.add(createSubaccount(Currency.getInstance("PLN"), createAccountDto.getBalance()));
        subAccounts.add(createSubaccount(Currency.getInstance("USD"), BigDecimal.ZERO));

        return Account.builder()
                .firstName(createAccountDto.getFirstName())
                .lastName(createAccountDto.getLastName())
                .pesel(createAccountDto.getPesel())
                .subAccounts(subAccounts)
                .build();
    }

    public SubAccount createSubaccount(Currency currency, BigDecimal balance) {
        return SubAccount.builder()
                .balance(balance)
                .currency(currency)
                .build();
    }

}
