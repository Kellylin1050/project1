package com.example.project1.Service;

import com.example.project1.Entity.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService  {
    Integer updateUser(User Entity);
    User findById(Integer id);
    Integer saveUser(User Entity);
    Integer deleteById(Integer id);
    List<User> findUser();
    public User getUserByNameAndPassword(String name, String password) throws UsernameNotFoundException;

    Map<String, String> generateToken(User user);
}
