package application.facade;

import application.dto.AccountDto;
import application.dto.BankSharesDto;
import application.mappers.AccountMap;
import application.services.BankSharesServices;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@AllArgsConstructor
@Component
public class BankSharesServicesFacade {

    BankSharesServices bankSharesServices;

    public Mono<AccountDto> withdrawal(BankSharesDto bankSharesDto){

        return bankSharesServices.withdrawal(bankSharesDto).map(AccountMap::mapToDto);
    }

    public Mono<AccountDto> deposit(BankSharesDto bankSharesDto){
        return bankSharesServices.deposit(bankSharesDto).map(AccountMap::mapToDto);
    }

    public Mono<AccountDto> transfer(BankSharesDto bankSharesDto){
        return bankSharesServices.transfer(bankSharesDto).map(Tuple2::getT1).map(AccountMap::mapToDto);
    }
}
