package application.controllers;

import application.domain.Account;
import application.domain.Person;
import application.domain.enums.PersonType;
import application.dto.BindAccountDto;
import application.dto.PersonDto;
import application.facade.PersonServiceFacade;
import application.mappers.PersonMap;
import application.repository.PersonRepository;
import application.services.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PersonService.class, PersonServiceFacade.class, PersonController.class})
@WebFluxTest
public class PersonControllerTest {

    @Autowired
    private
    ApplicationContext applicationContext;

    @Autowired
    private
    WebTestClient webTestClient;

    @MockBean
    private
    PersonRepository personRepository;

    @InjectMocks
    Person person;

    @InjectMocks
    Account account;

    @Before
    public void setUp(){
        webTestClient =
                WebTestClient.bindToApplicationContext(applicationContext)
                        .configureClient()
                        .baseUrl("/persons")
                        .build();
    }

    @Test
    public void findAll() {

        person.setAccounts(new ArrayList<>());

        Flux<Person> persons = Flux.fromIterable(new ArrayList<>(){{
            add(person);
            add(person);
            add(person);
        }});

        when(personRepository.findAll()).thenReturn(persons);

        webTestClient.get().accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBodyList(PersonDto.class).hasSize(3);

    }

    @Test
    public void findByPage() {
        int page =0;
        int size = 3;

        person.setAccounts(new ArrayList<>());

        Flux<Person> persons = Flux.fromIterable(new ArrayList<>(){{
            add(person);
            add(person);
            add(person);
        }});

        PageRequest pageRequest = PageRequest.of(page, size);

        when(personRepository.findAllBy(pageRequest)).thenReturn(persons);

        webTestClient.get().uri("/paging?page={page}&size={size}", page,size)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PersonDto.class).hasSize(3);
    }

    @Test
    public void findById() {
        Mono<Person> personMono = Mono.just(Person.builder()
                .idPerson("123")
                .accounts(new ArrayList<>())
                .age(25)
                .CPF("165165")
                .name("Teste")
                .build()
        );

        when(personRepository.findById("123")).thenReturn(personMono);

        webTestClient.get()
                .uri("/{id}", "123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonDto.class);
    }

    @Test
    public void insertPhysicalPerson() {

        person = Person.builder()
                .idPerson("123")
                .accounts(new ArrayList<>())
                .age(25)
                .CPF("00000000000")
                .name("Teste")
                .personType(PersonType.PHYSICAL)
                .build();

        PersonDto personDto = PersonMap.mapToDto(person);

        personDto.setPersonType(null);

        when(personRepository.insert(person)).thenReturn(Mono.just(person));

        webTestClient.post()
                .body(Mono.just(personDto), PersonDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonDto.class);

    }

    @Test
    public void bindsAccount() {

        person.setAccounts(new ArrayList<>(){{
            add(account);
            add(account);
        }});


        Person personAfter = person;
        personAfter.setAccounts(new ArrayList<>(){{
            add(account);
            add(account);
            add(account);
            add(account);
            add(account);
        }});

        when(personRepository.findById("123")).thenReturn(Mono.just(person));
        when(personRepository.save(personAfter)).thenReturn(Mono.just(personAfter));

        BindAccountDto bindAccountDto = BindAccountDto.builder().id("123")
                .accounts( new ArrayList<>(){{
                    add(account);
                    add(account);
                    add(account);
                }}).build();

        webTestClient.post()
                .uri("/bindsAccount")
                .body(Mono.just(bindAccountDto), BindAccountDto.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(PersonDto.class)
                .consumeWith(listEntityExchangeResult ->
                    Objects.requireNonNull(listEntityExchangeResult.getResponseBody()).size()
                );

    }

    @Test
    public void update() {

        person = Person.builder()
                .idPerson("123")
                .accounts(new ArrayList<>())
                .age(25)
                .CPF("00000000000")
                .name("Teste")
                .personType(PersonType.PHYSICAL)
                .build();
        Person personAfter = person;
        personAfter.setCNPJ("123456789");
        personAfter.setName("Carlos");

        PersonDto personUpdate = PersonDto.builder().idPerson("123").CNPJ("123456789").name("Carlos").build();

        when(personRepository.findById("123")).thenReturn(Mono.just(person));
        when(personRepository.save(personAfter)).thenReturn(Mono.just(personAfter));

        webTestClient.put()
                .body(Mono.just(personUpdate), PersonDto.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PersonDto.class)
                .consumeWith(response -> {
                    assertThat(Objects.requireNonNull(response.getResponseBody()).getIdPerson()).isEqualTo("123");
                    assertThat(Objects.requireNonNull(response.getResponseBody()).getName()).isEqualTo("Carlos");
                    assertThat(Objects.requireNonNull(response.getResponseBody()).getCNPJ()).isEqualTo("123456789");
                });


    }

    @Test
    public void delete() {

        when(personRepository.deleteById("123")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/{id}", "123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Void.class);

    }
}