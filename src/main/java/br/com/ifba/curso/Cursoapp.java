package br.com.ifba;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.service.ICursoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * ════════════════════════════════════════════════════════════════
 *  Classe principal — demonstra o projeto refatorado com
 *  Spring Data JPA.
 *
 *  Não há mais JPAUtil, persistence.xml ou CursoDAO!
 *  O Spring gerencia tudo via applicationContext.xml.
 * ════════════════════════════════════════════════════════════════
 *
 * @author PRG03 - IFBA
 */
public class CursoApp {

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  Spring Data JPA — Gerenciamento de Cursos ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // Inicializa o contexto Spring (lê applicationContext.xml)
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        // Obtém o serviço — o Spring injeta ICursoRepository automaticamente
        ICursoService service = ctx.getBean(ICursoService.class);

        // ─── 1. SALVAR cursos ─────────────────────────────────────
        System.out.println("\n── SALVANDO CURSOS ──────────────────────────");

        Curso c1 = new Curso();
        c1.setNome("Análise e Desenvolvimento de Sistemas");
        c1.setCodigoCurso("ADS001");
        c1.setAtivo(true);
        service.salvar(c1);

        Curso c2 = new Curso();
        c2.setNome("Redes de Computadores");
        c2.setCodigoCurso("RDC002");
        c2.setAtivo(true);
        service.salvar(c2);

        Curso c3 = new Curso();
        c3.setNome("Segurança da Informação");
        c3.setCodigoCurso("SEG003");
        c3.setAtivo(false);
        service.salvar(c3);

        // ─── 2. LISTAR TODOS ──────────────────────────────────────
        System.out.println("\n── LISTANDO TODOS OS CURSOS ─────────────────");
        List<Curso> todos = service.listarTodos();
        todos.forEach(System.out::println);

        // ─── 3. BUSCAR POR ID ─────────────────────────────────────
        System.out.println("\n── BUSCAR POR ID (1) ────────────────────────");
        service.buscarPorId(1L).ifPresentOrElse(
            c -> System.out.println("Encontrado: " + c),
            ()  -> System.out.println("Não encontrado.")
        );

        // ─── 4. BUSCAR POR CÓDIGO ─────────────────────────────────
        System.out.println("\n── BUSCAR POR CÓDIGO (RDC002) ───────────────");
        service.buscarPorCodigo("RDC002").ifPresentOrElse(
            c -> System.out.println("Encontrado: " + c),
            ()  -> System.out.println("Não encontrado.")
        );

        // ─── 5. BUSCAR POR ATIVO ──────────────────────────────────
        System.out.println("\n── CURSOS ATIVOS ────────────────────────────");
        service.buscarPorAtivo(true).forEach(System.out::println);

        System.out.println("\n── CURSOS INATIVOS ──────────────────────────");
        service.buscarPorAtivo(false).forEach(System.out::println);

        // ─── 6. BUSCAR POR NOME (BÔNUS Spring Data) ──────────────
        System.out.println("\n── BUSCAR POR NOME contendo 'ção' ───────────");
        service.buscarPorNome("ção").forEach(System.out::println);

        // ─── 7. ATUALIZAR ─────────────────────────────────────────
        System.out.println("\n── ATUALIZANDO CURSO ID 1 ───────────────────");
        service.buscarPorId(1L).ifPresent(c -> {
            c.setNome("ADS — Atualizado");
            service.salvar(c);
        });
        service.buscarPorId(1L).ifPresent(c -> System.out.println("Após update: " + c));

        // ─── 8. REMOVER ───────────────────────────────────────────
        System.out.println("\n── REMOVENDO CURSO ID 3 ─────────────────────");
        service.remover(3L);

        System.out.println("\n── LISTA FINAL ──────────────────────────────");
        service.listarTodos().forEach(System.out::println);

        System.out.println("\n✅ Demonstração concluída com sucesso!");

        ((ClassPathXmlApplicationContext) ctx).close();
    }
}