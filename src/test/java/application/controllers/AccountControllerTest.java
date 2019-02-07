package application.controllers;

import application.domain.Account;
import application.domain.enums.AccountType;
import application.dto.AccountDto;
import application.facade.AccountServiceFacade;
import application.mappers.AccountMap;
import application.repository.AccountRepository;
import application.services.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AccountService.class, AccountServiceFacade.class, AccountController.class})
@WebFluxTest
public class AccountControllerTest {

    @Autowired
    private
    ApplicationContext applicationContext;

    @Autowired
    private
    WebTestClient webTestClient;

    @MockBean
    private
    AccountRepository accountRepository;

    @Before
    public void setUp(){
        webTestClient =
                WebTestClient.bindToApplicationContext(applicationContext)
                .configureClient()
                .baseUrl("/accounts")
                .build();
    }

    @Test
    public void findAll() {
        Flux<Account> accountFlux = Flux.fromIterable( new ArrayList<>(){{
            add(Account.builder().card("123").password("123").accountType(AccountType.NORMAL).balance(1000D).build());
            add(Account.builder().card("456").password("456").accountType(AccountType.PRIVATE).balance(1000D).build());
            add(Account.builder().card("789").password("789").accountType(AccountType.SAVING).balance(1000D).build());
        }});

        when(accountRepository.findAll()).thenReturn(accountFlux);

        webTestClient.get().accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBodyList(AccountDto.class).hasSize(3);
    }

    @Test
    public void findById() {
        Mono<Account> account = Mono.just(Account.builder().card("123").password("123").accountType(AccountType.NORMAL).balance(1000D).build());

        when(accountRepository.findById("123")).thenReturn(account);

        webTestClient.get().uri("/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountDto.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getAccountType()).isEqualTo(AccountType.NORMAL));
    }

    @Test
    public void findByPage() {
        int page = 0;
        int size = 3;
        PageRequest pageRequest = PageRequest.of(page, size);

        Flux<Account> accountFlux = Flux.fromIterable( new ArrayList<>(){{
            add(Account.builder().card("123").password("123").accountType(AccountType.NORMAL).balance(1000D).build());
            add(Account.builder().card("456").password("456").accountType(AccountType.PRIVATE).balance(1000D).build());
            add(Account.builder().card("789").password("789").accountType(AccountType.SAVING).balance(1000D).build());
        }});

        when(accountRepository.findAllBy(pageRequest)).thenReturn(accountFlux);

        webTestClient.get().uri("/paging?page={page}&size={size}", page,size)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(AccountDto.class).hasSize(3);
    }

    @Test
    public void insertNormalAccount() {

        Account account = Account.builder().card("123").password("123456").accountType(AccountType.NORMAL).balance(1000D).build();
        AccountDto accountDto = AccountMap.mapToDto(account);
        accountDto.setAccountType(null);

        when(accountRepository.insert(account)).thenReturn(Mono.just(account));

        webTestClient.post()
                .body(Mono.just(accountDto), AccountDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountDto.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getAccountType()).isEqualTo(AccountType.NORMAL));
    }

    @Test
    public void update() {

        Account accountBefore = Account.builder().card("123").password("123456").accountType(AccountType.NORMAL).balance(1000D).build();
        Account accountAfter = Account.builder().card("123").password("123456").accountType(AccountType.NORMAL).balance(800D).build();
        Account accountUpdate = Account.builder().card("123").balance(800D).build();
        AccountDto accountDto = AccountMap.mapToDto(accountUpdate);

        when(accountRepository.findById(accountDto.getCard())).thenReturn(Mono.just(accountBefore));
        when(accountRepository.save(accountAfter)).thenReturn(Mono.just(accountAfter));

        webTestClient.put()
                .body(Mono.just(accountDto), AccountDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(AccountDto.class)
                .consumeWith(response ->
                        assertThat(Objects.requireNonNull(response.getResponseBody()).getBalance()).isEqualTo(800D));
    }

    @Test
    public void delete() {

        when(accountRepository.deleteById("123")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/{id}", "123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);

    }
}