package entitymgr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class DBUtils {
    private final static Logger logger = LoggerFactory.getLogger(DBUtils.class);

    public static EntityManagerFactory initEMF() {
        return Persistence.createEntityManagerFactory("entitymgr");
    }

    public static void destroyEMF(final EntityManagerFactory emf) {
        if (emf != null) {
            emf.close();
        }
    }
}
