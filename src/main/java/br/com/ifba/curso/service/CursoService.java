package br.com.ifba.curso.service;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.repository.ICursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ════════════════════════════════════════════════════════════════
 *  REFATORAÇÃO: CursoService — DAO → Spring Data JPA Repository
 * ════════════════════════════════════════════════════════════════
 *
 * O que mudou em relação à versão anterior:
 *
 *   ANTES:  private final ICursoDAO cursoDAO;
 *   AGORA:  private final ICursoRepository cursoRepository;
 *
 *   A lógica de negócio (@Transactional, validações) permanece
 *   INTACTA — apenas o "canal" de acesso ao banco mudou.
 *
 *   Mapeamento de chamadas:
 *     cursoDAO.salvar(curso)          → cursoRepository.save(curso)
 *     cursoDAO.listarTodos()          → cursoRepository.findAll()
 *     cursoDAO.buscarPorId(id)        → cursoRepository.findById(id)
 *     cursoDAO.remover(id)            → cursoRepository.deleteById(id)
 *     cursoDAO.buscarPorCodigo(c)     → cursoRepository.findByCodigoCurso(c)
 *     cursoDAO.buscarPorAtivo(a)      → cursoRepository.findByAtivoOrderByNome(a)
 *
 * @Service      → Bean de serviço gerenciado pelo Spring.
 * @Transactional → Spring abre/fecha transação automaticamente.
 *
 * @author PRG03 - IFBA
 */
@Service
public class CursoService implements ICursoService {

    /**
     * Injeção via construtor — melhor prática com Spring.
     *
     * ANTES: injetava ICursoDAO (interface do DAO manual)
     * AGORA: injeta ICursoRepository (interface Spring Data JPA)
     *
     * O Spring detecta o bean criado pelo <jpa:repositories> e injeta aqui.
     */
    private final ICursoRepository cursoRepository;

    @Autowired
    public CursoService(ICursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    // ─────────────────────────────────────────
    // SALVAR
    // save() faz persist se id==null, merge se id!=null
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public Curso salvar(Curso curso) {
        validarCurso(curso);
        Curso salvo = cursoRepository.save(curso);  // ← era cursoDAO.salvar()
        System.out.println("✅ Curso salvo: " + salvo);
        return salvo;
    }

    // ─────────────────────────────────────────
    // LISTAR TODOS
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();  // ← era cursoDAO.listarTodos()
    }

    // ─────────────────────────────────────────
    // BUSCAR POR ID
    // findById já retorna Optional<Curso> nativamente
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> buscarPorId(Long id) {
        return cursoRepository.findById(id);  // ← era cursoDAO.buscarPorId()
    }

    // ─────────────────────────────────────────
    // REMOVER
    // deleteById lança EmptyResultDataAccessException se não existir
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public void remover(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException("Curso com ID " + id + " não encontrado.");
        }
        cursoRepository.deleteById(id);  // ← era cursoDAO.remover()
        System.out.println("🗑️  Curso ID " + id + " removido.");
    }

    // ─────────────────────────────────────────
    // BUSCAR POR CÓDIGO
    // Query Method derivado automaticamente pelo Spring Data
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> buscarPorCodigo(String codigoCurso) {
        return cursoRepository.findByCodigoCurso(codigoCurso); // ← era cursoDAO.buscarPorCodigo()
    }

    // ─────────────────────────────────────────
    // BUSCAR POR STATUS ATIVO
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorAtivo(boolean ativo) {
        return cursoRepository.findByAtivoOrderByNome(ativo); // ← era cursoDAO.buscarPorAtivo()
    }

    // ─────────────────────────────────────────
    // BUSCAR POR NOME (BÔNUS — novo!)
    // Não existia antes; Spring Data entrega pelo nome do método
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorNome(String nome) {
        return cursoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // ─────────────────────────────────────────
    // VALIDAÇÃO (regra de negócio — permanece igual)
    // ─────────────────────────────────────────
    private void validarCurso(Curso curso) {
        if (curso.getNome() == null || curso.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do curso é obrigatório.");
        }
        if (curso.getCodigoCurso() == null || curso.getCodigoCurso().isBlank()) {
            throw new IllegalArgumentException("O código do curso é obrigatório.");
        }
    }
}