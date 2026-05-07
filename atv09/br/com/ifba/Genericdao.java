package br.com.ifba.dao;

import br.com.ifba.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

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
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            System.out.println("✅ " + entityClass.getSimpleName() + " salvo com sucesso!");
            return entity;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // ATUALIZAR
    // ─────────────────────────────────────────
    @Override
    public T atualizar(T entity) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T atualizado = em.merge(entity);
            em.getTransaction().commit();
            System.out.println("✅ " + entityClass.getSimpleName() + " atualizado com sucesso!");
            return atualizado;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // REMOVER
    // ─────────────────────────────────────────
    @Override
    public void remover(ID id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity == null) {
                throw new EntityNotFoundException(
                    entityClass.getSimpleName() + " com ID " + id + " não encontrado."
                );
            }
            em.remove(entity);
            em.getTransaction().commit();
            System.out.println("✅ " + entityClass.getSimpleName() + " removido com sucesso!");
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            throw e;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao remover " + entityClass.getSimpleName() + ": " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // BUSCAR POR ID
    // ─────────────────────────────────────────
    @Override
    public T buscarPorId(ID id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            T entity = em.find(entityClass, id);
            if (entity == null) {
                throw new EntityNotFoundException(
                    entityClass.getSimpleName() + " com ID " + id + " não encontrado."
                );
            }
            return entity;
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────
    @Override
    public List<T> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            return em.createQuery(jpql, entityClass).getResultList();
        } finally {
            em.close();
        }
    }
}