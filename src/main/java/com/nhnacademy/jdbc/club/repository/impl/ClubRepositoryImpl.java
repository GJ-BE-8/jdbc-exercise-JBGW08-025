package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.Club;
import com.nhnacademy.jdbc.club.repository.ClubRepository;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

public class ClubRepositoryImpl implements ClubRepository {

    @Override
    public Optional<Club> findByClubId(Connection connection, String clubId) {
        //todo#3 club 조회

        String sql = "SELECT * FROM jdbc_club WHERE club_id = ?";

        ResultSet resultSet = null;
        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, clubId);
            resultSet = psmt.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("club_name");
                Timestamp createdAt = resultSet.getTimestamp("club_created_at");

                Club club = new Club(clubId, name, createdAt.toLocalDateTime());

                return Optional.of(club);
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
    public int save(Connection connection, Club club) {
        //todo#4 club 생성, executeUpdate() 결과를 반환

        String sql = "INSERT INTO jdbc_club VALUES(?, ?, ?)";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, club.getClubId());
            psmt.setString(2, club.getClubName());
            psmt.setTimestamp(3, Timestamp.valueOf(club.getClubCreatedAt()));

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Connection connection, Club club) {
        //todo#5 club 수정, clubName을 수정합니다. executeUpdate()결과를 반환

        String sql = "UPDATE jdbc_club SET club_name = ? WHERE club_id = ?";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, club.getClubName());
            psmt.setString(2, club.getClubId());

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByClubId(Connection connection, String clubId) {
        //todo#6 club 삭제, executeUpdate()결과 반환

        String sql = "DELETE FROM jdbc_club WHERE club_id = ?";

        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, clubId);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByClubId(Connection connection, String clubId) {
        //todo#7 clubId에 해당하는 club의 count를 반환
        int count = 0;

        String sql = "SELECT COUNT(*) as count FROM jdbc_club WHERE club_id = ?";


        ResultSet resultSet = null;
        try (PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, clubId);
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
}
