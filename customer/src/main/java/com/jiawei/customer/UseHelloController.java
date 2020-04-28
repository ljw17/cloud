package com.jiawei.customer;

import com.jiawei.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@RestController
public class UseHelloController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/hello1")
    public String hello1() {
        HttpURLConnection con = null;
        try {
            URL url = new URL("http://localhost:1113/hello");
            con = (HttpURLConnection) url.openConnection();
            if (con.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String s = br.readLine();
                br.close();
                return s;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("restTemplateTwo")
    RestTemplate restTemplateTwo;

    @GetMapping("/hello2")
    public String hello2() {
        List<ServiceInstance> list = discoveryClient.getInstances("provider");
        ServiceInstance instance = list.get(0);
        String host = instance.getHost();
        int port = instance.getPort();
        StringBuilder sb = new StringBuilder();
        sb.append("http://")
                .append(host)
                .append(":")
                .append(port)
                .append("/hello");
        String s = restTemplate.getForObject(sb.toString(), String.class);
        return s;

    }

    @GetMapping("/hello3")
    public String hello3() {
        return restTemplateTwo.getForObject("http://provider/hello", String.class);
    }

    @GetMapping("/hello6")
    public void hello6() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", "jiawei");
        map.add("password", "123");
        map.add("id", "1");
        User user = restTemplateTwo.postForObject("http://provider/user1", map, User.class);
        System.out.println(user);
        user.setId(12);
        restTemplateTwo.postForObject("http://provider/user1", user, User.class);
        System.out.println(user);
    }

    @GetMapping("/hello7")
    public void hello7() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", "jiawei");
        map.add("password", "123");
        map.add("id", "1");
        URI uri = restTemplateTwo.postForLocation("http://provider/register", map);
        String s = restTemplateTwo.getForObject(uri, String.class);
        System.out.println(s);
    }

    @GetMapping("/hello8")
    public void hello8() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("username", "zhangsan");
        map.add("password", "1111");
        map.add("id", "1");
        restTemplateTwo.put("http://provider/user1", map);

        User user = new User();
        user.setId(12);
        user.setUsername("lisi");
        user.setPassword("121212");
        restTemplateTwo.put("http://provider/user2", user);
    }

    @GetMapping("hello9")
    public void hello9() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("id", "1");
        restTemplateTwo.delete("http://provider/user1?id={1}", 12);
        User user = new User();
        user.setId(12);
        restTemplateTwo.delete("http://provider/user2/{1}", 13);
    }
}
