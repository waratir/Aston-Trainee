import controller.InputHandler;
import controller.UserControllerApp;
import dao.UserDAO;
import dao.UserDAOImpl;
import mapper.UserMapper;
import mapper.UserMapperImpl;
import org.hibernate.SessionFactory;
import service.UserService;
import service.UserServiceImpl;
import util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        InputHandler inputHandler = new InputHandler();
        UserMapper userMapper = new UserMapperImpl();

        UserDAO userDAO = new UserDAOImpl(sessionFactory);

        UserService userService = new UserServiceImpl(userDAO, userMapper);

        UserControllerApp app = new UserControllerApp(userService, inputHandler);

        app.start();
    }
}
