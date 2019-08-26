package com.train4game.munoon.to;

import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RestaurantTo extends AbstractBaseTo {
    @Size(min = 2, max = 225)
    @NotBlank
    @SafeHtml
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
