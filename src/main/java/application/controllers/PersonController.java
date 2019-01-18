package application.controllers;

import application.dto.BindAccountDto;
import application.dto.PersonDto;
import application.mappers.PersonMap;
import application.services.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
@RequestMapping("persons")
public class PersonController {

    PersonService personService;

    @GetMapping
    public Flux<PersonDto> findAll(){
        return personService.findAll().map(PersonMap::mapToDto);

    }

    @GetMapping("paging")
    public Flux<PersonDto> findByPage(@RequestParam int page, @RequestParam int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return personService.findAllBy(pageRequest).map(PersonMap::mapToDto);

    }

    @GetMapping("{id}")
    public Mono<PersonDto> findById(@PathVariable String id){
        return personService.findById(id).map(PersonMap::mapToDto);
    }

    @PostMapping
    public Mono<PersonDto> insert(@Valid @RequestBody PersonDto personDto){
        return personService.insert(PersonMap.dtoToMap(personDto)).map(PersonMap::mapToDto);
    }

    @PostMapping("bindsAccount")
    public Mono<PersonDto> bindsAccount(@RequestBody BindAccountDto bindAccountDto){
        return personService.bindsAccount(bindAccountDto).map(PersonMap::mapToDto);
    }

    @PutMapping
    public Mono<PersonDto> update(@Valid @RequestBody PersonDto personDto){
        return personService.update(PersonMap.dtoToMap(personDto)).map(PersonMap::mapToDto);
    }

    @DeleteMapping("{id}")
    public Mono<Void> delete(@PathVariable String id){
        return personService.delete(id);
    }

}
