package vn.hoidanit.jobhunter.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.*;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        // User user = this.userRepository.findById(id);
        this.userRepository.deleteById(id);
    }
    
    public User handleGetUserById(long id) {
        return this.userRepository.findById(id);
    }

    public List<User> handleGetAllUser() {
        return this.userRepository.findAll();
    }

    public User handleUpdateUser(User user) {
        User updateUser = this.handleGetUserById(user.getId());
        if(updateUser != null) {
            updateUser.setName(user.getName());
            updateUser.setEmail(user.getEmail());
            updateUser.setPassword(user.getPassword());
            this.userRepository.save(updateUser);
        }

        return updateUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}   



    