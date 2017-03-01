package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;


public class TaskIO {

    private static final int lengthOfDay = 86_400;
    private static final int lengthOfHour = 3_600;
    private static final int lengthOfMinute = 60;


    public static void write (TaskList tasks, OutputStream out) throws IOException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (out == null) throw new IllegalArgumentException("out must be not null [out = '" + out + "']");


        DataOutput dataOutputStream = new DataOutputStream(out);
        dataOutputStream.writeInt(tasks.size());

        for (Task task : tasks){
            dataOutputStream.writeInt(task.getTitle().length());
            dataOutputStream.writeUTF(task.getTitle());
            dataOutputStream.writeInt(task.isActive() ? 1 : 0);
            dataOutputStream.writeInt(task.getRepeatInterval());
            if (!task.isRepeated()){
                dataOutputStream.writeLong(task.getTime().getTime());
            }else{
                dataOutputStream.writeLong(task.getStartTime().getTime());
                dataOutputStream.writeLong(task.getEndTime().getTime());
            }
        }

        //out.close();
    }

    public static void read (TaskList tasks, InputStream in) throws IOException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (in == null) throw new IllegalArgumentException("in must be not null [out = '" + in + "']");
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(in);

            int numberOfTasks = dataInputStream.readInt();
            for (int i = 0; i < numberOfTasks; i++) {
                Task temporaryTask = null;

                int titleLength = dataInputStream.readInt();
                String title = dataInputStream.readUTF();
                boolean isActive = dataInputStream.readInt() == 1;
                int interval = dataInputStream.readInt();

                if (interval > 0) {
                    Date start = new Date(dataInputStream.readLong());
                    Date end = new Date(dataInputStream.readLong());
                    temporaryTask = new Task(title, start, end, interval);
                } else {
                    Date time = new Date(dataInputStream.readLong());
                    temporaryTask = new Task(title, time);
                }

                temporaryTask.setActive(isActive);
                tasks.add(temporaryTask);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                dataInputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }




    }

    public static void writeBinary (TaskList tasks, File file) throws IOException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (file == null) throw new IllegalArgumentException("file must be not null [file = '" + file + "']");

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        write(tasks, fileOutputStream);
/*        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);

        dataOutputStream.write(tasks.size());

        for (Task task : tasks){
            dataOutputStream.write(task.getTitle().length());
            dataOutputStream.writeUTF(task.getTitle());
            dataOutputStream.writeByte(task.isActive() ? 1 : 0);
            dataOutputStream.writeInt(task.getRepeatInterval());
            if (task.isRepeated()){
                dataOutputStream.writeLong(task.getTime().getTime());
            }else{
                dataOutputStream.writeLong(task.getStartTime().getTime());
                dataOutputStream.writeLong(task.getEndTime().getTime());
            }
        }*/

    }

    public static void readBinary (TaskList tasks, File file) throws IOException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (file == null) throw new IllegalArgumentException("file must be not null [file = '" + file + "']");
        if (!file.exists()) throw new IllegalArgumentException("file is not exist [file = '" + file + "']");

        FileInputStream fileInputStream = new FileInputStream(file);

        read(tasks, fileInputStream);
        /*DataInput dataInputStream = new DataInputStream(fileInputStream);

        int numberOfTasks = dataInputStream.readInt();

        for (int i = 0; i < numberOfTasks; i++){
            Task temporaryTask = null;

            int titleLength = dataInputStream.readInt();
            String title = dataInputStream.readUTF();
            boolean isActive = dataInputStream.readByte() == 1;
            int interval = dataInputStream.readInt();

            if (interval > 0) {
                Date start = new Date(dataInputStream.readLong());
                Date end = new Date(dataInputStream.readLong());
                temporaryTask = new Task(title,start,end,interval);
            }else{
                Date time = new Date(dataInputStream.readLong());
                temporaryTask = new Task(title, time);
            }

            temporaryTask.setActive(isActive);
            tasks.add(temporaryTask);
        }
*/
    }

    public static void write (TaskList tasks, Writer out) throws IOException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (out == null) throw new IllegalArgumentException("in must be not null [out = '" + out + "']");

        // File f = new File("C:\\Users\\User\\Desktop\\ex1.txt");
        // FileWriter fileWriter = new FileWriter(f);
        // BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        BufferedWriter bufferedWriter = new BufferedWriter(out);

        for (int k = 0; k < tasks.size(); k++ ){
            Task task = tasks.getTask(k);
            StringBuilder infoAboutTask = new StringBuilder("");

            infoAboutTask.append(formatTitle(task.getTitle())).append(" ");

            if (task.isRepeated()){
                infoAboutTask.append("from ").append(formatDate(task.getStartTime())).append(" ");
                infoAboutTask.append("to ").append(formatDate(task.getEndTime())).append(" ");
                infoAboutTask.append("every ").append(formatInterval(task.getRepeatInterval()));

            }else{
                infoAboutTask.append("at ").append(formatDate(task.getTime()));

            }

            if (!task.isActive()) infoAboutTask.append(" inactive");
            infoAboutTask.append((k == tasks.size() - 1) ? "." : ";") ;
            infoAboutTask.append("\n");
            bufferedWriter.write(infoAboutTask.toString());
            bufferedWriter.flush();
        }

        bufferedWriter.close();
    }

    public static void read (TaskList tasks, Reader in) throws IOException, ParseException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (in == null) throw new IllegalArgumentException("in must be not null [out = '" + in + "']");


        BufferedReader bufferedReader = new BufferedReader(in);

        String currentLine;
        while ((currentLine = bufferedReader.readLine()) != null){

            String title = "";
            Date time = null;
            Date start = null;
            Date end = null;
            int interval = 0;
            boolean active = false;
            boolean isRepeated = false;

            String[] array = currentLine.split(" ");
            active = !array[array.length - 1].contains("inactive");

            int endOfUnknownLine = array.length - (active ?  1 : 2);
            for (int k = endOfUnknownLine; k >=0; k--){

                if (array[k].equals("at")){

                    String formatedTime = array[k + 1] + " " +
                            ((array[endOfUnknownLine].contains(";") || (array[endOfUnknownLine].lastIndexOf(".") == (array[endOfUnknownLine].length() - 1))) ? array[endOfUnknownLine].substring(0, array[endOfUnknownLine].length() - 1 ) : array[endOfUnknownLine]);
//                    try {
                    time = unformatDate(formatedTime);
//                    } catch (ParseException e) {
//                        throw new IllegalArgumentException("Can't parse task, wrong format for Date " +
//                                "(correct format \"'['yyyy-MM-dd HH:mm:ss.SSS']'\")", e);
//                    }
                    String formatedTitle = "";
                    for (int j = 0; j < k; j++){
                        formatedTitle += array[j];
                        if (j != k -1) formatedTitle += " ";
                    }
                    title = unformatTitle(formatedTitle);
                    isRepeated = false;
                    break;
                }


                if (array[k].equals("every")){

                    String intervalString = "";
                    int endOfInterval = array.length - (active ? 1 : 2);
                    for (int j = k + 1; j <= endOfInterval; j++){
                        if (j == endOfInterval ){
                            intervalString += ((array[j].contains(";") || (array[j].lastIndexOf(".") == (array[j].length() - 1)))? array[j].substring(0, array[j].length() - 1) : array[j]);
                        }else{
                            intervalString += array[j] + " ";
                        }
                    }


                    String endString = array[k - 2] + " " + array[k - 1];
                    String startString = array[k - 5] + " " + array[k - 4];
                    String formatedTitle = "";
                    for (int j = 0; j < k - 6; j++){
                        formatedTitle += array[j];
                        if (j != k - 7) formatedTitle += " ";
                    }

                    title = unformatTitle(formatedTitle);
                    start = unformatDate(startString);
                    end = unformatDate(endString);
                    interval = unformatInterval(intervalString);
                    isRepeated = true;
                    break;
                }

            }

            Task task = isRepeated ? new Task(title,start,end,interval) : new Task(title, time);
            task.setActive(active);
            tasks.add(task);

            System.out.println(tasks.toString());

        }
        //bufferedReader.flush();
        bufferedReader.close();
    }

    public static void writeText (TaskList tasks, File file) throws IOException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (file == null) throw new IllegalArgumentException("file must be not null [file = '" + file + "']");

        FileWriter fileWriter = new FileWriter(file);
        write(tasks, fileWriter);
    }

    public static void readText (TaskList tasks, File file) throws IOException, ParseException {
        if (tasks == null) throw new IllegalArgumentException("tasks must be not null [task = '" + tasks + "']");
        if (file == null) throw new IllegalArgumentException("file must be not null [file = '" + file + "']");
        if (!file.exists()) throw new IllegalArgumentException("file is not exist [file = '" + file + "']");

        FileReader fileReader = new FileReader(file);
        read(tasks, fileReader);

    }

    private static String formatTitle(String title){

        String formated = title.replace("\"", "\"\"");

        /*String [] array = title.split("\"");
        for (int k = 0; k < array.length; k++){
            if (k != array.length - 1) formated += array[k] + "\"\"";
            if (title.charAt(title.length() - 1) == '"') formated += "\"\"";
        }*/

        return "\"" + formated + "\"";
    }

    private static String unformatTitle(String formatedTitle){
        String unformated = formatedTitle.substring(1, formatedTitle.length() - 1);
        unformated = unformated.replace("\"\"", "\"");
        return unformated;
    }

    private static String formatDate(Date date){

        SimpleDateFormat dateFormat = new SimpleDateFormat("'['yyyy-MM-dd HH:mm:ss.SSS']'");
        return dateFormat.format(date);

    }

    private static Date unformatDate(String formatedDate) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("'['yyyy-MM-dd HH:mm:ss.SSS']'");
        return dateFormat.parse(formatedDate);
    }

    private static String formatInterval(int interval) {
        int days = interval / lengthOfDay;
        int hours = (interval % lengthOfDay) / lengthOfHour;
        int minutes = ((interval % lengthOfDay) % lengthOfHour) / lengthOfMinute;
        int seconds = ((interval % lengthOfDay) % lengthOfHour) % lengthOfMinute;
        String result = "";
        boolean needSpace = false;
        if (days >= 1){
            result += days + (days > 1 ? " days" : " day");
            needSpace = true;
        }
        if (hours >= 1){
            result += needSpace ? " " : "";
            result += hours + (hours > 1 ? " hours" : " hour");
            needSpace = true;
        }
        if (minutes >= 1){
            result += needSpace ? " " : "";
            result += minutes + (minutes > 1 ? " minutes" : " minute");
            needSpace = true;
        }

        if (seconds >= 1){
            result += needSpace ? " " : "";
            result += " " + seconds + (seconds > 1 ? " seconds" : " second");
            needSpace = false;
        }

        return "[" + result + "]";
    }

    private static int  unformatInterval(String interval) {

        String unformated = interval.substring(1, interval.length() - 1);
        String[] arr = unformated.split(" ");
        int result = 0;
        switch (arr.length){
            case 8:
                result += Integer.parseInt(arr[0]) * lengthOfDay;
                result += Integer.parseInt(arr[2]) * lengthOfHour;
                result += Integer.parseInt(arr[4]) * lengthOfMinute;
                result += Integer.parseInt(arr[6]);
                break;
            case 6:
                result += Integer.parseInt(arr[0]) * (arr[1].contains("day") ? lengthOfDay : lengthOfHour);
                result += Integer.parseInt(arr[2]) * (arr[3].contains("hour") ? lengthOfHour : lengthOfMinute);
                result += Integer.parseInt(arr[4]) * (arr[5].contains("minute") ? lengthOfMinute: 1);
                break;
            case 4:
                result += Integer.parseInt(arr[0]) * (arr[1].contains("day") ? lengthOfDay : arr[1].contains("hour")? lengthOfHour : lengthOfMinute);
                result += Integer.parseInt(arr[2]) * (arr[3].contains("hour") ? lengthOfHour : arr[3].contains("minute") ? lengthOfMinute: 1);
                break;
            case 2:
                if (arr[1].contains("day")) result = Integer.parseInt(arr[0]) * lengthOfDay;
                if (arr[1].contains("hour")) result = (Integer.parseInt(arr[0]) * lengthOfHour);
                if (arr[1].contains("minute")) result = Integer.parseInt(arr[0]) * lengthOfMinute;
                if (arr[1].contains("second")) result = Integer.parseInt(arr[0]);
                break;
        }

        return result;
    }

}
