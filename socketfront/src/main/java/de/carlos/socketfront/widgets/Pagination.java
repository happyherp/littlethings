package de.carlos.socketfront.widgets;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;
import de.carlos.socketfront.widgets.events.ClickEvent;

public class Pagination extends GroupComposition implements InputSourceWidget<Integer> {

    public static int FIRSTPAGE = 1;

    protected int currentPage = FIRSTPAGE;
    protected int maxPage = FIRSTPAGE;

    Button goFirst;
    Button goLeft;

    NumberInput currentPageInput;

    Text maxPageText;

    Button goRight;
    Button goLast;

    private Observable<ChangeEvent<Pagination>> onchange = new Observable<ChangeEvent<Pagination>>();


    @Override
    public JSWidget createJSWidget(GuiContext context) {
	super.createJSWidget(context);
	this.context = context;

	this.group = new VGroup();
	this.group.createJSWidget(context);

	goFirst = new Button("<<");
	this.group.add(goFirst.createJSWidget(context));
	goFirst.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		Pagination.this.goToPage(1);
	    }
	});

	goLeft = new Button("<");
	this.group.add(goLeft.createJSWidget(context));
	goLeft.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		Pagination.this.goToPage(Pagination.this.currentPage - 1);
	    }
	});

	Button go = new Button("go to");
	group.add(go.createJSWidget(context));
	go.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		Pagination.this.goToPage(Pagination.this.currentPageInput
			.getValue());
	    }
	});

	this.group.add(new Text("Page").createJSWidget(context));

	currentPageInput = new NumberInput();

	this.group.add(currentPageInput.createJSWidget(context));

	this.group.add(new Text("of").createJSWidget(context));

	maxPageText = new Text(maxPage + "");
	this.group.add(maxPageText.createJSWidget(context));

	goRight = new Button(">");
	this.group.add(goRight.createJSWidget(context));
	goRight.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		Pagination.this.goToPage(Pagination.this.currentPage + 1);
	    }
	});

	goLast = new Button(">>");
	this.group.add(goLast.createJSWidget(context));
	goLast.getOnClick().addObserver(new Observer<ClickEvent<Button>>() {
	    @Override
	    public void update(ClickEvent<Button> event) {
		Pagination.this.goToPage(Pagination.this.maxPage);
	    }
	});

	this.goToPage(getCurrentPage());

	return group;
    }

    protected void goToPage(int i) {

	if (i <= 0 || i > this.getMaxPage()) {
	    throw new RuntimeException("Page out of range. Page: " + i);
	}

	this.currentPage = i;
	this.updateButtons();
	this.onchange.fire(new ChangeEvent<Pagination>(this, this.context));
    }

    protected void updateButtons() {

	if (this.context != null) {

	    this.goFirst.setDisabled(this.getCurrentPage() == FIRSTPAGE);
	    this.goLeft.setDisabled(this.getCurrentPage() == FIRSTPAGE);

	    this.currentPageInput.setValue(this.currentPage);
	    this.maxPageText.setText("" + this.maxPage);

	    this.goRight
		    .setDisabled(this.getCurrentPage() == this.getMaxPage());
	    this.goLast.setDisabled(this.getCurrentPage() == this.getMaxPage());

	}
    }

    public int getCurrentPage() {
	return currentPage;
    }

    public void setCurrentPage(int currentPage) {
	if (currentPage > this.maxPage) {
	    throw new RuntimeException("currentPage > this.maxPage");
	}
	this.goToPage(currentPage);
	;
    }

    public int getMaxPage() {
	return maxPage;
    }

    public void setMaxPage(int maxPage) {
	this.maxPage = maxPage;
	if (this.currentPage > maxPage) {
	    this.goToPage(this.maxPage);
	}
	this.updateButtons();
    }

    @Override
    public Integer getValue() {
	return this.getCurrentPage();
    }

    @Override
    public void setValue(Integer value) {
	this.setCurrentPage(value);
    }

    @Override
    public boolean hasValidInput() {
	return true;
    }

    @Override
    public Observable<ChangeEvent<Pagination>> getOnChange() {
	return this.onchange;
    }

}
