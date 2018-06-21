package de.amr.easy.data;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Another stack implementation.
 * 
 * @author Armin Reichert
 *
 * @param <T>
 *          stack element type
 */
public class Stack<T> implements Iterable<T> {

	private final Deque<T> stack = new ArrayDeque<>();

	@Override
	public Iterator<T> iterator() {
		return stack.iterator();
	}

	/**
	 * @return {@code true} if the stack is empty
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * Pushes the given element on top of the stack.
	 * 
	 * @param e
	 *          some element
	 */
	public void push(T e) {
		Objects.requireNonNull(e);
		stack.push(e);
	}

	/**
	 * Pops the top element of the stack.
	 * 
	 * @return the top stack element
	 * @throws NoSuchElementException
	 *           if this stack is empty
	 */
	public T pop() {
		return stack.pop();
	}

	/**
	 * @return the top stack element
	 */
	public Optional<T> peek() {
		return Optional.ofNullable(stack.peek());
	}

	/**
	 * Makes this stack empty.
	 */
	public void clear() {
		stack.clear();
	}

	/**
	 * @param e
	 *          some element
	 * @return {@code true} if the element is contained in the stack
	 */
	public boolean contains(T e) {
		return stack.contains(e);
	}
}