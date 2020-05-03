package com.jiawei.provider;

import com.jiawei.commons.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class HelloController {

    @Value("${server.port}")
    Integer port;
    @GetMapping("hello")
    public String hello() {
        return "hello:" + port;
    }

    @GetMapping("/hello2")
    public String hello2(String name) {
        System.out.println(new Date() + ">>>" + name);
        return "hello:" + name;
    }

    @PostMapping("/user1")
    public User addUser1(User user) {
        return user;
    }

    @PostMapping("/user2")
    public User addUser2(@RequestBody User user) {
        return user;
    }

    @PutMapping("/user1")
    public void updateUser1(User user) {
        System.out.println(user);
    }

    @PutMapping("/user2")
    public void updateUser2(@RequestBody User user) {
        System.out.println(user);
    }

    @DeleteMapping("/user1")
    public void deleteUser1(Integer id) {
        System.out.println(id);
    }

    @DeleteMapping("/user2/{id}")
    public void deleteUser2(@PathVariable Integer id) {
        System.out.println(id);
    }
}
