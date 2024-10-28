package com.nhnacademy.jdbc.student.repository.impl;

import com.nhnacademy.jdbc.student.domain.Student;
import com.nhnacademy.jdbc.student.repository.StudentRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Optional;

@Slf4j
public class PreparedStatementStudentRepository implements StudentRepository {

    @Override
    public int save(Student student) {
        //todo#1 학생 등록

        String sql = "insert into jdbc_students values(?, ?, ?, ?, ?)";

        try (
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);
        ) {
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
    public Optional<Student> findById(String id) {
        //todo#2 학생 조회

        String sql = "SELECT * FROM jdbc_students WHERE id = ?";

        try (
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);
        ) {
            psmt.setString(1, id);
            ResultSet resultSet = psmt.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Student.GENDER gender = Student.GENDER.valueOf(resultSet.getString("gender"));
                int age = resultSet.getInt("age");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                Student student = new Student(id, name, gender, age, createdAt.toLocalDateTime());
                return Optional.of(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int update(Student student) {
        //todo#3 학생 수정 , name 수정

        String sql = "UPDATE jdbc_students SET name = ?, gender = ?, age = ?, created_at = ? WHERE id = ?";

        try (
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);)
        {
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
    public int deleteById(String id) {
        //todo#4 학생 삭제

        String sql = "DELETE FROM jdbc_students WHERE id = ?";

        try(
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);)
        {
            psmt.setString(1, id);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
