package br.com.ifba.curso.dao;

import br.com.ifba.JPAUtil;
import br.com.ifba.curso.entity.Curso;
import br.com.ifba.dao.GenericDAO;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Implementação concreta do DAO para a entidade Curso.
 * Herda as operações CRUD de GenericDAO e implementa consultas específicas.
 *
 * @author PRG03 - IFBA
 */
public class CursoDAO extends GenericDAO<Curso, Long> implements ICursoDAO {

    // ─────────────────────────────────────────
    // BUSCAR POR STATUS ATIVO
    // ─────────────────────────────────────────
    @Override
    public List<Curso> buscarPorAtivo(boolean ativo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Curso c WHERE c.ativo = :ativo ORDER BY c.nome",
                    Curso.class
            )
            .setParameter("ativo", ativo)
            .getResultList();
        } finally {
            em.close();
        }
    }

    // ─────────────────────────────────────────
    // BUSCAR POR CÓDIGO
    // ─────────────────────────────────────────
    @Override
    public Curso buscarPorCodigo(String codigoCurso) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                    "SELECT c FROM Curso c WHERE c.codigoCurso = :codigo",
                    Curso.class
            )
            .setParameter("codigo", codigoCurso)
            .getSingleResult();
        } catch (Exception e) {
            return null; // retorna null se não encontrar
        } finally {
            em.close();
        }
    }
}