package com.example.socialnetworkgui.repository.file;



import com.example.socialnetworkgui.domain.Entity;
import com.example.socialnetworkgui.domain.validators.Validator;
import com.example.socialnetworkgui.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.List;

/**
 * An abstract file repository
 * @param <ID> - type E must have an attribute of type ID
 * @param <E> - type of entities saved in repository
 */
public abstract class AbstractFileRepo<ID,E extends Entity<ID>> extends InMemoryRepository<ID,E> {

    private String filename;

    /**
     * Constructor
     * @param validator-validator for entity of type E
     * @param filename-the name of the file that we write to
     */

    public AbstractFileRepo(Validator<E> validator, String filename) {
        super(validator);
        this.filename = filename;
        loadData();
    }

    /**
     * Read data from file, save entities in repository
     */
    private void loadData() {
        try(BufferedReader br = new BufferedReader(new FileReader(filename)))
        {
            String line;
            while((line= br.readLine())!=null)
            {
                List<String> attributes = List.of(line.split(","));
                E entity = extractEntity(attributes);
                super.save(entity);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Getting an entity from a list of strings
     * @param attributes-list of string for attributes
     * @return entity which has the attributes: attributes
     */

    public abstract E extractEntity(List<String> attributes);

    /**
     * transforming an entity into a string
     * @param entity-an entity
     * @return a string that contains the entity's attributes
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * Appending to a file the attributes of an entity
     * @param entity-an entity
     */
    protected void writeToFile(E entity)
    {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename,true)))
        {
            String entityToString = createEntityAsString(entity);
            bw.write(entityToString);
            bw.newLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Write every entity from the repository into a file
     */

    protected void writeAllToFile()
    {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename,false)))
        {
            for(E entity : findAll())
            {
                String entityToString = createEntityAsString(entity);
                bw.write(entityToString);
                bw.newLine();
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public E save(E entity) {

        E rez = super.save(entity);
        writeToFile(entity);
        return rez;
    }

    @Override
    public E delete(ID id) {
        E rez = super.delete(id);
        if(rez!=null)
            writeAllToFile();
        return rez;
    }

    @Override
    public E update(E entity) {
        E rez = super.update(entity);
        writeAllToFile();
        return rez;
    }

    @Override
    public E findOne(ID id) {
        return super.findOne(id);
    }
}
