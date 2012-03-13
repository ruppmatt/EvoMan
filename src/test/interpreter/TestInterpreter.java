package test.interpreter;


import org.junit.*;

import evoict.io.*;



public class TestInterpreter {

	protected String	filename	= "test/test_config_0.cfg";



	@Test
	public void test() {
		CommentStrippedInFile fin = new CommentStrippedInFile(null, filename);

	}

}
