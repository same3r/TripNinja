package edu.columbia.tripninja.client.view;


import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.columbia.tripninja.client.presenter.EditUserPresenter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class EditUserView extends Composite implements EditUserPresenter.Display {

	private final TextBox password;
	private final FlexTable detailsTable;
	private final Button saveButton;
	private final Button cancelButton;
	private final Button logoutButton;

	public EditUserView() {

		VerticalPanel logoutPanel = new VerticalPanel();
		FormPanel contentDetailsDecorator = new FormPanel();
		contentDetailsDecorator.setSize("421px", "182px");
		initWidget(contentDetailsDecorator);

		VerticalPanel contentDetailsPanel = new VerticalPanel();
		contentDetailsPanel.setWidth("75%");

		// Create the contacts list
		//
		contentDetailsDecorator.setStyleName("form-body");
		detailsTable = new FlexTable();
		detailsTable.setCellSpacing(0);
		detailsTable.setWidth("100%");
		detailsTable.addStyleName("contacts-ListContainer");
		detailsTable.getColumnFormatter().addStyleName(1, "add-contact-input");
		password = new PasswordTextBox();
		initDetailsTable();
		password.setStyleName("textbox");
		logoutButton = new Button("Logout");
		logoutButton.setStyleName("btn btn-link");
		contentDetailsPanel.add(detailsTable);
		logoutPanel.add(logoutButton);
		HorizontalPanel menuPanel = new HorizontalPanel();
		saveButton = new Button("Save");
		cancelButton = new Button("Cancel");
		saveButton.setStyleName("btn");
		cancelButton.setStyleName("btn btn-danger");
		menuPanel.add(logoutPanel);
		menuPanel.add(saveButton);
		menuPanel.setCellHorizontalAlignment(saveButton, HasHorizontalAlignment.ALIGN_RIGHT);
		menuPanel.setCellWidth(saveButton, "115px");
		saveButton.setWidth("80px");
		menuPanel.add(cancelButton);
		menuPanel.setCellHorizontalAlignment(cancelButton, HasHorizontalAlignment.ALIGN_CENTER);
		menuPanel.setCellWidth(cancelButton, "120px");
		cancelButton.setWidth("91px");
		contentDetailsPanel.add(menuPanel);
		menuPanel.setWidth("321px");
		contentDetailsDecorator.add(contentDetailsPanel);
	}

	private void initDetailsTable() {
		detailsTable.setWidget(1, 0, new Label("Password"));
		detailsTable.setWidget(1, 1, password);
		password.setFocus(true);
	}

	public HasValue<String> getPassword() {
		return password;
	}

	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	public Widget asWidget() {
		return this;
	}

	public HasClickHandlers getLogoutButton() {
		return logoutButton;
	}
}
