package annotated.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import annotated.model.Event;
import annotated.service.EventService;

import java.lang.reflect.Type;
import java.util.List;

import static spark.Spark.*;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class EventController {
    private final static Logger logger = LoggerFactory.getLogger(EventController.class);
    private final Gson gson = new GsonBuilder().serializeNulls().create();

    private EventService eventService;

    public EventController(final EventService eventService) {
        this.eventService = eventService;
        registerRoutes();
    }

    private void registerRoutes() {
        get("/event", (request, response) -> {
            response.type("application/json");
            logger.info("Retrieving Events");
            final List<Event> events = eventService.getEvents();
            logger.info("Retrieved {} Events", events.size());
            final Type type = new TypeToken<List<Event>>() {}.getType();
            return gson.toJson(events, type);
        });

        get("/event/:name", (request, response) -> {
            response.type("application/json");
            final String name = request.params("name");
            logger.info("Retrieving Event with name: {}", name);
            final Event event = eventService.getEventByName(name);
            if (event == null) {
                logger.error("No Event found with name: {}", name);
                halt(HttpStatus.NOT_FOUND_404);
                return null;
            } else {
                logger.info("Found Event with name: {}", name);
                return gson.toJson(event, Event.class);
            }
        });

        post("/event/:name", (request, response) -> {
            final String name = request.params("name");
            logger.info("Inserting Event with name: {}", name);
            eventService.createUpdateEvent(name);
            halt(HttpStatus.ACCEPTED_202);
            return null;
        });

        delete("/event/:name", (request, response) -> {
            final String name = request.params("name");
            logger.info("Deleting Event with name: {}", name);
            eventService.deleteEventByName(name);
            halt(HttpStatus.ACCEPTED_202);
            return null;
        });

        delete("/event", (request, response) -> {
            logger.info("Deleting expired events");
            eventService.deleteExpiredEvents();
            halt(HttpStatus.ACCEPTED_202);
            return null;
        });
    }
}
