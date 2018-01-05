package org.vaadin.thomas.timefield;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;

/**
 * Abstract parent class for time fields.
 */
public abstract class AbstractDropdownTimeField<T> extends AbstractTimeField<T> {

	private static final long serialVersionUID = -676425827861766118L;

	private boolean nullSelectionAllowed = true;
	
	final NativeSelect hourSelect = new NativeSelect();
	final NativeSelect minuteSelect = new NativeSelect();
	final NativeSelect secondSelect = new NativeSelect();

	private HorizontalLayout root;

	public AbstractDropdownTimeField(String caption) {
		this();
		setCaption(caption);
	}

	public AbstractDropdownTimeField() {

		getSelect(hourSelect);
		getSelect(minuteSelect);
		getSelect(secondSelect);

		root = new HorizontalLayout();
		root.setHeight(null);
		root.setWidth(null);
		
		Label dots = new Label(":");
		dots.addStyleName("dosPuntos");
		
		fillHours();
		root.addComponent(hourSelect);
		root.addComponent(dots);
		
		fillMinutes();
		root.addComponent(minuteSelect);

		fillSeconds();
		root.addComponent(secondSelect);

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

	/*
	Todo esto acaba heredando de CustomField, que no lo tiene, así que no veo otro remedio que reimplementarlo
	*/
	public void setNullSelectionAllowed(boolean nullSelectionAllowed){
		if (nullSelectionAllowed != this.nullSelectionAllowed) {
            this.nullSelectionAllowed = nullSelectionAllowed;
            markAsDirty();
        }
	}
	
	private void getSelect(NativeSelect ns) {
		//final NativeSelect select = new NativeSelect();
		ns.setImmediate(true);
		//if(ns.equals(hourSelect)){
			ns.setNullSelectionAllowed(this.nullSelectionAllowed); // solo permito la selección de null en las horas, para simplificar
		//}
		//else{
		//	ns.setNullSelectionAllowed(false);
		//}
		if(!(ns.equals(hourSelect))){ // si son minutos o segundos...
			if(ns.getValue()==null){
				ns.setEnabled(false); // hasta que no pongamos las horas con valor no se habilitarán los minutos y segundos
			}
		}
		ns.addValueChangeListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 3383351188340627219L;

			@Override
			public void valueChange( // continuar per ací
					com.vaadin.data.Property.ValueChangeEvent event) {
				if (maskInternalValueChange) {
					return;
				}
				maskInternalValueChange = true;
				//event.getProperty().
				//Object cac = event.getProperty();
				// la part del updateValue() que ara canvie ací per a actuar en funció del dropdown mogut:
				if(ns.equals(hourSelect)){
					if(ns.getValue()==null){
						minuteSelect.setValue(null);
						minuteSelect.setEnabled(false);
						secondSelect.setValue(null);
						secondSelect.setEnabled(false);
					}
					else{
						minuteSelect.setEnabled(true);
						secondSelect.setEnabled(true);
						if(minuteSelect.getValue()==null){
							minuteSelect.setValue(0);
						}
						if(secondSelect.getValue()==null){
							secondSelect.setValue(0);
						}
					}
				}
				else{ // si hemos seleccionado minutos o segundos...
					if(ns.getValue()==null){
						ns.setValue(0);
					}
				}
				
				updateValue();
				//markAsDirty(); // test
				maskInternalValueChange = false;
				AbstractDropdownTimeField.this.fireValueChange(true);

			}
		});
		//return select;
	}

	/**
	 * This method gets called when any of the internal Select components are
	 * called.
	 */
	private void updateValue() {

		
		
		// if the value of any select is null at this point, we need to init it
		// to zero to prevent exceptions.
		/*
		if (secondSelect.getValue() == null) {
			//secondSelect.setValue(0);
			minuteSelect.setValue(null);
			hourSelect.setValue(null);
		}
		if (minuteSelect.getValue() == null) {
			//minuteSelect.setValue(0);
			secondSelect.setValue(null);
			//minuteSelect.select(null);
			hourSelect.setValue(null);
		}
		if (hourSelect.getValue() == null) {
			//hourSelect.setValue(0);
			secondSelect.setValue(null);
			minuteSelect.setValue(null);
			//hourSelect.setValue(null);
		}
		*/
		resetValue();

		Integer h = 0, m = 0, s = 0;

		if (resolution.ordinal() <= Resolution.HOUR.ordinal()) {
			//Integer hourSelectValue = null;
			//(Integer)hourSelectvalue;
			h = (Integer) hourSelect.getValue();
		}
		if (resolution.ordinal() < Resolution.HOUR.ordinal()) {
			m = (Integer) minuteSelect.getValue();
		}
		if (resolution.ordinal() < Resolution.MINUTE.ordinal()) {
			s = (Integer) secondSelect.getValue();
		}

		setInternalValue(h, m, s);
	}

	protected abstract void setInternalValue(Integer h, Integer m, Integer s);

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
	protected void setHoursInternal(int val) {
		hourSelect.setValue(val);
	}

	@Override
	protected void setMinutesInternal(int val) {
		minuteSelect.setValue(val);
	}

	@Override
	protected void setSecondsInternal(int val) {
		secondSelect.setValue(val);
	}

}
