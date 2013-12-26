package de.carlos.socketfront.sample;

import java.util.ArrayList;
import java.util.List;

public class PersonProvider {
    
    private static PersonProvider instance = new PersonProvider();
            
    private List<Person> persons = new ArrayList<>();

    private Integer currentId = 1;
    
    private PersonProvider(){
	this.addPerson("Hans", "Hart");
	this.addPerson("Carlos", "Freund");
	this.addPerson("Enric", "Freund");
    }
    
    public Person findById(int id){
	for (Person p: this.persons){
	    if(p.getId().equals(id)){
		return p;
	    }
	}
	return null;
    }
    
    public List<Person> findByLastName(String lastName){
	List<Person> found = new ArrayList<>();
	
	for (Person p: this.persons){
	    if(p.getId().equals(lastName)){
		found.add(p);
	    }
	}
	return found;
    }
    
    public void remove(Person p){
	this.persons.remove(p);
    }
    
    
    private void addPerson(String firstName, String lastName) {
	Person p = new Person();
	p.setId(this.currentId );
	this.currentId++;
	p.setFirstName(firstName);
	p.setLastName(lastName);
	this.persons.add(p);
    }


    public static PersonProvider getInstance(){
	return instance;
    }
    
    public List<Person> getAll(){
	return new ArrayList<Person>(this.persons);
    }
    
    
    public static class Person{
	
	private Integer id; 
	
	private String lastName;
	
	private String firstName;

	public String getLastName() {
	    return lastName;
	}

	public void setLastName(String lastName) {
	    this.lastName = lastName;
	}

	public String getFirstName() {
	    return firstName;
	}

	public void setFirstName(String firstName) {
	    this.firstName = firstName;
	}

	public Integer getId() {
	    return id;
	}

	public void setId(Integer id) {
	    this.id = id;
	}
	
	
    }
}
