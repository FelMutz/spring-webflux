package application.facade;

import application.dto.BindAccountDto;
import application.dto.PersonDto;
import application.mappers.PersonMap;
import application.services.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import static application.mappers.PersonMap.*;

@AllArgsConstructor
@Component
public class PersonServiceFacade {

    PersonService personService;

    public Mono<PersonDto> bindsAccount(BindAccountDto bindAccountDto){
        return personService.bindsAccount(bindAccountDto).map(PersonMap::mapToDto);
    }

    public Mono<PersonDto> findById(String id){
        return personService.findById(id).map(PersonMap::mapToDto);
    }

    public Mono<PersonDto> update(PersonDto person){
        return personService.update(dtoToMap(person)).map(PersonMap::mapToDto);
    }

    public Mono<PersonDto> insert(PersonDto person) {
        return personService.insert(dtoToMap(person)).map(PersonMap::mapToDto);
    }

    public Mono<Void> delete(String id){
       return personService.delete(id);
    }

    public Flux<PersonDto> findAll(){
        return personService.findAll().map(PersonMap::mapToDto);
    }


    public Flux<PersonDto> findAllBy(Integer page, Integer size){
        PageRequest pageRequest = PageRequest.of(page, size);
        return personService.findAllBy(pageRequest).map(PersonMap::mapToDto);
    }

    public Mono<Void> deleteAll(){
        return personService.deleteAll();
    }
}
