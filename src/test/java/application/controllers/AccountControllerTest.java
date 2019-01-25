package application.controllers;

import application.domain.Account;
import application.domain.enums.AccountType;
import application.dto.AccountDto;
import application.repository.AccountRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Mock
    AccountRepository accountRepository;

    @Test
    public void findAll() {
        Flux<Account> accountFlux = Flux.fromIterable( new ArrayList<>(){{
            add(Account.builder().card("123").password("123").accountType(AccountType.NORMAL).balance(1000D).build());
            add(Account.builder().card("456").password("456").accountType(AccountType.PRIVATE).balance(1000D).build());
            add(Account.builder().card("789").password("789").accountType(AccountType.SAVING).balance(1000D).build());
        }});

        when(accountRepository.findAll()).thenReturn(accountFlux);

        webTestClient.get().uri("/accounts").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBodyList(AccountDto.class).hasSize(3);
    }

    @Test
    public void findById() {
    }

    @Test
    public void findByPage() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}