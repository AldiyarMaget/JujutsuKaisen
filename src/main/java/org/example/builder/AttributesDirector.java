package org.example.builder;

import org.example.model.Attributes;

public class AttributesDirector {
    private final AttributesBuilder builder;

    public AttributesDirector(AttributesBuilder builder) {
        this.builder = builder;
    }

    public Attributes createPlayerAttributes(){
        return builder.withDefaults().build();
    }

    public Attributes createMonsterAttributes(){
        return builder.withDefaults().build();
    }
}
