package application.services;

import application.domain.Account;
import application.domain.enums.AccountType;
import application.repository.AccountRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    @Mock
    AccountRepository accountRepository;

    @InjectMocks
    AccountService accountService;


    Mono<Account> accountMono;

    @InjectMocks
    Account account;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findAll() {
        Flux<Account> accountFlux = Flux.fromIterable(new ArrayList<>(){{
            add(account);
            add(account);
            add(account);
            add(account);
            add(account);
        }});

        when(accountRepository.findAll()).thenReturn(accountFlux);

        List<Account> accounts = new ArrayList<>();

        accountService.findAll().subscribe(accounts::add);

        assertEquals(5, accounts.size());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    public void findById() {

        accountMono = Mono.just(Account.builder()
                                    .card("456")
                                    .accountType(AccountType.SAVING)
                                    .balance(1000D)
                                    .password("123").build());

        Account teste = Account.builder()
                .card("456")
                .accountType(AccountType.SAVING)
                .balance(1000D)
                .password("123").build();



        when(accountRepository.findById(account.getCard())).thenReturn(accountMono);

        accountService.findById(account.getCard()).subscribe(account1 -> account = account1);

        assertEquals(account, teste);
    }

    @Test
    public void insert() {
        accountMono = Mono.just(Account.builder()
                .card("456")
                .accountType(AccountType.SAVING)
                .balance(1000D)
                .password("123").build());

        Account teste = Account.builder()
                .card("456")
                .accountType(AccountType.SAVING)
                .balance(1000D)
                .password("123").build();



        when(accountRepository.insert(account)).thenReturn(accountMono);

        accountService.insert(account).subscribe(account1 -> account = account1);

        assertEquals(account, teste);

    }

    @Test
    public void updateAccount() {
        accountMono = Mono.just(Account.builder()
                .card("456")
                .accountType(AccountType.SAVING)
                .balance(1000D)
                .password("123").build());


        account.setCard("456");
        account.setAccountType(AccountType.NORMAL);
        account.setBalance(900D);
        account.setPassword("789");


        when(accountRepository.save(account)).thenReturn(accountMono);

        accountService.update(account).subscribe(account1 -> account = account1);

        assertEquals(account, account);
    }

    @Test
    public void findByAccountType() {

        Flux<Account> accountFlux = Flux.fromIterable(new ArrayList<>(){{
            add(Account.builder().accountType(AccountType.SAVING).build());
            add(Account.builder().accountType(AccountType.NORMAL).build());
            add(Account.builder().accountType(AccountType.PRIVATE).build());
            add(Account.builder().accountType(AccountType.SAVING).build());
            add(Account.builder().accountType(AccountType.NORMAL).build());
            add(Account.builder().accountType(AccountType.NORMAL).build());
        }});

        when(accountRepository.findByAccountType(AccountType.SAVING)).thenReturn(accountFlux.filter(account1 -> account1.getAccountType()==AccountType.SAVING));
        when(accountRepository.findByAccountType(AccountType.PRIVATE)).thenReturn(accountFlux.filter(account1 -> account1.getAccountType()==AccountType.PRIVATE));
        when(accountRepository.findByAccountType(AccountType.NORMAL)).thenReturn(accountFlux.filter(account1 -> account1.getAccountType()==AccountType.NORMAL));
        List<Account> accounts = new ArrayList<>();

        accountService.findByAccountType(AccountType.SAVING).subscribe(accounts::add);
        assertEquals(2, accounts.size());
        accounts.clear();

        accountService.findByAccountType(AccountType.PRIVATE).subscribe(accounts::add);
        assertEquals(1, accounts.size());
        accounts.clear();

        accountService.findByAccountType(AccountType.NORMAL).subscribe(accounts::add);
        assertEquals(3, accounts.size());

        verify(accountRepository, times(1)).findByAccountType(AccountType.SAVING);

    }
}