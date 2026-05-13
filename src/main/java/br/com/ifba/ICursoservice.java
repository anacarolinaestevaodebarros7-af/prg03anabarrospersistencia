package br.com.ifba.curso.service;

import br.com.ifba.curso.entity.Curso;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para regras de negócio relacionadas a Curso.
 *
 * @author PRG03 - IFBA
 */
public interface ICursoService {

    Curso salvar(Curso curso);

    List<Curso> listarTodos();

    Optional<Curso> buscarPorId(Long id);

    void remover(Long id);

    Optional<Curso> buscarPorCodigo(String codigoCurso);

    List<Curso> buscarPorAtivo(boolean ativo);
}