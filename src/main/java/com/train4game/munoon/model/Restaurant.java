package com.train4game.munoon.model;

import org.springframework.lang.Nullable;

import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

public class Restaurant extends AbstractNamedEntity {
    @JoinColumn(name = "restaurant_id", foreignKey = @ForeignKey(name = "global_seq", foreignKeyDefinition = "START WITH 100"))
    @OneToMany(fetch = FetchType.LAZY)
    @Nullable
    private List<Meal> menu;

    public Restaurant() {
    }

    public Restaurant(Restaurant r) {
        this(r.getId(), r.getName(), r.getMenu());
    }

    public Restaurant(Integer id, String name, List<Meal> menu) {
        super(id, name);
        this.menu = menu;
    }

    public List<Meal> getMenu() {
        return menu;
    }

    public void setMenu(List<Meal> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "menu=" + menu +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
