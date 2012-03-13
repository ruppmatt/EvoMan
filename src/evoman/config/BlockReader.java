package evoman.config;


import java.util.*;

import evoict.io.*;



public class BlockReader {

	Stack<Integer>	_col		= new Stack<Integer>();
	BlockFrame		_cur_block	= new BlockFrame();



	public BlockReader(TextScanner sc) throws BadFormat {
		_col.push(0);
		process(sc);
		while (_cur_block.parent() != null) {
			_cur_block = _cur_block.parent();
		}
	}



	public void process(TextScanner sc) throws BadFormat {
		while (!sc.hasNextLine()) {
			String cur_line = sc.nextLine();
			doBlocking(cur_line);
			_cur_block.addText(cur_line);
		}
	}



	public void doBlocking(String line) throws BadFormat {
		int col = getColumn(line);
		int cur_col = _col.peek();
		if (col > _col.peek()) {
			_col.push(col);
			BlockFrame new_block = new BlockFrame(_cur_block);
			_cur_block.addChild(new_block);
			_cur_block = new_block;
		} else if (col < cur_col) {
			do {
				_col.pop();
				cur_col = _col.peek();
				_cur_block = _cur_block.parent();
				if (_cur_block == null) {
					throw new BadFormat("File improperly blocked at line:\n + line");
				}
			} while (!_col.empty() && col < cur_col);

			if (_col.empty() || cur_col != col) {
				throw new BadFormat("Invalid indentation at line:\n" + line);
			}
		}
	}



	public int getColumn(String line) throws BadFormat {
		int col = 0;
		while (col < line.length() && line.charAt(col) == ' ') {
			col++;
		}
		if (col == line.length()) {
			throw new BadFormat("Cannot process empty line.");
		}
		return col;
	}
}
