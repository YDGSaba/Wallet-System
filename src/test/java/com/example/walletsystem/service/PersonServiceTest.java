package com.example.walletsystem.service;

import com.example.walletsystem.model.Person;
import com.example.walletsystem.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    private PersonService personService;
    private Person person;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        person = new Person();
        person.setPersonId(1L);
        person.setEmail("test@example.com");
        person.setPassword("plainPassword");
    }

    @Test
    void registerPerson_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(personRepository.save(any(Person.class))).thenReturn(person);
        Person savedPerson = personService.registerPerson(person);
        assertNotNull(savedPerson);
        assertEquals("hashedPassword", savedPerson.getPassword());
        verify(personRepository, times(1)).save(person);
    }

    @Test
    void authenticate_Success() {
        when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        Person authenticatedPerson = personService.authenticate("test@example.com", "plainPassword");
        assertNotNull(authenticatedPerson);
        assertEquals(person.getEmail(), authenticatedPerson.getEmail());
    }

    @Test
    void authenticate_Fail() {
        when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        Person authenticatedPerson = personService.authenticate("test@example.com", "wrongPassword");
        assertNull(authenticatedPerson);
    }
    @Test
    void updatePerson_Success() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);
        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Mina");
        updatedPerson.setLastName("Dell");
        Person result = personService.updatePerson(1L, updatedPerson);
        assertEquals("Mina", result.getFirstName());
        assertEquals("Dell", result.getLastName());
        verify(personRepository, times(1)).save(person);
    }
}
