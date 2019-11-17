package com.ics.ecommerce.common;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class clazz, Object id) {
        super("Could not find " + clazz.getSimpleName().toLowerCase() + " with id " + id);
    }
}
