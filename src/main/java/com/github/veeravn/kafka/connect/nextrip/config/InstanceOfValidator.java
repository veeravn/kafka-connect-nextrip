package com.github.veeravn.kafka.connect.nextrip.config;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

public class InstanceOfValidator implements ConfigDef.Validator {

    private Class<?> parent;

    public InstanceOfValidator(Class<?> parent) {
        this.parent = parent;
    }

    @Override
    public void ensureValid(String name, Object obj) {
        if (obj instanceof Class
                && parent.isAssignableFrom((Class<?>) obj)) {
            return;
        }
        throw new ConfigException(name, obj, "Class must extend: " + parent);
    }

    @Override
    public String toString() {
        return "Any class implementing: " + parent;
    }

}
