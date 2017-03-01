package model;

import java.util.*;
import java.lang.InstantiationException;
import java.lang.IllegalAccessException;


public class Tasks {

    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end){

        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (start == null || end == null)throw new IllegalArgumentException("start == null || end == null [start = '" + start + "' end = '" + end + "']");
        if (start.after(end))throw new IllegalArgumentException("start after end !!! It's wrong [start = '" + start + "' end = '" + end + "']");
        // if(! (tasks instanceof TaskList)) throw new IllegalArgumentException("I can work only with TaskList");
        TaskList incomingTaskList =  null;

        try{
            incomingTaskList = (TaskList) tasks.getClass().newInstance();
        }
        catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        catch(ClassCastException e){
            incomingTaskList = new LinkedTaskList();
            //throw new IllegalArgumentException("I can work only with TaskList");
        }

        for (Task currentTask : tasks ){
            Date nextTime = currentTask.nextTimeAfter( start );
            if (nextTime != null && start.compareTo(nextTime) <= 0 && end.compareTo(nextTime) >= 0){

                if (incomingTaskList != null) incomingTaskList.add(currentTask);
            }
        }
        return incomingTaskList;
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end){
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (start == null || end == null)throw new IllegalArgumentException("start == null || end == null [start = '" + start + "' end = '" + end + "']");
        if (start.after(end))throw new IllegalArgumentException("start after end !!! It's wrong [start = '" + start + "' end = '" + end + "']");

        SortedMap<Date, Set<Task>> map = new TreeMap<Date, Set<Task>>();

        Iterable<Task> incomingList = incoming(tasks, start, end);
        for (Task task : incomingList){
            Date currentTime = task.nextTimeAfter(start);

            while ( currentTime != null && currentTime.compareTo(end) <= 0){
                //map.putIfAbsent(currentTime, new HashSet<Task>());
                if (map.get(currentTime) == null){
                    map.put(currentTime, new HashSet<>());
                }
                map.get(currentTime).add(task);
                currentTime = task.nextTimeAfter(currentTime);
            }
        }
        return map;
    }
}
