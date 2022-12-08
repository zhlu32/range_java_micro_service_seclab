package com.mdsec.microserviceseclab.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mdsec.microserviceseclab.dao.PersonRepository;
import com.mdsec.microserviceseclab.data.Person;
import com.mdsec.microserviceseclab.data.Student;
import com.mdsec.microserviceseclab.data.Teacher;
import com.mdsec.microserviceseclab.logic.IndexLogic;
import com.mdsec.microserviceseclab.mapper.IStudentMapper;

@RestController
@RequestMapping(value = "/sqlinjection")
public class SqlInjectController {

    @Autowired
    IndexLogic indexLogic;

    @Autowired
    IStudentMapper iStudentMapper;

    @Autowired
    PersonRepository personRepository;

    // POC: curl -d "username=' or 1=1 or '" -X POST localhost:8080/sqlinjection/one
    @RequestMapping(value = "/one")
    public List<Student> one(@RequestParam(value = "username") String username) {
        return indexLogic.getStudent(username);
    }

    // POC: curl -d "[\"') or 1=1 or ('\"]" -H "Content-type: application/json"  -X POST localhost:8080/sqlinjection/in
    // in 类型的注入问题
    @PostMapping(value = "/in")
    public List<Student> in(@RequestBody List<String> user_list) {
        return indexLogic.getStudentWithIn(user_list);
    }

    // 【误报】这并不是一个注入，如果SAST扫描器将这个报为SQL注入漏洞，那么这是一个误报问题
    @PostMapping(value = "/longin")
    public List<Student> longin(@RequestBody List<Long> user_list) {
        return indexLogic.getStudentWithInLong(user_list);
    }

    // POC: curl -d "{\"username\":\"' or 1=1 or '\"}" -H "Content-type: application/json"  -X POST localhost:8080/sqlinjection/object
    @PostMapping(value = "/object")
    public List<Student> objectParam(@RequestBody Student user) {
        return indexLogic.getStudent(user.getUsername());
    }

    // 【误报】这里并不存在注入，只是用来测试看SAST扫描器是否将这个误报为SQL注入漏洞
    @PostMapping(value = "/objectint")
    public List<Student> objectIntParam(@RequestBody Student user) {
        return indexLogic.getStudentById(user.getId());
    }

    // POC: curl -d "{\"username\":\"' or 1=1 or '\"}" -H "Content-type: application/json"  -X POST localhost:8080/sqlinjection/object_lombok
    // 测试Lombok插件是否会影响漏洞的判断
    @PostMapping(value = "/object_lombok")
    public List<Teacher> objectLomBok(@RequestBody Teacher user) {
        return indexLogic.getTeacher(user.getUsername());
    }

    // POC: curl -d "username=' or 1=1 or '" -X POST localhost:8080/sqlinjection/optional_like
    // java8 Optional新特性
    @RequestMapping(value = "/optional_like")
    public List<Student> optionalLike(@RequestParam(value = "username") Optional<String> optinal_username) {
        return indexLogic.getStudentWithOptional(optinal_username);
    }

    // POC: curl -d "name=' or 1=1 or '" -X POST localhost:8080/sqlinjection/myBatis
    // XML分离SQL检测
    @RequestMapping(value = "/myBatis")
    public List<Student> myBatis(@RequestParam(value = "name") String name) {
        return iStudentMapper.queryAll(name);
    }

    // POC: curl -d "name=' or 1=1 or '" -X POST localhost:8080/sqlinjection/myBatisWithAnnotations
    // MyBatis注解方式注入
    @RequestMapping(value = "/myBatisWithAnnotations")
    public List<Student> myBatisWithAnnotations(@RequestParam(value = "name") String name) {
        return iStudentMapper.queryAllByAnnotations(name);
    }

    // 【误报】一般性jpa查询，不存在注入问题
    @RequestMapping(value = "/jpaone")
    public List<Person> jpaOne(@RequestParam(value = "name") String name) {
        return personRepository.findPersonByUsername(name);
    }

    // 【误报】JPA的@Query用法
    @RequestMapping(value = "/jpaWithAnnotations")
    public List<Person> jpawithAnnotations(@RequestParam(value = "name") String name) {
        return personRepository.findPersonByNickname(name);
    }
}
