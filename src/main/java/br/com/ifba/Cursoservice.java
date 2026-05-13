package br.com.ifba.curso.service;

import br.com.ifba.curso.dao.ICursoDAO;
import br.com.ifba.curso.entity.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Camada de Serviço para regras de negócio de Curso.
 *
 * @Service      → Marca como Bean de serviço no contexto Spring.
 *                O Spring instancia e gerencia o ciclo de vida deste objeto.
 *
 * @Autowired    → Injeção de Dependência: o Spring injeta automaticamente
 *                o Bean que implementa ICursoDAO (no caso, CursoDAO).
 *                Programamos para a INTERFACE, não para a implementação concreta.
 *
 * @Transactional → O Spring abre e fecha a transação automaticamente.
 *                  Em caso de exceção, faz rollback. Sem try/finally manual!
 *
 * @author PRG03 - IFBA
 */
@Service
public class CursoService implements ICursoService {

    /**
     * Injeção de Dependência via construtor (melhor prática com Spring).
     * O Spring procura um Bean que implemente ICursoDAO e injeta aqui.
     */
    private final ICursoDAO cursoDAO;

    @Autowired
    public CursoService(ICursoDAO cursoDAO) {
        this.cursoDAO = cursoDAO;
    }

    // ─────────────────────────────────────────
    // SALVAR
    // Transação obrigatória para escrita no banco
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public Curso salvar(Curso curso) {
        validarCurso(curso);
        return cursoDAO.salvar(curso);
    }

    // ─────────────────────────────────────────
    // LISTAR
    // readOnly=true: otimização para operações de leitura
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Curso> listarTodos() {
        return cursoDAO.listarTodos();
    }

    // ─────────────────────────────────────────
    // BUSCAR POR ID
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> buscarPorId(Long id) {
        return cursoDAO.buscarPorId(id);
    }

    // ─────────────────────────────────────────
    // REMOVER
    // ─────────────────────────────────────────
    @Override
    @Transactional
    public void remover(Long id) {
        cursoDAO.remover(id);
    }

    // ─────────────────────────────────────────
    // BUSCAR POR CÓDIGO
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> buscarPorCodigo(String codigoCurso) {
        return cursoDAO.buscarPorCodigo(codigoCurso);
    }

    // ─────────────────────────────────────────
    // BUSCAR POR STATUS
    // ─────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public List<Curso> buscarPorAtivo(boolean ativo) {
        return cursoDAO.buscarPorAtivo(ativo);
    }

    // ─────────────────────────────────────────
    // VALIDAÇÃO (Regra de Negócio)
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