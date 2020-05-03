package com.jiawei.hystrix;

import com.jiawei.commons.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        System.out.println("hystrix");
        return helloService.hello();
    }

    @GetMapping("/hello2")
    public void hello2() {
        //String execute = command.execute();//直接执行
        HelloCommand command = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("jiawei")), restTemplate);
        HelloCommand command2 = new HelloCommand(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("jiawei")), restTemplate);
        String execute = command2.execute();
        System.out.println(execute);
        Future<String> queue = command.queue();//先入队
        try {
            String s = queue.get();
            System.out.println(s);//后执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/hello3")
    public void hello3() {
        Future<String> future = helloService.hello2();
        try {
            String s = future.get();
            System.out.println(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("/hello4")
    public void hello4() {
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        String haha = helloService.hello3("haha");
        helloService.delete();
        String haha1 = helloService.hello3("haha");
        ctx.close();
    }

    @Autowired
    UserService userService;

    @GetMapping("/hello5")
    public void hello5() throws ExecutionException, InterruptedException {
        HystrixRequestContext ctx = HystrixRequestContext.initializeContext();
        UserCollapseCommand command1 = new UserCollapseCommand(userService, 99);
        UserCollapseCommand command2 = new UserCollapseCommand(userService, 98);
        UserCollapseCommand command3 = new UserCollapseCommand(userService, 97);
        UserCollapseCommand command4 = new UserCollapseCommand(userService, 96);
        Future<User> queue = command1.queue();
        Future<User> queue2 = command2.queue();
        Future<User> queue3 = command3.queue();
        Future<User> queue4 = command4.queue();
        User user = queue.get();
        User user2 = queue2.get();
        User user3 = queue3.get();
        User user4 = queue4.get();
        System.out.println(user);
        System.out.println(user2);
        System.out.println(user3);
        System.out.println(user4);
        ctx.close();
    }

}
