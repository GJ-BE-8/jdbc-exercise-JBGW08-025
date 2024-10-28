package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@Slf4j
public class StudentSaveTest {

    StudentRepository studentRepository = new StatementStudentRepository();

    @Test
    void save() {
        studentRepository.save(new Student("student0", "NHN0", Student.GENDER.F, 20));
    }

    @Test
    void findById() {
        Optional<Student> student0 = studentRepository.findById("student9");
        log.debug("{}", student0);
    }
}
