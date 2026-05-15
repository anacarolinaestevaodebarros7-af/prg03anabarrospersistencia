package br.com.ifba.curso.repository;

import br.com.ifba.curso.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * ════════════════════════════════════════════════════════════════
 *  REFATORAÇÃO: DAO  →  Spring Data JPA Repository
 * ════════════════════════════════════════════════════════════════
 *
 * ANTES (versão anterior):
 *   - ICursoDAO.java   → interface com assinaturas manuais
 *   - CursoDAO.java    → implementação com EntityManager, JPQL, etc.
 *   - GenericDAO.java  → classe abstrata com operações básicas (CRUD)
 *
 * AGORA (Spring Data JPA):
 *   - ICursoRepository.java  → UMA ÚNICA interface, sem implementação!
 *   - O Spring gera TUDO automaticamente em runtime.
 *
 * ────────────────────────────────────────────────────────────────
 *  Como funciona JpaRepository<Curso, Long>:
 *
 *   JpaRepository herda de:
 *     └── PagingAndSortingRepository
 *           └── CrudRepository  (save, findById, findAll, delete, count…)
 *
 *   Métodos herdados gratuitos (sem escrever nada):
 *     save(S entity)          → INSERT ou UPDATE (substitui salvar())
 *     findById(Long id)       → retorna Optional<Curso>
 *     findAll()               → retorna List<Curso>
 *     deleteById(Long id)     → remove pelo ID
 *     existsById(Long id)     → verifica existência
 *     count()                 → total de registros
 *     saveAll(Iterable)       → salva múltiplos de uma vez
 *
 * ────────────────────────────────────────────────────────────────
 *  Query Methods — derivação automática pelo nome do método:
 *
 *   Spring Data lê o nome do método e gera o SQL correspondente.
 *   Convenção: findBy + NomeDoCampo + [Condicional]
 *
 *   Exemplos desta interface:
 *     findByCodigoCurso(String)  → SELECT * FROM curso WHERE codigo_curso = ?
 *     findByAtivo(boolean)       → SELECT * FROM curso WHERE ativo = ?
 *     findByNomeContaining(String) → SELECT * FROM curso WHERE nome LIKE %?%
 *
 *   Palavras-chave disponíveis: And, Or, Between, LessThan, GreaterThan,
 *   Like, Containing, StartingWith, EndingWith, OrderBy, Not, In, etc.
 *
 * ────────────────────────────────────────────────────────────────
 *  O que FOI ELIMINADO:
 *    ✖ CursoDAO.java       — Spring Data gera a implementação
 *    ✖ GenericDAO.java     — JpaRepository já é o genérico universal
 *    ✖ ICursoDAO.java      — substituída por esta interface
 *    ✖ JPAUtil.java        — Spring gerencia EntityManager/Factory
 *    ✖ persistence.xml     — configuração migrada para applicationContext.xml
 *
 * @author PRG03 - IFBA
 */
public interface ICursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Busca um curso pelo código único.
     *
     * Equivalente ao método buscarPorCodigo() do ICursoDAO anterior.
     * Spring Data gera automaticamente:
     *   SELECT c FROM Curso c WHERE c.codigoCurso = :codigoCurso
     */
    Optional<Curso> findByCodigoCurso(String codigoCurso);

    /**
     * Lista cursos filtrados por status ativo/inativo.
     *
     * Equivalente ao método buscarPorAtivo() do ICursoDAO anterior.
     * Spring Data gera automaticamente:
     *   SELECT c FROM Curso c WHERE c.ativo = :ativo ORDER BY c.nome
     */
    List<Curso> findByAtivoOrderByNome(boolean ativo);

    /**
     * BÔNUS — busca por parte do nome (LIKE).
     * Não existia antes; Spring Data entrega de graça pelo nome do método.
     *   SELECT c FROM Curso c WHERE c.nome LIKE %:nome%
     */
    List<Curso> findByNomeContainingIgnoreCase(String nome);
}