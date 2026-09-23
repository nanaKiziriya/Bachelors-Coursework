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
import java.util.Objects;

// Nana Kiziriya's MyHashMap assignment... I went overboard. It's unironically a mental illness.

public class MyHashApp {
    public static void main(String[]args) {
        
        MyHashMap<String,Integer> hm = new MyHashMap<>();
        
        System.out.println("\nAdding entries to MyHashMap in this order:");
        System.out.println("hm.put(\"apple\", 17) -> "+hm.put("apple", 17));
        System.out.println("hm.put(\"boot\", 37) -> "+hm.put("boot", 37));
        System.out.println("hm.put(\"soot\", 230) -> "+hm.put("soot", 230));

        System.out.println("\nChecking MyHashMap:");
        System.out.println("hm -> "+hm);
        System.out.println("Holy moly not in insert order! Math. Crazy.");

        System.out.println("\nChecking existing keys:");
        System.out.println("hm.containsKey(\"apple\") -> " + hm.containsKey("apple"));
        System.out.println("hm.containsKey(\"boot\") -> " + hm.containsKey("boot"));
        System.out.println("hm.containsKey(\"soot\") -> " + hm.containsKey("soot"));
        System.out.println("hm.get(\"soot\") -> " + hm.get("soot"));

        System.out.println("\nChecking NON-existant keys:");
        System.out.println("hm.containsKey(\"coot\") -> " + hm.containsKey("coot"));
        System.out.println("hm.get(\"coot\") -> " + hm.get("coot"));
        
        System.out.println("\nUpdating a preexisting key (uses same method as adding cuz lazy):");
        System.out.println("hm.put(\"apple\", 1084034873) -> "+hm.put("apple", 1084034873));
        System.out.println("hm -> "+hm);

        System.out.println("\nRemoving an existing key:");
        System.out.println("hm.remove(\"boot\") -> "+hm.remove("boot"));
        System.out.println("hm -> "+hm);

        System.out.println("\nChecking the removed key:");
        System.out.println("hm.containsKey(\"boot\") -> " + hm.containsKey("boot"));
        System.out.println("hm.get(\"boot\") -> "+hm.get("boot"));

        System.out.println("\nTrying to remove \"boot\" again / remove nonexistant key:");
        System.out.println("hm.remove(\"boot\") -> "+hm.remove("boot"));
    }
}

// Hashing: equivalent Objects must return the same hash, with different Objects having hashes that (almost) never coincide.
// Potential issue from overflow if capacity*hashFactor exceeds Integer.MAX_VALUE ?
public class MyHashMap<K,V> {

    /* DATAFIELDS */
    
    // Standard HashSet defaults I believe :3
    private static final int DEFAULT_INIT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    
    private KeyValuePair<K,V>[] buckets; // I do not want automatic container growth: Do not use ArrayList, Vector, etc. for external container
    private final double loadFactor;
    // just gunna have growthFactor be *2 ... capacity grows by growthFactor when size exceeds loadFactor*capacity
    private int hashFactor; // Always < capacity; used for hash calculation; value not hardcoded; generated randomly to be coprime to container capacity: see this.generatehashFactor()
    private int size = 0; // number of entries, NOTTT the number of buckets w shit in it
    // capacity is buckets.length
    
    /* CONSTRUCTORS */
    
    public MyHashMap(){ this(DEFAULT_INIT_CAPACITY); }
    public MyHashMap(int initCapacity) { this(initCapacity, DEFAULT_LOAD_FACTOR); }
    @SuppressWarnings("unchecked") //ew
    public MyHashMap(int initCapacity, double loadFactor) {
        if (initCapacity<=0 || loadFactor<=0) {
            System.err.print("initCapacity & loadFactor must be positive");
            System.exit(1);
        }

        buckets = (KeyValuePair<K,V>[]) new KeyValuePair[initCapacity];
        this.loadFactor = loadFactor;
        updateHashFactor();
    }

    private void updateHashFactor(){
        this.hashFactor = generatehashFactor(this.buckets.length);
    }

    // adds kvpair (updates if k exists)
    // returns previous v of k
    public V put(K key, V value) {
        int index = calculateHashedIndex(key);
        
        KeyValuePair<K,V> current = buckets[index];

        while (current != null) {
            if (Objects.equals(current.key, key)) {
                V oldValue = current.value;
                current.value = value;
                return oldValue;
            }

            current = current.next;
        }

        KeyValuePair<K,V> newKeyValuePair = new KeyValuePair<>(key, value);
        newKeyValuePair.next = buckets[index];
        buckets[index] = newKeyValuePair;
        size++;

        if (size > buckets.length * loadFactor) resize();
        
        return null;
    }

    public V get(K key) {
        int index = calculateHashedIndex(key);
        KeyValuePair<K,V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) return current.value;
            current = current.next;
        }

        return null;
    }

    public boolean containsKey(K key) {
        int index = calculateHashedIndex(key);
        KeyValuePair<K,V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) return true;
            current = current.next;
        }

        return false;
    }

    public V remove(K key) {
        int index = calculateHashedIndex(key);
        KeyValuePair<K,V> current = buckets[index], previous=null;
    
        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) buckets[index] = current.next;
                else previous.next = current.next;
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }
        
        return null;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    private int calculateHashedIndex(Object key) {
        return calculateHashedIndex(key,this.hashFactor,this.buckets.length);
    }

    // Equivalent objects must return the same index
    // Easier alternative: just use hashCode()
    private static int calculateHashedIndex(Object key, int hashFactor, int capacity){
        byte bytes[] = (""+key.hashCode()).getBytes();
        int index = 0;
        for(byte b : bytes){
            index+=b; index%=capacity;
            index*=hashFactor; index%=capacity;
        }
        return index + index<0? capacity:0;
    }

    // returns random int that's coprime to newCapacity
    private static int generatehashFactor(int newCapacity){
        Random random = new Random();
        int newhashFactor = random.nextInt()%newCapacity;
        while(!coprime(newCapacity,newhashFactor)) newhashFactor = random.nextInt()%newCapacity;
        return newhashFactor%newCapacity;
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

    @SuppressWarnings("unchecked")
    private void resize() {
        KeyValuePair<K,V>[] oldBuckets = buckets;
        buckets = (KeyValuePair<K,V>[]) new KeyValuePair[oldBuckets.length * 2]; // double da capacityyy
        updateHashFactor();

        for (KeyValuePair<K,V> oldBucket : oldBuckets){
            KeyValuePair<K,V> current = oldBucket; // start at head of this bucket chain

            while(current != null) {
                // KeyValuePair<K,V> next = current.next;
                int newIndex = calculateHashedIndex(current.key);
                this.put(current.key,current.value);
                // current.next = buckets[newIndex];
                // buckets[newIndex] = current;

                current = current.next;
            }
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(KeyValuePair<K,V> bucket:buckets){
            KeyValuePair<K,V> current = bucket;
            while(current!=null){
                sb.append(current.toString());
                sb.append(", ");
            }
        }
        
        sb.replace(sb.length()-2,sb.length(),"}");
        return sb.toString();
    }

    private static class KeyValuePair<K,V> {
        private final K key; // do not change key! else wrong bucket!
        private V value;
        private KeyValuePair<K,V> next; // way better than removing from an ArrayList (O(n))

        KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
