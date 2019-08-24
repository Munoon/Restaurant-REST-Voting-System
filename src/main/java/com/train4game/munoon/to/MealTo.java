package com.train4game.munoon.to;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class MealTo extends AbstractBaseTo {
    @Size(min = 2, max = 200)
    @NotBlank
    @SafeHtml
    private String name;

    @Range(min = 10, max = 50000)
    private int price;

    private int restaurantId;

    @NotNull
    private LocalDate date = LocalDate.now();

    public MealTo() {
    }

    public MealTo(Integer id, @Size(min = 2, max = 200) @NotBlank String name, @Range(min = 10, max = 50000) int price, int restaurantId, @NotNull LocalDate date) {
        super(id);
        this.name = name;
        this.price = price;
        this.restaurantId = restaurantId;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurant) {
        this.restaurantId = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", restaurantId=" + restaurantId +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
