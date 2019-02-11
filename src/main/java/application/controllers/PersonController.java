package application.controllers;

import application.dto.BindAccountDto;
import application.dto.PersonDto;
import application.facade.PersonServiceFacade;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@RequestMapping("persons")
public class PersonController {


    PersonServiceFacade personServiceFacade;


    @GetMapping
    public Flux<PersonDto> findAll(){
        return personServiceFacade.findAll();

    }

    @GetMapping("paging")
    public Flux<PersonDto> findByPage(@RequestParam int page, @RequestParam int size){
        return personServiceFacade.findAllBy(page,size);

    }

    @GetMapping("{id}")
    public Mono<PersonDto> findById(@PathVariable String id){
        return personServiceFacade.findById(id);
    }

    @PostMapping
    public Mono<PersonDto> insert(@Valid @RequestBody PersonDto personDto){
        return personServiceFacade.insert(personDto);
    }

    @PostMapping("bindsAccount")
    public Mono<PersonDto> bindsAccount(@Valid @RequestBody BindAccountDto bindAccountDto){
        return personServiceFacade.bindsAccount(bindAccountDto);
    }

    @PutMapping
    public Mono<PersonDto> update(@RequestBody PersonDto personDto){
        return personServiceFacade.update(personDto);
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable String id){
        return personServiceFacade.delete(id);
    }

    @GetMapping("by")
    public Flux<PersonDto> findAllByAccount(@RequestParam String account){
        return personServiceFacade.findAllByAccount(account);
    }

}
