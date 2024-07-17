package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @GetMapping("/user/create")

    @GetMapping("/user")
    public List<User> getAllUser() {

        // User user = new User();
        // user.setEmail("quan23403@gmail.com");
        // user.setName("HMQ");
        // user.setPassword("123456");
        List<User> users = this.userService.handleGetAllUser();
        return users;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") long id) {

        // User user = new User();
        // user.setEmail("quan23403@gmail.com");
        // user.setName("HMQ");
        // user.setPassword("123456");
        User user = this.userService.handleGetUserById(id);
        return user;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User user) {

        // User user = new User();
        // user.setEmail("quan23403@gmail.com");
        // user.setName("HMQ");
        // user.setPassword("123456");
        User newUser = this.userService.handleCreateUser(user);
        return newUser;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "Delete user";
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User user) {

        return this.userService.handleUpdateUser(user);
    }
}
