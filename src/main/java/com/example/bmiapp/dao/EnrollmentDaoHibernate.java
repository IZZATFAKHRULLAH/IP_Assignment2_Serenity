package com.example.bmiapp.dao;

import com.example.bmiapp.model.Enrollment;
import com.example.bmiapp.model.Person;
import com.example.bmiapp.model.Program;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class EnrollmentDaoHibernate implements EnrollmentDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public EnrollmentDaoHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void save(Enrollment enrollment) {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(enrollment);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        Enrollment enrollment = session.get(Enrollment.class, id);
        if (enrollment != null) {
            session.delete(enrollment);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Enrollment findById(Integer id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Enrollment.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Enrollment", Enrollment.class).list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByMember(Person member) {
        Session session = sessionFactory.getCurrentSession();
        Query<Enrollment> query = session.createQuery(
            "FROM Enrollment e WHERE e.member = :member", Enrollment.class);
        query.setParameter("member", member);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByProgram(Program program) {
        Session session = sessionFactory.getCurrentSession();
        Query<Enrollment> query = session.createQuery(
            "FROM Enrollment e WHERE e.program = :program", Enrollment.class);
        query.setParameter("program", program);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Enrollment findByMemberAndProgram(Person member, Program program) {
        Session session = sessionFactory.getCurrentSession();
        Query<Enrollment> query = session.createQuery(
            "FROM Enrollment e WHERE e.member = :member AND e.program = :program", 
            Enrollment.class);
        query.setParameter("member", member);
        query.setParameter("program", program);
        return query.uniqueResult();
    }
}