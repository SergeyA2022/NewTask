package com.haulmont.testTask.dao;

import com.haulmont.testTask.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

}
