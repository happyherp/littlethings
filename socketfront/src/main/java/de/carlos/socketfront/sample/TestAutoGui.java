package de.carlos.socketfront.sample;

import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.SocketGUI;
import de.carlos.socketfront.autogui.AutoGuiConfig;
import de.carlos.socketfront.autogui.CallGui;

public class TestAutoGui implements SocketGUI {

    @Override
    public void onCreate(GuiContext context) {

	AutoGuiConfig autoguiconfig = new AutoGuiConfig();

	CallGui callgui = new CallGui();
	callgui.createStatic(context, autoguiconfig, TestAutoGui.class,
		"myMethod");
	context.getMainPane().add(callgui.getGroup());

	CallGui callgui2 = new CallGui();
	callgui2.createStatic(context, autoguiconfig, TestAutoGui.class,
		"parseInt");
	context.getMainPane().add(callgui2.getGroup());

	CallGui callgui3 = new CallGui();
	callgui3.createMember(context, autoguiconfig, new TestClass(), "greet");
	context.getMainPane().add(callgui3.getGroup());

	CallGui callgui4 = new CallGui();
	callgui4.createStatic(context, autoguiconfig, Integer.class, "parseInt");
	context.getMainPane().add(callgui4.getGroup());

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

    }

    public enum Color {
	RED, BLUE, GREY, YELLOW, GREEN;
    }

}
