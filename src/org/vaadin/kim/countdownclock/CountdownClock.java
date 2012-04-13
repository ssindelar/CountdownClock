package org.vaadin.kim.countdownclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.vaadin.kim.countdownclock.client.ui.CountdownClockRpc;
import org.vaadin.kim.countdownclock.client.ui.CountdownClockState;

import com.vaadin.ui.AbstractComponent;

public class CountdownClock extends AbstractComponent {

	private static final long serialVersionUID = -4093579148150450057L;

	protected Date date = new Date();

	protected boolean sendEvent = false;

	protected String format = "%dD %hH %mM %sS";

	protected List<EndEventListener> listeners = new ArrayList<EndEventListener>();

	public CountdownClock() {
		CountdownClockRpc rpc = new CountdownClockRpc() {
			private static final long serialVersionUID = -7392569455421206075L;

			public void countdownEnded() {
				for (EndEventListener listener : listeners) {
					listener.countDownEnded(CountdownClock.this);
				}
			}
		};
		registerRpc(rpc);
	}

	/**
	 * Set the target date for the countdown
	 * 
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
		sendEvent = true;
		requestRepaint();
	}

	/**
	 * Get the current target date for the countdown
	 * 
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public void changeVariables(Object source, Map variables) {
	// super.changeVariables(source, variables);
	// if (sendEvent) {
	// if (variables.containsKey("end") && !isReadOnly()) {
	// sendEvent = false;
	// for (EndEventListener listener : listeners) {
	// listener.countDownEnded(this);
	// }
	// }
	// }
	// }
	//
	// @Override
	// public void paintContent(PaintTarget target) throws PaintException {
	// super.paintContent(target);
	// target.startTag("countdown");
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(date);
	// long difference = calendar.getTimeInMillis()
	// - Calendar.getInstance().getTimeInMillis();
	//
	// target.addAttribute("time", difference);
	// target.addAttribute("format", format);
	// target.endTag("countdown");
	// }

	@Override
	public CountdownClockState getState() {
		return (CountdownClockState) super.getState();
	}

	@Override
	public void requestRepaint() {
		super.requestRepaint();
		getState().setTimeFormat(getFormat());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		long difference = calendar.getTimeInMillis()
				- Calendar.getInstance().getTimeInMillis();
		getState().setCountdownTarget(difference);
	}

	/**
	 * Set the format for the clock. Available parameters:
	 * 
	 * %d - days %h - hours %m - minutes %s - seconds %ts - tenth of a seconds
	 * 
	 * For example "%d day(s) %h hour(s) and %m minutes" could produce the
	 * string "2 day(s) 23 hour(s) and 5 minutes"
	 * 
	 * @param format
	 */
	public void setFormat(String format) {
		this.format = format;
		requestRepaint();
	}

	/**
	 * Get the current format being used
	 * 
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Interface for listening to countdown events
	 * 
	 * @author Kim
	 * 
	 */
	public interface EndEventListener {
		/**
		 * Listener for countdown events. Takes as input the clock which reached
		 * its target date and time.
		 * 
		 * @param clock
		 */
		public void countDownEnded(CountdownClock clock);
	}

	/**
	 * Add a listener for countdown events.
	 * 
	 * @param listener
	 */
	public void addListener(EndEventListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove listener for countdown events.
	 * 
	 * @param listener
	 */
	public void removeListener(EndEventListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
	}

}
