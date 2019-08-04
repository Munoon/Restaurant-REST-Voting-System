package com.train4game.munoon.web.converter;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    @Override
    public LocalDate parse(String s, Locale locale) throws ParseException {
        return LocalDate.parse(s);
    }

    @Override
    public String print(LocalDate date, Locale locale) {
        return date.toString();
    }
}
