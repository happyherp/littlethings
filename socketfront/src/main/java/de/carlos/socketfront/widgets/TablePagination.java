package de.carlos.socketfront.widgets;

import de.carlos.observer.Observable;
import de.carlos.observer.Observer;
import de.carlos.socketfront.GuiContext;
import de.carlos.socketfront.widgets.events.ChangeEvent;

public class TablePagination implements
	InputSourceWidget<TablePagination.Range> {

    protected Pagination pagination;

    protected int pagesize = 5;

    protected int totalrows = 0;

    private Observable<ChangeEvent<TablePagination>> onchange = new Observable<ChangeEvent<TablePagination>>();

    public Range getCurrentRange() {
	Range r = new Range();
	r.setFromIndex((this.pagination.getCurrentPage() - 1) * this.pagesize);
	r.setToIndex(Math.min(this.totalrows, this.pagination.getCurrentPage()
		* this.pagesize));
	return r;
    }

    protected void updatePagination() {
	if (this.pagination != null) {
	    this.pagination
		    .setMaxPage((int) Math.ceil((double)this.getTotalrows() / this.getPagesize()));
	}
    }

    @Override
    public JSWidget createJSWidget(GuiContext context) {
	this.pagination = new Pagination();
	this.pagination.getOnChange().addObserver(
		new Observer<ChangeEvent<Pagination>>() {
		    @Override
		    public void update(ChangeEvent<Pagination> event) {
			TablePagination.this.onchange
				.fire(new ChangeEvent<TablePagination>(
					TablePagination.this, event
						.getContext()));
		    }
		});
	this.pagination.createJSWidget(context);
	this.updatePagination();

	return this.pagination.getMainJSWidget();
    }

    public int getPagesize() {
	return pagesize;
    }

    public void setPagesize(int pagesize) {
	this.pagesize = pagesize;
	this.updatePagination();
    }

    public int getTotalrows() {
	return totalrows;
    }

    public void setTotalrows(int totalrows) {
	this.totalrows = totalrows;
	this.updatePagination();
    }

    public static class Range {
	int fromIndex;
	int toIndex;

	public int getFromIndex() {
	    return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
	    this.fromIndex = fromIndex;
	}

	public int getToIndex() {
	    return toIndex;
	}

	public void setToIndex(int toIndex) {
	    this.toIndex = toIndex;
	}

    }

    @Override
    public Range getValue() {
	return this.getCurrentRange();
    }

    @Override
    public void setValue(Range value) {
	throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean hasValidInput() {
	return true;
    }

    @Override
    public Observable<ChangeEvent<TablePagination>> getOnChange() {
	return this.onchange;
    }

    @Override
    public JSWidget getMainJSWidget() {
	return pagination.getMainJSWidget();
    }

    @Override   
    public void addInfo(Widget info) {
	pagination.addInfo(info);
    }

    @Override    
    public void remove() {
	pagination.remove();
    }


}
