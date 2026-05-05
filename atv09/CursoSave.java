package br.com.ifba;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Scanner;

/**
 * Menu interativo para gerenciamento de Cursos via JPA + Hibernate.
 * Demonstra: salvar, atualizar, remover, listar e tratamento de exceções.
 *
 * @author PRG03 - IFBA
 */
public class CursoSave {

    private static final CursoRepository repository = new CursoRepository();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            int opcao;
            do {
                exibirMenu();
                opcao = lerInt("Escolha uma opção: ");

                switch (opcao) {
                    case 1 -> salvarCurso();
                    case 2 -> listarCursos();
                    case 3 -> atualizarCurso();
                    case 4 -> removerCurso();
                    case 0 -> System.out.println("Encerrando sistema...");
                    default -> System.out.println("⚠️  Opção inválida!");
                }

            } while (opcao != 0);

        } finally {
            scanner.close();
            JPAUtil.close();
        }
    }

    // ─────────────────────────────────────────
    // MENU
    // ─────────────────────────────────────────
    private static void exibirMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║    GERENCIAMENTO DE CURSOS   ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║  1 - Salvar novo curso       ║");
        System.out.println("║  2 - Listar todos os cursos  ║");
        System.out.println("║  3 - Atualizar curso         ║");
        System.out.println("║  4 - Remover curso           ║");
        System.out.println("║  0 - Sair                    ║");
        System.out.println("╚══════════════════════════════╝");
    }

    // ─────────────────────────────────────────
    // OPERAÇÃO: SALVAR
    // ─────────────────────────────────────────
    private static void salvarCurso() {
        try {
            System.out.println("\n--- Novo Curso ---");
            Curso curso = new Curso();
            curso.setNome(lerTexto("Nome do curso: "));
            curso.setCodigoCurso(lerTexto("Código do curso: "));
            curso.setAtivo(lerBoolean("Ativo? (s/n): "));
            repository.salvar(curso);
        } catch (RuntimeException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // OPERAÇÃO: LISTAR
    // ─────────────────────────────────────────
    private static void listarCursos() {
        try {
            List<Curso> cursos = repository.listarTodos();
            if (cursos.isEmpty()) {
                System.out.println("⚠️  Nenhum curso cadastrado.");
                return;
            }
            System.out.println("\n--- Lista de Cursos ---");
            cursos.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.err.println("❌ Erro ao listar cursos: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // OPERAÇÃO: ATUALIZAR
    // ─────────────────────────────────────────
    private static void atualizarCurso() {
        try {
            Long id = (long) lerInt("ID do curso a atualizar: ");
            Curso curso = repository.buscarPorId(id); // lança EntityNotFoundException se não achar

            System.out.println("Curso atual: " + curso);
            System.out.println("(Deixe em branco para manter o valor atual)");

            String novoNome = lerTexto("Novo nome [" + curso.getNome() + "]: ");
            String novoCodigo = lerTexto("Novo código [" + curso.getCodigoCurso() + "]: ");

            if (!novoNome.isBlank()) curso.setNome(novoNome);
            if (!novoCodigo.isBlank()) curso.setCodigoCurso(novoCodigo);
            curso.setAtivo(lerBoolean("Ativo? (s/n): "));

            repository.atualizar(curso);
        } catch (EntityNotFoundException e) {
            System.err.println("❌ " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("❌ Erro ao atualizar: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // OPERAÇÃO: REMOVER
    // ─────────────────────────────────────────
    private static void removerCurso() {
        try {
            Long id = (long) lerInt("ID do curso a remover: ");
            repository.remover(id);
        } catch (EntityNotFoundException e) {
            System.err.println("❌ " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("❌ Erro ao remover: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────
    // UTILITÁRIOS DE LEITURA
    // ─────────────────────────────────────────
    private static String lerTexto(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int lerInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Digite um número válido.");
            }
        }
    }

    private static boolean lerBoolean(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }
}