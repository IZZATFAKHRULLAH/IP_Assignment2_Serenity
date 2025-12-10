package com.example.bmiapp.dao;

import com.example.bmiapp.model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class PersonDaoHibernate implements PersonDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public PersonDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void add(Person person) {
        Session session = sessionFactory.getCurrentSession();
        session.save(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        Session session = sessionFactory.getCurrentSession();
        Query<Person> query = session.createQuery("FROM Person", Person.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Person findById(String id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Person.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findBySearchId(String searchId) {
        Session session = sessionFactory.getCurrentSession();
        Query<Person> query = session.createQuery("FROM Person WHERE id LIKE :searchId", Person.class);
        query.setParameter("searchId", "%" + searchId + "%");
        return query.getResultList();
    }

    @Override
    @Transactional
    public boolean update(String id, Person updatedPerson) {
        Session session = sessionFactory.getCurrentSession();
        Person existingPerson = session.get(Person.class, id);
        if (existingPerson != null) {
            existingPerson.setName(updatedPerson.getName());
            existingPerson.setYob(updatedPerson.getYob());
            existingPerson.setWeight(updatedPerson.getWeight());
            existingPerson.setHeight(updatedPerson.getHeight());
            existingPerson.setGender(updatedPerson.getGender());
        
            if (updatedPerson.getInterestsArray() != null) {
                existingPerson.setInterestsArray(updatedPerson.getInterestsArray());
            } else if (updatedPerson.getInterests() != null) {
                existingPerson.setInterests(updatedPerson.getInterests());
            } else {
                existingPerson.setInterests(null);
            }
        
            session.update(existingPerson);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        if (person != null) {
            session.delete(person);
            return true;
        }
        return false;
    }
}