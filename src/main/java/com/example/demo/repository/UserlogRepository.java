package com.example.demo.repository;
 
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Userlog;

public interface UserlogRepository extends CrudRepository<Userlog, Long>{
    public Optional<Userlog> findByName(String name);
}