package br.com.ifba.curso.repository;

import br.com.ifba.JPAUtil;
import br.com.ifba.curso.entity.Curso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Repositório responsável pelas operações CRUD da entidade Curso.
 * Cada método gerencia seu próprio EntityManager e transação.
 *
 * @author PRG03 - IFBA
 */
public class CursoRepository {

    // ─────────────────────────────────────────
    // SALVAR
    // ─────────────────────────────────────────
    public Curso salvar(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(curso);
            em.getTransaction().commit();
            System.out.println("✅ Curso salvo! ID gerado: " + curso.getId());
            return curso;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao salvar curso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // ATUALIZAR
    // ─────────────────────────────────────────
    public Curso atualizar(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Curso atualizado = em.merge(curso);
            em.getTransaction().commit();
            System.out.println("✅ Curso atualizado: " + atualizado);
            return atualizado;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao atualizar curso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // REMOVER POR ID
    // ─────────────────────────────────────────
    public void remover(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Curso curso = em.find(Curso.class, id);
            if (curso == null) {
                throw new EntityNotFoundException("Curso com ID " + id + " não encontrado.");
            }
            em.remove(curso);
            em.getTransaction().commit();
            System.out.println("✅ Curso removido com sucesso!");
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            throw e; // relança para ser tratada na camada superior
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Erro ao remover curso: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // BUSCAR POR ID
    // ─────────────────────────────────────────
    public Curso buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Curso curso = em.find(Curso.class, id);
            if (curso == null) {
                throw new EntityNotFoundException("Curso com ID " + id + " não encontrado.");
            }
            return curso;
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────
    public List<Curso> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Curso c ORDER BY c.nome", Curso.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}