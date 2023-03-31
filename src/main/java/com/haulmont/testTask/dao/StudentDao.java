package com.haulmont.testTask.dao;


import com.haulmont.testTask.entity.Group;
import com.haulmont.testTask.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentDao extends JpaRepository<Student, Long> {
    @Query("from Student s where (s.lastName) like concat( :name, '%') ")
    List<Student> findAllByName(@Param("name") String name);

    @Query("from Student s where (s.group) = ( :name ) ")
    List<Student> findAllStudentGroup(@Param("name") Group group);

    @Query("from Student s  where (s.lastName) " +
            "like concat( :name, '%') and (s.group) = ( :group ) ")
    List<Student> findAllStudentGroupStudent(@Param("name") String name, @Param("group") Group group);
}
