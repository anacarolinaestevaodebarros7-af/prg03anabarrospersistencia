package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO para a entidade Curso.
 * Define o contrato de acesso a dados — implementado por CursoDAO.
 *
 * @author PRG03 - IFBA
 */
public interface ICursoDAO {

    /** Persiste ou atualiza um curso. */
    Curso salvar(Curso curso);

    /** Retorna todos os cursos cadastrados. */
    List<Curso> listarTodos();

    /** Busca um curso pelo ID. */
    Optional<Curso> buscarPorId(Long id);

    /** Remove um curso pelo ID. */
    void remover(Long id);

    /** Busca um curso pelo código único. */
    Optional<Curso> buscarPorCodigo(String codigoCurso);

    /** Lista cursos filtrados por status ativo/inativo. */
    List<Curso> buscarPorAtivo(boolean ativo);
}