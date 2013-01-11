package edu.columbia.tripninja.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.columbia.tripninja.client.presenter.UsersPresenter;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class UsersView extends Composite implements UsersPresenter.Display {
	private final Button blockButton;
	private FlexTable usersTable;
	private final FlexTable contentTable;
	private final Button logoutButton;

	public UsersView() {
		FormPanel contentTableDecorator = new FormPanel();
		VerticalPanel logoutPanel = new VerticalPanel();
		initWidget(contentTableDecorator);
		contentTableDecorator.setWidth("100%");
		contentTableDecorator.setWidth("18em");
		contentTableDecorator.setStyleName("form-body");
		contentTable = new FlexTable();
		logoutButton = new Button("Logout");
		logoutPanel.add(logoutButton);
		logoutButton.setStyleName("btn btn-link");
		contentTable.setWidth("100%");
		contentTable.getCellFormatter().addStyleName(0, 0,
				"contacts-ListContainer");
		contentTable.getCellFormatter().setWidth(0, 0, "100%");
		contentTable.getFlexCellFormatter().setVerticalAlignment(0, 0,
				DockPanel.ALIGN_TOP);

		// Create the menu
		//
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setBorderWidth(0);
		hPanel.setSpacing(0);
		hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		blockButton = new Button("Block");
		blockButton.addStyleName("btn");
		hPanel.add(blockButton);
		hPanel.setCellHorizontalAlignment(blockButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hPanel.setCellWidth(blockButton, "250");
		blockButton.setWidth("75");
		contentTable.getCellFormatter().addStyleName(0, 0, "contacts-ListMenu");
		contentTable.setWidget(2, 0, hPanel);
		contentTable.setWidget(0,1, logoutPanel);
		// Create the contacts list
		//
		usersTable = new FlexTable();
		usersTable.setCellSpacing(0);
		usersTable.setCellPadding(0);
		usersTable.setWidth("100%");
		usersTable.addStyleName("contacts-ListContents");
		usersTable.getColumnFormatter().setWidth(0, "15px");
		contentTable.setWidget(1, 0, usersTable);

		contentTableDecorator.add(contentTable);
	}

	public HasClickHandlers getBlockButton() {
		return blockButton;
	}

	public HasClickHandlers getList() {
		return usersTable;
	}

	public void setData(List<String> data, List<Boolean> blockStatusList) {
		usersTable.removeAllRows();

		for (int i = 0; i < data.size(); ++i) {
			CheckBox ckBox = new CheckBox();
			ckBox.setValue(blockStatusList.get(i));
			usersTable.setWidget(i, 0, ckBox);
			usersTable.setText(i, 1, data.get(i));
		}
	}

	public int getClickedRow(ClickEvent event) {
		int selectedRow = -1;
		HTMLTable.Cell cell = usersTable.getCellForEvent(event);

		if (cell != null) {
			// Suppress clicks if the user is actually selecting the
			// check box
			//
			if (cell.getCellIndex() > 0) {
				selectedRow = cell.getRowIndex();	
			}
		}

		return selectedRow;
	}

	public List<Integer> getSelectedRows() {
		List<Integer> selectedRows = new ArrayList<Integer>();

		for (int i = 0; i < usersTable.getRowCount(); ++i) {
			CheckBox checkBox = (CheckBox) usersTable.getWidget(i, 0);
			if (checkBox.getValue()) {
				selectedRows.add(i);
			}
		}

		return selectedRows;
	}

	public Widget asWidget() {
		return this;
	}

	public HasClickHandlers getLogoutButton() {
		return logoutButton;
	}
}
