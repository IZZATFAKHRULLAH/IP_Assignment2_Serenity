package com.example.bmiapp.dao;

import com.example.bmiapp.model.Person;
import java.util.List;

public interface PersonDao {
    void add(Person person);
    List<Person> findAll();
    Person findById(String id);
    List<Person> findBySearchId(String searchId);
    boolean update(String id, Person person);
    boolean delete(String id);
}