package com.mdsec.microserviceseclab.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mdsec.microserviceseclab.data.Person;

// 创建PersionRespository的Dao对象，供Controller使用
public interface PersonRepository extends JpaRepository<Person, String> {

    public List<Person> findPersonByUsername(String username);

    @Query("SELECT p FROM Person p WHERE p.nickname like %?1%")
    public List<Person> findPersonByNickname(String nickname);
}
