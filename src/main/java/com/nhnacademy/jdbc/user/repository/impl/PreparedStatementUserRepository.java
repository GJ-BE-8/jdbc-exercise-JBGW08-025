package com.nhnacademy.jdbc.user.repository.impl;

import com.nhnacademy.jdbc.user.domain.User;
import com.nhnacademy.jdbc.user.repository.UserRepository;
import com.nhnacademy.jdbc.util.DbUtils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class PreparedStatementUserRepository implements UserRepository {
    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        //todo#11 -PreparedStatement- 아이디 , 비밀번호가 일치하는 회원조회

        String sql = "SELECT * FROM jdbc_users WHERE user_id = ? AND user_password = ?";

        ResultSet resultSet = null;

        try (
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);

        ) {
            psmt.setString(1, userId);
            psmt.setString(2, userPassword);

            resultSet = psmt.executeQuery();

            if (resultSet.next()) {
                String userId1 = resultSet.getString("user_id");
                String name = resultSet.getString("user_name");
                String userPassword1 = resultSet.getString("user_password");

                User user = new User(userId, name, userPassword1);

                return Optional.of(user);
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
    public Optional<User> findById(String userId) {
        //todo#12-PreparedStatement-회원조회

        String sql = "SELECT * FROM jdbc_users WHERE user_id = ?";

        Connection connection = null;
        PreparedStatement psmt = null;
        ResultSet resultSet = null;


        try {
            connection = DbUtils.getConnection();
            psmt = connection.prepareStatement(sql);

            psmt.setString(1, userId);

            resultSet = psmt.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("user_name");
                String userPassword = resultSet.getString("user_password");

                User user = new User(userId, name, userPassword);

                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (Objects.nonNull(connection)) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            if (Objects.nonNull(psmt)) {
                try {
                    psmt.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

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
    public int save(User user) {
        //todo#13-PreparedStatement-회원저장

        String sql = "INSERT INTO jdbc_users VALUES(?, ?, ?)";

        try (
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);
        ) {
            psmt.setString(1, user.getUserId());
            psmt.setString(2, user.getUserName());
            psmt.setString(3, user.getUserPassword());

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateUserPasswordByUserId(String userId, String userPassword) {
        //todo#14-PreparedStatement-회원정보 수정

        String sql = "UPDATE jdbc_users SET user_password = ? WHERE user_id = ?";

        try (
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);
        ) {
            psmt.setString(1, userPassword);
            psmt.setString(2, userId);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#15-PreparedStatement-회원삭제

        String sql = "DELETE FROM jdbc_users WHERE user_id = ?";

        try (
                Connection connection = DbUtils.getConnection();
                PreparedStatement psmt = connection.prepareStatement(sql);
        ) {
            psmt.setString(1, userId);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
