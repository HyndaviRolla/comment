package com.example.demo.service;
 
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entity.Userlog; 
import com.example.demo.repository.UserlogRepository;
@Service
public class UserService {
    @Autowired
    private UserlogRepository userlogRepository;

    public Optional<Userlog> authenticate(String username, String password) {
        Optional<Userlog> optUser = userlogRepository.findByName(username);
        
        if (!optUser.get().getPassword().equals(password)) {
            return Optional.empty();
        }
        return optUser;
    }
    public Userlog create(Userlog user) {
        return userlogRepository.save(user);
    } 
}