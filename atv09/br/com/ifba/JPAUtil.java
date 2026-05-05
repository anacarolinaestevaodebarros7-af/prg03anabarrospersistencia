package br.com.ifba;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Classe utilitária para gerenciar o EntityManagerFactory da JPA.
 * Utiliza o padrão Singleton para evitar múltiplas instâncias.
 *
 * @author PRG03 - IFBA
 */
public class JPAUtil {

    // Nome da persistence-unit definida no persistence.xml
    private static final String PERSISTENCE_UNIT = "gerenciamento_curso";

    private static EntityManagerFactory factory;

    static {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    }

    /**
     * Retorna um novo EntityManager a cada chamada.
     * Lembre-se de fechar o EntityManager após o uso.
     */
    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    /**
     * Encerra o EntityManagerFactory (chamar ao fechar a aplicação).
     */
    public static void close() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}