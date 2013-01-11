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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.columbia.tripninja.client.presenter.LoginPresenter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class LoginView extends Composite implements LoginPresenter.Display {
	private final TextBox username;
	private final PasswordTextBox password;
	private final FlexTable detailsTable;
	private final Button loginButton;
	private final Button signupButton;

	public LoginView() {
		FormPanel contentDetailsDecorator = new FormPanel();
		contentDetailsDecorator.setStyleName("form-body");
		contentDetailsDecorator.setSize("656px", "361px");
		initWidget(contentDetailsDecorator);
		
		VerticalPanel contentDetailsPanel = new VerticalPanel();
		contentDetailsPanel.setWidth("315px");

		detailsTable = new FlexTable();
		detailsTable.setCellSpacing(0);
		detailsTable.setWidth("100%");
		detailsTable.addStyleName("contacts-ListContainer");
		detailsTable.getColumnFormatter().addStyleName(1, "add-contact-input");
		username = new TextBox();
		password = new PasswordTextBox();
		initDetailsTable();
		contentDetailsPanel.add(detailsTable);

		HorizontalPanel menuPanel = new HorizontalPanel();
		loginButton = new Button("Login");
		signupButton = new Button("Signup");
		loginButton.setStyleName("btn");
		signupButton.setStyleName("btn btn-primary");
		menuPanel.add(loginButton);
		menuPanel.setCellHorizontalAlignment(loginButton, HasHorizontalAlignment.ALIGN_RIGHT);
		menuPanel.setCellWidth(loginButton, "150px");
		loginButton.setWidth("75px");
		menuPanel.add(signupButton);
		menuPanel.setCellWidth(signupButton, "150px");
		menuPanel.setCellHorizontalAlignment(signupButton, HasHorizontalAlignment.ALIGN_CENTER);
		signupButton.setWidth("75px");
		contentDetailsPanel.add(menuPanel);
		contentDetailsDecorator.add(contentDetailsPanel);
	}

	private void initDetailsTable() {
		detailsTable.setWidget(0, 0, new Label("Username"));
		detailsTable.setWidget(0, 1, username);
		detailsTable.setWidget(1, 0, new Label("Password"));
		detailsTable.setWidget(1, 1, password);
		username.setFocus(true);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return loginButton;
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
	public HasValue<String> getPassword() {
		return password;
	}
}
