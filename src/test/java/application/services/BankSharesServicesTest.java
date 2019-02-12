package application.services;

import application.domain.Account;
import application.domain.enums.AccountType;
import application.dto.BankSharesDto;
import application.exceptions.ExceptionCustom;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BankSharesServicesTest {


    @Mock
    AccountService accountService;

    @InjectMocks
    BankSharesServices bankSharesServices;

    @InjectMocks
    BankSharesDto bankSharesDto;

    @InjectMocks
    Account account;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void withdrawalNormalAccountt() {
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

        when(accountService.findById(bankSharesDto.getCard())).thenReturn(accountMono);

        when(accountService.update(any())).thenReturn(accountMono);

        bankSharesServices.withdrawal(bankSharesDto).subscribe(account1 -> account = account1);

        assertEquals(account.getBalance(), Double.valueOf(900));

    }

    @Test
    public void withdrawalPasswordIncorrect() {


        bankSharesDto.setCard("123");
        bankSharesDto.setAmount(100D);
        bankSharesDto.setPassword("456");

        Account accountBase = Account.builder()
                .accountType(AccountType.NORMAL)
                .balance(1000D)
                .password("123")
                .card("123")
                .build();

        Mono<Account> accountMono = Mono.just(accountBase);

        when(accountService.findById(bankSharesDto.getCard())).thenReturn(accountMono);

        when(accountService.update(any())).thenReturn(accountMono);

        bankSharesServices.withdrawal(bankSharesDto).subscribe(foo -> {
            assertThat( foo,
                    is(instanceOf(ExceptionCustom.class)));
        }, error -> {
            assertThat( error,
                    is(instanceOf(ExceptionCustom.class)));
            assertThat( error.getMessage(),
                    containsString("Dado não confere com o banco!!"));
        });



    }

    @Test
    public void withdrawalPrivateAccountt() {
        bankSharesDto.setCard("123");
        bankSharesDto.setAmount(100D);
        bankSharesDto.setPassword("123");

        Account accountBase = Account.builder()
                .accountType(AccountType.PRIVATE)
                .balance(1000D)
                .password("123")
                .card("123")
                .build();

        Mono<Account> accountMono = Mono.just(accountBase);

        when(accountService.findById(bankSharesDto.getCard())).thenReturn(accountMono);

        when(accountService.update(any())).thenReturn(accountMono);

        bankSharesServices.withdrawal(bankSharesDto).subscribe(account1 -> account = account1);

        assertEquals(account.getBalance(), Double.valueOf(895));

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

        when(accountService.findById(bankSharesDto.getCard())).thenReturn(accountMono);

        when(accountService.update(any())).thenReturn(accountMono);

        bankSharesServices.deposit(bankSharesDto).subscribe(account1 -> account = account1);

        assertEquals(account.getBalance(), Double.valueOf(1100));

    }

    @Test
    public void transferAccountSubmit() {
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

        Mono<Account> accountSubmitMono = Mono.just(accountBaseSubmit);

        when(accountService.findById(bankSharesDto.getCard())).thenReturn(accountSubmitMono);

        when(accountService.update(accountBaseSubmit)).thenReturn(accountSubmitMono);

        bankSharesServices.transferAccountSubmit(bankSharesDto.getCard(), bankSharesDto.getAmount(), bankSharesDto.getPassword())
                .subscribe(account1 -> account = account1);

        assertEquals(Double.valueOf(894), account.getBalance());

    }

    @Test
    public void transferAccountReceive() {
        bankSharesDto.setCard("123");
        bankSharesDto.setAmount(100D);
        bankSharesDto.setPassword("123");
        bankSharesDto.setAccountTransfer("456");

        Account accountBaseReceive = Account.builder()
                .accountType(AccountType.NORMAL)
                .balance(1000D)
                .password("123")
                .card("456")
                .build();

        Mono<Account> accountReceiveMono = Mono.just(accountBaseReceive);

        when(accountService.findById(bankSharesDto.getAccountTransfer())).thenReturn(accountReceiveMono);

        when(accountService.update(accountBaseReceive)).thenReturn(accountReceiveMono);

        bankSharesServices.transferAccountReceive(bankSharesDto.getAccountTransfer(), bankSharesDto.getAmount()).subscribe(account1 -> account = account1);

        assertEquals(Double.valueOf(1100), account.getBalance());
    }

}