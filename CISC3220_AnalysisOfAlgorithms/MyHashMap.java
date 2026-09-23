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

public class MyHashApp{
    public static void main(String[] args){
        // for testing
        MyHashMap = new MyHashMap();
    }
}

// Hashing: equivalent Objects must return the same hash, with different Objects having hashes that (almost) never coincide.
class MyHashSet<E>{
    private E entries[]; // Do NOT want automatic container growth: Do not use ArrayList, Vector, etc.
    private int hashFactor; // Always <capacity; used for hash calculation; value not hardcoded; generated randomly to be coprime to container capacity: see this.generateHashFactor()
    private double loadFactor, growthFactor; // capacity grows by growthFactor when size exceeds loadFactor*capacity
    private static Random random = new Random(); // for generateHashFactor()
    
    MyHashSet(){
        this(16); }
    MyHashSet(int initCapacity){
        this(initCapacity, 0.75); }
    MyHashSet(int initCapacity, double loadFactor){
        this(initCapacity, loadFactor, 1.5); }
    MyHashSet(int initCapacity, double loadFactor, double growthFactor){
        this.values = new V[initCapacity];
        this.loadFactor = loadFactor;
        this.growthFactor = growthFactor;
        this.hashFactor = generateHashFactor(initCapacity);
    }

    public boolean add(Object o)
    public boolean contains(Object o)
    public boolean remove(Object o)
    public int size()

    /* Private Helper Methods */
    
    // ASSUMES 2 objects equivalent iff their toString() are equivalent
    private static int calcHashCode(Object o,int hashFactor, int capacity){
        bytes[] bytes = o.toString().getBytes();
        int hc = 0;
        for(byte b : bytes){
            hc+=b; hc%=capacity;
            hc*=hashFactor; hc%=capacity;
        }
        return hc;
    }

    // returns random int coprime to newCapacity
    private static int generateHashFactor(int newCapacity){
        int newHashFactor = random.nextInt()%newCapacity;
        while(!coprime(newCapacity,newHashFactor)) newHashFactor = random.nextInt()%newCapacity;
        return newHashFactor;
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
        this.hashFactor = newHashFactor(initCapacity);
    }
}
