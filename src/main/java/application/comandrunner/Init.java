package application.comandrunner;

import application.domain.Account;
import application.domain.Person;
import application.domain.enums.AccountType;
import application.domain.enums.PersonType;
import application.exceptions.ExceptionCustom;
import application.services.AccountService;
import application.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.List;

@Component
public class Init implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        deleteAllBank().subscribe();
        bankSeeder().subscribe(person -> System.out.println("Dados do banco reiniciado"));
    }

    @Autowired
    private PersonService personService;

    @Autowired
    private AccountService accountService;

    public Mono<Tuple2<Void, Void>> deleteAllBank(){
        return Mono.zip(accountService.deleteAll(), personService.deleteAll());
    }

    public Mono<Person> bankSeeder(){

        List<Account> listAccount = new ArrayList<>();
        return Mono.just(Account.builder().password("123").balance(100.00).accountType(AccountType.NORMAL).build())
                .flatMap(account -> accountService.insert(account))
                .doOnSuccess(listAccount::add)
                .map(account ->  Account.builder().password("456").balance(3000.00).accountType(AccountType.SAVING).build())
                .flatMap(account -> accountService.insert(account))
                .doOnSuccess(listAccount::add)
                .map(account ->  Account.builder().password("789").balance(10000.00).accountType(AccountType.PRIVATE).build())
                .flatMap(account -> accountService.insert(account))
                .doOnSuccess(listAccount::add)
                .map(account ->  Person.builder().name("Felipe Castilhos").CPF("000.000.000-00").age(28).accounts(listAccount).personType(PersonType.PHYSICAL).build())
                .flatMap(person -> personService.insert(person))
                .doOnSuccess(account -> listAccount.clear())
                .map(account -> Account.builder().password("123").balance(100.00).accountType(AccountType.PRIVATE).build())
                .flatMap(account -> accountService.insert(account))
                .doOnSuccess(listAccount::add)
                .map(account ->  Account.builder().password("456").balance(3000.00).accountType(AccountType.NORMAL).build())
                .flatMap(account -> accountService.insert(account))
                .doOnSuccess(listAccount::add)
                .map(account ->  Account.builder().password("789").balance(10000.00).accountType(AccountType.SAVING).build())
                .flatMap(account -> accountService.insert(account))
                .doOnSuccess(listAccount::add)
                .map(account ->  Person.builder().name("Carlos Alberto").CPF("111.111.111-11").age(28).accounts(listAccount).CNPJ("1135277000195").personType(PersonType.LEGAL).build())
                .flatMap(person -> personService.insert(person))
                .doOnError(throwable -> ExceptionCustom.builder().code(600).message("Erro ao iniciar a aplicação").detail("Dados não inseridos no banco").build());

    }
}
