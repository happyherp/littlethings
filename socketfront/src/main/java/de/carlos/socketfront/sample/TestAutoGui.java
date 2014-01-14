package de.carlos.socketfront.sample;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.SocketGUI;
import de.carlos.socketfront.autogui.AutoGuiConfig;
import de.carlos.socketfront.autogui.CallGui;

public class TestAutoGui implements SocketGUI {

    @Override
    public void onCreate(GuiContext context) {

	AutoGuiConfig autoguiconfig = new AutoGuiConfig();

	CallGui callgui = new CallGui(TestAutoGui.class,
		"myMethod");
	context.getMainPane().add(callgui.createJSWidget(context));
	
	CallGui callgui2 = new CallGui(TestAutoGui.class,
		"parseInt");
	context.getMainPane().add(callgui2.createJSWidget(context));

	CallGui callgui3 = new CallGui( new TestClass(), "greet");
	context.getMainPane().add(callgui3.createJSWidget(context));
	
	CallGui callgui4 = new CallGui(Integer.class, "parseInt");
	context.getMainPane().add(callgui4.createJSWidget(context));
	
	CallGui callgui5 = new CallGui(new TestClass(), "describeRGB");
	context.getMainPane().add(callgui5.createJSWidget(context));

    }

    public static String myMethod(Boolean doSomething, String content,
	    Integer repetitions) {

	String result = "";
	if (doSomething) {
	    for (int i = 0; i < repetitions; i++) {
		result += content;
	    }
	}
	return result;

    }

    public static Integer parseInt(String input) {
	return Integer.parseInt(input);
    }
    

    public class TestClass {

	public String greet(String name, Color color) {
	    return "Hello " + name + " your color is " + color.toString();
	}
	
	public String describeRGB(RGB rgb){
	    return String.format("R:%d G:%d B:%d", rgb.r, rgb.g, rgb.b);
	}

    }

    public enum Color {
	RED, BLUE, GREY, YELLOW, GREEN;
    }
    
    public static class RGB{
	
	public int r,g,b;
	
	public RGB(int r,int g,int b){
	    this.r = r;
	    this.g = g;
	    this.b = b;	    
	}
	
    }

}
