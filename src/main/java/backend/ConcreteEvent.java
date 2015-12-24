package backend;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.beanutils.BeanUtils;

public class ConcreteEvent implements Serializable, Cloneable {


	@Id
	@GeneratedValue
	private Long id;
    private String name = "";
    private String description = "";
    private double width = 0;
    private double length = 0;
    private String date;
    

    public ConcreteEvent(String name, String description){
    	this.name = name;
    	this.description = description;
    }
    public ConcreteEvent(){}
       
    public String getDescription(){
    	return this.description;
    }
    public void setDescription(String d){
    	this.description = d;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long nextId) {
        this.id = nextId;
    }
    public String getName() {
        return name;
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
    public double getWidth(){
    	return this.width;
    }
    public void setWidth(double width){
    	this.width = width;
    }
    public double getLength(){
    	return this.length;
    }
    public void setLength(double length){
    	this.length = length;
    }
    
   

    @Override
    public ConcreteEvent clone() throws CloneNotSupportedException {
        try {
            return (ConcreteEvent) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return ", EventName=" + name
        		+", length="+ length + ", width=" + width + ", date=" + date + '}';
    }

}
