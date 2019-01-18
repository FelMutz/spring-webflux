package application.mappers;

import application.controllers.AccountController;
import application.domain.Account;
import application.domain.enums.AccountType;
import application.dto.AccountDto;

import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class AccountMap {

    public static Account dtoToMap(AccountDto accountDto){
        return Optional.ofNullable(accountDto).map(account -> Account.builder()
            .accountType(accountDto.getAccountType() != null ? accountDto.getAccountType() : AccountType.NORMAL)
            .balance(accountDto.getBalance())
            .password(accountDto.getPassword())
            .card(accountDto.getCard())
            .build()).orElse(null);
    }

    public static AccountDto mapToDto(Account account){
        return Optional.ofNullable(account).map(accountDto  ->{
            AccountDto accountDtoLink = AccountDto.builder()
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .password(account.getPassword())
                .card(account.getCard())
                .build();
            accountDtoLink.add(linkTo(methodOn(AccountController.class).findById(accountDtoLink.getCard())).withSelfRel());
            accountDtoLink.add(linkTo(methodOn(AccountController.class).findAll()).withRel("BuscaTodasContas"));
            return accountDtoLink;
            }).orElse(null);
    }

}
