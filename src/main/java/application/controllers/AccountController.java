package application.controllers;


import application.dto.AccountDto;
import application.facade.AccountServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("accounts")
@AllArgsConstructor
public class AccountController {

    AccountServiceFacade accountServiceFacade;

    @GetMapping
    public Flux<AccountDto> findAll(){

        return accountServiceFacade.findAll();
    }

    @GetMapping("{card}")
    public Mono<AccountDto> findById(@PathVariable String card){
        return accountServiceFacade.findById(card);
    }

    @GetMapping("paging")
    public Flux<AccountDto> findByPage(@RequestParam int page, @RequestParam int size){
        return accountServiceFacade.findAllBy(page,size);
    }

    @PostMapping
    public Mono<AccountDto> insert(@Valid @RequestBody AccountDto accountDto){
        return accountServiceFacade.insert(accountDto);
    }

    @PutMapping
    public Mono<AccountDto> update(@RequestBody AccountDto accountDto){
        return accountServiceFacade.update(accountDto);
    }

    @DeleteMapping("{card}")
    public Mono<Void> delete(@PathVariable String card){
        return accountServiceFacade.delete(card);
    }


}
