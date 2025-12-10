package com.example.bmiapp.dao;

import com.example.bmiapp.model.Program;
import java.util.List;

public interface ProgramDao {
    List<Program> findAll();
    Program findById(Integer id);
    void save(Program program); // insert or update
    void delete(Integer id);
}
