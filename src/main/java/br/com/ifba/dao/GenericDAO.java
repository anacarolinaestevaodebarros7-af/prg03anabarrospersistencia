package br.com.ifba.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Implementação genérica do padrão DAO usando JPA/Hibernate.
 * Todas as classes DAO concretas devem estender esta classe.
 *
 * @param <T>  Tipo da entidade JPA
 * @param <ID> Tipo da chave primária
 *
 * @author PRG03 - IFBA
 */
public abstract class GenericDAO<T, ID> implements IGenericDAO<T, ID> {

    @PersistenceContext
    protected EntityManager em;

    // Classe da entidade resolvida via reflexão em tempo de execução
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericDAO() {
        // Descobre automaticamente o tipo T via reflexão
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) type.getActualTypeArguments()[0];
    }

    // ─────────────────────────────────────────
    // SALVAR
    // ─────────────────────────────────────────
    @Override
    public T salvar(T entity) {
        em.persist(entity);
        System.out.println("✅ " + entityClass.getSimpleName() + " salvo com sucesso!");
        return entity;
    }

    // ─────────────────────────────────────────
    // ATUALIZAR
    // ─────────────────────────────────────────
    @Override
    public T atualizar(T entity) {
        T atualizado = em.merge(entity);
        System.out.println("✅ " + entityClass.getSimpleName() + " atualizado com sucesso!");
        return atualizado;
    }

    // ─────────────────────────────────────────
    // REMOVER
    // ─────────────────────────────────────────
    @Override
    public void remover(ID id) {
        T entity = em.find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException(
                entityClass.getSimpleName() + " com ID " + id + " não encontrado."
            );
        }
        em.remove(entity);
        System.out.println("✅ " + entityClass.getSimpleName() + " removido com sucesso!");
    }

    // ─────────────────────────────────────────
    // BUSCAR POR ID
    // ─────────────────────────────────────────
    @Override
    public T buscarPorId(ID id) {
        T entity = em.find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException(
                entityClass.getSimpleName() + " com ID " + id + " não encontrado."
            );
        }
        return entity;
    }

    // ─────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────
    @Override
    public List<T> listarTodos() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, entityClass).getResultList();
    }
}
