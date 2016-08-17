package org.vaadin.thomas.timefield;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;

/**
 * Abstract parent class for time fields.
 */
public abstract class AbstractDropdownTimeField<T> extends AbstractTimeField<T> {

	private static final long serialVersionUID = -676425827861766118L;

	final NativeSelect hourSelect;
	final NativeSelect minuteSelect;
	final NativeSelect secondSelect;

	private HorizontalLayout root;

	public AbstractDropdownTimeField(String caption) {
		this();
		setCaption(caption);
	}

	public AbstractDropdownTimeField() {

		hourSelect = getSelect();
		minuteSelect = getSelect();
		secondSelect = getSelect();

		root = new HorizontalLayout();
		root.setHeight(null);
		root.setWidth(null);

		fillHours();
		root.addComponent(hourSelect);

		fillMinutes();
		root.addComponent(minuteSelect);

		fillSeconds();
		root.addComponent(secondSelect);

		// when setValue() is called, update selects
		addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 3383351188340627219L;

			@Override
			public void valueChange(Property.ValueChangeEvent event) {
				if (maskInternalValueChange) {
					return;
				}
				updateFields();
			}
		});

		addStyleName("timefield");
		setSizeUndefined();

		updateFields();
	}

	private void fillHours() {

		hourSelect.removeAllItems();

		for (int i = minHours; i <= maxHours; i++) {
			hourSelect.addItem(i);
			if (!use24HourClock) {
				final Integer val = i == 0 || i == 12 ? 12 : i % 12;

				String suffix;
				if (i < 12) {
					suffix = " am";
				} else {
					suffix = " pm";
				}
				hourSelect.setItemCaption(i, val + suffix);
			}
		}
	}

	private void fillMinutes() {

		minuteSelect.removeAllItems();
		for (int i = 0; i < 60; i++) {
			if (i % intervalMinutes == 0) {
				minuteSelect.addItem(i);
				minuteSelect.setItemCaption(i, i < 10 ? "0" + i : i + "");
			}
		}
	}

	private void fillSeconds() {

		secondSelect.removeAllItems();
		for (int i = 0; i < 60; i++) {
			secondSelect.addItem(i);
			secondSelect.setItemCaption(i, i < 10 ? "0" + i : i + "");
		}
	}

	private NativeSelect getSelect() {
		final NativeSelect select = new NativeSelect();
		select.setImmediate(true);
		select.setNullSelectionAllowed(false);
		select.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 3383351188340627219L;

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				if (maskInternalValueChange) {
					return;
				}
				maskInternalValueChange = true;
				updateValue();
				maskInternalValueChange = false;
				AbstractDropdownTimeField.this.fireValueChange(true);

			}
		});
		return select;
	}

	/**
	 * This method gets called when any of the internal Select components are
	 * called.
	 */
	private void updateValue() {

		// if the value of any select is null at this point, we need to init it
		// to zero to prevent exceptions.

		if (secondSelect.getValue() == null) {
			secondSelect.setValue(0);
		}
		if (minuteSelect.getValue() == null) {
			minuteSelect.setValue(0);
		}
		if (hourSelect.getValue() == null) {
			hourSelect.setValue(0);
		}

		resetValue();

		int val;
		if (resolution.ordinal() <= Resolution.HOUR.ordinal()) {
			val = (Integer) hourSelect.getValue();

			setHoursInternal(val);
		}
		if (resolution.ordinal() < Resolution.HOUR.ordinal()) {
			val = (Integer) minuteSelect.getValue();
			setMinutesInternal(val);
		}
		if (resolution.ordinal() < Resolution.MINUTE.ordinal()) {
			val = (Integer) secondSelect.getValue();
			setSecondsInternal(val);
		}

	}

	protected abstract void resetValue();

	/**
	 * This method gets called when we update the actual value (Date) of this
	 * TimeField.
	 */
	@Override
	protected void updateFields() {

		maskInternalValueChange = true;

		minuteSelect.setVisible(resolution.ordinal() < Resolution.HOUR
				.ordinal());
		secondSelect.setVisible(resolution.ordinal() < Resolution.MINUTE
				.ordinal());

		final Object val = getValue();

		// make sure to update alternatives, wont spoil value above
		fillHours();
		fillMinutes();

		if (val == null) {
			// clear values
			hourSelect.setValue(null);
			minuteSelect.setValue(null);
			secondSelect.setValue(null);

			maskInternalValueChange = false;
			return;
		}

		checkBoundsAndInterval();

		hourSelect.setValue(getHoursInternal());
		minuteSelect.setValue(getMinutesInternal());
		secondSelect.setValue(getSecondsInternal());

		maskInternalValueChange = false;
	}

	@Override
	protected Component initContent() {

		return root;
	}

	@Override
	public void setTabIndex(int tabIndex) {
		hourSelect.setTabIndex(tabIndex);
		minuteSelect.setTabIndex(tabIndex);
		secondSelect.setTabIndex(tabIndex);
	}

	@Override
	public int getTabIndex() {
		return hourSelect.getTabIndex();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		hourSelect.setReadOnly(readOnly);
		minuteSelect.setReadOnly(readOnly);
		secondSelect.setReadOnly(readOnly);
		super.setReadOnly(readOnly);
	}

	@Override
	public void setResolution(Resolution resolution) {

		if (this.resolution.ordinal() < resolution.ordinal()) {
			if (resolution.ordinal() > Resolution.SECOND.ordinal()) {
				secondSelect.setValue(0);
			}
			if (resolution.ordinal() > Resolution.MINUTE.ordinal()) {
				minuteSelect.setValue(0);
			}
		}
		super.setResolution(resolution);
	}

	@Override
	public void setHours(int hours) throws IllegalArgumentException {

		super.setHours(hours);
		hourSelect.setValue(hours);
	}

	@Override
	public void setMinutes(int minutes) {
		super.setMinutes(minutes);
		minuteSelect.setValue(minutes);
	}

	@Override
	public void setSeconds(int seconds) {
		super.setSeconds(seconds);
		secondSelect.setValue(seconds);
	}
}
