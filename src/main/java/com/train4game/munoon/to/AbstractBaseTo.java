package com.train4game.munoon.to;

abstract public class AbstractBaseTo {
    protected Integer id;

    public AbstractBaseTo() {
    }

    public AbstractBaseTo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isNew() {
        return id == null;
    }
}
