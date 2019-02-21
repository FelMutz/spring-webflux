package application.facade;

import application.dto.AccountDto;
import application.dto.BankSharesDto;
import application.mappers.AccountMap;
import application.services.BankSharesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@AllArgsConstructor
@Component
public class BankSharesServicesFacade {

    BankSharesService bankSharesService;

    public Mono<AccountDto> withdrawal(BankSharesDto bankSharesDto){

        return bankSharesService.withdrawal(bankSharesDto).map(AccountMap::mapToDto);
    }

    public Mono<AccountDto> deposit(BankSharesDto bankSharesDto){
        return bankSharesService.deposit(bankSharesDto).map(AccountMap::mapToDto);
    }

    public Mono<AccountDto> transfer(BankSharesDto bankSharesDto){
        return bankSharesService.transfer(bankSharesDto).map(Tuple2::getT1).map(AccountMap::mapToDto);
    }
}
