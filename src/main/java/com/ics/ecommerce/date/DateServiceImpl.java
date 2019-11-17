package com.ics.ecommerce.date;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Profile("prod")
@Service
// TODO: add UTs
public class DateServiceImpl implements DateService {

    public TemporalAccessor parse(String toParse, DateFormat dateFormat) {
        switch (dateFormat) {
            case ISO_LOCAL_DATE: return LocalDate.parse(toParse, DateTimeFormatter.ISO_LOCAL_DATE);
            case ISO_DATE_TIME: return LocalDateTime.parse(toParse, DateTimeFormatter.ISO_DATE_TIME);
            default: throw new IllegalArgumentException("Can't format " + dateFormat);
        }
    }

    public String nowFormatted(DateFormat dateFormat) {
        switch (dateFormat) {
            case ISO_LOCAL_DATE: return DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
            case ISO_DATE_TIME: return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
            default: throw new IllegalArgumentException("Can't format " + dateFormat);
        }
    }

    public TemporalAccessor now(DateFormat dateFormat) {
        switch (dateFormat) {
            case ISO_LOCAL_DATE: return LocalDate.now();
            case ISO_DATE_TIME: return LocalDateTime.now();
            default: throw new IllegalArgumentException("Can't format " + dateFormat);
        }
    }

    public String format(TemporalAccessor localDateTime, DateFormat dateFormat) {
        switch (dateFormat) {
            case ISO_LOCAL_DATE: return DateTimeFormatter.ISO_LOCAL_DATE.format(localDateTime);
            case ISO_DATE_TIME: return DateTimeFormatter.ISO_DATE_TIME.format(localDateTime);
            default: throw new IllegalArgumentException("Can't format " + dateFormat);
        }
    }
}
