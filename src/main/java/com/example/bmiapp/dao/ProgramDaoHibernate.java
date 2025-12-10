package com.example.bmiapp.dao;

import com.example.bmiapp.model.Program;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ProgramDaoHibernate implements ProgramDao {

    private final SessionFactory sessionFactory;
    
    @Autowired
    public ProgramDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Program> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Program", Program.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public Program findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Program.class, id);
    }

    @Override
    @Transactional
    public void save(Program program) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(program);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Program program = session.get(Program.class, id);
        if (program != null) {
            session.delete(program);
        }
    }
}