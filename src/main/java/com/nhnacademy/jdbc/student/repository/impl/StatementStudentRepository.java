package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class StatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student) {
        //todo#1 insert student
        String id = student.getId();
        String name = student.getName();
        Student.GENDER gender = student.getGender();
        Integer age = student.getAge();
        LocalDateTime createdAt = student.getCreatedAt();
        log.debug("id: {}, name: {}, gender: {}, age: {}, createdAt: {}", id, name, gender, age, createdAt);

        String sql = "INSERT INTO jdbc_students (id, name, gender, age, created_at) VALUES ('"
                + id + "', '"
                + name + "', '"
                + gender + "', "
                + age + ", '"
                + createdAt + "')";

        Connection connection = null;
        Statement statement = null;


        try {
            connection = DbUtils.getConnection();
            statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (Objects.nonNull(statement)) {
                    statement.close();
                }
                if (Objects.nonNull(connection)) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Optional<Student> findById(String id) {
        //todo#2 student 조회

        String sql = "SELECT id, name, gender, age, created_at FROM jdbc_students WHERE id = '" + id + "'";

        try (
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);) {

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Student.GENDER gender = Student.GENDER.valueOf(resultSet.getString("gender"));
                int age = resultSet.getInt("age");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

                Student student = new Student(id, name, gender, age, createdAt);
                return Optional.of(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Student student) {
        //todo#3 student 수정, name <- 수정합니다.
        String id = student.getId();
        String name = student.getName();
        Student.GENDER gender = student.getGender();
        Integer age = student.getAge();
        LocalDateTime createdAt = student.getCreatedAt();

        String sql = "UPDATE jdbc_students SET name = '" + name + "', "
                + "gender = '" + gender + "', "
                + "age = " + age + ", "
                + "created_at = '" + createdAt + "' "
                + "WHERE id = '" + id + "'";

        try(
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String id) {
        //todo#4 student 삭제
        String sql = "DELETE FROM jdbc_students WHERE id = '" + id + "'";
        try(
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
