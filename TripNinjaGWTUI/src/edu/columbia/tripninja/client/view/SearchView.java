package edu.columbia.tripninja.client.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.columbia.tripninja.client.presenter.SearchPresenter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class SearchView extends Composite implements SearchPresenter.Display {

	private final Button searchButton;
	private final Button voteButton;
	private final TextBox searchBar;
	private FlexTable resultsTable;
	private final FlexTable searchTable;
	private final Button logoutButton;
	private final Button ninjasPickButton;

	public SearchView() {
		VerticalPanel logoutPanel = new VerticalPanel();
		FormPanel searchTableDecorator = new FormPanel();
		initWidget(searchTableDecorator);
		searchTableDecorator.setSize("730px", "217px");
		searchTableDecorator.setStyleName("form-search search-body");
		searchTable = new FlexTable();
		searchTable.setWidth("100%");
		searchTable.getCellFormatter().addStyleName(0, 0,
				"contacts-ListContainer");
		searchTable.getCellFormatter().setWidth(0, 0, "100%");
		searchTable.getFlexCellFormatter().setVerticalAlignment(0, 0,
				DockPanel.ALIGN_TOP);

		searchBar = new TextBox();
		logoutButton = new Button("Logout");
		logoutButton.setStyleName("btn btn-link");
		logoutPanel.add(logoutButton);
		searchBar.setStyleName("span4 input-medium search-query");
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setBorderWidth(0);
		hPanel.setSpacing(0);
		hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		hPanel.add(searchBar);
		searchBar.setHeight("30px");
		searchTable.getCellFormatter().addStyleName(0, 0, "contacts-ListMenu");
		searchTable.setWidget(0, 1, logoutPanel);
		searchTable.setWidget(1, 0, hPanel);
		searchButton = new Button("Search");
		searchButton.setStyleName("btn");
		hPanel.add(searchButton);
		hPanel.setCellHorizontalAlignment(searchButton,
				HasHorizontalAlignment.ALIGN_CENTER);
		hPanel.setCellWidth(searchButton, "100px");
		ninjasPickButton = new Button("Ninja's Pick!");
		hPanel.add(ninjasPickButton);
		ninjasPickButton.setStyleName("btn btn-primary");
		voteButton = new Button("Vote");
		hPanel.add(voteButton);
		hPanel.setCellHorizontalAlignment(voteButton, HasHorizontalAlignment.ALIGN_RIGHT);
		voteButton.setWidth("50");
		hPanel.setCellWidth(voteButton, "65");
		voteButton.setVisible(false);
		voteButton.setStyleName("btn btn-success");
		resultsTable = new FlexTable();
		resultsTable.setStyleName("table-striped");
		resultsTable.setCellSpacing(0);
		resultsTable.setCellPadding(0);
		resultsTable.setWidth("100%");
		resultsTable.addStyleName("contacts-ListContents");
		resultsTable.getColumnFormatter().setWidth(0, "15px");
		searchTable.setWidget(2, 0, resultsTable);
		searchTableDecorator.add(searchTable);

	}

	@Override
	public HasClickHandlers getSearchButton() {

		return searchButton;
	}

	@Override
	public void setData(List<String> data, HashSet<String> likeSet) {
		resultsTable.removeAllRows();

		for (int i = 0; i < data.size(); ++i) {
			CheckBox ckBx = new CheckBox();
			if (likeSet.contains(data.get(i).split(" ")[0]))
				ckBx.setValue(true);
			else
				ckBx.setValue(false);

			resultsTable.setWidget(i, 0, ckBx);
			resultsTable.setText(i, 1, data.get(i));
		}
	}

	public List<Integer> getSelectedRows() {
		List<Integer> selectedRows = new ArrayList<Integer>();

		for (int i = 0; i < resultsTable.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) resultsTable.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRows.add(i);
			}
		}

		return selectedRows;
	}

	public int getClickedResult(ClickEvent event) {
		int selectedRow = -1;
		HTMLTable.Cell cell = resultsTable.getCellForEvent(event);

		if (cell != null) {
			// Suppress clicks if the user is actually selecting the
			// check box
			//
			if (cell.getCellIndex() > 0) {
				selectedRow = cell.getRowIndex();
			}
		}
		System.out.println("Row Selected" + selectedRow);

		return selectedRow;
	}

	@Override
	public String getSearchText() {

		return searchBar.getText();
	}

	@Override
	public HasClickHandlers getList() {
		return resultsTable;
	}

	public HasClickHandlers getVoteButton() {
		return voteButton;
	}

	public HasClickHandlers getLogoutButton() {
		return logoutButton;
	}

	@Override
	public HasClickHandlers getNinjasPick() {
		return ninjasPickButton;
	}

	@Override
	public void showVoteButton() {
		voteButton.setVisible(true);

	}

}
