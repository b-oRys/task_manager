package model;


import java.util.Date;

public class Validator implements ValidateModel{
    @Override
    public boolean validateInterval(Date start, Date end) {
        return false;
    }

    @Override
    public boolean validateRepeatedTask(String title, Date start, Date end, int interval) {
        return false;
    }

    @Override
    public boolean validateNotRepeatedTask(String title, Date time) {
        return false;
    }
}
