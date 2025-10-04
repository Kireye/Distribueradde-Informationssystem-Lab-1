package com.werkstrom.distinfolab1;

import com.werkstrom.distinfolab1.bo.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1) Skapa kategorier
        ItemCategory games = new ItemCategory(1, "Games");
        ItemCategory consoles = new ItemCategory(2, "Consoles");

        // 2) Skapa items
        List<ItemCategory> ps5Cats = new ArrayList<>();
        ps5Cats.add(games);
        ps5Cats.add(consoles);

        Item ps5 = new Item(
                1,
                "PlayStation 5",
                "PlayStation 5 Pro – snabb och kraftfull.",
                5500.0f,
                10,
                ps5Cats
        );

        List<ItemCategory> gameCats = new ArrayList<>();
        gameCats.add(games);

        Item gow = new Item(
                2,
                "God of War Ragnarök",
                "Actionäventyr med fet story.",
                699.0f,
                50,
                gameCats
        );

        // 3) Skapa tom kundvagn + user
        ShoppingCart cart = new ShoppingCart(1001, new ArrayList<>());
        User user = new User(
                1001,
                UserRole.CUSTOMER,
                "Amir Alshammaa",
                "Amir@example.com",
                cart,
                new ArrayList<>()
        );

        System.out.println("== Start ==");
        System.out.println("User: " + user);
        System.out.println("Cart (empty): " + user.getCart());

        // 4) Lägg varor i kundvagnen
        user.getCart().addItem(ps5);
        user.getCart().addItem(gow);
        user.getCart().addItem(gow); // samma spel två gånger (ingen kvantitet, bara 2 rader)

        System.out.println("\n== After adding items to cart ==");
        System.out.println("Cart items: " + user.getCart().getItems());
        System.out.println("Cart total: " + user.getCart().getTotalPrice() + " kr");

        // 5) Skapa en order från kundvagnen (frys nuvarande innehåll)
        List<Item> itemsForOrder = user.getCart().getItems(); // defensiv kopia tillbaka
        Order order = new Order(
                5001,
                user.getId(),
                itemsForOrder,
                OrderStatus.ORDERED
        );
        user.addOrder(order);

        System.out.println("\n== After creating order ==");
        System.out.println("Order: " + order);
        System.out.println("User orders: " + user.getOrders());

        // 6) Töm kundvagnen (som man gör vid checkout)
        user.getCart().clearItems();
        System.out.println("\n== After clearing cart ==");
        System.out.println("Cart: " + user.getCart());

        // 7) Uppdatera orderstatus
        order.setStatus(OrderStatus.PACKAGED);
        System.out.println("Order status -> " + order.getStatus());
        order.setStatus(OrderStatus.SHIPPED);
        System.out.println("Order status -> " + order.getStatus());
        order.setStatus(OrderStatus.DELIVERED);
        System.out.println("Order status -> " + order.getStatus());

        // 8) Visa att defensiva kopior funkar (man kan inte smyga in beställningar utifrån)
        List<Order> external = user.getOrders(); // detta är en kopia
        external.clear(); // rensar bara kopian, inte userns interna lista
        System.out.println("\n== Defensive copy check ==");
        System.out.println("External cleared size: " + external.size());
        System.out.println("User still has orders: " + user.getOrders().size());

        // 9) Snabbt valideringstest (förväntade fel)
        try {
            user.setEmail("felmail"); // saknar '@'
        } catch (IllegalArgumentException e) {
            System.out.println("Expected email error: " + e.getMessage());
        }

        try {
            user.getCart().addItem(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Expected cart error: " + e.getMessage());
        }

        System.out.println("\n== Done ==");
    }
}
