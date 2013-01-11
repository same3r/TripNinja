package edu.columbia.tripninja.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import edu.columbia.tripninja.client.presenter.SignupPresenter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class SignupView extends Composite implements SignupPresenter.Display {

	private final TextBox username;
	private final TextBox password;
	private final TextBox firstname;
	private final TextBox lastname;
	private final TextBox emailId;
	private final FlexTable detailsTable;
	private final Button signupButton;
	private VerticalPanel verticalPanel;

	public SignupView() {
		FormPanel contentDetailsDecorator = new FormPanel();
		contentDetailsDecorator.setSize("518px", "352px");
		initWidget(contentDetailsDecorator);
		contentDetailsDecorator.setStyleName("form-body");
		VerticalPanel contentDetailsPanel = new VerticalPanel();
		contentDetailsPanel.setWidth("75%");

		// Create the contacts list
		//
		detailsTable = new FlexTable();
		detailsTable.setCellSpacing(0);
		detailsTable.setSize("387px", "159px");
		detailsTable.addStyleName("contacts-ListContainer");
		detailsTable.getColumnFormatter().addStyleName(1, "add-contact-input");
		username = new TextBox();
		password = new PasswordTextBox();
		firstname = new TextBox();
		lastname = new TextBox();
		emailId = new TextBox();

		initDetailsTable();
		contentDetailsPanel.add(detailsTable);
		
		verticalPanel = new VerticalPanel();
		contentDetailsPanel.add(verticalPanel);
		contentDetailsPanel.setCellHeight(verticalPanel, "25px");
		verticalPanel.setWidth("389px");

		HorizontalPanel menuPanel = new HorizontalPanel();
		signupButton = new Button("Signup");
		menuPanel.add(signupButton);
		signupButton.setWidth("150px");
		signupButton.setStyleName("btn btn-primary");
		menuPanel.setCellHorizontalAlignment(signupButton, HasHorizontalAlignment.ALIGN_CENTER);
		menuPanel.setCellWidth(signupButton, "388px");
		contentDetailsPanel.add(menuPanel);
		menuPanel.setWidth("353px");
		contentDetailsDecorator.add(contentDetailsPanel);
	}

	private void initDetailsTable() {
		detailsTable.setWidget(0, 0, new Label("Username"));
		detailsTable.setWidget(0, 1, username);
		detailsTable.setWidget(1, 0, new Label("First Name"));
		detailsTable.setWidget(1, 1, firstname);
		detailsTable.setWidget(2, 0, new Label("Last Name"));
		detailsTable.setWidget(2, 1, lastname);
		detailsTable.setWidget(3, 0, new Label("email Id"));
		detailsTable.setWidget(3, 1, emailId);
		detailsTable.setWidget(4, 0, new Label("Password"));
		detailsTable.setWidget(4, 1, password);

		username.setFocus(true);
	}

	@Override
	public HasClickHandlers getSignupButton() {

		return signupButton;
	}

	@Override
	public HasValue<String> getUsername() {

		return username;
	}

	@Override
	public HasValue<String> getFirstName() {
		return firstname;
	}

	@Override
	public HasValue<String> getLastName() {

		return lastname;
	}

	@Override
	public HasValue<String> getEmailAddress() {

		return emailId;
	}
	@Override
	public HasValue<String> getPassword() {

		return password;
	}

	@Override
	public void setLabel(String str) {
		Window.alert(str);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

}
