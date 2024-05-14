package de.carlos.socketfront.widgets;

public interface Parent{
    
    public <T extends Widget> T add(T child);

}
