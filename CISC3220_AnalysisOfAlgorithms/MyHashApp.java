/*  TASK:
    Create your own HashMap implementation. Do not use a built-in HashMap or similar map structure.
    
    Requirements:
    
        Store key-value pairs.
        Use an array of "buckets" - if you're doing this in Java, the buckets will probably be easiest as Lists.
        Use a hash function and the mod operation to determine which bucket a key belongs in.
        Handle collisions by allowing multiple entries in the same bucket.
        Implement methods to:
            add or update a key-value pair
            remove a key-value pair
            check whether a key exists
        Print enough output to show that each operation works correctly.
*/

import java.util.Random;
import java.util.ArrayList;

public class MyHashApp{
    public static void main(String[] args){
        // for testing
        MyHashMap hm = new MyHashMap();
    }
}

// Hashing: equivalent Objects must return the same hash, with different Objects having hashes that (almost) never coincide.
// Potential issue from overflow: if capacity*hashFactor exceeds Integer.MAX_VALUE
class MyHashSet<E>{
    
    /* Datafields */
    
    private ArrayList<E> entries[]; // I do not want automatic container growth: Do not use ArrayList, Vector, etc. for external container
    private int numEntries = 0; // number of entries in all ArrayLists; NOT necessarily equal to loadSize (num ArrayLists)
    private int loadSize = 0; // entries.length return CAPACITY, not number of ArrayLists in entries
    private double loadFactor;
    private int growthFactor; // capacity grows by growthFactor when loadSize exceeds loadFactor*capacity
    
    private int hashFactor; // Always < capacity; used for hash calculation; value not hardcoded; generated randomly to be coprime to container capacity: see this.generatehashFactor()
    private static Random random = new Random(); // for generatehashFactor()

    /* Constructors */
    
    MyHashSet(){
        this(16); } // Java HashSet default
    MyHashSet(int initCapacity){
        this(initCapacity, 0.75); } // Java HashSet default
    MyHashSet(int initCapacity, double loadFactor){
        this(initCapacity, loadFactor, 2); } // Java HashSet default
    @SuppressWarnings("unchecked")
    MyHashSet(int initCapacity, double loadFactor, int growthFactor){
        this.entries = (ArrayList<E>[]) new ArrayList<?>[initCapacity];
        this.loadFactor = loadFactor;
        this.growthFactor = growthFactor;
        this.hashFactor = generatehashFactor(initCapacity);
    }

    public boolean add(E o){
        int hashIndex = calculateHashCodeIndex(o);        
        if(entries[hashIndex].contains(o)) return false; // checks entire ArrayList at proper hash index
        entries[hashIndex].add(o);
        numEntries++;
        return true;
    }
    public boolean contains(Object o){
        int hashIndex = calculateHashCodeIndex(o);
        return entries[hashIndex].contains(o);
    }
    public boolean remove(Object o){
        int hashIndex = calculateHashCodeIndex(o);        
        if(! entries[hashIndex].contains(o)) return false; // checks entire ArrayList at proper hash index
        entries[hashIndex].remove(o);
        numEntries--;
        return true;
    }
    public int size(){ return this.numEntries; }

    /* Private Methods */
    
    private int calculateHashCodeIndex(Object o){ return calculateHashCodeIndex(0,this.hashFactor,this.entries.length); }
    
    // Equivalent objects must return the same index
    // Easier alternative: just use hashCode()
    private static int calculateHashCodeIndex(Object o,int hashFactor, int capacity){
        byte bytes[] = (""+o.hashCode()).getBytes();
        int hci = 0;
        for(byte b : bytes){
            hci+=b; hci%=capacity;
            hci*=hashFactor; hci%=capacity;
        }
        return hci;
    }

    // returns random int coprime to newCapacity
    private static int generatehashFactor(int newCapacity){
        int newhashFactor = random.nextInt()%newCapacity;
        while(!coprime(newCapacity,newhashFactor)) newhashFactor = random.nextInt()%newCapacity;
        return newhashFactor;
    }

    // uses Euclidean Algorithm
    private static boolean coprime(int M, int m){
        if(M==0||m==0) return false; // all ints are factors of 0
        if(M<0) M*=-1;
        if(m<0) m*=-1;
        if(M<m){ int hold=M; M=m; m=hold; }
        
        while(M%m>0){ int hold = M%m; M=m; m=hold; } // M>m
        return m==1;
    }
}


//INC
class MyHashMap<K,V> {

    
    private MyHashSet<K> keys;   // automatic growth, constant-time storage and retrieval
    private MyHashSet<V> values; // automatic growth, constant-time storage and retrieval
    

    // Similar constructors as Java HashMap
    // Same default load/growth factors as ArrayList
    MyHashMap(){
        this(16); }
    MyHashMap(int initCapacity){
        this(initCapacity, 0.75); }
    MyHashMap(int initCapacity, double loadFactor){
        this(initCapacity, loadFactor, 1.5); }
    MyHashMap(int initCapacity, double loadFactor, double growthFactor){
        this.values = new V[initCapacity];
        this.loadFactor = loadFactor;
        this.growthFactor = growthFactor;
        this.hashFactor = newhashFactor(initCapacity);
    }
}
