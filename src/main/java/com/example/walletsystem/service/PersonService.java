package com.example.walletsystem.service;

import com.example.walletsystem.model.Person;
import com.example.walletsystem.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    public Person registerPerson(Person person) {
        // Encrypt the password before saving
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        return personRepository.save(person);
    }
    public Person authenticate(String email, String password) {
        Optional<Person> personOpt = personRepository.findByEmail(email);

        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            // Check if the password matches the encrypted password
            if (passwordEncoder.matches(password, person.getPassword())) {
                return person;
            }
        }
        return null;
    }
    public Optional<Person> findPersonById(Long personId) {
        return personRepository.findById(personId);
    }
    public Person updatePerson(Long personId, Person updatedPerson) {
        Optional<Person> existingPersonOpt = personRepository.findById(personId);
        if (existingPersonOpt.isPresent()) {
            Person existingPerson = existingPersonOpt.get();
            existingPerson.setFirstName(updatedPerson.getFirstName());
            existingPerson.setLastName(updatedPerson.getLastName());
            existingPerson.setEmail(updatedPerson.getEmail());
            // Update other fields as necessary
            return personRepository.save(existingPerson);
        }
        return null;
    }
    public void deletePerson(Long personId) {
        personRepository.deleteById(personId);
    }
}
