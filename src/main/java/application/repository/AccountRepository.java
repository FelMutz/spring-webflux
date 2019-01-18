package application.repository;


import application.domain.Account;
import application.domain.enums.AccountType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AccountRepository extends ReactiveMongoRepository  <Account, String> {

    Flux<Account> findByAccountType(AccountType accountType);
    Flux<Account> findAllBy(PageRequest pageRequest);
}
