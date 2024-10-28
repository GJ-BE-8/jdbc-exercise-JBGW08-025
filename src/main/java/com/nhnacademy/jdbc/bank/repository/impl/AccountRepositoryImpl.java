package com.nhnacademy.jdbc.bank.repository.impl;

import com.nhnacademy.jdbc.bank.domain.Account;
import com.nhnacademy.jdbc.bank.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class AccountRepositoryImpl implements AccountRepository {

    public Optional<Account> findByAccountNumber(Connection connection, long accountNumber) {
        //todo#1 계좌-조회

        String sql = "SELECT * FROM jdbc_account WHERE account_number = ?";

        ResultSet resultSet = null;

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setLong(1, accountNumber);
            resultSet = psmt.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                long balance = resultSet.getLong("balance");

                Account account = new Account(accountNumber, name, balance);

                return Optional.of(account);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public int save(Connection connection, Account account) {
        //todo#2 계좌-등록, executeUpdate() 결과를 반환 합니다.

        String sql = "INSERT INTO jdbc_account VALUES(?, ?, ?)";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setLong(1, account.getAccountNumber());
            psmt.setString(2, account.getName());
            psmt.setLong(3, account.getBalance());

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByAccountNumber(Connection connection, long accountNumber) {
        int count = 0;
        //todo#3 select count(*)를 이용해서 계좌의 개수를 count해서 반환

        String sql = "SELECT count(*) as count FROM jdbc_account WHERE account_number = ?";

        ResultSet resultSet = null;

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setLong(1, accountNumber);
            resultSet = psmt.executeQuery();

            if (resultSet.next()) {
                count = resultSet.getInt("count");
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

        return count;
    }

    @Override
    public int deposit(Connection connection, long accountNumber, long amount) {
        //todo#4 입금, executeUpdate() 결과를 반환 합니다.

        String sql = "UPDATE jdbc_account SET balance = balance + ? WHERE account_number = ?";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setLong(1, amount);
            psmt.setLong(2, accountNumber);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int withdraw(Connection connection, long accountNumber, long amount) {
        //todo#5 출금, executeUpdate() 결과를 반환 합니다.

        String sql = "UPDATE jdbc_account SET balance = balance - ? WHERE account_number = ?";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setLong(1, amount);
            psmt.setLong(2, accountNumber);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByAccountNumber(Connection connection, long accountNumber) {
        //todo#6 계좌 삭제, executeUpdate() 결과를 반환 합니다.

        String sql = "DELETE FROM jdbc_account WHERE account_number=?";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setLong(1, accountNumber);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
