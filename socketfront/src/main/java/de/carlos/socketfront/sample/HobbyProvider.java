package de.carlos.socketfront.sample;
import java.util.ArrayList;
import java.util.List;

import de.carlos.socketfront.autogui.Provider;
import de.carlos.socketfront.sample.HobbyProvider.Hobby;

public class HobbyProvider implements Provider<Hobby> {
    
    static HobbyProvider instance = new HobbyProvider();
    
    private List<Hobby> data = new ArrayList<>();


    protected HobbyProvider() {
	addHobby("Volleyball");
	addHobby("Programming");
	addHobby("Playing drums");
	addHobby("Chilling");
	
    }
    
    public void addHobby(String name){
	Hobby hobby = new Hobby();
	hobby.setName(name);
	this.insert(hobby);
    }

    public static HobbyProvider getInstance() {
	return instance;
    }


    

    @Override
    public List<Hobby> getAll() {
	return data;
    }

    @Override
    public void remove(Hobby entity) {
	this.data.remove(entity);
    }


    @Override
    public void insert(Hobby entity) {
	this.data.add(entity);
    }

    @Override
    public Class<Hobby> getEntityClass() {
	return Hobby.class;
    }

    @Override
    public void save(Hobby entity) {
    }

    @Override
    public Hobby newEntity() {
	return new Hobby();
    }
    
    public static class Hobby{	
	
	String name;
	
	public String toString(){
	    return this.name;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}
	
    }


}
