package application.controllers;


import application.dto.AccountDto;
import application.mappers.AccountMap;
import application.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@RequestMapping("accounts")
@AllArgsConstructor
public class AccountController {

    AccountService accountService;

    @GetMapping
    public Flux<AccountDto> findAll(){

        return accountService.findAll().map(AccountMap::mapToDto);
    }

    @GetMapping("{card}")
    public Mono<AccountDto> findById(@PathVariable String card){
        return accountService.findById(card).map(AccountMap::mapToDto);
    }

    @GetMapping("paging")
    public Flux<AccountDto> findByPage(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return accountService.findAllBy(pageRequest).map(AccountMap::mapToDto);
    }

    @PostMapping
    public Mono<AccountDto> insert(@Valid @RequestBody AccountDto accountDto){
        return accountService.insert(AccountMap.dtoToMap(accountDto)).map(AccountMap::mapToDto);
    }

    @PutMapping
    public Mono<AccountDto> update(@Valid @RequestBody AccountDto accountDto){
        return accountService.updateAccount(AccountMap.dtoToMap(accountDto)).map(AccountMap::mapToDto);
    }

    @DeleteMapping("{card}")
    public Mono<Void> delete(@PathVariable String card){
        return accountService.delete(card);
    }


}
