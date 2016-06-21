package annotated.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import annotated.model.Event;
import annotated.repository.EventRepository;

import java.util.List;

/**
 * @author Abhishek Gupta
 *         https://github.com/abhigupta912
 */
public class EventService {
    private final static Logger logger = LoggerFactory.getLogger(EventService.class);
    private EventRepository eventRepository;

    public boolean init() {
        eventRepository = new EventRepository();
        return eventRepository.init();
    }

    public void cleanup() {
        eventRepository.cleanup();
    }

    public List<Event> getEvents() {
        return eventRepository.getAllEvents();
    }

    public Event getEventByName(final String name) {
        return eventRepository.getEventByName(name);
    }

    public void createUpdateEvent(final String name) {
        eventRepository.insertUpdateEvent(new Event(name));
    }

    public void deleteEventByName(final String name) {
        eventRepository.deleteEvent(new Event(name));
    }

    public void deleteExpiredEvents() {
        eventRepository.deleteExpiredEvents();
    }
}
