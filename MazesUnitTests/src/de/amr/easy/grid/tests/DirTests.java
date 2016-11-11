package de.amr.easy.grid.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.amr.easy.grid.api.dir.Dir;

public class DirTests {

	@Test
	public void testInverse() {
		assertTrue(Dir.N.inverse().equals(Dir.S));
		assertTrue(Dir.E.inverse().equals(Dir.W));
		assertTrue(Dir.S.inverse().equals(Dir.N));
		assertTrue(Dir.W.inverse().equals(Dir.E));
	}

	@Test
	public void testRight() {
		assertTrue(Dir.N.right().equals(Dir.E));
		assertTrue(Dir.E.right().equals(Dir.S));
		assertTrue(Dir.S.right().equals(Dir.W));
		assertTrue(Dir.W.right().equals(Dir.N));
	}

	@Test
	public void testLeft() {
		assertTrue(Dir.N.left().equals(Dir.W));
		assertTrue(Dir.E.left().equals(Dir.N));
		assertTrue(Dir.S.left().equals(Dir.E));
		assertTrue(Dir.W.left().equals(Dir.S));
	}

}
