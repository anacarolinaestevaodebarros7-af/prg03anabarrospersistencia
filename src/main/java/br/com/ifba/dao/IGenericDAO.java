package br.com.ifba.dao;

import java.util.List;

/**
 * Interface genérica para o padrão DAO.
 * Define as operações CRUD básicas para qualquer entidade.
 *
 * @param <T>  Tipo da entidade
 * @param <ID> Tipo da chave primária
 *
 * @author PRG03 - IFBA
 */
public interface IGenericDAO<T, ID> {

    /**
     * Persiste uma nova entidade no banco de dados.
     *
     * @param entity entidade a ser salva
     * @return entidade salva (com ID gerado)
     */
    T salvar(T entity);

    /**
     * Atualiza uma entidade existente no banco de dados.
     *
     * @param entity entidade com os dados atualizados
     * @return entidade atualizada (estado gerenciado pelo JPA)
     */
    T atualizar(T entity);

    /**
     * Remove uma entidade pelo seu identificador.
     *
     * @param id identificador da entidade a ser removida
     */
    void remover(ID id);

    /**
     * Busca uma entidade pelo seu identificador.
     *
     * @param id identificador da entidade
     * @return entidade encontrada
     */
    T buscarPorId(ID id);

    /**
     * Retorna todas as entidades do tipo T.
     *
     * @return lista de entidades
     */
    List<T> listarTodos();
}