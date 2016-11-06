public class HeapPriorityQueue<K extends Comparable<K>,V> implements PriorityQueue<K ,V> {

    private Entry<K,V>[] storage; //The Heap itself in array form
    private int tail;        //Index of last element in the heap

    /**
    * Default constructor
    */
    public HeapPriorityQueue(){
        this(100);
    }

    /**
    * HeapPriorityQueue constructor with max storage of size elements
    */
    public HeapPriorityQueue(int size){
        storage = new Entry[size];
        tail = -1;
    }

    public int size(){
        return tail +1;
    }

    public boolean isEmpty(){
        return tail < 0;
    }

    public Entry<K,V> insert(K key, V value) throws IllegalArgumentException{
        if(tail == storage.length -1) throw new IllegalArgumentException("Heap Overflow");
        Entry<K,V> e = new Entry<>(key,value);
        storage[++tail] = e;
        upHeap(tail);
        return e;
    }

    public Entry<K,V> min(){
        if(isEmpty()) return null;
        return storage[0];
    }

    public Entry<K,V> removeMin(){
        if(isEmpty()) return null;
        Entry<K,V> ret = storage[0];
        if(tail == 0){
            tail = -1;
            storage[0] = null;
            return ret;
        }
        storage[0] = storage[tail];
        storage[tail--] = null;
        downHeap(0);
        return ret;

    }


    private void upHeap(int location){
         if(location == 0) return;
         int parent = parent(location);
         if(storage[parent].key.compareTo(storage[location].key) > 0){
             swap(location,parent);
             upHeap(parent);
         }
    }

    private void downHeap(int location){
         int left = (location*2) +1;
         int right = (location*2) +2;

         //Both children null or out of bound
         if(left > tail) return;
         //left in right out;
         if(left == tail){
              if(storage[location].key.compareTo(storage[left].key) > 0){
                  swap(location,left);
              }
              return;
         }
         int toSwap= (storage[left].key.compareTo(storage[right].key) < 0)? left:right;
         if(storage[location].key.compareTo(storage[toSwap].key) > 0){
             swap(location,toSwap);
             downHeap(toSwap);
         }
    }

    private int parent(int location){
        return (location-1)/2;
    }

   
    private void swap(int location1, int location2){
        Entry<K,V> temp = storage[location1];
        storage[location1] = storage[location2];
        storage[location2] = temp;
    }

}
