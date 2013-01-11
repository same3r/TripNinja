package edu.columbia.tripninja.client.view;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.columbia.tripninja.client.presenter.DetailPresenter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class DetailView extends Composite implements DetailPresenter.Display {

	private final Button PostButton;
	private final TextArea Comment;
	private FlexTable resultsTable;
	private FlexTable commentsTable;
	private final FlexTable searchTable;
	private final Button getMoreDetails;
	private final Button logoutButton;
	
	public DetailView() {
		VerticalPanel logoutPanel = new VerticalPanel();
		FormPanel searchTableDecorator = new FormPanel();
		initWidget(searchTableDecorator);
		searchTableDecorator.setSize("900px", "700px");

		searchTable = new FlexTable();
		logoutButton = new Button("Logout");
		logoutButton.setStyleName("btn btn-link");
		logoutPanel.add(logoutButton);
		logoutPanel.setCellHorizontalAlignment(logoutButton, HasHorizontalAlignment.ALIGN_RIGHT);
		logoutPanel.setCellWidth(logoutButton, "250");
		searchTable.setWidth("900px");
		searchTable.getCellFormatter().addStyleName(0, 0,
				"contacts-ListContainer");
		searchTable.getCellFormatter().setWidth(0, 1, "100%");
		searchTable.getFlexCellFormatter().setVerticalAlignment(0, 1,
				DockPanel.ALIGN_TOP);
		getMoreDetails = new Button("Get More Details");
		getMoreDetails.setStyleName("btn btn-success");
		
		Comment = new TextArea();
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setBorderWidth(0);
		hPanel.setSpacing(0);
		hPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);

		hPanel.add(Comment);
		hPanel.setCellHorizontalAlignment(Comment,
				HasHorizontalAlignment.ALIGN_RIGHT);
		Comment.setWidth("185px");
		hPanel.setCellWidth(Comment, "400px");
		searchTable.getCellFormatter().addStyleName(0, 0, "contacts-ListMenu");

		// Create the contacts list
		//
		resultsTable = new FlexTable();
		resultsTable.setStyleName("table-hover");
		resultsTable.setCellSpacing(0);
		resultsTable.setCellPadding(0);
		resultsTable.setWidth("600px");
		resultsTable.addStyleName("contacts-ListContents");
		resultsTable.getColumnFormatter().setWidth(0, "15px");

		commentsTable = new FlexTable();
		commentsTable.setStyleName("table-striped table-hover");
		commentsTable.setCellSpacing(0);
		commentsTable.setCellPadding(0);
		commentsTable.setWidth("296px");
		commentsTable.addStyleName("contacts-ListContents");
		commentsTable.getColumnFormatter().setWidth(0, "15px");

		searchTable.setWidget(0, 1, logoutPanel);
		searchTable.setWidget(1, 0, resultsTable);
		commentsTable.setWidget(0, 0, hPanel);
		hPanel.setWidth("267px");
		searchTable.setWidget(1, 1, commentsTable);
		searchTable.setWidget(0, 0, getMoreDetails);
		PostButton = new Button("Post");
		PostButton.setStyleName("btn");
		hPanel.add(PostButton);
		hPanel.setCellHorizontalAlignment(PostButton, HasHorizontalAlignment.ALIGN_RIGHT);
		hPanel.setCellWidth(PostButton, "75px");
		PostButton.setWidth("50px");
		searchTableDecorator.add(searchTable);
		searchTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_LEFT);
		searchTable.getCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
	}

	@Override
	public HasClickHandlers getPostButton() {

		return PostButton;
	}

	@Override
	public void setData(List<String> data) {

		resultsTable.removeAllRows();
		
		resultsTable.setText(0, 0, data.get(0));

		if (data.size() > 1) {
			for (int i = 1; i < data.size(); ++i) {
				commentsTable.setText(i, 0, data.get(i));
			}
		}
	}

	@Override
	public String getComment() {
		// TODO Auto-generated method stub
		return Comment.getText();
	}

	public HasClickHandlers getMoreDetails() {
		return getMoreDetails;
	}

	public HasClickHandlers getLogoutButton() {
		return logoutButton;
	}

	@Override
	public HasClickHandlers getMoreDetailsButton() {
		// TODO Auto-generated method stub
		return getMoreDetails;
	}

	@Override
	public void clearCommentBox() {
		Comment.setText("");

	}

}
