import java.util.ArrayList;

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
	private ArrayList <HashNode> buckets = new ArrayList <HashNode> (); /* ArrayList of Linked List nodes */
	private int numBuckets; /* Size of the array list*/
	private int size; /* Number of items in the list */

	public Hashtable () {
		numBuckets = 300000;
		size = 0;
		for(int i = 0; i < numBuckets; i++) { /* Initialize all as null */
			buckets.add(null);
		}
	}

	public boolean containsKey (String key) { /* Returns true if a key exists as that given in arg*/
		String find = get(key);
		return (find != null);
	}

	public int findIndex (String key) { /* Returns index of key */
		int code = key.hashCode(); /* Get key code */
		code %= numBuckets;
		return Math.abs(code);
	}

	public String get (String key) { /* Returns value associated with key in arg */
		int headIndex = findIndex(key); /* Get bucket index = index in ArrayList  */
		HashNode node = buckets.get(headIndex); /* Node starts at bucket index = head of LL of a specific key */
		while(node != null) {
			if(node.key.equals(key)) { /* If current node equals key to be returned */
				return node.value;
			}
			node = node.next; /* Otherwise continue traversing */
		}
		return null; /* If not found in list */
	}

	public void put (String key, String value) { /* Add key/value pair into hashtable */
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

	public void doubleArr() { /* If load factor surpasses threshold, double AL size */	
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