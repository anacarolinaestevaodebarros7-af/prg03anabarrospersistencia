package br.com.ifba.curso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidade JPA que representa um Curso.
 * Mapeada para a tabela "curso" no banco de dados.
 *
 * @author PRG03 - IFBA
 */
@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "codigo_curso", nullable = false, unique = true)
    private String codigoCurso;

    @Column(nullable = false)
    private Boolean ativo = true;

    // ─── Getters e Setters ───────────────────

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Curso{id=" + id
                + ", nome='" + nome + '\''
                + ", codigoCurso='" + codigoCurso + '\''
                + ", ativo=" + ativo + '}';
    }
}