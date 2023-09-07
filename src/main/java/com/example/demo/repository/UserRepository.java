package com.example.demo.repository;
 
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Userlog;

@Repository
public interface UserRepository extends CrudRepository<Userlog, Integer>{
  Optional<Userlog> findByName(String name); 
  Integer countByName(String name);
  @Query(value = "select * from user where name = ?1", nativeQuery = true)
  Userlog findBySomeConstraintSpringCantParse(String name);
 


}