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
    

public class MyHashMap<K,V>{
    
    private MyHashTable<Entry<K,V>> entries; // K,V pairs
    private MyHashTable<K> keys;   // automatic growth, constant-time storage and retrieval
    private MyHashTable<V> values; // automatic growth, constant-time storage and retrieval

    private class MyHashTable<K,V>{
        
        /* Datafields */
        
        private EntryList<K,V> entries[]; // I do not want automatic container growth: Do not use ArrayList, Vector, etc. for external container
        private int numEntries = 0; // number of entries in all ArrayLists; NOT necessarily equal to loadSize (num ArrayLists)
        private int loadSize = 0; // entries.length return CAPACITY, not number of ArrayLists in entries
        private double loadFactor;
        private int growthFactor; // capacity grows by growthFactor when loadSize exceeds loadFactor*capacity
        private int hashFactor; // Always < capacity; used for hash calculation; value not hardcoded; generated randomly to be coprime to container capacity: see this.generatehashFactor()
    
        /* Constructors */
        
        MyHashTable(){
            this(16); } // Java HashSet default
        MyHashTable(int initCapacity){
            this(initCapacity, 0.75); } // Java HashSet default
        MyHashTable(int initCapacity, double loadFactor){
            this(initCapacity, loadFactor, 2); } // Java HashSet default
        @SuppressWarnings("unchecked")
        MyHashTable(int initCapacity, double loadFactor, int growthFactor){
            this.entries = (EntryList<Entry<K,V>>[]) new EntryList<?>[initCapacity];
            this.loadFactor = loadFactor;
            this.growthFactor = growthFactor;
            this.hashFactor = generatehashFactor(initCapacity);
        }
    
        public boolean put(K key, V value){
            int hashIndex = calculateHashCodeIndex(key);
            if(entries[hashIndex]==null) entries[hashIndex] = new ArrayList<Entry<K,V>>(2);
            if(this.contains(entry)) return false; // checks entire ArrayList at proper hash index
            if(this.containsKey(entry.getKey()){
                this.remove(entry.key)entries[hashIndex].setValue()
            entries[hashIndex].add(o);
            numEntries++;
            return true;
        }
        public boolean contains(Object o){
            int hashIndex = calculateHashCodeIndex(o);
            return entries[hashIndex]!=null && entries[hashIndex].contains(o);
        }
        public boolean remove(Object o){
            int hashIndex = calculateHashCodeIndex(o);        
            if(! entries[hashIndex].contains(o)) return false; // checks entire ArrayList at proper hash index
            entries[hashIndex].remove(o);
            numEntries--;
            if(entries[hashIndex].isEmpty()) entries[hashIndex] = null;
            return true;
        }
        public int size(){ return this.numEntries; }
    
        public String toString(){
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for(ArrayList arr:this.entries) if(arr!=null) for(E entry:arr){
                sb.append(entry.toString());
                sb.append(", ");
            }
            sb.replace(sb.length()-2,sb.length(),"}");
            return sb.toString();
        }
    
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
            Random random = new Random();
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

    private class EntryList<K,V>{
        private ArrayList<Entry<K,V>> list;
        public addEntry(K, k, V v){
            if(this.containsKey(k)) list.replace(this.indexOfKey(k),new Entry(k,v));
            else list.add(new Entry(k,v));
        }
        public void removeKey(K k){
            if(containsKey(k)) list.remove(indexOfKey(k));
        }
        private boolean containsKey(K k){
            return indexOfKey(k)!=-1;
        }
        private int indexOfKey(K k){
            for(int i=0; i<list.size; i++) if(list.get(i).equals(k)) return i;
            return -1;
        }
    }
    
    private class Entry<K,V>{
        K key;
        V value;
        Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
        K getKey(){ return key; }
        V getValue(){ return value; }
        void setValue(V value){ this.value = value; }
        
    }
}
