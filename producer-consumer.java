import java.util.ArrayList;

public class ProducerConsumer {

    public static void main(String[] args) {
        
        ArrayList<Integer> sharedList = new ArrayList<Integer>();
        
        Thread t1 = new Thread(new Producer(sharedList));
        Thread t2 = new Thread(new Consumer(sharedList));
        
        t1.start();
        t2.start();
        
    }
    
}

class Producer implements Runnable {

    ArrayList<Integer> sharedList = null;
    final int MAX_BUFFER = 5;
    int i = 0;
    
    public Producer(ArrayList<Integer> sharedList) {
        this.sharedList = sharedList;
    }
    
    public void produce(int i) throws InterruptedException {
    
        synchronized(sharedList) {
            
            while(sharedList.size()==MAX_BUFFER) {
                System.out.println("Buffer is full. Waiting for consumer to consume...");
                sharedList.wait();
            }
            
            sharedList.add(i);
            System.out.println("Producer produced item " + i);
            sharedList.notify();
            Thread.sleep(100);
        
        }
    
    }
    
    public void run() {
        
        int k = 0;
        while(k < 10) {
            try {
                produce(i++);
                k++;
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        
    }
    
}

class Consumer implements Runnable {

    ArrayList<Integer> sharedList = null;
    
    public Consumer(ArrayList<Integer> sharedList) {
        this.sharedList = sharedList;
    }
    
    public void consume() throws InterruptedException {
    
        synchronized(sharedList) {
            
            while(sharedList.isEmpty()) {
                System.out.println("Buffer is empty. Waiting for Producer to produce...");
                sharedList.wait();
            }
            
            System.out.println("Consumer consumed item " + sharedList.remove(0));
            sharedList.notify();
            Thread.sleep(1000);
        
        }
    
    }
    
    public void run() {
        
        int k = 0;
        while(k<10) {
            try {
                consume();
                k++;
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        
    }
    
}
