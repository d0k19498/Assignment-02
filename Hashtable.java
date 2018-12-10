/* --------------------------------
   Hashtable.java
   Purpose: An "open-hashing" implementation of a Hashtable using an ArrayList
   of LinkedList elements. 
   For CS 245 - David Guy Brizan
   @jxu74 Mina Xu
   --------------------------------
*/

import java.util.ArrayList;

/* --------------------------------
   HashNode contructor 
   - variables - 
   String key: Designates a node in linked list 
   Value: Data corresponding to key
   next: HashNode - pointer to next node
   --------------------------------
*/
class HashNode  {
	public String key;
	public String value;
	public HashNode next;

	public HashNode (String key, String value) {
		this.key = key;
		this.value = value;
	}
}


class Hashtable  {
	/* ArrayList of Linked List nodes */
	private ArrayList <HashNode> buckets = new ArrayList <HashNode> (); 
	/* Size of the array list */
	private int numBuckets; 
	/* Number of items in the list */
	private int size; 

    /* --------------------------------
      HashTable contructor 
      - variables -
      int numBuckets: Number of items in ArrayList 
      int size: Number of items currently in ArrayList of LinkedLists
      --------------------------------
    */
	public Hashtable () {
		numBuckets = 300000;
		size = 0;
		for(int i = 0; i < numBuckets; i++) { /* Initialize all as null */
			buckets.add(null);
		}
	}

	/* --------------------------------
	   containsKey(String key): Returns true if a key exists as that given 
	   in the parameter, otherwise returns null
	   - variables -
       String find: The value we are trying to get of parameter key
       --------------------------------
    */	

	public boolean containsKey (String key) { 
		String find = get(key); /* Get value of key */
		return (find != null); /* Return true if value is not null */
	}

	/* 
	 --------------------------------
	 int findIndex (String key): Returns index of parameter key
	 - variables - 
     int code: The absolute value of the code returned by hash function hashCode()
     --------------------------------
    */

	public int findIndex (String key) { 
		int code = key.hashCode(); /* Get key code using hash function */
		code %= numBuckets;
		return Math.abs(code);
	}

	/* 
	 --------------------------------
	 String get (String key): Returns the value associated with the parameter 
	 key, returns null if not found
	 - variables -
     int headIndex: Index of the head of the LinkedList
     HashNode node: The current node which we use to traverse the LinkedList
     --------------------------------
    */

	public String get (String key) { 
		int headIndex = findIndex(key); /* Get bucket index = index in ArrayList  */
		/* Node starts at bucket index = head of LL of a specific key */
		HashNode node = buckets.get(headIndex); 
		while(node != null) {
			if(node.key.equals(key)) { /* If current node equals key to be returned */
				return node.value;
			}
			node = node.next; /* Otherwise continue traversing */
		}
		return null; /* If not found in list */
	}

	/* 
	 --------------------------------
	 void put (String key, String value): Adds the key/value pair into hashtable 
	 - variables -
     int headIndex: Index of the head of the LinkedList
     HashNode node: Current node which we use to traverse the LinkedList
     HashNode newNode: A new node to be input if the key is not in the list
     --------------------------------
    */

	public void put (String key, String value) { 
		int headIndex = findIndex(key); 
		HashNode node = buckets.get(headIndex); /* Start at head of LL */
		if(!containsKey(key)) { /* If not in HT, add it */
			if(size >= numBuckets) {
				doubleArr();
			}
			HashNode newNode = new HashNode (key, value);
			newNode.next = node; /* Add new node at head */
			buckets.set(headIndex, newNode); /* Update head index with new node */
		} 

		while(node != null) { /* If in HT, search for it, then update value */
			if(node.key.equals(key)) { 
				node.value = value;
				return;
			}
			node = node.next;
		}

		size++;
	}


	/* 
	 --------------------------------
	 String remove(String key): removes a key/value from the list, returning the value. 
	 If not found, throws an exception.
	 - variables -
     int headIndex: Index of the head of the LinkedList
     HashNode node: Current node which we use to traverse the LinkedList
     HashNode prev: Previous node used to skip over current node when removing
     --------------------------------
    */

	public String remove (String key) {
		if(!containsKey(key)) {
			throw new RuntimeException("Could not find key in Hashtable.");
		}
		/* Key is in HT, search for it, remove it */
		int headIndex = findIndex(key);
		HashNode node = buckets.get(headIndex); /* Start at node = head of LL */
		HashNode prev = null;

		while(node != null) {
			if(node.key.equals(key)) {
				break; /* Exit, remove node and return node value */
			}
			/* Else keep traversing */
			prev = node;
			node = node.next;
		}
		
		if(prev != null) { /* Key in list - remove it by skipping over */
			prev.next = node.next;
 		} else {
 			buckets.set(headIndex, node.next); /* prev = null, update bucket index with next node */
 		}

 		size--; 
 		return node.value;
	}

	/* 
	 --------------------------------
	 void doubleArr(): If the load factor surpasses the threshold, the arraylist is doubled.
	 The hashtable re-hashed to be put back in to the new array.
	 - variables -
     ArrayList <HashNode> copyBuckets: A copy of the arraylist to retain memory reference
     HashNode n: Used to traverse HashNodes in LinkedList of each bucket in the ArrayList 
     --------------------------------
    */

	public void doubleArr() { 	
		numBuckets = 2 * numBuckets; 
		/* Make a copy of array list */
        ArrayList<HashNode> copyBuckets = buckets; 
        /* Keep ref - set to ArrayList of double size */
        buckets = new ArrayList<>(numBuckets); 
        for (int i = 0; i < numBuckets; i++) {
            buckets.add(null); 
        }
        /* Copy buckets to list of doubled size */
		for (HashNode n : copyBuckets) { 
            while (n != null) { 
                put(n.key, n.value); 
                n = n.next; 
            } 
    	} 

		size = 0; /* Reset size */
  	}

} /* Close Hashtable */