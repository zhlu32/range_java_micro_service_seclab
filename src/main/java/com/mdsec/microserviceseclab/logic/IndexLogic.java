package com.mdsec.microserviceseclab.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mdsec.microserviceseclab.data.Student;
import com.mdsec.microserviceseclab.data.Teacher;
import com.mdsec.microserviceseclab.db.IndexDb;

@Service
public class IndexLogic {

    @Autowired
    IndexDb indexDb;

    public List<Student> getStudent(String username) {
        return indexDb.getStudent(username);
    }

    public List<Student> getStudentById(Integer id) {
        return indexDb.getStudentById(id);
    }

    public List<Student> getStudentWithOptional(Optional<String> username) {
        return indexDb.getStudentWithOptional(username);
    }

    public List<Student> getStudentWithIn(List<String> user_list) {
        return indexDb.getStudentWithIn(user_list);
    }

    public List<Student> getStudentWithInLong(List<Long> user_list) {
        return indexDb.getStudentWithInLong(user_list);
    }

    public List<Teacher> getTeacher(String userName) {
        return indexDb.getTeacher(userName);
    }

}
