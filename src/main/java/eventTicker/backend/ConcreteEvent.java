package eventTicker.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "EVENT")
public class ConcreteEvent implements Serializable, Cloneable {

	private static final long serialVersionUID = 7830500968813704194L;

	private Long id;
	private String name;
	@Column(length = 800)
	private String description;
	private double width = 0;
	private double length = 0;
	private String date;
	private Date abstractDate;

	@ManyToMany(fetch = FetchType.EAGER)
	private List<ConcreteUser> participants = new ArrayList<ConcreteUser>();

	public ConcreteEvent(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public ConcreteEvent() {
	}
    
	public void setAbstractDate(Date date){
		this.abstractDate = date;
	}
	
	public Date getAbstractDate(){
		return this.abstractDate;
	}

	public void addUser(ConcreteUser user) {
		participants.add(user);
	}

	public List<ConcreteUser> getUsers() {
		return this.participants;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String birthDate) {
		this.date = birthDate;
	}

	public double getWidth() {
		return this.width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getLength() {
		return this.length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	@Override
	public ConcreteEvent clone() throws CloneNotSupportedException {
		try {
			return this;
		} catch (Exception ex) {
			throw new CloneNotSupportedException();
		}
	}

	@Override
	public String toString() {
		return "ID:" + id +", EventName=" + name + ", length=" + length + ", width=" + width + ", date=" + date + '}';
	}

}
