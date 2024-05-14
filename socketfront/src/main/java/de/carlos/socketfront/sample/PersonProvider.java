package de.carlos.socketfront.sample;

import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.autogui.Provider;
import de.carlos.socketfront.sample.HobbyProvider.Hobby;
import de.carlos.socketfront.sample.PersonProvider.Person;

public class PersonProvider implements Provider<Person> {

    private static PersonProvider instance = new PersonProvider();

    private List<Person> persons = new ArrayList<>();

    private Integer nextId = 1;

    private PersonProvider() {
	this.addPerson("Hans", "Hart");
	this.addPerson("Carlos", "Freund");
	this.addPerson("Enric", "Freund");
    }

    public Person findById(int id) {
	for (Person p : this.persons) {
	    if (p.getId().equals(id)) {
		return p;
	    }
	}
	return null;
    }

    public List<Person> findByLastName(String lastName) {
	List<Person> found = new ArrayList<>();

	for (Person p : this.persons) {
	    if (p.getId().equals(lastName)) {
		found.add(p);
	    }
	}
	return found;
    }

    @Override
    public void remove(Person p) {
	this.persons.remove(p);
    }

    public void addPerson(String firstName, String lastName) {
	Person p = new Person();
	p.setId(this.nextId);
	this.nextId++;
	p.setFirstName(firstName);
	p.setLastName(lastName);
	this.persons.add(p);
    }

    public static PersonProvider getInstance() {
	return instance;
    }

    @Override
    public List<Person> getAll() {
	return new ArrayList<Person>(this.persons);
    }

    @Override
    public void insert(Person person) {

	if (person.getId() == null) {
	    person.setId(this.nextId);
	    this.nextId++;
	}

	this.persons.add(person);

    }

    @Override
    public Class<Person> getEntityClass() {
	return Person.class;
    }

    @Override
    public void save(Person entity) {
	for (Person p : new ArrayList<Person>(this.persons)) {
	    if (p == entity) {
		this.persons.remove(p);
		this.persons.add(entity);
		return;
	    }
	}
	throw new RuntimeException("Person could not be found.");
    }

    @Override
    public Person newEntity() {
	return new Person();
    }

    public static class Person {

	private String lastName;

	private String firstName;

	private Integer age;

	private MaritalStatus maritalstatus;

	private Integer id;
	
	private Hobby favouriteHobby;

	public Integer getId() {
	    return id;
	}

	public void setId(Integer id) {
	    this.id = id;
	}

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

	public Integer getAge() {
	    return age;
	}

	public void setAge(Integer age) {
	    this.age = age;
	}

	public MaritalStatus getMaritalstatus() {
	    return maritalstatus;
	}

	public void setMaritalstatus(MaritalStatus maritalstatus) {
	    this.maritalstatus = maritalstatus;
	}

	public Hobby getFavouriteHobby() {
	    return favouriteHobby;
	}

	public void setFavouriteHobby(Hobby favouriteHobby) {
	    this.favouriteHobby = favouriteHobby;
	}

    }

    public static enum MaritalStatus {

	SINGLE, MARRIED, WIDOWED;

    }

}
