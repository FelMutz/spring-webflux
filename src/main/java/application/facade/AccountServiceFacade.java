package application.facade;

import application.domain.enums.AccountType;
import application.dto.AccountDto;
import application.mappers.AccountMap;
import application.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static application.mappers.AccountMap.dtoToMap;

@Component
@AllArgsConstructor
public class AccountServiceFacade {

    AccountService accountService;

    public Flux<AccountDto> findAll(){
        return accountService.findAll().map(AccountMap::mapToDto);
    }

    public Flux<AccountDto> findAllBy(Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return accountService.findAllBy(pageRequest).map(AccountMap::mapToDto);
    }

    public Mono<AccountDto> findById(String card){
        return accountService.findById(card).map(AccountMap::mapToDto);
    }

    public Mono<AccountDto> insert(AccountDto accountDto) {
        return accountService.insert(dtoToMap(accountDto)).map(AccountMap::mapToDto);
    }

    public Mono<AccountDto> update(AccountDto accountDto){
        return accountService.update(dtoToMap(accountDto)).map(AccountMap::mapToDto);
    }

    public Mono<Void> deleteAll() {
        return accountService.deleteAll();
    }

    public Mono<Void> delete(String card) {
        return accountService.delete(card);
    }

    public Flux<AccountDto> findByAccountType(String accountType){
        return accountService.findByAccountType( AccountType.valueOf(accountType)).map(AccountMap::mapToDto);
    }


}
