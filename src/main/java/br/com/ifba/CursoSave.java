package br.com.ifba;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.service.ICursoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada da aplicação.
 *
 * Aqui demonstramos o uso do Spring Framework:
 *
 *   ApplicationContext → "Fábrica" de Beans do Spring.
 *                        Lê o applicationContext.xml, instancia e
 *                        injeta todos os Beans automaticamente.
 *
 *   ctx.getBean()      → Recupera o Bean já configurado pelo Spring.
 *                        Não usamos "new CursoService()" — o Spring faz isso!
 *
 * Fluxo de chamadas:
 *   CursoSave → ICursoService (Bean: CursoService)
 *                   → ICursoDAO   (Bean: CursoDAO)
 *                       → EntityManager (gerenciado pelo Spring)
 *                           → Banco H2
 *
 * @author PRG03 - IFBA
 */
public class CursoSave {

    public static void main(String[] args) {

        // ── 1. Inicializa o Spring Container ──────────────────────────────
        // O Spring lê o XML, cria os Beans e faz todas as injeções
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  Spring Container inicializado com êxito ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // ── 2. Obtém o Bean de serviço ────────────────────────────────────
        // Programa para a INTERFACE (ICursoService), não para a implementação
        ICursoService cursoService = ctx.getBean(ICursoService.class);

        // ── 3. Menu interativo ────────────────────────────────────────────
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            exibirMenu();
            opcao = lerInt(scanner, "Escolha uma opção: ");

        
            switch (opcao) {
                case 1:
                    salvarCurso(scanner, cursoService);
                    break;
                case 2:
                    listarCursos(cursoService);
                    break;
                case 3:
                    atualizarCurso(scanner, cursoService);
                    break;
                case 4:
                    removerCurso(scanner, cursoService);
                    break;
                case 5:
                    buscarPorCodigo(scanner, cursoService);
                    break;
                case 6:
                    listarPorStatus(scanner, cursoService);
                    break;
                case 0:
                    System.out.println("Encerrando sistema Spring...");
                    break;
                default:
                    System.out.println("⚠️  Opção inválida!");
                    break;
            }
        } while (opcao != 0);

        scanner.close();

        // Fecha o contexto Spring (encerra EntityManagerFactory etc.)
        ((ClassPathXmlApplicationContext) ctx).close();
        System.out.println("✅ Contexto Spring encerrado.");
    }

    // ─── MENU ─────────────────────────────────────────────────────────────
    private static void exibirMenu() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║   GERENCIAMENTO DE CURSOS — SPRING DI    ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1 - Salvar novo curso                   ║");
        System.out.println("║  2 - Listar todos os cursos              ║");
        System.out.println("║  3 - Atualizar curso                     ║");
        System.out.println("║  4 - Remover curso                       ║");
        System.out.println("║  5 - Buscar por código                   ║");
        System.out.println("║  6 - Listar por status (ativo/inativo)   ║");
        System.out.println("║  0 - Sair                                ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    // ─── SALVAR ────────────────────────────────────────────────────────────
    private static void salvarCurso(Scanner sc, ICursoService service) {
        try {
            System.out.println("\n--- Novo Curso ---");
            Curso curso = new Curso();
            curso.setNome(lerTexto(sc, "Nome do curso: "));
            curso.setCodigoCurso(lerTexto(sc, "Código do curso: "));
            curso.setAtivo(lerBoolean(sc, "Ativo? (s/n): "));
            service.salvar(curso);
        } catch (IllegalArgumentException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }

    // ─── LISTAR ────────────────────────────────────────────────────────────
    private static void listarCursos(ICursoService service) {
        List<Curso> cursos = service.listarTodos();
        if (cursos.isEmpty()) {
            System.out.println("⚠️  Nenhum curso cadastrado.");
        } else {
            System.out.println("\n--- Lista de Cursos ---");
            cursos.forEach(System.out::println);
        }
    }

    // ─── ATUALIZAR ─────────────────────────────────────────────────────────
    private static void atualizarCurso(Scanner sc, ICursoService service) {
        Long id = (long) lerInt(sc, "ID do curso a atualizar: ");
        service.buscarPorId(id).ifPresentOrElse(curso -> {
            System.out.println("Curso atual: " + curso);
            String novoNome   = lerTexto(sc, "Novo nome [" + curso.getNome() + "]: ");
            String novoCodigo = lerTexto(sc, "Novo código [" + curso.getCodigoCurso() + "]: ");
            if (!novoNome.isBlank())   curso.setNome(novoNome);
            if (!novoCodigo.isBlank()) curso.setCodigoCurso(novoCodigo);
            curso.setAtivo(lerBoolean(sc, "Ativo? (s/n): "));
            service.salvar(curso);
        }, () -> System.err.println("❌ Curso com ID " + id + " não encontrado."));
    }

    // ─── REMOVER ───────────────────────────────────────────────────────────
    private static void removerCurso(Scanner sc, ICursoService service) {
        try {
            Long id = (long) lerInt(sc, "ID do curso a remover: ");
            service.remover(id);
            System.out.println("✅ Curso removido com sucesso.");
        } catch (IllegalArgumentException e) {
            System.err.println("❌ " + e.getMessage());
        }
    }

    // ─── BUSCAR POR CÓDIGO ─────────────────────────────────────────────────
    private static void buscarPorCodigo(Scanner sc, ICursoService service) {
        String codigo = lerTexto(sc, "Código do curso: ");
        service.buscarPorCodigo(codigo).ifPresentOrElse(
                c -> System.out.println("Curso encontrado: " + c),
                () -> System.out.println("⚠️  Nenhum curso com código '" + codigo + "'.")
        );
    }

    // ─── LISTAR POR STATUS ─────────────────────────────────────────────────
    private static void listarPorStatus(Scanner sc, ICursoService service) {
        boolean ativo = lerBoolean(sc, "Listar ativos? (s/n): ");
        List<Curso> cursos = service.buscarPorAtivo(ativo);
        if (cursos.isEmpty()) {
            System.out.println("⚠️  Nenhum curso " + (ativo ? "ativo" : "inativo") + ".");
        } else {
            cursos.forEach(System.out::println);
        }
    }

    // ─── UTILITÁRIOS ───────────────────────────────────────────────────────
    private static String lerTexto(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static int lerInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Digite um número válido.");
            }
        }
    }

    private static boolean lerBoolean(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim().equalsIgnoreCase("s");
    }
}