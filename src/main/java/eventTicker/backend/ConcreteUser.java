package eventTicker.backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;

@Entity
@Table(name = "USER")
public class ConcreteUser implements Serializable, Cloneable {

	private static final long serialVersionUID = -6940014377580278136L;
    
	private AppSynchronizer sync = AppSynchronizer.getSynchronizerInstance();

	private Long id;
	private String firstName = "";
	private String name = "";
	private double width;
	private double length;
	private String birthDate;
	private String weblogin = "";
	private String password = "";

	public ConcreteUser(String firstName, String name) {
		this.name = name;
		this.firstName = firstName;
	}

	public ConcreteUser() {
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String FirstName) {
		this.firstName = FirstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String lastName) {
		this.name = lastName;
	}

	public String getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
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
	
	public void setId(Long id){
		this.id = id;
	}
	public Long getId(){
		return this.id;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public String getWeblogin() {
		return this.weblogin;
	}

	public void setWeblogin(String weblogin) {
		this.weblogin = weblogin;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public ConcreteUser clone() throws CloneNotSupportedException {
		try {
			return (ConcreteUser) BeanUtils.cloneBean(this);
		} catch (Exception ex) {
			throw new CloneNotSupportedException();
		}
	}

	@Override
	public String toString() {
		return "ID:"+ id + "Contact{" +  ", firstName=" + firstName + ", lastName=" + name + ", weblogin=" + weblogin
				+ ", length=" + length + ", width=" + width + ", birthDate=" + birthDate + "password:"+ password+'}';
	}

}
