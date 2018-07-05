package de.amr.easy.data.tests;

import org.junit.Assert;
import org.junit.Test;

import de.amr.easy.data.UnorderedPair;

public class TwoSetTests {

	@Test
	public void testEquals() {
		UnorderedPair one = UnorderedPair.of(1, 2);
		UnorderedPair two = UnorderedPair.of(2, 1);
		Assert.assertEquals(one, two);
		UnorderedPair three = UnorderedPair.of(1, 2);
		Assert.assertEquals(one, three);
		Assert.assertEquals(two, three);
	}
}
