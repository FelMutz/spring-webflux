package application.controllers;

import application.domain.Account;
import application.domain.enums.AccountType;
import application.dto.AccountDto;
import application.dto.BankSharesDto;
import application.facade.BankSharesServicesFacade;
import application.repository.AccountRepository;
import application.services.AccountService;
import application.services.BankSharesServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BankSharesController.class, BankSharesServices.class, BankSharesServicesFacade.class, AccountService.class})
@WebFluxTest
public class BankSharesControllerTest {

    @Autowired
    private
    ApplicationContext applicationContext;

    @Autowired
    private
    WebTestClient webTestClient;

    @MockBean
    private
    AccountRepository accountRepository;

    @InjectMocks
    BankSharesDto bankSharesDto;

    @Before
    public void setUp(){
        webTestClient =
                WebTestClient.bindToApplicationContext(applicationContext)
                        .configureClient()
                        .baseUrl("/shares")
                        .build();
    }

    @Test
    public void withdrawalNormalAccount() {
        bankSharesDto.setCard("123");
        bankSharesDto.setAmount(100D);
        bankSharesDto.setPassword("123");

        Account accountBase = Account.builder()
                .accountType(AccountType.NORMAL)
                .balance(1000D)
                .password("123")
                .card("123")
                .build();

        Mono<Account> accountMono = Mono.just(accountBase);

        when(accountRepository.findById(bankSharesDto.getCard())).thenReturn(accountMono);

        when(accountRepository.save(any())).thenReturn(accountMono);

        webTestClient.post()
                .uri("/withdrawal")
                .body(Mono.just(bankSharesDto), BankSharesDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountDto.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getBalance()).isEqualTo(900D));

    }

    @Test
    public void deposit() {
        bankSharesDto.setCard("123");
        bankSharesDto.setAmount(100D);
        bankSharesDto.setPassword("123");

        Account accountBase = Account.builder()
                .accountType(AccountType.NORMAL)
                .balance(1000D)
                .password("123")
                .card("123")
                .build();

        Mono<Account> accountMono = Mono.just(accountBase);

        when(accountRepository.findById(bankSharesDto.getCard())).thenReturn(accountMono);

        when(accountRepository.save(any())).thenReturn(accountMono);

        webTestClient.post()
                .uri("/deposit")
                .body(Mono.just(bankSharesDto), BankSharesDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountDto.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getBalance()).isEqualTo(1100D));
    }

    @Test
    public void transferSavingAccount() {
        bankSharesDto.setCard("123");
        bankSharesDto.setAmount(100D);
        bankSharesDto.setPassword("123");
        bankSharesDto.setAccountTransfer("456");

        Account accountBaseSubmit = Account.builder()
                .accountType(AccountType.SAVING)
                .balance(1000D)
                .password("123")
                .card("123")
                .build();

        Account accountBaseTransfer = Account.builder()
                .accountType(AccountType.SAVING)
                .balance(1000D)
                .password("123")
                .card("456")
                .build();

        Mono<Account> accountSubmitMono = Mono.just(accountBaseSubmit);
        Mono<Account> accountTransferMono = Mono.just(accountBaseTransfer);

        when(accountRepository.findById(bankSharesDto.getCard())).thenReturn(accountSubmitMono);

        when(accountRepository.save(accountBaseSubmit)).thenReturn(accountSubmitMono);

        when(accountRepository.findById(bankSharesDto.getAccountTransfer())).thenReturn(accountTransferMono);

        when(accountRepository.save(accountBaseTransfer)).thenReturn(accountTransferMono);

        webTestClient.post()
                .uri("/transfer")
                .body(Mono.just(bankSharesDto), BankSharesDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountDto.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getBalance()).isEqualTo(894));
    }
}