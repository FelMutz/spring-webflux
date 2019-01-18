package application.controllers;

import application.dto.AccountDto;
import application.dto.BankSharesDto;
import application.mappers.AccountMap;
import application.services.BankSharesServices;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("shares")
@AllArgsConstructor
public class BankSharesControlle {

    BankSharesServices bankSharesServices;

    @PostMapping("withdrawal")
    @Transactional
    public Mono<AccountDto> withdrawal(@Valid @RequestBody BankSharesDto bankSharesDto){
        return bankSharesServices.withdrawal(bankSharesDto).map(AccountMap::mapToDto);
    }

    @PostMapping("deposit")
    public Mono<AccountDto> deposit(@Valid @RequestBody BankSharesDto bankSharesDto){
        return bankSharesServices.deposit(bankSharesDto).map(AccountMap::mapToDto);
    }

    @PostMapping("transfer")
    @Transactional
    public Mono<AccountDto> transfer(@Valid @RequestBody BankSharesDto bankSharesDto){
        return bankSharesServices.transfer(bankSharesDto).map(AccountMap::mapToDto);
    }


}
