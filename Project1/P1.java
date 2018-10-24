/*
1. Write a program in Java which creates producer and consumer threads. The 
producer should “produce” by setting the elements of an array of integers to 
FULL. The consumer should “consume” by setting the elements of an array of 
integers to EMPTY. Make sure you properly handle the following situations:
* A producer (consumer) producing (consuming) past the end (beginning) of 
    the buffer
* A producer writing to an array element that is not EMPTY and a consumer 
    reading (i.e. setting the array element to EMPTY) from an array element that 
    is not FULL.
* Use wait() and notify() to avoid errors producing and consuming and to 
    ensure thread synchronization.
Include your program, along with output showing it run correctly for an 
interesting case.

*/
import java.nio.Buffer;
import java.util.Scanner;

class P1{
    public static void main(String[] args){
        
        BoundedBuffer<Integer> bb = new BoundedBuffer<Integer>(5);
        
        Thread p1 = new Thread(new Producer(bb));
        //Thread p2 = new Thread(new Producer(bb));
        //Thread p3 = new Thread(new Producer(bb));
        //Thread p4 = new Thread(new Producer(bb));
        //Thread p5 = new Thread(new Producer(bb));
        Thread c1 = new Thread(new Consumer(bb));
        //Thread c2 = new Thread(new Consumer(bb));
        //Thread c3 = new Thread(new Consumer(bb));
        //Thread c4 = new Thread(new Consumer(bb));
        //Thread c5 = new Thread(new Consumer(bb));
        p1.start();
        c1.start();
        //p2.start();
        //c2.start();
        //c3.start();
        //p3.start();
        //p4.start();
        //c4.start();
        //c5.start();
        //p5.start();

    }
}


class BoundedBuffer<E>{

    private int BUFFER_SIZE = 10;

    private int count; // Number of items currently in buffer
    private int in; // Points to the next empty position in the buffer
    private int out; // Points to the first full position in the buffer
    private E[] buffer;

    public BoundedBuffer(int size){
        count = 0;
        in = 0;
        out = 0;
        BUFFER_SIZE = size;
        buffer = (E[]) new Object[BUFFER_SIZE];
    }

    public void produce(E item) throws InterruptedException{
        synchronized(buffer){
            while(count == BUFFER_SIZE){
                System.out.println("No space to produce. A producer is waiting.");
                buffer.wait();// Do nothing. No free space.
            }
            // Add an item to the buffer
            buffer[in] = item;
            System.out.println(Thread.currentThread().getName() +" produced: " + buffer[in]);
            in = (in + 1) % BUFFER_SIZE;
            ++count;
            this.printme();
            buffer.notifyAll();
        } 
    }

    public E consume() throws InterruptedException{
        synchronized(buffer){
            E item;

            while (count == 0){
                System.out.println("Nothing to consume. A consumer is waiting.");
                buffer.wait();// Do nothing. Nothing to consume.
            }
            // Remove an item from the buffer
            System.out.println(Thread.currentThread().getName()+" consumed: " + buffer[out]);
            item = buffer[out];
            buffer[out] = null;
            out = (out + 1) % BUFFER_SIZE;
            --count;
            this.printme();
            buffer.notifyAll();
            return item;
        }
    }

    public void printme(){
        System.out.printf("%s", '[');
        for (int i = 0; i < this.BUFFER_SIZE; i++){
            System.out.printf(" %d ", this.buffer[i]);
        }
        System.out.printf("%s\n", ']');
    }
}

class Producer implements Runnable{
    
    BoundedBuffer bb;
    //E item;

    Producer(BoundedBuffer bb){
        //Can pass objects to produce
        this.bb = bb;
    }

    public void run(){
        int num = 0;
        while (true){
            try{
                this.bb.produce(num++);
                Thread.sleep(500);
            } catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable{
    
    BoundedBuffer bb;
    Object tmp;

    Consumer(BoundedBuffer bb){
        this.bb = bb;
    }

    public void run(){
        int tmp;
        while (true){
            try{
                this.bb.consume();
                Thread.sleep(500);
            } catch(InterruptedException ie){
                ie.printStackTrace();
            }
        }
    }
}