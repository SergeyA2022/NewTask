package com.haulmont.testTask.dao;


import com.haulmont.testTask.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {
    @Query("from Student s " +
            "where concat(s.lastName, ' ', s.fistName, ' ', s.patronymic) " +
    "like concat('%', :name, '%') ")
    List<Student> findAllByName(@Param("name") String name);
}
