package de.amr.easy.data.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import de.amr.easy.data.TwoSet;

public class TwoSetTest {

	@Test
	public void testEquals() {
		TwoSet<Integer> one = TwoSet.of(1, 2);
		TwoSet<Integer> two = TwoSet.of(2, 1);
		TwoSet<Integer> three = TwoSet.of(1, 3);
		assertEquals(one, two);
		assertNotEquals(one, three);
	}

	@Test
	public void testMapAccess() {
		Map<TwoSet<Integer>, String> map = new HashMap<>();
		TwoSet<Integer> one = TwoSet.of(1, 2);
		TwoSet<Integer> two = TwoSet.of(2, 1);
		map.put(one, "A");
		assertEquals(map.get(one), map.get(two));
	}
}