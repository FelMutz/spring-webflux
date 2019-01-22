package application.controllers;

import application.dto.AccountDto;
import application.dto.BankSharesDto;
import application.facade.BankSharesServicesFacade;
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

    BankSharesServicesFacade bankSharesServicesFacade;

    @PostMapping("withdrawal")
    @Transactional
    public Mono<AccountDto> withdrawal(@Valid @RequestBody BankSharesDto bankSharesDto){
        return bankSharesServicesFacade.withdrawal(bankSharesDto);
    }

    @PostMapping("deposit")
    public Mono<AccountDto> deposit(@Valid @RequestBody BankSharesDto bankSharesDto){
        return bankSharesServicesFacade.deposit(bankSharesDto);
    }

    @PostMapping("transfer")
    @Transactional
    public Mono<AccountDto> transfer(@Valid @RequestBody BankSharesDto bankSharesDto){
        return bankSharesServicesFacade.transfer(bankSharesDto);
    }


}
