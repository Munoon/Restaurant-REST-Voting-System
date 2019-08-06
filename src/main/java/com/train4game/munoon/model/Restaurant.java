package com.train4game.munoon.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.List;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "restaurants", uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "restaurants_unique_name_idx"))
public class Restaurant extends AbstractNamedEntity {
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    @OrderBy("date desc")
    private List<Meal> menu;

    public Restaurant() {
    }

    public Restaurant(Restaurant r) {
        this(r.getId(), r.getName());
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
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
