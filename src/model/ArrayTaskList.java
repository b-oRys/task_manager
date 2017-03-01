package model;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayTaskList extends TaskList implements Cloneable {

    private Task[] array;
    private int size;

    public ArrayTaskList() {
        array = new Task[ 10 ];
        size = 0;
    }

    public ArrayTaskList( Task task ) {

        if (task == null) throw new IllegalArgumentException("Список задач має містити задачі, " +
                "тому у нього не можна додавати порожні посилання  [task = '" + task + "']");

        array = new Task[ 10 ];
        array[ 0 ] = task;
        size++;
    }

    @Override
    public void add( Task task ){

        if ( task == null ) throw new IllegalArgumentException( "Список задач має містити задачі, " +
                "тому у нього не можна додавати порожні посилання  [task = '" + task + "']");

        if ( size == array.length ){
            int newLength = ( int ) ( 1.5 * array.length );
            array = Arrays.copyOf( array, newLength );
        }

        array[ size ] = task;
        size++;

    }

    @Override
    public boolean remove(Task task){

        if ( task == null ) throw new IllegalArgumentException( "Список задач має містити задачі, " +
                "тому у нього не можна додавати порожні посилання  [task = '" + task + "']");

        boolean isFind = false;

        for (int i = 0; i < this.size; i++){

            if (array[i].equals(task)) {
                isFind = true;
                int numberOfElementsToBeCopied = this.size - 1 - i;

                if (numberOfElementsToBeCopied > 0){
                    System.arraycopy(this.array, i + 1, this.array, i, numberOfElementsToBeCopied);
                }

                array[--size] = null;
                break;
            }
        }
        return isFind;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Task getTask( int index ){

        if (index >= size || index < 0) throw new IndexOutOfBoundsException( "index >= size() || index < 0 [size = '" + size() + "' index = '"+ index + "']" );

        return  array[index];

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArrayTaskList list = (ArrayTaskList) o;

        if (size != list.size) return false;

        //for (int i = 0; i < this.size; i++){
        //    if (!this.array[i].equals(list.array[i])) return false;

        // }

        return true;
    }

    @Override
    public int hashCode() {
        int result = size;

        for (Task task : this){
            result = 3 * result + task.hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "[" + "size=" + size;
        for (Task task : this){
            result = result + (", " + task.toString());
        }
        result += ']';
        return result;
    }

    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList list = (ArrayTaskList) super.clone();
        list.size = 0;
        list.array = new Task[ 10 ];

        for ( int i = 0; i < size; i++){
            list.add(this.getTask(i).clone());

        }

        //       for (Task task : this){
        //           list.add(task.clone());
        //       }

        return list;
    }

    @Override
    public Iterator<Task> iterator() {
        return new ArrayTaskListIterator();
    }

    private class ArrayTaskListIterator implements Iterator<Task>{

        private int index;

        ArrayTaskListIterator(){
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Task next() {
            return  array[index++];
        }

        @Override
        public void remove() {

            if (index == 0) throw new IllegalStateException("can't call remove() before next()");

            int removeIndex = index - 1;

            for ( int i = removeIndex + 1; i < array.length; i++ ){
                if ( array [ i ] == null ) {
                    array [removeIndex] = null;
                    size--;
                    index--;
                    break;
                }

                array[removeIndex] = array[ i ];

                removeIndex ++;
            }


        }


    }
}
