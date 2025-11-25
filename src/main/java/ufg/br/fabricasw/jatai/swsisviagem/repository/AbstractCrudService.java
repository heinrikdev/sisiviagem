package ufg.br.fabricasw.jatai.swsisviagem.repository;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guilherme on 06/09/17.
 */
public interface AbstractCrudService<T, ID extends Serializable> {

    public T save(T entity);

    public T update(T entity);

    public T findOne(ID id);

    public boolean exists(ID id);

    public List<T> findAll();

    public void delete(ID id);

    public void delete(T entity);

}
