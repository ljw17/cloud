package com.jiawei.provider;

import com.jiawei.commons.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @GetMapping("/user")
    public List<User> getUserById(@PathVariable int[] ids) {
        System.out.println(ids.toString());
        List<User> users = new ArrayList<>();
        for (int id: ids) {
            User user = new User();
            user.setId(id);
            users.add(user);
        }
        return users;
    }
}
