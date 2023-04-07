package org.ma99us.sbda.service;

import lombok.Getter;
import lombok.Setter;
import org.ma99us.sbda.model.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemsService {
    @Getter @Setter
    private List<Item> items = new ArrayList<>();

    public ItemsService() {
        // populate some items
        for (int i = 0; i < 10; i++) {
            items.add(new Item("Item #" + (i + 1), (int) (Math.random() * 100)));
        }
    }
}
