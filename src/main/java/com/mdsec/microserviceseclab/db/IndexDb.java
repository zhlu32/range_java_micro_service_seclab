package com.mdsec.microserviceseclab.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Joiner;
import com.mdsec.microserviceseclab.data.Student;
import com.mdsec.microserviceseclab.data.Teacher;

@Repository
public class IndexDb {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final RowMapper<Student> ROW_MAPPER = (rs, i) -> {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setAge(rs.getInt("age"));
        student.setSex(rs.getInt("sex"));
        student.setUsername(rs.getString("username"));
        return student;
    };

    private static final RowMapper<Teacher> ROW_MAPPER_TEACHER = (rs, i) -> {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getInt("id"));
        teacher.setSex(rs.getBoolean("sex"));
        teacher.setUsername(rs.getString("username"));
        return teacher;
    };

    public List<Student> getStudent(String username) {
        //String sql = "select * from students where username like '%" + username.get() + "%'";
        String sql = "select * from students where username like '%" + username + "%'";
        //String sql = "select * from students where username like ?";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public List<Student> getStudentWithOptional(Optional<String> username) {
        String sqlWithOptional = "select * from students where username like '%" + username.get() + "%'";
        //String sql = "select * from students where username like ?";
        return jdbcTemplate.query(sqlWithOptional, ROW_MAPPER);
    }

    public List<Student> getStudentById(Integer id) {
        String sqlWithInt = "select * from students where id = '" + String.valueOf(id) + "'";
        return jdbcTemplate.query(sqlWithInt, ROW_MAPPER);
    }

    public List<Student> getStudentWithIn(List<String> name_list) {
        if(CollectionUtils.isEmpty(name_list)) {
            return null;
        }
        String sqlWithIn = "select * from students where username in ('"+ Joiner.on("','").join(name_list)+"')";
        return jdbcTemplate.query(sqlWithIn, ROW_MAPPER);
    }

    public List<Student> getStudentWithInLong(List<Long> name_list) {
        if(CollectionUtils.isEmpty(name_list)) {
            return null;
        }
        String sqlWithIn = "select * from students where username in ('"+ Joiner.on("','").join(name_list)+"')";
        return jdbcTemplate.query(sqlWithIn, ROW_MAPPER);
    }

    public List<Teacher> getTeacher(String userName) {
        String sqlWithInt = "select * from teachers where username = '" + userName + "'";
        return jdbcTemplate.query(sqlWithInt, ROW_MAPPER_TEACHER);
    }

}
