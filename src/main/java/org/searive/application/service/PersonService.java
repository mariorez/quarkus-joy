package org.searive.application.service;

import org.searive.application.domain.Person;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PersonService {

    public void create(String name, Integer age) {

        var person = new Person();
        person.name = name;
        person.age = age;

        person.persist();
    }
}
