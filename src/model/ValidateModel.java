package model;

import java.util.Date;

public interface ValidateModel {

    public boolean validateInterval (Date start, Date end);

    public boolean validateRepeatedTask (String title, Date start, Date end, int interval);

    public boolean validateNotRepeatedTask (String title, Date time);
}
