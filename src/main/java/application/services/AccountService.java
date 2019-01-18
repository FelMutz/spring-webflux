package application.services;

import application.domain.Account;
import application.domain.enums.AccountType;
import application.exceptions.ExceptionCustom;
import application.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AccountService {

    private AccountRepository accountRepository;

    public Flux<Account> findAll(){
        return accountRepository.findAll();
    }

    public Flux<Account> findAllBy(PageRequest pageRequest){
        return accountRepository.findAllBy(pageRequest);
    }

    public Mono<Account> findById(String card){
        return  accountRepository.findById(card)
                .switchIfEmpty(
                        Mono.error(
                                ExceptionCustom
                                        .builder()
                                        .code(10)
                                        .httpStatus(HttpStatus.NOT_FOUND).message("Dado não existe no banco!").detail("Conta não encontrada com o cartão: "+card).build()));
    }

    public Mono<Account> insert(Account account) {
        return  accountRepository.insert(account);
    }

    public Mono<Account> updateAccount(Account account){
        return accountRepository.save(account);
    }

    public Mono<Void> deleteAll() {
        return accountRepository.deleteAll();
    }

    public Mono<Void> delete(String card){
       return accountRepository.deleteById(card);
    }

    public Flux<Account> findByAccountType(AccountType accountType){
        return accountRepository.findByAccountType(accountType);
    }
}
