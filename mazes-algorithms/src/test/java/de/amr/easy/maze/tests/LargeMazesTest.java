package de.amr.easy.maze.tests;

import org.junit.Test;

import de.amr.maze.alg.RecursiveDivision;
import de.amr.maze.alg.core.UnobservableMazesFactory;
import de.amr.maze.alg.mst.KruskalMST;
import de.amr.util.StopWatch;

public class LargeMazesTest {

	private void test_Kruskal(int numCols, int numRows) {
		
		StopWatch watch = new StopWatch();
		watch.measure(() -> new KruskalMST(UnobservableMazesFactory.get(),numCols, numRows).createMaze(0, 0));
		System.out.println(String.format("Kruskal: %d vertices (%.0f ms)", numCols * numRows, watch.getMillis()));
	}

	private void test_RecursiveDivision(int numCols, int numRows) {
		
		StopWatch watch = new StopWatch();
		watch.measure(() -> new RecursiveDivision(UnobservableMazesFactory.get(),numCols, numRows).createMaze(0, 0));
		System.out.println(
				String.format("RecursiveDivision: %d vertices (%.0f ms)", numCols * numRows, watch.getMillis()));
	}

	@Test
	public void test_Kruskal_100_000() {
		test_Kruskal(100, 1000);
	}

	@Test
	public void test_Kruskal_500_000() {
		test_Kruskal(500, 1000);
	}

	@Test
	public void test_Kruskal_1_000_000() {
		test_Kruskal(1000, 1000);
	}

	@Test
	public void test_RecursiveDivision_100_000() {
		test_RecursiveDivision(100, 1000);
	}

	@Test
	public void test_RecursiveDivision_500_000() {
		test_RecursiveDivision(500, 1000);
	}

	@Test
	public void test_RecursiveDivision_1_000_000() {
		test_RecursiveDivision(1000, 1000);
	}
}