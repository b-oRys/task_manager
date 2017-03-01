package model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedTaskList extends TaskList implements Cloneable{

    private int size;


    private LinkedTaskList.Node first;
    private LinkedTaskList.Node last;

    public LinkedTaskList() {
        super();
        size = 0;
    }

    public LinkedTaskList(Task task) {
        this();
        if (task == null) throw new IllegalArgumentException("Список задач має містити задачі, " +
                "тому у нього не можна додавати порожні посилання  [task = '" + task + "']");
        this.add(task);
    }

    @Override
    public void add(Task task) {

        if (task == null) throw new IllegalArgumentException("Список задач має містити задачі, " +
                "тому у нього не можна додавати порожні посилання  [task = '" + task + "']");

        LinkedTaskList.Node previousForNewNode = this.last;
        LinkedTaskList.Node node = new LinkedTaskList.Node(task, previousForNewNode, null);
        this.last = node;
        if (previousForNewNode==null){
            this.first = node;
        }else {
            previousForNewNode.next = node;
        }

        size++;

    }

    @Override
    public boolean remove(Task task) {

        if ( task == null ) throw new IllegalArgumentException( "Список задач має містити задачі, " +
                "тому у нього не можна додавати порожні посилання  [task = '" + task + "']");

        LinkedTaskList.Node currentNode;

        for (currentNode = this.first; currentNode != null; currentNode = currentNode.next){
            if (currentNode.item.equals(task)){

                LinkedTaskList.Node delNodePrev = currentNode.prev;
                LinkedTaskList.Node delNodeNext = currentNode.next;

                if (delNodePrev == null){
                    this.first = delNodeNext;
                }else {
                    delNodePrev.next = delNodeNext;
                    currentNode.prev = null;
                }

                if (delNodeNext == null){
                    this.last = delNodePrev;
                }else {
                    delNodeNext.prev = delNodePrev;
                    currentNode.next = null;
                }

                currentNode.item = null;
                size--;
                return true;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }


    @Override
    public Task getTask(int index) {
        if (index >= size || index < 0) throw new IndexOutOfBoundsException( "index >= size() || index < 0 [size = " + size() + " index = '"+ index + "]" );

        Node targetNode = null;

        if (index < size() >> 1) {
            targetNode = this.first;

            for (int i = 0; i < index; i++){
                targetNode = targetNode.next;
            }
        }else {
            targetNode = this.last;

            for (int i = size() - 1; i > index; i--){
                targetNode = targetNode.prev;
            }
        }

        return targetNode.item;
    }

    @Override
    public Iterator<Task> iterator() {
        return new LinkedTaskListIterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinkedTaskList tasks = (LinkedTaskList) o;

        if (size != tasks.size) return false;

        LinkedTaskList.Node tasksNode = tasks.first;
        for (LinkedTaskList.Node thisNode = this.first; thisNode != null; thisNode = thisNode.next){
            if (thisNode != null ? !thisNode.item.equals(tasksNode.item) : tasksNode != null) return false;
            tasksNode = tasksNode.next;
        }
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
    public TaskList clone() throws CloneNotSupportedException {
        LinkedTaskList list = (LinkedTaskList) super.clone();
        list.size = 0;
        list.first = list.last = null;

        for (LinkedTaskList.Node node = this.first; node != null; node = node.next){
            list.add(node.item.clone());
        }

        return list;
    }

    private class LinkedTaskListIterator implements Iterator<Task>{
        LinkedTaskList.Node current;

        public LinkedTaskListIterator() {
            current = first;
        }

        @Override
        public boolean hasNext() {
            if (current == null) return false;
            return current != null;
        }

        @Override
        public Task next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Task currentItem = current.item;
            current = current.next;
            return currentItem;
        }

        @Override
        public void remove() {

            if ( current == first ) throw new IllegalStateException("can't call remove() before next()");

            LinkedTaskList.Node removeNode = current.prev;

            LinkedTaskList.Node delNodePrev = removeNode.prev;
            LinkedTaskList.Node delNodeNext = removeNode.next;

            if (delNodePrev == null){
                first = delNodeNext;
            }else {
                delNodePrev.next = delNodeNext;
                removeNode.prev = null;
            }

            if (delNodeNext == null){
                last = delNodePrev;
            }else {
                delNodeNext.prev = delNodePrev;
                removeNode.next = null;
            }

            removeNode.item = null;
            size--;
        }
    }

    private static class Node{
        Task item;
        LinkedTaskList.Node prev;
        LinkedTaskList.Node next;

        public Node(Task task, Node prev, Node next) {
            this.item = task;
            this.prev = prev;
            this.next = next;
        }
    }


}
