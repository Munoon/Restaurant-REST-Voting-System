package com.train4game.munoon.model;

import org.hibernate.validator.constraints.SafeHtml;
import com.train4game.munoon.View;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractBaseEntity {
    @Size(min = 2, max = 225)
    @Column(name = "name", nullable = false)
    @SafeHtml(groups = {View.Web.class})
    @NotBlank
    protected String name;

    public AbstractNamedEntity() {
    }

    public AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
