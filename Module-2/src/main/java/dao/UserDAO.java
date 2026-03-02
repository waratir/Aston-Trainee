package dao;

import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Optional;

/**
 * Data Access Object (DAO) for User entity.
 * Provides CRUD operations using Hibernate.
 * All methods use try-with-resources to ensure the Session is closed automatically.
 */
@Slf4j
@RequiredArgsConstructor
public class UserDAO {
    private final SessionFactory sessionFactory;

    /**
     * Saves a new User to the database.
     *
     * @param user Entity to persist.
     */
    public void create(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("DB: Failed to persist user with email: {}", user.getEmail(), e);
            throw e;
        }
    }

    /**
     * Finds a User by unique ID.
     *
     * @param id Primary key.
     * @return User object or null if not found.
     */
    public Optional<User> read(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception e) {
            log.error("DB: Error retrieving user with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Updates an existing User's data.
     *
     * @param user Detached entity with updated fields.
     */
    public void update(User user) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("DB: Error updating user with ID: {}", user.getId(), e);
            throw e;
        }
    }

    /**
     * Deletes a User from the database.
     *
     * @param id ID of the record to delete.
     */
    public void delete(Long id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("DB: Error deleting user with ID: {}", id, e);
            throw e;
        }
    }
}
