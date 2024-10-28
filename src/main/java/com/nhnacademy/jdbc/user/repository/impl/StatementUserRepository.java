package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Slf4j
public class StatementUserRepository implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#1 아이디, 비밀번호가 일치하는 User 조회

        log.info("user-password: {}", userPassword);

        String sql = "SELECT * FROM jdbc_users WHERE user_id = '" + userId + "' AND user_password = '" + userPassword + "'";

        try (
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);)
        {

            if (resultSet.next()) {
                String findUserId = resultSet.getString("user_id");
                String name = resultSet.getString("user_name");
                String findUserPassword = resultSet.getString("user_password");

                User user = new User(findUserId, name, findUserPassword);

                return Optional.of(user);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
        //#todo#2-아이디로 User 조회

        String sql = "SELECT * FROM jdbc_users WHERE user_id = '" + userId + "'";

        try (
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);
        ) {
            if (resultSet.next()) {
                String name = resultSet.getString("user_name");
                String userPassword = resultSet.getString("user_password");

                User user = new User(userId, name, userPassword);

                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return Optional.empty();
    }

    @Override
    public int save(User user) {
        //todo#3- User 저장

        String sql = "INSERT INTO jdbc_users VALUES('" + user.getUserId() + "','" + user.getUserName() + "','" + user.getUserPassword() + "')";

        try (
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#4-User 비밀번호 변경

        String sql = "UPDATE jdbc_users SET user_password = '" + userPassword + "' WHERE user_id = '" + userId + "'";

        try (
                Connection connection = DbUtils.getConnection();
                Statement statement = connection.createStatement();
        ) {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#5 - User 삭제

        String sql = "DELETE FROM jdbc_users WHERE user_id = '" + userId + "'";

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
