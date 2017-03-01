package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class FormatDateForFXML {

    private FormatDateForFXML() {
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat ("yyyy.MM.dd");

    public static Date valueOf(String val) throws ParseException {
        return DATE_FORMAT.parse(val);
    }

    public static String toString(Date date) {
        return date == null ? null : DATE_FORMAT.format(date);
    }
}
