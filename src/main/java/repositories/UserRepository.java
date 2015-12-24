package repositories;

import org.springframework.data.repository.CrudRepository;

import backend.ConcreteUser;
/** 
 * Repository for all Users Stored in the EventTicker System
 * @author lars
 *
 */
public interface UserRepository extends CrudRepository<ConcreteUser, Long>{
	public Iterable<ConcreteUser> findAll();
    public Iterable<ConcreteUser> findById(Long id);
 	public Iterable<ConcreteUser> findByName(String name);
}
