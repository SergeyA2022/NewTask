package com.haulmont.testTask.dao;

import com.haulmont.testTask.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

    @Query("from Group g " +
            "where concat(g.number, ' ', g.faculty) " +
            "like concat('%', :name, '%') ")
    List<Group> findAllByName(@Param("name") String name);



}
