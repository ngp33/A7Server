package util;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RingBuffer<T> implements BlockingQueue<T> {
	T [] t_array;
	/**Invariant, head is less than array length*/
	int head;
	/**invariant: tail is less than array length*/
	int tail;
	/**Invariant: Represents the index of the next item that would be returned by a next call*/
	int iteratorplace;
	
	public RingBuffer(int capacity) {
		t_array = (T[]) new Object[capacity];
		head = 0;
		tail = 0;
	}

	@Override
	public T remove() {
		synchronized(this) {
			T returned = peek();
			head = head + 1 == t_array.length ? 0 : head + 1;
			this.notifyAll();
			return returned;
		}
	}

	@Override
	public T poll() {
		return remove();
	}

	@Override
	public T element() {
		return peek();
	}

	@Override
	public T peek() {
		synchronized (this) {
			while(size() == 0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}	
		return t_array[head];
		}
	}

	@Override
	public int size() {
		synchronized (this) {
			return tail - head < 0 ? (t_array.length - head + 1) + tail : tail - head;
		}
	}

	@Override
	public boolean isEmpty() {
		synchronized (this) {
			return size() == 0;
		}
	}

	@Override
	public Iterator<T> iterator() {
		synchronized (this) {
			iteratorplace = head;
			return (Iterator<T>) this;
		}
	}

	@Override
	public Object[] toArray() {
		synchronized (this) {
			return toArray(new Object [2]);
		}
	}

	@Override
	public <T> T[] toArray(T[] a) {
		synchronized (this) {
			T [] returned = (T[]) new Object[this.size()];
			if (tail >= head) {
				for (int p = head; p < tail; p++) {
					returned[p - head] = (T) t_array[p];
				}
			} else {
				for (int p = head; p < t_array.length; p++) {
					returned[p - head] = (T) t_array[p];
				}
				for (int p = 0; p < tail; p++) {
					returned[t_array.length - head + p + 1] = (T) t_array[p];
				}
			}
			return returned;
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public boolean add(T e) {
		synchronized (this) {
			if (!contains(e)) {
				while (size() == t_array.length) {
					try {
						this.wait();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				t_array[tail] = e;
				tail = tail + 1 == t_array.length ? 0 : tail + 1;
				this.notifyAll();
			}
			return true;
		}
	}

	@Override
	public boolean offer(T e) {
		synchronized (this) {
			return add(e);
		}
	}

	@Override
	public void put(T e) throws InterruptedException {
		add(e);
		
	}

	@Override
	public boolean offer(T e, long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public T take() throws InterruptedException {
		return remove();
	}

	@Override
	public T poll(long timeout, TimeUnit unit) throws InterruptedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int remainingCapacity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		synchronized (this) {
			Iterator<T> i = iterator();
			while (i.hasNext()) {
				if (i.next().equals(o)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public int drainTo(Collection<? super T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int drainTo(Collection<? super T> c, int maxElements) {
		throw new UnsupportedOperationException();
	}

	public boolean equals(Object o) {
		try {
			RingBuffer<T> other = (RingBuffer<T>) o;
			if (other.t_array.length == t_array.length) {
				Iterator<T> i = iterator();
				while (i.hasNext()) {
					if (!other.contains(i.next())) {
						return false;
					}
				}
				return true;
			}
			return false;
		} finally {
			return false;//Something is wrong here?
		}
	}
	
	
	public boolean hasNext() {
		synchronized(this) {
			return (!(iteratorplace == tail));
		}
	}
	

	public T next() {
		synchronized (this) {
			if (hasNext()) {
				T returned = (t_array[iteratorplace]);
				iteratorplace = iteratorplace + 1 == t_array.length ? 0 : iteratorplace + 1;
				return returned;
			} else {
				throw new NoSuchElementException();
			}
		}
	}
	
}
