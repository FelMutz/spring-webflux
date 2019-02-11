package application.services;

import application.domain.Person;
import application.dto.BindAccountDto;
import application.exceptions.ExceptionCustom;
import application.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PersonService {

    private PersonRepository personRepository;

    public Mono<Person> bindsAccount(BindAccountDto bindAccountDto){
       return findById(bindAccountDto.getId()).flatMap(person -> {
           bindAccountDto.getAccounts().forEach(x -> person.getAccounts().add(x));
           return update(person);
       });
    }

    public Mono<Person> findById(String id){
        return personRepository.findById(id).switchIfEmpty(
                Mono.error(
                        ExceptionCustom
                                .builder()
                                .code(10)
                                .httpStatus(HttpStatus.NOT_FOUND).message("Dado não existe no banco!")
                                .detail("Pessoa não encontrada com o id: "+id).build()));
    }

    public Mono<Person> update(Person person){
        return personRepository.save(person);
    }

    public Mono<Person> insert(Person person) {
        return personRepository.insert(person);
    }

    public Mono<Void> delete(String id){
        return personRepository.deleteById(id);
    }

    public Flux<Person> findAll(){
        return personRepository.findAll();
    }


    public Flux<Person> findAllBy(PageRequest pageRequest){
        return personRepository.findAllBy(pageRequest);
    }

    public Mono<Void> deleteAll(){
        return personRepository.deleteAll();
    }

    public Flux<Person> findAllByAccount(String account){

        return personRepository.findAllByAccounts(account).switchIfEmpty(
                Mono.error(
                        ExceptionCustom
                                .builder()
                                .code(10)
                                .httpStatus(HttpStatus.NOT_FOUND).message("Dado não existe no banco!")
                                .detail("Não foi encontrada nenhuma Pessoa vinculada com a Conta de id: "+account).build()));

    }
}
