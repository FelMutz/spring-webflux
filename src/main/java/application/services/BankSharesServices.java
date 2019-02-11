package application.services;

import application.domain.Account;
import application.dto.BankSharesDto;
import application.exceptions.ExceptionCustom;
import application.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@Service
@AllArgsConstructor
public class BankSharesServices {

    private AccountService accountService;

    public Mono<Account> withdrawal(BankSharesDto bankSharesDto){
      return accountService.findById(bankSharesDto.getCard()).flatMap(account ->{
            ValidPassword.validPassword(account.getPassword(), bankSharesDto.getPassword());

            Double newBalance = Roud.roudBalance( account.getAccountType().withdrawal(account.getBalance(), bankSharesDto.getAmount()));

            account.getAccountType().validBalanceLimit(newBalance);

            account.setBalance(newBalance);

            return accountService.update(account);
        }).doOnError(throwable -> ExceptionCustom.builder().code(100).detail("Erro Interno. Entre em contato com o suporte.").message("Saque não realizado."));
    }

    public Mono<Account> deposit(BankSharesDto bankSharesDto){

        return  accountService.findById(bankSharesDto.getCard()).flatMap(account -> {
            ValidPassword.validPassword(account.getPassword(), bankSharesDto.getPassword());

            account.setBalance(Roud.roudBalance( account.getAccountType().deposit( account.getBalance(), bankSharesDto.getAmount() ) ));
            return accountService.update(account);
        }).doOnError(throwable -> ExceptionCustom.builder().code(100).detail("Erro Interno. Entre em contato com o suporte.").message("Deposito não realizado."));

    }

    public Mono<Tuple2<Account, Account>> transfer(BankSharesDto bankSharesDto){

        if(bankSharesDto.getAccountTransfer() == null){
            throw ExceptionCustom.builder().code(20).httpStatus(HttpStatus.BAD_REQUEST).message("Erro na solicitação").detail("AccountTransfer não pode ser Null").build();
        }

        Mono<Account> accountMonoSubmit = transferAccountSubmit(bankSharesDto.getCard(),bankSharesDto.getAmount(), bankSharesDto.getPassword());

        Mono<Account> accountMonoReceive = transferAccountReceive(bankSharesDto.getAccountTransfer(),bankSharesDto.getAmount());

        return Mono.zip(accountMonoSubmit, accountMonoReceive);
    }

    public Mono<Account> transferAccountSubmit(String accountId, Double amount, String password){
       return  accountService.findById(accountId)
                .flatMap(account -> {
                    ValidPassword.validPassword(account.getPassword(), password);

                    Double newBalance = Roud.roudBalance(account.getAccountType().transfer(account.getBalance(), amount));

                    account.getAccountType().validBalanceLimit(newBalance);

                    account.setBalance(newBalance);

                    return accountService.update(account);
                });
    }

    public Mono<Account> transferAccountReceive(String accountId, Double amount){

        return  accountService.findById(accountId).flatMap(accountTransfer -> {
                    accountTransfer.setBalance(Roud.roudBalance(accountTransfer.getBalance() + amount));
                    return accountService.update(accountTransfer);
                });
    }
}
