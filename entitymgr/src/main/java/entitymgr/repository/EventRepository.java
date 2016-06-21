package entitymgr.repository;

import entitymgr.model.Event;
import entitymgr.util.DBUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class EventRepository {
    private final static Logger logger = LoggerFactory.getLogger(EventRepository.class);
    private EntityManagerFactory entityManagerFactory;

    public boolean init() {
        logger.info("Initializing DB");
        entityManagerFactory = DBUtils.initEMF();
        if (entityManagerFactory == null) {
            logger.error("Unable to initialize DB");
            return false;
        }
        return true;
    }

    public void cleanup() {
        logger.info("Releasing DB Connections");
        DBUtils.destroyEMF(entityManagerFactory);
    }

    public List<Event> getAllEvents() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        List<Event> result = new ArrayList<>();
        try {
            result.addAll(entityManager.createQuery("from Event", Event.class).getResultList());
        } catch (Exception e) {
            logger.error("Unable to retrieve events", e);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
            return result;
        }
    }

    public Event getEventByName(final String name) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        final Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));
        Event result = null;
        try {
            result = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            logger.error("Event with name: {} not found", name);
        } catch (Exception e) {
            logger.error("Unable to retrieve event with name: {}", name, e);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
            return result;
        }
    }

    public void insertEvent(final Event event) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(event);
        } catch (EntityExistsException e) {
            logger.error("Event with name: {} already exists", event.getName());
        } catch (Exception e) {
            logger.error("Unable to insert event with name: {}", event.getName(), e);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    public void updateEvent(final Event event) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaUpdate<Event> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Event.class);
        final Root<Event> root = criteriaUpdate.from(Event.class);
        criteriaUpdate.set("triggeredAt", event.getTriggeredAt());
        criteriaUpdate.set("expireAt", event.getExpireAt());
        criteriaUpdate.where(criteriaBuilder.equal(root.get("name"), event.getName()));
        try {
            entityManager.createQuery(criteriaUpdate).executeUpdate();
        } catch (Exception e) {
            logger.error("Unable to update event with name: {}", event.getName(), e);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    public void deleteEvent(final String name) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<Event> criteriaDelete = criteriaBuilder.createCriteriaDelete(Event.class);
        final Root<Event> root = criteriaDelete.from(Event.class);
        criteriaDelete.where(criteriaBuilder.equal(root.get("name"), name));
        try {
            entityManager.createQuery(criteriaDelete).executeUpdate();
        } catch (Exception e) {
            logger.error("Unable to delete event with name: {}", name, e);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    public void deleteExpiredEvents() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaDelete<Event> criteriaDelete = criteriaBuilder.createCriteriaDelete(Event.class);
        final Root<Event> root = criteriaDelete.from(Event.class);
        criteriaDelete.where(criteriaBuilder.lessThan(root.get("expireAt"), new Date()));
        try {
            entityManager.createQuery(criteriaDelete).executeUpdate();
        } catch (Exception e) {
            logger.error("Unable to delete expired events", e);
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }
}
