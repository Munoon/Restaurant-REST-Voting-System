package com.train4game.munoon.to;

import com.train4game.munoon.HasId;

abstract public class AbstractBaseTo implements HasId {
    protected Integer id;

    public AbstractBaseTo() {
    }

    public AbstractBaseTo(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
