package xml.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class DBUtils {
    private final static Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public static SessionFactory initSessionFactory() {
        SessionFactory sessionFactory = null;
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            logger.error("Error creating session factory", e);
            StandardServiceRegistryBuilder.destroy(registry);
        }

        return sessionFactory;
    }

    public static void destroySessionFactory(final SessionFactory sessionFactory) {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
