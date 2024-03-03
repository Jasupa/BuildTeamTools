package net.buildtheearth.modules.generator.model;

import lombok.Getter;

public enum GeneratorType {

    HOUSE("House"),
    ROAD("Road"),
    RAILWAY("Railway"),
    TREE("Tree");

    @Getter
    private final String name;

    GeneratorType(String name) {
        this.name = name;
    }

}
