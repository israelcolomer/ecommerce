package com.ics.ecommerce.date;

import java.time.temporal.TemporalAccessor;

public interface DateService {
    /**
     * Parses the provided date using the formatter defined by the {@link DateFormat}. It will return a
     * {@link java.time.LocalDate} for {@link DateFormat#ISO_LOCAL_DATE} and a
     * {@link java.time.LocalDateTime} for {@link DateFormat#ISO_DATE_TIME}
     *
     * @param toParse the date to parse
     * @param dateFormat the format to use when parsing
     *
     * @return the {@link TemporalAccessor} as specified by the {@link DateFormat} provided
     * @throws IllegalArgumentException if the dateFormat cannot be handled
     */
    TemporalAccessor parse(String toParse, DateFormat dateFormat);

    /**
     * Returns a string representation of the current time as defined by the {@link DateFormat}. It will return a
     * {@link java.time.LocalDate} formatted using {@link ISO_LOCAL_DATE} for {@link DateFormat#ISO_LOCAL_DATE} and a
     * {@link java.time.LocalDateTime} formatted using {@link ISO_DATE_TIME} for {@link DateFormat#ISO_DATE_TIME}
     *
     * @param dateFormat the format to use when formatting
     *
     * @return the formatted date
     * @throws IllegalArgumentException if the dateFormat cannot be handled
     */
    String nowFormatted(DateFormat dateFormat);

    /**
     * Returns a {@link TemporalAccessor} as defined by the {@link DateFormat}. It will return a
     * {@link java.time.LocalDate} for {@link DateFormat#ISO_LOCAL_DATE} and a
     * {@link java.time.LocalDateTime} for {@link DateFormat#ISO_DATE_TIME}
     *
     * @param dateFormat the format to use when parsing
     *
     * @return the {@link TemporalAccessor} as specified by the {@link DateFormat} provided
     * @throws IllegalArgumentException if the dateFormat cannot be handled
     */
    TemporalAccessor now(DateFormat dateFormat);

    /**
     * Returns a string representation for the provided time as defined by the {@link DateFormat}. It will return a
     * {@link java.time.LocalDate} formatted using {@link ISO_LOCAL_DATE} for {@link DateFormat#ISO_LOCAL_DATE} and a
     * {@link java.time.LocalDateTime} formatted using {@link ISO_DATE_TIME} for {@link DateFormat#ISO_DATE_TIME}
     *
     * @param date thedate to format
     * @param dateFormat the format to use when formatting
     *
     * @return the formatted date
     * @throws IllegalArgumentException if the dateFormat cannot be handled
     */
    String format(TemporalAccessor date, DateFormat dateFormat);

}
