package edu.columbia.tripninja.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import edu.columbia.tripninja.client.presenter.MoreDetailsPresenter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class MoreDetailsView extends Composite implements
		MoreDetailsPresenter.Display {

	private String placeId = "united_states";
	private Button logoutButton;
	private final VerticalPanel vp;

	public MoreDetailsView() {

		vp = new VerticalPanel();
		logoutButton = new Button("Logout");
		logoutButton.setStyleName("btn btn-link");
		vp.add(logoutButton);
		vp.setCellHorizontalAlignment(logoutButton, HasHorizontalAlignment.ALIGN_RIGHT);
		vp.setCellWidth(logoutButton, "900px");
		logoutButton.setWidth("50px");

		initWidget(vp);

	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public HasClickHandlers getLogoutButton() {
		return logoutButton;
	}

	@Override
	public void setPlaceId(String placeId) {
		Frame frame = new Frame("http://www.freebase.com/view/en/" + placeId);
		frame.setSize("900px", "500px");
		vp.add(frame);

	}
}
