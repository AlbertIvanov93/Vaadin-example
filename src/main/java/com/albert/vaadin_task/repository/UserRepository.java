package com.albert.vaadin_task.repository;

import com.albert.vaadin_task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("from User u " +
            "where concat(u.lastName, '', u.firstName, '', u.patronymic) " +
            "like concat('%', :name, '%') " )
    List<User> findByName(@Param("name") String name);

    @Query("from User u " +
            "where concat(u.lastName, u.firstName, u.patronymic) = :fullName")
    User findByFullName(@Param("fullName") String fullName);
}
