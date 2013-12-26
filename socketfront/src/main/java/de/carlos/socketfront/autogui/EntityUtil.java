package de.carlos.socketfront.autogui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityUtil {

    /**
     * Maps Names of fields to their getters and setters.
     * 
     * @param clazz
     * @return
     */
    public static Map<String, Accessor> findAccessors(Class clazz) {

	Map<String, Accessor> accessors = new HashMap<>();

	for (Method method : clazz.getMethods()) {
	    if (method.getParameterTypes().length == 0
		    && (method.getName().startsWith("get") || method.getName()
			    .startsWith("is"))) {
		String attrname;
		if (method.getName().startsWith("is")) {
		    attrname = method.getName().substring(2);
		} else {
		    attrname = method.getName().substring(3);
		}
		if (!accessors.containsKey(attrname)){
		    accessors.put(attrname, new Accessor());
		}
		accessors.get(attrname).getter = method;
		accessors.get(attrname).type = method.getReturnType();
	    }
	    if (method.getParameterTypes().length == 1 && method.getName().startsWith("set")){
		String attrname = method.getName().substring(3);
		if (!accessors.containsKey(attrname)){
		    accessors.put(attrname, new Accessor());
		}
		accessors.get(attrname).setter = method;
		accessors.get(attrname).type = method.getParameterTypes()[0];
	    }


	}

	return accessors;
    }

    public static List<EntityField> findFields(Class clazz) {
	
	Map<String, Accessor> accessors = findAccessors(clazz);
	
	List<EntityField> fields = new ArrayList<EntityUtil.EntityField>();
	for (String name : accessors.keySet()){
	    Accessor accessor = accessors.get(name);
	    EntityField field = new EntityField();
	    field.name = name;
	    field.getter = accessor.getter;
	    field.setter = accessor.setter;
	    field.type = accessor.type;
	    fields.add(field);
	}
	

	final List<Method> methods = new ArrayList<Method>(Arrays.asList(clazz.getMethods()));
	
	//Sort by position of getter in class.
	Collections.sort(fields, new Comparator<EntityField>() {
	    @Override
	    public int compare(EntityField o1, EntityField o2) {
		return methods.indexOf(o1.getter) - methods.indexOf(o2.getter) ;
	    }
	});
	
	
	return fields;
    }

    public static class Accessor {
	public Class type;
	public Method getter;
	public Method setter;
    }

    public static class EntityField {
	public Class type;
	public String name;
	public Method getter;
	public Method setter;
    }
}
