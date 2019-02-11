package application.repository;

import application.domain.Account;
import application.domain.Person;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PersonRepository extends ReactiveMongoRepository<Person, String> {

    Flux<Person> findAllBy(PageRequest pageRequest);

    Flux<Person> findAllByAccounts(String account);

 }
