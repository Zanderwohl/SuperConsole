package com.zanderwohl.util.structures;

public class CircularQueue<T> {

    private Object[] items;
    private int start = 0;
    private int end = 1;
    private int size = 0;
    private final int maxSize;
    private final boolean overwrite;

    /**
     * A circular queue.
     * @param size The maximum size of the queue. Allocates all at once.
     * @param overwrite If true, will overwrite oldest values if full on push. If false, will reject push.
     */
    public CircularQueue(int size, boolean overwrite){
        items = new Object[size];
        this.overwrite = overwrite;
        maxSize = size;
    }

    public void enqueue(T newItem){
        if(size == maxSize){
            if(overwrite){
                size--;
                start = next(start);
                put(newItem);
            }
            //Silently fail if overwrite is false;
        } else {
            put(newItem);
        }
    }

    private void put(T item){
        items[end] = item;
        end = next(end);
        size++;
    }

    public T peek(){
        return (T) items[previous(end)];
    }

    public T peek(int offset){
        int index = (end - offset) - 1;
        int adjustedIndex = wrap(index);
        return (T) items[adjustedIndex];
    }

    public T dequeue(){
        if(size > 0){
            T item = peek();
            items[previous(end)] = null;
            size--;
            end = previous(end);
            return item;
        } else {
            return null;
        }
    }

    public int size(){
        return size;
    }

    private int next(int n){
        int next = n + 1;
        if(next >= maxSize){
            next = 0;
        }
        return next;
    }

    private int previous(int n){
        int previous = n - 1;
        if(previous < 0){
            previous = maxSize - 1;
        }
        return previous;
    }

    private int wrap(int n){
        int wrapped = n;
        while(wrapped >= maxSize){
            wrapped -= maxSize;
        }
        while(wrapped < 0){
            wrapped += maxSize;
        }
        return wrapped;
    }

    public static void main(String[] args){
        CircularQueue<String> c = new CircularQueue(5, true);
        c.enqueue("First");
        c.enqueue("Second");
        c.enqueue("Third");
        c.enqueue("Fourth");
        c.enqueue("Fifth");
        c.enqueue("Sixth");
        c.enqueue("Seventh");
        for(int i = 0; i < 5; i++){
            System.out.println(c.dequeue());
        }
    }
}
