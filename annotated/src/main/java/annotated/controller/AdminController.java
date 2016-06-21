package annotated.controller;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import annotated.service.EventService;

import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.stop;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class AdminController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
    private EventService eventService;

    public AdminController(final EventService eventService) {
        this.eventService = eventService;
        registerRoutes();
    }

    private void registerRoutes() {
        post("/admin/shutdown", (request, response) -> {
            logger.info("Shutdown initiated");
            eventService.cleanup();
            new Thread(() -> stop()).start();
            halt(HttpStatus.ACCEPTED_202);
            return null;
        });
    }
}
