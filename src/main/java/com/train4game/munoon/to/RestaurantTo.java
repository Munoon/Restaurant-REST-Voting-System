package com.train4game.munoon.to;

import java.util.List;

public class RestaurantTo extends AbstractBaseTo {
    private String name;
    private List<MealTo> menu;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MealTo> getMenu() {
        return menu;
    }

    public void setMenu(List<MealTo> menu) {
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
