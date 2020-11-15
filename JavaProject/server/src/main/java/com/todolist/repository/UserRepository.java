package com.todolist.repository;

import com.todolist.repository.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long>{

    @Query(" SELECT u FROM UserInfo u WHERE u.username = :username" )
    UserInfo findByUsername(@Param("username") String username);

}
