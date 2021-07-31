/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.currency.exchange.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.currency.exchange.entities.Account;

/**
 *
 * @author rober
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

    Optional<Account> findByPesel(String pesel);

}
