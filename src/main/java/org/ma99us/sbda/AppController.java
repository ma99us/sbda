package org.ma99us.sbda;

import org.ma99us.sbda.model.Item;
import org.ma99us.sbda.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin            // #TEST ONLY!
public class AppController {

    @Autowired
    private ItemsService itemsService;

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @RequestMapping("/items")
    public List<Item> items() {
        return itemsService.getItems();
    }
}
