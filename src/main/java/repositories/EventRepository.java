package repositories;

import org.springframework.data.repository.CrudRepository;

import backend.ConcreteEvent;
/**
 * Repository for all events stored in the event Management system
 * Provides simple query methods for all kind of backend data request concerning ConcreteEvent.class
 * @author lars
 *
 */
public interface EventRepository extends CrudRepository<ConcreteEvent, Long>{
	public Iterable<ConcreteEvent> findAll();
	public Iterable<ConcreteEvent> findById(Long id);
	public Iterable<ConcreteEvent> findByName(String name);
	public Iterable<ConcreteEvent> findByLengthAndWidth(double length, double width);
	public Iterable<ConcreteEvent> findByDate(String date);
}
