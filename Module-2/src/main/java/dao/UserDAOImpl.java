package dao;

import entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Data Access Object (DAO) for User entity.
 * Provides CRUD operations using Hibernate.
 * All methods use try-with-resources to ensure the Session is closed automatically.
 */
@Slf4j
@RequiredArgsConstructor
public class UserDAOImpl implements UserDAO{
    private final SessionFactory sessionFactory;

    /**
     * Saves a new User to the database.
     *
     * @param user Entity to persist.
     */
    @Override
    public void create(User user) {
        executeInTransaction(session -> session.persist(user));
    }

    /**
     * Finds a User by unique ID.
     *
     * @param id Primary key.
     * @return User object or null if not found.
     */
    @Override
    public Optional<User> read(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    /**
     * Updates an existing User's data.
     *
     * @param user Detached entity with updated fields.
     */
    @Override
    public void update(User user) {
        executeInTransaction(session -> session.merge(user));
    }

    /**
     * Deletes a User from the database.
     *
     * @param id ID of the record to delete.
     */
    @Override
    public void delete(Long id) {
        executeInTransaction(session -> {
            User user = session.get(User.class, id);
            if (user != null) session.remove(user);
        });
    }

    private void executeInTransaction(Consumer<Session> action) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            log.error("DB Error: {}", e.getMessage());
            throw e;
        }
    }
}
