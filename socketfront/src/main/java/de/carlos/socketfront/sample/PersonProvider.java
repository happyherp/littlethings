package de.carlos.socketfront.sample;

import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.autogui.Provider;
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

    @Override
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

    public static class Person extends EntityBase {

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

    }

    @Override
    public void create(Person person) {

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
}
