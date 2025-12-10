package com.example.bmiapp.dao;

import com.example.bmiapp.model.Enrollment;
import com.example.bmiapp.model.Person;
import com.example.bmiapp.model.Program;

import java.util.List;

public interface EnrollmentDao {
    void save(Enrollment enrollment);
    void delete(Integer id);
    Enrollment findById(Integer id);
    List<Enrollment> findAll();
    List<Enrollment> findByMember(Person member);
    List<Enrollment> findByProgram(Program program);
    Enrollment findByMemberAndProgram(Person member, Program program);
}