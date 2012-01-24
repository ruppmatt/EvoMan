package test;


import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import evoict.*;



public class TestWeightedIndex {

	@Test
	public void test() {
		WeightedIndex<String> win = new WeightedIndex<String>();

		win.add("a", 1.0);
		win.add("b", 10.0);
		win.add("c", 100.0);
		win.add("d", 1000.0);
		win.add("e", 10000.0);

		assertEquals(win.totalWeight(), new Double(11111));
		HashMap<String, Integer> count = new HashMap<String, Integer>();
		int counter = 0;

		RandomGenerator rand = new RandomGenerator();
		for (int k = 0; k < 1000000; k++) {
			double ndx = rand.nextDouble() * 11111;
			// System.err.println(ndx);
			String v = win.get(ndx);
			if (!count.containsKey(v)) {
				count.put(v, 1);
			} else {
				count.put(v, count.get(v) + 1);
			}
			if (ndx < 1.0) {
				counter++;
			}
		}
		for (String s : count.keySet()) {
			System.err.println(s + "\t" + count.get(s));
		}
		System.err.println(counter);

	}
}
