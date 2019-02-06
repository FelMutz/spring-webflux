package application.controllers;

import application.facade.PersonServiceFacade;
import application.repository.PersonRepository;
import application.services.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PersonService.class, PersonServiceFacade.class})
@WebFluxTest(PersonController.class)
public class PersonControllerTest {

    @MockBean
    PersonRepository personRepository;

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void findAll() {
    }

    @Test
    public void findByPage() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void bindsAccount() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}