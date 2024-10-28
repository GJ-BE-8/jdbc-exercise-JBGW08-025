package com.nhnacademy.jdbc.club.repository.impl;

import com.nhnacademy.jdbc.club.domain.ClubStudent;
import com.nhnacademy.jdbc.club.repository.ClubRegistrationRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class ClubRegistrationRepositoryImpl implements ClubRegistrationRepository {

    @Override
    public int save(Connection connection, String studentId, String clubId) {
        //todo#11 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환

        String sql = "INSERT INTO jdbc_club_registrations VALUES(?, ?)";

        try(PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, studentId);
            psmt.setString(2, clubId);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByStudentIdAndClubId(Connection connection, String studentId, String clubId) {
        //todo#12 - 핵생 -> 클럽 탈퇴, executeUpdate() 결과를 반환

        String sql = "DELETE FROM jdbc_club_registrations WHERE student_id = ? AND club_id = ?";

        try(PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, studentId);
            psmt.setString(2, clubId);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClubStudent> findClubStudentsByStudentId(Connection connection, String studentId) {
        //todo#13 - 핵생 -> 클럽 등록, executeUpdate() 결과를 반환

        String sql = "SELECT id, name, club_id, club_name FROM jdbc_club NATURAL JOIN jdbc_club_registrations NATURAL JOIN jdbc_students WHERE id = ?";

        List<ClubStudent> list = new ArrayList<>();
        ResultSet resultSet = null;

        try(PreparedStatement psmt = connection.prepareStatement(sql);) {
            psmt.setString(1, studentId);

            resultSet = psmt.executeQuery();

            while (resultSet.next()) {
                String studentName = resultSet.getString("name");
                String clubId = resultSet.getString("club_id");
                String clubName = resultSet.getString("club_name");

                ClubStudent clubStudent = new ClubStudent(studentId, studentName, clubId, clubName);
                list.add(clubStudent);
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClubStudent> findClubStudents(Connection connection) {
        //todo#21 - join
        String sql = "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  inner join jdbc_club_registrations b on a.id=b.student_id inner join jdbc_club c on b.club_id=c.club_id order by a.id asc, b.club_id asc";
        log.debug("sql:{}",sql);
        try{
            return getClubStudentList(connection,sql);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_left_join(Connection connection) {
        //todo#22 - left join
        String sql = "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id order by a.id asc, b.club_id asc";
        try{
            return getClubStudentList(connection,sql);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_right_join(Connection connection) {
        //todo#23 - right join
        String sql = "select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id order by c.club_id asc,a.id asc";
        try{
            return getClubStudentList(connection,sql);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_full_join(Connection connection) {
        //todo#24 - full join

        StringBuilder sb = new StringBuilder();
        //left join
        sb.append("select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id");
        sb.append(System.lineSeparator());
        sb.append("union");
        sb.append(System.lineSeparator());
        //right join
        sb.append("select a.id as student_id, a.name as student_name, c.club_id, c.club_name from jdbc_students a right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id");

        try{
            return getClubStudentList(connection,sb.toString());
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_left_excluding_join(Connection connection) {
        //todo#25 - left excluding join

        String sql = "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id where c.club_id is null order by a.id asc";
        try{
            return getClubStudentList(connection,sql);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_right_excluding_join(Connection connection) {
        //todo#26 - right excluding join
        String sql = "select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id where a.id is null order by b.club_id asc ";
        try{
            return getClubStudentList(connection,sql);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<ClubStudent> findClubStudents_outher_excluding_join(Connection connection) {
        //todo#27 - outher_excluding_join
        StringBuilder sb = new StringBuilder();
        //left join
        sb.append("select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  left join jdbc_club_registrations b on a.id=b.student_id left join jdbc_club c on b.club_id=c.club_id where c.club_id is null");
        sb.append(System.lineSeparator());
        sb.append("union");
        sb.append(System.lineSeparator());
        //right join
        sb.append("select   a.id as student_id,  a.name as student_name,  c.club_id,  c.club_name from jdbc_students a  right join jdbc_club_registrations b on a.id=b.student_id right join jdbc_club c on b.club_id=c.club_id where a.id is null");

        try{
            return getClubStudentList(connection,sb.toString());
        }catch (Exception e){
            throw e;
        }
    }

    private List<ClubStudent> getClubStudentList(Connection connection, String sql){
        ResultSet rs = null;

        try(PreparedStatement psmt = connection.prepareStatement(sql)){
            rs = psmt.executeQuery();

            List<ClubStudent> clubStudentList = new ArrayList<>();
            while (rs.next()){
                clubStudentList.add(
                        new ClubStudent(
                                rs.getString("student_id"),
                                rs.getString("student_name"),
                                rs.getString("club_id"),
                                rs.getString("club_name")
                        )
                );
            }
            return clubStudentList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(Objects.nonNull(rs)) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}