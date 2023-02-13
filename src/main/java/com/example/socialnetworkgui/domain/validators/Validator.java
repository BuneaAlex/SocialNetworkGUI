package com.example.socialnetworkgui.domain.validators;

/**
 * Validator interface
 * @param <T> -generic
 */

public interface Validator<T> {
    /**
     * Validates the entity's attributes
     * @param entity -serializable entity
     * @throws ValidationException -if the entity is not valid
     */

    void validate(T entity) throws ValidationException;
}
