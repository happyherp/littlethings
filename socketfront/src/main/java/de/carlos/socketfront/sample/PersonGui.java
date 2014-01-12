package de.carlos.socketfront.sample;

import java.util.List;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.SocketGUI;
import de.carlos.socketfront.autogui.CRUD;
import de.carlos.socketfront.autogui.EntityEdit;
import de.carlos.socketfront.autogui.EntityTableDrawInstuctions;
import de.carlos.socketfront.sample.HobbyProvider.Hobby;
import de.carlos.socketfront.sample.PersonProvider.Person;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.table.Grid;
import de.carlos.socketfront.widgets.table.RowSelectTable;
import de.carlos.socketfront.widgets.table.RowTable;

public class PersonGui implements SocketGUI {

    @Override
    public void onCreate(GuiContext context) {
	
	context.getMainPane().add(new Text("CRUD").createJSWidget(context));
	
	CRUD<Person> personCRUD = new CRUD<Person>(PersonProvider.getInstance());
	context.getMainPane().add(personCRUD.createJSWidget(context));
	
	CRUD<Hobby> hobbyCRUD = new CRUD<>(HobbyProvider.getInstance());
	context.getMainPane().add(hobbyCRUD.createJSWidget(context));
	
	context.getMainPane().add(new Text("Other stuff").createJSWidget(context));


	List<Person> allpersons = PersonProvider.getInstance().getAll();

	
	Grid table = new Grid(3, allpersons.size() + 1).createJSWidget(context);
	context.getMainPane().add(table);

	table.setCell(new Text("ID").createJSWidget(context), 0, 0);
	table.setCell(new Text("Firstname").createJSWidget(context), 1, 0);
	table.setCell(new Text("Last name").createJSWidget(context), 2, 0);

	int row = 1;
	for (Person person : allpersons) {
	    table.setCell(new Text("" + person.getId()).createJSWidget(context), 0,
		    row);
	    table.setCell(new Text(person.getFirstName()).createJSWidget(context),
		    1, row);
	    table.setCell(new Text(person.getLastName()).createJSWidget(context), 2,
		    row);
	    row++;
	}
	
	context.getMainPane().add(new Text("Automated").createJSWidget(context));
	

	final RowTable<Person> persontable = new RowTable<>(new EntityTableDrawInstuctions<>(Person.class));
	persontable.setData(PersonProvider.getInstance().getAll());
	context.getMainPane().add(persontable.createJSWidget(context));

	
	EntityEdit<Person> personedit = new EntityEdit<PersonProvider.Person>(
		allpersons.get(0));
	personedit.createJSWidget(context);
	context.getMainPane().add(personedit);

	personedit.getOnChange().addObserver(new Observer<ChangeEvent<EntityEdit<Person>>>() {	    
	    @Override
	    public void update(ChangeEvent<EntityEdit<Person>> event) {
		persontable.redrawData();
	    }
	});
	
	
	final RowSelectTable<Person> selecttable = new RowSelectTable<PersonProvider.Person>();
	selecttable.setDrawInstructions(new EntityTableDrawInstuctions<PersonProvider.Person>(Person.class));
	selecttable.setData(allpersons);
	selecttable.setValue(allpersons.get(2));	
	context.getMainPane().add(selecttable.createJSWidget(context));
	
	selecttable.getOnChange().addObserver(new Observer<ChangeEvent<RowSelectTable<Person>>>() {

	    @Override
	    public void update(ChangeEvent<RowSelectTable<Person>> event) {
		event.getContext().alert("You selected "+event.getSource().getValue().getFirstName());		
	    }
	});
	

    }

}
