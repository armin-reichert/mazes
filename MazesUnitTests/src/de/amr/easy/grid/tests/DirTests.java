package de.amr.easy.grid.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.amr.easy.grid.api.dir.DirExperimental;

public class DirTests {

	@Test
	public void testInverse() {
		assertTrue(DirExperimental.N.inverse().equals(DirExperimental.S));
		assertTrue(DirExperimental.E.inverse().equals(DirExperimental.W));
		assertTrue(DirExperimental.S.inverse().equals(DirExperimental.N));
		assertTrue(DirExperimental.W.inverse().equals(DirExperimental.E));
	}

	@Test
	public void testRight() {
		assertTrue(DirExperimental.N.right().equals(DirExperimental.E));
		assertTrue(DirExperimental.E.right().equals(DirExperimental.S));
		assertTrue(DirExperimental.S.right().equals(DirExperimental.W));
		assertTrue(DirExperimental.W.right().equals(DirExperimental.N));
	}

	@Test
	public void testLeft() {
		assertTrue(DirExperimental.N.left().equals(DirExperimental.W));
		assertTrue(DirExperimental.E.left().equals(DirExperimental.N));
		assertTrue(DirExperimental.S.left().equals(DirExperimental.E));
		assertTrue(DirExperimental.W.left().equals(DirExperimental.S));
	}

}
