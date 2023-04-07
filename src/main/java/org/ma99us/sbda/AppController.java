package org.ma99us.sbda;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @RequestMapping("/hello")
    String hello() {
        return "Hello World!";
    }

    @RequestMapping("/title")
    String title() {
        return "Spring Boot Desktop Application";
    }
}
