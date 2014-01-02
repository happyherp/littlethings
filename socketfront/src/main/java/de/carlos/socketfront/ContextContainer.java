package de.carlos.socketfront;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ContextContainer {
    
    static ContextContainer instance = new ContextContainer();
    
    Map<Integer, GuiContext> idToContext = new HashMap<>();
    
    static Random random = new Random();
    
    public static ContextContainer getInstance(){
	return instance;
    }
    
    protected ContextContainer(){
	
    }
    
    /**
     * Register the new Context and return its it.
     * 
     * @param context
     * @return
     */
    public Integer addContext(GuiContext context){
	
	Integer id = random.nextInt();
	while(idToContext.containsKey(id)){
	    id = random.nextInt();
	}
	
	idToContext.put(id, context);
	return id;
	
    }
    
    public GuiContext getContext(Integer id){
	return this.idToContext.get(id);
    }

}
