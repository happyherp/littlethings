package de.carlos.socketfront.sample;

import java.util.List;

import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.SocketGUI;
import de.carlos.socketfront.autogui.EntityEdit;
import de.carlos.socketfront.autogui.EntityTableDrawInstuctions;
import de.carlos.socketfront.sample.PersonProvider.Person;
import de.carlos.socketfront.widgets.Text;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.table.Grid;
import de.carlos.socketfront.widgets.table.RowTable;

public class PersonGui implements SocketGUI {

    @Override
    public void onCreate(GuiContext context) {

	List<Person> allpersons = PersonProvider.getInstance().getAll();

	Grid table = context.addWidget(new Grid(3, allpersons.size() + 1),
		context.getMainPane());

	table.setCell(context.addWidget(new Text("ID")), 0, 0);
	table.setCell(context.addWidget(new Text("Firstname")), 1, 0);
	table.setCell(context.addWidget(new Text("Last name")), 2, 0);

	int row = 1;
	for (Person person : allpersons) {
	    table.setCell(context.addWidget(new Text("" + person.getId())), 0,
		    row);
	    table.setCell(context.addWidget(new Text(person.getFirstName())),
		    1, row);
	    table.setCell(context.addWidget(new Text(person.getLastName())), 2,
		    row);
	    row++;
	}
	
	context.addWidget(new Text("Automated"), context.getMainPane());

	final RowTable<Person> persontable = new RowTable<>(new EntityTableDrawInstuctions<>(context, Person.class));
	persontable.setData(PersonProvider.getInstance().getAll());
	context.addWidget(persontable, context.getMainPane());

	EntityEdit<Person> personedit = new EntityEdit<PersonProvider.Person>(
		allpersons.get(0));
	context.addWidget(personedit, context.getMainPane());
	personedit.getOnChange().addObserver(new Observer<ChangeEvent<EntityEdit<Person>>>() {	    
	    @Override
	    public void update(ChangeEvent<EntityEdit<Person>> event) {
		persontable.redrawData();
	    }
	});

    }

}
