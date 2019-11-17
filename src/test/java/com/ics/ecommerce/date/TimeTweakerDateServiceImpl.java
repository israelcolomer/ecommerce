package com.ics.ecommerce.date;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

@Slf4j
@Profile("!prod")
@Service
public class TimeTweakerDateServiceImpl extends DateServiceImpl {

    @Getter
    private LocalDateTime currentTime;

    @PostConstruct
    public void init() {
        currentTime = LocalDateTime.now();
    }

    public String nowFormatted(DateFormat dateFormat) {
        switch (dateFormat) {
            case ISO_LOCAL_DATE: return DateTimeFormatter.ISO_LOCAL_DATE.format(currentTime);
            case ISO_DATE_TIME: return DateTimeFormatter.ISO_DATE_TIME.format(currentTime);
            default: throw new IllegalArgumentException("Can't format " + dateFormat);
        }
    }

    public TemporalAccessor now(DateFormat dateFormat) {
        switch (dateFormat) {
            case ISO_LOCAL_DATE: return LocalDate.of(currentTime.getYear(), currentTime.getMonth(), currentTime.getDayOfMonth());
            case ISO_DATE_TIME: return currentTime;
            default: throw new IllegalArgumentException("Can't format " + dateFormat);
        }
    }

    public void setCurrentTime(LocalDateTime newTime) {
        log.debug("Setting current date: {}", newTime);
        this.currentTime = newTime;
    }
}
