package annotated.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import annotated.model.Event;
import annotated.util.DBUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class EventRepository {
    private final static Logger logger = LoggerFactory.getLogger(EventRepository.class);
    private SessionFactory sessionFactory;

    public boolean init() {
        logger.info("Initializing DB");
        sessionFactory = DBUtils.initSessionFactory();
        if (sessionFactory == null) {
            logger.error("Unable to initialize DB");
            return false;
        }
        return true;
    }

    public void cleanup() {
        logger.info("Releasing DB Connections");
        DBUtils.destroySessionFactory(sessionFactory);
    }

    public List<Event> getAllEvents() {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final List result = session.createQuery("from Event").list();
        transaction.commit();
        session.close();
        return result;
    }

    public Event getEventByName(final String name) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final Event result = session.find(Event.class, name);
        transaction.commit();
        session.close();
        return result;
    }

    public void insertUpdateEvent(final Event event) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(event);
        transaction.commit();
        session.close();
    }

    public void deleteEvent(final Event event) {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        session.delete(event);
        transaction.commit();
        session.close();
    }

    public void deleteExpiredEvents() {
        final Session session = sessionFactory.openSession();
        final Transaction transaction = session.beginTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaDelete<Event> criteriaDelete = criteriaBuilder.createCriteriaDelete(Event.class);
        final Root<Event> root = criteriaDelete.from(Event.class);
        criteriaDelete.where(criteriaBuilder.lessThan(root.get("expireAt"), new Date()));
        session.createQuery(criteriaDelete).executeUpdate();
        transaction.commit();
        session.close();
    }
}
