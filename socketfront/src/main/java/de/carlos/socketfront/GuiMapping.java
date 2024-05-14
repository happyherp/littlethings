package de.carlos.socketfront;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import de.carlos.socketfront.sample.PersonGui;
import de.carlos.socketfront.sample.TestAutoGui;
import de.carlos.socketfront.sample.TestGUI;
import de.carlos.util.Factory;
import de.carlos.util.FactoryImpl;

public class GuiMapping {
    
    private static Logger LOGGER = Logger.getLogger(GuiMapping.class);

    static GuiMapping instance = new GuiMapping();

    private Map<String, Factory<SocketGUI>> nameToGui = new HashMap<String, Factory<SocketGUI>>();

    protected GuiMapping() {
	nameToGui.put("Test", new FactoryImpl<SocketGUI>(TestGUI.class));
	nameToGui.put("TestAuto", new FactoryImpl<SocketGUI>(TestAutoGui.class));
	nameToGui.put("Person", new FactoryImpl<SocketGUI>(PersonGui.class));
    }

    public static GuiMapping getInstance() {
	return instance;
    }

    
    public SocketGUI createGUI(String guiname){
	
	Factory<SocketGUI> factory = this.nameToGui.get(guiname);
	 if (factory == null){
	     throw new RuntimeException("GUI with name: "+ guiname + " not found.");
	 }
	 return factory.create();
    }

}
