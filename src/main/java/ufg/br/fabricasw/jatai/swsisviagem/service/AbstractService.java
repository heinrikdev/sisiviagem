package ufg.br.fabricasw.jatai.swsisviagem.service;

/**
 * Created by thevilela on 02/02/17.
 */
import java.io.Serializable;
import java.util.List;

public interface AbstractService<T extends Object, ID extends Serializable> {

    /**
     * @param entidade
     * @return
     */
    public T save(T entidade);

    /**
     * @param entidade
     */
    public void apagar(T entidade);

    /**
     * @param entidade
     * @return
     */
    public T atualizar(T entidade);

    /**
     * @param entidade
     * @return
     */
    public T find(T entidade);

    /**
     * @param id
     * @return
     */
    public T find(ID id);

    /**
     * @return
     */
    public List<T> findAll();

}
