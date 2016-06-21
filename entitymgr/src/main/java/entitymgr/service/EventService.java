package entitymgr.service;

import entitymgr.model.Event;
import entitymgr.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void insertEvent(final String name) {
        eventRepository.insertEvent(new Event(name));
    }

    public void updateEvent(final String name) {
        eventRepository.updateEvent(new Event(name));
    }

    public void deleteEventByName(final String name) {
        eventRepository.deleteEvent(name);
    }

    public void deleteExpiredEvents() {
        eventRepository.deleteExpiredEvents();
    }
}
