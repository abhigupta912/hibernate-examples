package entitymgr;

import entitymgr.controller.AdminController;
import entitymgr.controller.EventController;
import entitymgr.service.EventService;

import static spark.Spark.port;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class Application {
    public static void main(String[] args) {
        port(9000);

        final EventService eventService = new EventService();
        if (eventService.init()) {
            new AdminController(eventService);
            new EventController(eventService);
        }
    }
}
