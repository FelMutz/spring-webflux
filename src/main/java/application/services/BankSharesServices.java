package application.services;

import application.domain.Account;
import application.dto.BankSharesDto;
import application.exceptions.ExceptionCustom;
import application.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class BankSharesServices {

    private AccountRepository accountRepository;

    private AccountService accountService;

    public Mono<Account> withdrawal(BankSharesDto bankSharesDto){
      return accountService.findById(bankSharesDto.getCard()).flatMap(account ->{
            ValidPassword.validPassword(account.getPassword(), bankSharesDto.getPassword());

            Double newBalance = Roud.roudBalance( account.getAccountType().withdrawal(account.getBalance(), bankSharesDto.getAmount()));

            account.getAccountType().validBalanceLimit(newBalance);

            account.setBalance(newBalance);

            return accountService.updateAccount(account);
        }).doOnError(throwable -> ExceptionCustom.builder().code(100).detail("Erro Interno. Entre em contato com o suporte.").message("Saque não realizado."));
    }

    public Mono<Account> deposit(BankSharesDto bankSharesDto){

        return  accountService.findById(bankSharesDto.getCard()).flatMap(account -> {
            ValidPassword.validPassword(account.getPassword(), bankSharesDto.getPassword());

            account.setBalance(Roud.roudBalance( account.getAccountType().deposit( account.getBalance(), bankSharesDto.getAmount() ) ));
            return accountService.updateAccount(account);
        }).doOnError(throwable -> ExceptionCustom.builder().code(100).detail("Erro Interno. Entre em contato com o suporte.").message("Deposito não realizado."));

    }

    public Mono<Account> transfer(BankSharesDto bankSharesDto){

        if (bankSharesDto.getAccountTransfer() == null){
            throw ExceptionCustom.builder().code(20).httpStatus(HttpStatus.BAD_REQUEST).message("Erro na solicitação").detail("AccountTransfer não pode ser Null").build();
        }

        Mono<Account> accountMono = accountService.findById(bankSharesDto.getCard())
                .flatMap(account -> {
                    ValidPassword.validPassword(account.getPassword(), bankSharesDto.getPassword());

                    Double newBalance = Roud.roudBalance(account.getAccountType().transfer(account.getBalance(), bankSharesDto.getAmount()));

                    account.getAccountType().validBalanceLimit(newBalance);

                    account.setBalance(newBalance);

                    return accountService.updateAccount(account);
                });

        Mono<Account> accountMono1 = accountMono
                .flatMap(account -> accountService.findById(bankSharesDto.getAccountTransfer())).flatMap(accountTransfer -> {
                    accountTransfer.setBalance(Roud.roudBalance(accountTransfer.getBalance() + bankSharesDto.getAmount()));
                    return accountService.updateAccount(accountTransfer);
                })
                .flatMap(account -> accountService.findById(bankSharesDto.getCard()));

        return Mono.zip(accountMono, accountMono1)
                .map(objects -> objects.getT1()).doOnError(throwable ->
                        ExceptionCustom.builder().code(100).detail("Erro Interno. Entre em contato com o suporte.").message("Transferencia não realizado."));
    }
}
