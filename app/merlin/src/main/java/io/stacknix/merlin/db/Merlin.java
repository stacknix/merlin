package io.stacknix.merlin.db;

import io.stacknix.merlin.db.commons.MappingFactory;
import io.stacknix.merlin.db.commons.ReflectionMappingFactory;

public class Merlin {

    private static Merlin instance;
    private final MappingFactory<?> mappingFactory;

    public static synchronized Merlin getInstance() {
        return instance;
    }

    public static MappingFactory<?> getMappingFactory(){
        return instance.mappingFactory;
    }

    public Merlin(Class<? extends MerlinObject>... models) {
        this.mappingFactory = new ReflectionMappingFactory();
    }

}
