package model;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.util.Date;

public class Task implements Cloneable, Serializable {

    private String title;
    private Date time;
    private Date start;
    private Date end;
    private int interval;
    private boolean active;
    private boolean isRepeated;

    //need for javaFX TableView
    public Task (){ }

    public Task (String title, Date time) {

        if ( title == null ) throw new IllegalArgumentException( "Заголовок задачі не може бути null [title = '"
                + title + "' time = '" + time.toString() + "']"  );

        if ( time == null ) throw new IllegalArgumentException( "Момент часу не може бути null [title = '"
                + title + "' time = '" + time.toString() + "']"  );

        this.title = title;
        this.time = time;
        this.isRepeated = false;
        this.active = false;
    }

    public Task (String title, Date start, Date end, int interval) {

        if ( title == null ) throw new IllegalArgumentException( "Заголовок задачі не може бути null [title = '"
                + title + "' start = '" + start + "' end ='" +end + "' interval = '" + interval + "']");

        if ( start == null || end == null ) throw new IllegalArgumentException( "Момент часу не може бути null [title = '"
                + title + "' time = '" + time.toString() + "']"  );

        if ( end.before(start)) throw new IllegalArgumentException( "Час початку має бути мешне часу кінця задачі[title = '"
                + title + "' start = '" + start + "' end ='" +end + "' interval = '" + interval + "']" );

        if ( interval <= 0 ) throw new IllegalArgumentException( "Інтервал повторення задачі повинен бути більше нуля. [title = '"
                + title + "' start = '" + start + "' end ='" +end + "' interval = '" + interval + "']" );

        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.isRepeated = true;
        this.active = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        if ( title == null ) throw new IllegalArgumentException( "Заголовок задачі не може бути null [title = '"
                + title + "']");

        this.title = title;
    }

    public boolean isActive(){
        return active;
    }

    public void setActive( boolean active ){
        this.active = active;
    }

    public Date getTime(){
        return (Date) (isRepeated ? start.clone() : time.clone());
    }

    public void setTime( Date time ){

        if ( time == null ) throw new IllegalArgumentException( "Момент часу не може бути null [time = '" + time + "']" );

        this.time = time;

        if ( isRepeated ){
            isRepeated = false;
        }
    }

    public Date getStartTime(){
        return (Date) (isRepeated ? start.clone() : time.clone());
    }

    public Date getEndTime(){
        return (Date) (isRepeated ?  end.clone() : time.clone());
    }

    public int  getRepeatInterval(){
        return isRepeated ? interval : 0;
    }

    public void setTime (Date start, Date end, int interval){

        if ( start == null || end == null ) throw new IllegalArgumentException( "Момент часу не може бути null [title = '"
                + title + "' time = '" + time.toString() + "']"  );

        if ( end.before(start) ) throw new IllegalArgumentException( "Час початку має бути мешне часу кінця задачі[title = '"
                + title + "' start = '" + start + "' end ='" +end + "' interval = '" + interval + "']" );

        if ( interval <= 0 ) throw new IllegalArgumentException( "Інтервал повторення задачі повинен бути більше нуля. [start = '"
                + start + "' end ='" +end + "' interval = '" + interval + "']" );

        this.start = start;
        this.end = end;
        this.interval = interval;

        if ( ! isRepeated ){
            isRepeated = true;
        }

    }

    public boolean isRepeated(){
        return isRepeated;
    }

    public Date nextTimeAfter(Date current){



        if (current == null) throw new IllegalArgumentException( "Момент часу не може бути null [current = '" + current + "']" );

        Date nextTimeAfter =  null;

        if (active){
            if (isRepeated){
                if ((current.getTime() - interval)<= end.getTime()){
                    int intervalInMS = interval * 1000;

                    nextTimeAfter = (Date) start.clone();
                    if (current.compareTo(start) < 0) return nextTimeAfter;

                    while (end.compareTo(nextTimeAfter) >= 0 && current.compareTo(nextTimeAfter) >= 0){
                        if(end.getTime() - nextTimeAfter.getTime() < intervalInMS ){
                            nextTimeAfter = null;
                            break;
                        }
                        if(current.before(nextTimeAfter)) {

                            break;
                        }
                        nextTimeAfter.setTime(nextTimeAfter.getTime() + intervalInMS);
                    }
                }
            }else{
                if (current.before(time)){
                    nextTimeAfter = (Date) time.clone();
                }
            }
        }

        return nextTimeAfter;
    }

    @Override
    public Task clone () throws CloneNotSupportedException {
        Task task = (Task) super.clone();
        task.time = (Date) (this.time == null ? null : this.time.clone());
        task.start = (Date) (this.start == null ? null : this.start.clone());
        task.end = (Date) (this.end == null ? null : this.end.clone());

        return task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (active != task.active) return false;
        if (isRepeated != task.isRepeated) return false;

        if ( isRepeated ) {
            if (!start.equals(task.start)) return false;
            if (!end.equals(task.end)) return false;
            if (interval != task.interval) return false;
        }else {
            if (!time.equals(task.time)) return false;
        }

        return title.equals(task.title);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();

        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (isRepeated ? 1 : 0);

        if ( isRepeated ){
            result = 31 * result + start.hashCode();
            result = 31 * result + end.hashCode();
            result = 31 * result + (int) (interval ^ (interval >>> 32));
        }else {
            result = 31 * result + time.hashCode();
        }

        return result;
    }

    @Override
    public String toString() {
        String str = "Task{title='" + title + '\'';

        str += ", time=" + ((time == null) ? null :  time.toString());
        str += ", start=" + ((start == null) ? null : start.toString());
        str += ", end=" + ((end == null) ? null : end.toString());

        str += ", interval=" + interval +
                ", active=" + active +
                ", isRepeated=" + isRepeated +
                '}';

        return str;
    }
}

