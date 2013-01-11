package edu.columbia.tripninja.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public class GetDetailEvent extends GwtEvent<GetDetailEventHandler>{

	public static Type<GetDetailEventHandler> TYPE = new Type<GetDetailEventHandler>();
	private final String location;
	
	public GetDetailEvent(String id) {

		location = id;
	}
	
	public String getLocation() {
		
		return this.location;
	}
	@Override
	public Type<GetDetailEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	@Override
	protected void dispatch(GetDetailEventHandler handler) {
		handler.getDetail(this);
		
	}

}
