package com.train4game.munoon.to.meal;

import com.train4game.munoon.to.AbstractBaseTo;

import java.time.LocalDate;

public abstract class AbstractMealTo extends AbstractBaseTo {
    protected String name;
    protected int price;
    protected LocalDate date = LocalDate.now();

    public AbstractMealTo() {
    }

    public AbstractMealTo(Integer id, String name, int price, LocalDate date) {
        super(id);
        this.name = name;
        this.price = price;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}