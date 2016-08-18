package org.vaadin.thomas.timefield;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class DateConverter implements Converter<String, Date> {

	private static final long serialVersionUID = -4269690903609902636L;

	private final AbstractTimeField<?> field;

	private final LocalTimeConverter converter;

	public DateConverter(AbstractTimeField<?> timeTextField) {
		field = timeTextField;

		converter = new LocalTimeConverter(field);
	}

	@Override
	public Date convertToModel(String value, Class<? extends Date> targetType,
			Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (value == null) {
			return null;
		}

		final LocalTime time = converter.convertToModel(value, LocalTime.class,
				locale);
		final ZonedDateTime time2 = time.atDate(LocalDate.of(1970, 1, 1))
				.atZone(ZoneId.systemDefault());
		return Date.from(time2.toInstant());
	}

	@Override
	public String convertToPresentation(Date value,
			Class<? extends String> targetType, Locale locale)
					throws com.vaadin.data.util.converter.Converter.ConversionException {

		if (value == null) {
			return null;
		}

		final LocalTime localTime = LocalDateTime.from(
				value.toInstant().atZone(ZoneId.systemDefault())
				.toLocalDateTime()).toLocalTime();

		return converter.convertToPresentation(localTime, targetType, locale);
	}

	@Override
	public Class<Date> getModelType() {
		return Date.class;
	}

	@Override
	public Class<String> getPresentationType() {
		return String.class;
	}

}
