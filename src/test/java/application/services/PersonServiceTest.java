package application.services;

import application.domain.Account;
import application.domain.Person;
import application.domain.enums.PersonType;
import application.dto.BindAccountDto;
import application.exceptions.ExceptionCustom;
import application.repository.PersonRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

public class PersonServiceTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    PersonService personService;


    Mono<Person> personMono;

    @InjectMocks
    Person person;

    @InjectMocks
    Account account;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void bindsAccount() {

        personMono = Mono.just(Person.builder()
                .idPerson("123")
                .name("Carlos")
                .personType(PersonType.PHYSICAL)
                .age(25)
                .CPF("00000000000")
                .accounts(new ArrayList<>(){{add(account);}})
                .CNPJ("4567879123")
                .build());

        List<Account> accounts = new ArrayList<>(){{
            add(account);
            add(account);
        }};

        when(personRepository.findById(person.getIdPerson())).thenReturn(personMono);
        when(personRepository.save(any())).thenReturn(personMono);

        BindAccountDto bindAccountDto = BindAccountDto.builder().id(person.getIdPerson()).accounts(accounts).build();


        personService.bindsAccount(bindAccountDto).subscribe(person1 -> person = person1);

        assertEquals(3, person.getAccounts().size());

    }

    @Test
    public void findByIdPersonNotFound() {

        when(personRepository.findById("123")).thenReturn(Mono.empty());

      try{
          personService.findById("123").subscribe();
      } catch (RuntimeException e) {
          assertThat( e.getCause(),
                  is(instanceOf(ExceptionCustom.class)));
          assertThat( e.getMessage(),
                  containsString("Dado não existe no banco!"));
      }

    }

    @Test
    public void insert() {

        personMono = Mono.just(Person.builder()
                .idPerson("123")
                .name("Carlos")
                .personType(PersonType.PHYSICAL)
                .age(25)
                .CPF("00000000000")
                .accounts(new ArrayList<>())
                .CNPJ("4567879123")
                .build());

        Person teste = Person.builder()
                .idPerson("123")
                .name("Carlos")
                .personType(PersonType.PHYSICAL)
                .age(25)
                .CPF("00000000000")
                .accounts(new ArrayList<>())
                .CNPJ("4567879123")
                .build();

        when(personService.insert(any())).thenReturn(personMono);

        this.personService.insert (person).subscribe(person1 -> person = person1);

       assertFalse(person == null);

       assertTrue(person.equals(teste));

    }

    @Test
    public void delete() {
        personMono = Mono.just(Person.builder()
                .idPerson("123")
                .name("Carlos")
                .personType(PersonType.PHYSICAL)
                .age(25)
                .CPF("00000000000")
                .accounts(new ArrayList<>())
                .CNPJ("4567879123")
                .build());

        when(personService.insert(any())).thenReturn(personMono);

        this.personService.insert (person).subscribe(person1 -> person = person1);

        assertFalse(person == null);

        when(personRepository.deleteById(anyString())).thenReturn(Mono.empty());

        personService.delete(person.getIdPerson()).subscribe();

    }

    @Test
    public void findAll() {

        Flux<Person> personList = Flux.fromIterable(new ArrayList<>(){{
            add(person);
            add(person);
            add(person);
            add(person);
            add(person);
        }});


        when(personRepository.findAll()).thenReturn(personList);

        List<Person> teste = new ArrayList<>();

        personService.findAll().subscribe(teste::add);

        assertEquals(5, teste.size());
        verify(personRepository, times(1)).findAll();

    }

    @Test
    public void findAllBy() {

        Flux<Person> personList = Flux.fromIterable(new ArrayList<>(){{
            add(person);
            add(person);
            add(person);
        }});

        PageRequest pageRequest = PageRequest.of(0, 3);
        when(personRepository.findAllBy(any())).thenReturn(personList);

        List<Person> teste = new ArrayList<>();
        personService.findAllBy(pageRequest).subscribe(teste::add);

        assertEquals(3,teste.size());
        verify(personRepository, times(1)).findAllBy(pageRequest);
    }

}