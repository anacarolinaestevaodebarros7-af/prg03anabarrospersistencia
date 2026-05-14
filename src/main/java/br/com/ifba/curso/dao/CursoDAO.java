package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do DAO de Curso usando Spring + JPA.
 *
 * @Repository  → Marca este Bean como componente de acesso a dados.
 *               O Spring detecta automaticamente via component-scan
 *               e o registra no contexto (ApplicationContext).
 *
 * @PersistenceContext → O Spring injeta o EntityManager gerenciado
 *                       (thread-safe, escopo de transação).
 *                       Não precisamos mais do JPAUtil!
 *
 * @author PRG03 - IFBA
 */
@Repository
public class CursoDAO implements ICursoDAO {

    /**
     * O Spring injeta o EntityManager automaticamente.
     * Ele é um proxy thread-safe — cada transação usa sua própria instância.
     */
    @PersistenceContext
    private EntityManager em;

    // ─────────────────────────────────────────
    // SALVAR / ATUALIZAR
    // ─────────────────────────────────────────
    @Override
    public Curso salvar(Curso curso) {
        if (curso.getId() == null) {
            em.persist(curso);          // INSERT
            System.out.println("✅ Curso salvo: " + curso);
        } else {
            curso = em.merge(curso);    // UPDATE
            System.out.println("✅ Curso atualizado: " + curso);
        }
        return curso;
    }

    // ─────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────
    @Override
    public List<Curso> listarTodos() {
        return em.createQuery("SELECT c FROM Curso c ORDER BY c.nome", Curso.class)
                 .getResultList();
    }

    // ─────────────────────────────────────────
    // BUSCAR POR ID
    // ─────────────────────────────────────────
    @Override
    public Optional<Curso> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Curso.class, id));
    }

    // ─────────────────────────────────────────
    // REMOVER
    // ─────────────────────────────────────────
    @Override
    public void remover(Long id) {
        buscarPorId(id).ifPresentOrElse(curso -> {
            em.remove(curso);
            System.out.println("🗑️  Curso removido: " + curso);
        }, () -> {
            throw new IllegalArgumentException("Curso com ID " + id + " não encontrado.");
        });
    }

    // ─────────────────────────────────────────
    // BUSCAR POR CÓDIGO
    // ─────────────────────────────────────────
    @Override
    public Optional<Curso> buscarPorCodigo(String codigoCurso) {
        try {
            Curso curso = em.createQuery(
                    "SELECT c FROM Curso c WHERE c.codigoCurso = :codigo", Curso.class)
                    .setParameter("codigo", codigoCurso)
                    .getSingleResult();
            return Optional.of(curso);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // ─────────────────────────────────────────
    // BUSCAR POR STATUS ATIVO
    // ─────────────────────────────────────────
    @Override
    public List<Curso> buscarPorAtivo(boolean ativo) {
        return em.createQuery(
                "SELECT c FROM Curso c WHERE c.ativo = :ativo ORDER BY c.nome", Curso.class)
                .setParameter("ativo", ativo)
                .getResultList();
    }
}