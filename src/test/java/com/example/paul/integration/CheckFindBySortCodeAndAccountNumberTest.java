package com.example.paul.integration;

import com.example.paul.constants.CURRENCY;
import com.example.paul.models.Account;
import com.example.paul.repositories.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CheckFindBySortCodeAndAccountNumberTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    // write test cases here
    @Test
    public void whenFindBySortCodeAndAccountNumber_thenReturnAccountOwnerName() {
        // given
        Account alex = new Account("bankName", "ownerName", "generateSortCode",  "generateAccountNumber", 0.0, CURRENCY.USD);
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Optional<Account> found = accountRepository.findBySortCodeAndAccountNumber(alex.getSortCode(), alex.getAccountNumber());

        // then
        found.ifPresent(acc -> assertEquals(acc.getCurrency(), alex.getCurrency()));
    }

}