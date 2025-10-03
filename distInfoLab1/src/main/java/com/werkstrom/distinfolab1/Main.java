package com.werkstrom.distinfolab1;

import com.werkstrom.distinfolab1.bo.Item;
import com.werkstrom.distinfolab1.bo.ItemCategory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ItemCategory games = new ItemCategory(1, "Games");
        ItemCategory consoles = new ItemCategory(2, "Consoles");

        List<ItemCategory> categoryList = new ArrayList<>();
        categoryList.add(games);
        categoryList.add(consoles);

        Item item = new Item(1, "Playstation 5", "PlayStation 5 Pro är den senaste konsolen från Sony, som lanserades i november 2024. " +
                "Den har fått stor uppmärksamhet tack vare sin höga prestanda, " +
                "nya funktioner och enorma mängd spel som är tillgängliga.", 5500.0f, 13, categoryList);

        System.out.println("ID:" + item.getId());
        System.out.println("Name:" + item.getName());
        System.out.println("Description:" + item.getDescription());
        System.out.println("Price:" + item.getPrice() + " kr");
        System.out.println("Stock:" + item.getStock() + " st");
        System.out.println("Categories:" + item.getCategories());

        item.addStock(2);
        System.out.println("After addStock(2): stock=" + item.getStock());

        item.removeStock(5);;
        System.out.println("After removeStock(5): stock=" + item.getStock());

        try {
            item.removeStock(1000); // för mycket
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        try {
            item.addCategory(null); // borde kasta om du har null-koll
        } catch (IllegalArgumentException e) {
            System.out.println("Expected error: " + e.getMessage());
        }
    }
}
