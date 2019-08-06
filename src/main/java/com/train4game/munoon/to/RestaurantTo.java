package com.train4game.munoon.to;

import java.time.LocalDate;
import java.util.List;

public class RestaurantTo extends AbstractBaseTo {
    private String name;
    private List<MealToForRestaurant> menu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MealToForRestaurant> getMenu() {
        return menu;
    }

    public void setMenu(List<MealToForRestaurant> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "RestaurantTo{" +
                "name='" + name + '\'' +
                ", menu=" + menu +
                ", id=" + id +
                '}';
    }
}

class MealToForRestaurant extends AbstractBaseTo {
    private String name;
    private int price;
    private LocalDate date = LocalDate.now();

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MealToForRestaurant{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
