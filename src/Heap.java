
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;

/** An instance is a max-heap or a min-heap of distinct values of type E <br>
 * with priorities of type double. */
public class Heap<E> {

	

	/** Class Invariant: <br>
	 * 1. b[0..size-1] represents a complete binary tree. <br>
	 * b[0] is the root. <br>
	 * For each k, b[2k+1] and b[2k+2] are the left and right children of b[k]. <br>
	 * If k != 0, b[(k-1)/2] (using integer division) is the parent of b[k].
	 *
	 * 2. For k in 0..size-1, b[k] contains a value and its priority.<br>
	 * The values in b[size..] may or may not be null. DO NOT RELY ON THEM BEING NULL. <br>
	 * NEVER TEST WHETHER ONE OF THESE VALUES IS NULL OR IS NOT NULL.
	 *
	 * 3. The values in b[0..size-1] are all different.
	 *
	 * 4. For k in 1..size-1, <br>
	 * .. if isMinHeap, (b[k]'s parent's priority) <= (b[k]'s priority),<br>
	 * .. if !isMinHeap, (b[k]'s parent's priority) >= (b[k]'s priority).<br>
	 * <br>
	 * .. Examples: min-heap ... max-heap <br>
	 * ................ 2 ........ 5 <br>
	 * ............... / \ ...... / \ <br>
	 * .............. 5 . 3 .... 2 . 3 <br>
	 *
	 * ===================================================================
	 *
	 * map and the tree are in sync, meaning:
	 *
	 * 5. The keys of map are the values in b[0..size-1]. <br>
	 * This implies that size = map.size().
	 *
	 * 6. if value v is in b[k], then map.get(v) returns k. */
	protected final boolean isMinHeap;
	protected VP[] b;
	protected int size;
	protected HashMap<E, Integer> map; // "map" for dictionary

	/** Constructor: an empty min-heap with capacity 10. */
	public Heap() {
		isMinHeap= true;
		b= createVPArray(10);
		map= new HashMap<>();
	}

	/** Constructor: an empty heap with capacity 10. <br>
	 * It is a min-heap if isMin is true, a max-heap if isMin is false. */
	public Heap(boolean isMin) {
		isMinHeap= isMin;
		b= createVPArray(10);
		map= new HashMap<>();
	}

	/** A VP object houses a value and a priority. */
	class VP {
		E val;             // The value
		double priority;   // The priority

		/** An instance with value v and priority p. */
		VP(E v, double p) {
			val= v;
			priority= p;
		}

		/** Return a representation of this VP object. */
		@Override
		public String toString() {
			return "(" + val + ", " + priority + ")";
		}
	}

	/** Add v with priority p to the heap. <br>
	 * Throw an illegalArgumentException if v is already in the heap. <br>
	 * The expected time is logarithmic and the <br>
	 * worst-case time is linear in the size of the heap. */
	public void add(E v, double p) throws IllegalArgumentException {
		
		if (map.containsKey(v)) throw new IllegalArgumentException();
		ensureSpace();
		VP hello= new VP(v, p);
		b[size]= hello;
		map.put(v, size);
		size= size + 1;
		bubbleUp(size - 1);

	}

	/** If size = length of b, double the length of array b. <br>
	 * The worst-case time is proportional to the length of b. */
	protected void ensureSpace() {
		
		if (size == b.length) {
			VP[] bprime= Arrays.copyOf(b, size * 2);
			b= bprime;
		}

	}

	/** Return the size of this heap. <br>
	 * This operation takes constant time. */
	public int size() { // Do not change this method
		return size;
	}

	/** Swap b[i] and b[j]. <br>
	 * Precondition: 0 <= i < heap-size, 0 <= j < heap-size. */
	void swap(int i, int j) {
		assert 0 <= i && i < size && 0 <= j && j < size;
	
		VP temp= b[i];
		E valtemp= b[i].val;
		E val2temp= b[j].val;
		b[i]= b[j];
		b[j]= temp;
		map.put(val2temp, i); // inserting correct value in map
		map.put(valtemp, j);

	}

	/** If a value with priority p1 should be above a value with priority p2 in the heap, return 1. If
	 * priority p1 and priority p2 are the same, return 0. <br>
	 * If a value with priority p1 should be below a value with priority p2 in the heap, return -1. This
	 * is based on what kind of a heap this is, <br>
	 * ... E.g. a min-heap, the value with the smallest priority is in the root. <br>
	 * ... E.g. a max-heap, the value with the largest priority is in the root. */
	public int compareTo(double p1, double p2) {
		if (p1 == p2) return 0;
		if (isMinHeap) { return p1 < p2 ? 1 : -1; }
		return p1 > p2 ? 1 : -1;
	}

	/** If b[h] should be above b[k] in the heap, return 1.<br>
	 * If b[h]'s priority and b[k]'s priority are the same, return 0. <br>
	 * If b[h] should be below b[k] in the heap, return -1. <br>
	 * This is based on what kind of a heap this is, <br>
	 * ... E.g. a max-heap, the value with the largest priority is in the root. <br>
	 * ... E.g. a min-heap, the value with the smallest priority is in the root. */
	public int compareTo(int h, int k) {
		return compareTo(b[h].priority, b[k].priority);
	}

	/** Bubble b[k] up the heap to its right place. <br>
	 * Precondition: 0 <= k < size and <br>
	 * The class invariant is true, except perhaps that b[k] belongs above its <br>
	 * parent (if k > 0) in the heap, not below it. */
	void bubbleUp(int k) {
		
		assert 0 <= k && k < size;

		int parent= (k - 1) / 2;
		while (compareTo(k, parent) > 0) {
			swap(parent, k);
			k= parent;
			parent= (k - 1) / 2; // update the parent of k and iterate
		}

	}

	/** If this is a max-heap, return the heap value with highest priority. <br>
	 * If this is a min-heap, return the heap value with lowest priority. <br>
	 * Do not change the heap. <br>
	 * Throw a NoSuchElementException if the heap is empty.<br>
	 * This operation takes constant time. */
	public E peek() {
		
		if (size == 0) throw new NoSuchElementException("No elements present");
		else return b[0].val;
	}

	/** If k is not in 0..size-1, return. <br>
	 * OTHERWISE:<br>
	 * Bubble b[k] down in heap until it finds the right place. <br>
	 * If there is a choice to bubble down to both the left and right children<br>
	 * (because their priorities are equal), choose the right child. <br>
	 * Precondition: 0 <= k < size and class invariant is true except that <br>
	 * perhaps b[k] belongs below one or both of its children. */
	void bubbleDown(int k) {
		
		if (k < 0 || k > size - 1) return;

		int finalchild= childcalc(k);

		while (compareTo(finalchild, k) > 0 && finalchild != k) {
			swap(k, finalchild);
			k= finalchild;
			finalchild= childcalc(k); // updating the value of final child

		}
	}

	private int childcalc(int k) { // helper function for calculating the next value of child
		int childl= 2 * k + 1;
		int childr= 2 * k + 2;
		if (childl > size - 1) return k;
		else if (childl == size - 1) return childl;
		else {
			if (compareTo(childl, childr) == 0) return childr;

			else if (compareTo(childl, childr) > 0) return childl;
			else return childr;
		}

	}

	/** If this is a max-heap, remove and return the heap value with highest priority.<br>
	 * If this is a min-heap, remove and return heap value with lowest priority. <br>
	 * Throw a NoSuchElementException if the heap is empty.<br>
	 * The expected time is logarithmic and the worst-case time is linear in the <br>
	 * size of the heap. . */
	public E poll() {
		
		if (size == 0) throw new NoSuchElementException(); // checking if heap is not empty
		E top= b[0].val;
		swap(size - 1, 0);// when swapped the last element is now at the top
		size= size - 1;// doing this removes the element initially on top
		if (size > 0) bubbleDown(0); // bubble down the element initially in the last.
		map.remove(top);// updating map
		return top;

		// return null;
	}

	/** Change the priority of value v to p. <br>
	 * Throw an IllegalArgumentException if v is not in the heap.<br>
	 * The expected time is logarithmic and the worst-case time is linear <br>
	 * in the size of the heap. */
	public void changePriority(E v, double p) {
		
		if (!map.containsKey(v)) throw new IllegalArgumentException("v not in heap");

		int initprior= map.get(v);
		b[initprior].priority= p;
		bubbleUp(initprior);
		bubbleDown(initprior);
	}

	
	VP[] createVPArray(int n) {
		return (VP[]) Array.newInstance(VP.class, n);
	}
}
