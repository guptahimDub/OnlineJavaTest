package com.todolist.repository;

import com.todolist.repository.model.TaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTaskRepository extends JpaRepository<TaskInfo, Integer> {

    @Query(" SELECT ut FROM TaskInfo ut WHERE ut.user.id = :userId" )
    List<TaskInfo> findByUserId(@Param("userId") long userId);

}
