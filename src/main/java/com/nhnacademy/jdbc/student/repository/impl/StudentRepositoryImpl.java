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
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public int save(Connection connection, Student student) {
        //todo#2 학생등록

        String sql = "INSERT INTO jdbc_students VALUES(?, ?, ?, ?, ?)";

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, student.getId());
            psmt.setString(2, student.getName());
            psmt.setString(3, String.valueOf(student.getGender()));
            psmt.setInt(4, student.getAge());
            psmt.setTimestamp(5, Timestamp.valueOf(student.getCreatedAt()));

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<Student> findById(Connection connection, String id) {
        //todo#3 학생조회

        String sql = "SELECT * FROM jdbc_students WHERE id = ?";

        ResultSet resultSet = null;

        try(PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, id);
            resultSet = psmt.executeQuery();

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
        } finally {
            if (Objects.nonNull(resultSet)) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public int update(Connection connection, Student student) {
        //todo#4 학생수정

        String sql = "UPDATE jdbc_students SET name = ?, gender = ?, age = ?, created_at = ? WHERE id = ? ";

        try(PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, student.getName());
            psmt.setString(2, String.valueOf(student.getGender()));
            psmt.setInt(3, student.getAge());
            psmt.setTimestamp(4, Timestamp.valueOf(student.getCreatedAt()));
            psmt.setString(5, student.getId());

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(Connection connection, String id) {
        //todo#5 학생삭제

        String sql = "DELETE FROM jdbc_students WHERE id = ?";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, id);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}