package de.amr.easy.data.tests;

import org.junit.Assert;
import org.junit.Test;

import de.amr.easy.graph.impl.TwoSet;

public class TwoSetTests {

	@Test
	public void testEquals() {
		TwoSet one = TwoSet.of(1, 2);
		TwoSet two = TwoSet.of(2, 1);
		Assert.assertEquals(one, two);
		TwoSet three = TwoSet.of(1, 2);
		Assert.assertEquals(one, three);
		Assert.assertEquals(two, three);
	}
}
