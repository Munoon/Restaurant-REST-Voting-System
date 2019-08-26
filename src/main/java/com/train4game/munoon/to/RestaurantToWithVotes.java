package com.train4game.munoon.to;

import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class RestaurantToWithVotes extends AbstractBaseTo {
    @Size(min = 2, max = 225)
    @NotBlank
    @SafeHtml
    private String name;

    private List<MealTo> menu;

    private int votes;

    public RestaurantToWithVotes() {
    }

    public RestaurantToWithVotes(Integer id, String name, List<MealTo> menu, int votes) {
        super(id);
        this.name = name;
        this.menu = menu;
        this.votes = votes;
    }

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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "RestaurantToWithVotes{" +
                "name='" + name + '\'' +
                ", menu=" + menu +
                ", votes=" + votes +
                ", id=" + id +
                '}';
    }
}
