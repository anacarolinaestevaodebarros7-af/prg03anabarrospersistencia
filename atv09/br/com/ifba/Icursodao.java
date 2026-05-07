package br.com.ifba.curso.dao;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.dao.IGenericDAO;

import java.util.List;

/**
 * Interface DAO específica para a entidade Curso.
 * Estende IGenericDAO e permite adicionar métodos de consulta específicos.
 *
 * @author PRG03 - IFBA
 */
public interface ICursoDAO extends IGenericDAO<Curso, Long> {

    /**
     * Busca cursos pelo status ativo/inativo.
     *
     * @param ativo true para listar apenas ativos, false para inativos
     * @return lista de cursos filtrados
     */
    List<Curso> buscarPorAtivo(boolean ativo);

    /**
     * Busca um curso pelo código único.
     *
     * @param codigoCurso código identificador do curso
     * @return curso encontrado ou null
     */
    Curso buscarPorCodigo(String codigoCurso);
}