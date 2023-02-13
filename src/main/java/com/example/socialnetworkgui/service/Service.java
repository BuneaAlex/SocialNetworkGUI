package com.example.socialnetworkgui.service;


import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.repository.Repository;

/**
 * Abstract service with a CRUD repository
 * @param <ID> -type E must have an attribute of type ID
 * @param <E>  -type of entities saved in repository
 */
public abstract class Service<ID,E extends Entity<ID>> {

    protected Repository<ID,E> repository;

    /**
     * Constructor
     * @param repository -a generic repository
     */
    public Service(Repository<ID, E> repository) {
        this.repository = repository;
    }

    /**
     * saving an entity in the repository
     * @param entity -a serializable entity which must not be null
     * @return null- if the given entity is saved
     * otherwise returns the entity (id already exists)
     */
    public E save(E entity)
    {
        return repository.save(entity);
    }

    /**
     * Deleting an entity
     * @param id -must not be null
     * @return the removed entity or null if there is no entity with the given id
     */
    public E delete(ID id)
    {
        return repository.delete(id);
    }

    /**
     * Updating an entity
     * @param entity -a serializable entity which must not be null
     * @return null - if the entity is updated,
     * otherwise  returns the entity  - (e.g id does not exist).
     */
    public E update(E entity)
    {
        return repository.update(entity);
    }

    /**
     * A collection of entities
     * @return all the entities
     */
    public Iterable<E> findAll()
    {
        return repository.findAll();
    }
}
