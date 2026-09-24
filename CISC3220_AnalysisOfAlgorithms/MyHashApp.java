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
import java.util.Objects; // Objects.equals(a,b) to do .equals() with null

// Nana Kiziriya's MyHashMap assignment... I went overboard. Unironically, it's a mental illness.

public class MyHashApp {
    
    public static void main(String[] args) {
        
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
        
        System.out.println("\nUpdating/replacing a preexisting key (uses same method as adding cuz lazy):");
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

        System.out.println("\n[BONUS] Check hashmap hashcode equivalence: based on elements, not order or capacity");
        MyHashMap<String,Integer> hs1 = new MyHashMap<>(10);
        MyHashMap<String,Integer> hs2 = new MyHashMap<>(1000);
        hs1.put("1st",1);
        hs1.put("2nd",2);
        hs1.put("3rd",3);
        hs1.put("4th",4);
        
        hs2.put("3rd",3);
        hs2.put("1st",1);
        hs2.put("4th",4);
        hs2.put("2nd",2);

        System.out.println("\nhs1 capacity initialized to 10");
        System.out.println("hs1 insert order: 1,2,3,4");
        System.out.println("hs1 -> "+hs1);
        System.out.println("hs1.hashCode() -> "+hs1.hashCode());
        
        System.out.println("\nhs2 capacity initialized to 1000");
        System.out.println("hs2 insert order: 3,1,4,2");
        System.out.println("hs2 -> "+hs2);
        System.out.println("hs2.hashCode() -> "+hs2.hashCode());
        
        System.out.println("\nhs1.equals(hs2) -> "+hs1.equals(hs2));
        
    }
}



// Hashing: equivalent Objects must return the same hash, with different Objects having hashes that (almost) never coincide.
// Potential issue from overflow if capacity*hashFactor exceeds Integer.MAX_VALUE ? nvm it should still get same index each time anyways...
class MyHashMap<K,V> {

    /* DATAFIELDS */
    
    // Standard HashSet defaults I believe :3
    private static final int DEFAULT_INIT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private static final double DEFAULT_GROWTH_FACTOR = 2;
    
    // Outer container: array of <k,v> pairs; Inner container: linked nodes
    // inner container at each index needs/prefers constant removal, so don't use a basic List/Vector! Linked nodes are superior here.
    // I do not want automatic outer container growth => Do not use ArrayList, Vector, etc. for outer container!
    private KeyValuePair<K,V>[] buckets;
    
    // capacity grows by growthFactor when size exceeds loadFactor*capacity
    private final double loadFactor;
    private final double growthFactor;

    // (co)prime used for hash calculation
    // Security: value NOT hardcoded, generated randomly to be coprime to container capacity
    // Always positive and < buckets.length (mathematically redundant, computationally efficient)
    // See this.generatehashFactor()
    private int hashFactor;

    // number of entries, NOTTT the number of buckets w shit in it
    // capacity is buckets.length
    private int size = 0;

    // for this.generateHashFactor() : used at init and each resize
    private static Random random = new Random();


    
    /* CONSTRUCTORS */

    // delegation chain is pretty idc if it's efficient :D
    public MyHashMap(){ this(DEFAULT_INIT_CAPACITY); }
    public MyHashMap(int initCapacity) { this(initCapacity, DEFAULT_LOAD_FACTOR); }
    public MyHashMap(int initCapacity, double loadFactor) { this(initCapacity, loadFactor, DEFAULT_GROWTH_FACTOR); }
    @SuppressWarnings("unchecked") //ew
    public MyHashMap(int initCapacity, double loadFactor, double growthFactor) {
        if(initCapacity<1) throw new Error("initCapacity must be positive int");
        if(loadFactor<=0 || loadFactor>=1) throw new Error("loadFactor must be strictly between 0 and 1");
        if(growthFactor<=1) throw new Error("growthFactor must be strictly greater than 1");

        buckets = (KeyValuePair<K,V>[]) new KeyValuePair[initCapacity]; // unchecked :P
        this.loadFactor = loadFactor;
        this.growthFactor = growthFactor;
        updateHashFactor(); // sets this.hashFactor randomly based on capacity
    }



    /* INSTANCE METHODS (YUCKY) */
    
    // adds kvpair (updates if k exists)
    // returns previous v of k
    // parameter resizeable=false for particular case: called by resize()
    private V put(K key, V value, boolean resizeable) {
        // Case 1: key already exists
        // search for node and replace value
        // no size/capacity change
        int index = calculateHashedIndex(key);
        KeyValuePair<K,V> current = buckets[index];
        while(current != null) {
            if (Objects.equals(current.key, key)) { // if key found
                V oldValue = current.value; // to return
                current.value = value;
                return oldValue;
            }
            current = current.next;
        }

        // Case 2: novel key, append to head of bucket node chain
        KeyValuePair<K,V> newKeyValuePair = new KeyValuePair<>(key, value);
        newKeyValuePair.next = buckets[index];
        buckets[index] = newKeyValuePair;

        // incr size and check if resize needed now
        size++;
        if (resizeable && size >= buckets.length * loadFactor) resize();
        
        return null; // old value considered null when novel key
    }

    public V put(K key, V value){
        return put(key, value, true);
    }

    public V get(K key) {
        // Case 1: find key and return value
        int index = calculateHashedIndex(key);
        KeyValuePair<K,V> current = buckets[index];
        while(current != null) {
            if (Objects.equals(current.key, key)) return current.value;
            current = current.next;
        }

        // Case 2: key DNE
        return null;
    }

    public boolean containsKey(K key) {
        int index = calculateHashedIndex(key);
        KeyValuePair<K,V> current = buckets[index];
        while(current != null) {
            if(Objects.equals(current.key, key)) return true;
            current = current.next;
        }

        return false; // else not found
    }

    public V remove(K key) {
        int index = calculateHashedIndex(key);
        KeyValuePair<K,V> current = buckets[index], previous=null; // keep track of prev to close gap in linked node chain!!!
        while(current != null) {
            if (Objects.equals(current.key, key)) { // if key found, close gap in chain and return value
                // close gap in chain (2 cases: head or not head)
                if (previous == null) buckets[index] = current.next;
                else previous.next = current.next;
                // update size, return value
                size--;
                return current.value;
            }
            // not found yet? next!
            previous = current;
            current = current.next;
        }

        // not found at all
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        KeyValuePair<K,V>[] oldBuckets = buckets;

        // growthfactor strictly greater than 1, but might be a small double...
        // Round new capacity, and ensure strict growth
        int newCapacity = (int)Math.round(oldBuckets.length * this.growthFactor);
        if(newCapacity==oldBuckets.length) newCapacity++;

        // new empty hash table
        buckets = (KeyValuePair<K,V>[]) new KeyValuePair[newCapacity]; // unchecked warning
        
        // update all relevant datafields
        updateHashFactor();

        for(KeyValuePair<K,V> oldBucket : oldBuckets){ // for each index in buckets
            KeyValuePair<K,V> current = oldBucket;
            while(current != null) { // iterate through all nodes in old bucket
                // put into new bucket
                this.put(current.key,current.value,false); // "false" stops any potential recursive resize() call through put()
                current = current.next;
            }
        }
    }
    
    

    /* INSTANCE METHODS (EZ-PZ) */

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    @Override
    public int hashCode(){
        int hc = 0;
        for(KeyValuePair<K,V> bucket:buckets){
            KeyValuePair<K,V> current = bucket;
            while(current!=null){
                hc+=current.key.hashCode()+current.value.hashCode();
                current = current.next;
            }
        }
        return hc;
    }
    @Override // lazy version
    public boolean equals(Object that){
        return this.hashCode()==that.hashCode() && that instanceof MyHashMap;
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(KeyValuePair<K,V> bucket:buckets){
            KeyValuePair<K,V> current = bucket;
            while(current!=null){
                sb.append(current.toString());
                sb.append(", ");
                current = current.next;
            }
        }

        // 2 cases: closing an empty vs nonempty hashmap
        if(!this.isEmpty()) sb.replace(sb.length()-2,sb.length(),"}");
        else sb.append("}");
        
        return sb.toString();
    }


    
    /* HASHING METHODS: static helpers and instance */

    // Equivalent objects must return the same index
    // Easier alternative: just use hashCode()
    private static int calculateHashedIndex(Object key, int hashFactor, int capacity){
        if(key==null) return 0;
        
        byte bytes[] = (""+key.hashCode()).getBytes();
        int index = 0;
        for(byte b : bytes){
            index+=b; index%=capacity;
            index*=hashFactor; index%=capacity;
        }
        return index + (index<0? capacity:0);
    }
    
    // index given some key's hash (mod buckets.length)
    private int calculateHashedIndex(Object key) {
        return calculateHashedIndex(key,this.hashFactor,this.buckets.length);
    }

    // returns random int that's coprime to newCapacity
    private static int generatehashFactor(int newCapacity){
        int newhashFactor = random.nextInt()%newCapacity;
        while(!coprime(newCapacity,newhashFactor)) newhashFactor = random.nextInt()%newCapacity;
        return newhashFactor;
    }

    // called by constructor and resize()
    private void updateHashFactor(){
        this.hashFactor = generatehashFactor(this.buckets.length);
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

    

    /* HELPER CLASS */
    
    private static class KeyValuePair<K,V> {
        private final K key; // do not change key! else wrong bucket!
        private V value;
        private KeyValuePair<K,V> next; // way better than removing from an ArrayList (O(n))

        KeyValuePair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString(){
            return key+"="+value;
        }
    }
}
