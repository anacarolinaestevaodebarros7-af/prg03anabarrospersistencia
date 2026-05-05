package br.com.ifba.curso.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "codigo_curso", nullable = false, length = 10, unique = true)
    private String codigoCurso;

    @Column(nullable = false)
    private boolean ativo;

    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCodigoCurso() { return codigoCurso; }
    public void setCodigoCurso(String codigoCurso) { this.codigoCurso = codigoCurso; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    @Override
    public String toString() {
        return String.format(
            "Curso [id=%d | codigo=%s | nome=%s | ativo=%s]",
            id, codigoCurso, nome, ativo ? "Sim" : "Não"
        );
    }
}