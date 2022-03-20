// Board.java

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;

	//secondary structures
	private int[] widths;
	private int[] heights;
	private int maxHeight;

	//backup data structures
	private int[] xWidths;
	private int[] xHeights;
	private int xMaxHeight;
	private boolean[][] xGrid;

	// Here a few trivial methods are provided:
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;

		widths = new int[this.height];
		heights = new int[this.width];
		maxHeight = 0;

		xWidths = new int[this.height];
		xHeights = new int[this.width];
		xMaxHeight = 0;
		xGrid = new boolean[this.width][this.height];
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int maxHeightTmp = 0;
			int[] heightsTmp = new int[heights.length];
			int[] widthsTmp = new int[widths.length];
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[0].length; j++){
					if(grid[i][j]) {
						widthsTmp[j]++;
						heightsTmp[i] = j + 1;
					}
				}
				maxHeightTmp = Integer.max(heightsTmp[i], maxHeightTmp);
			}

			if(!Arrays.equals(heights,heightsTmp) || !Arrays.equals(widthsTmp,widths) || maxHeightTmp != maxHeight) throw new RuntimeException("DO NOT MATCH");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int maxCor = -1;
		for(int i = 0; i < piece.getWidth(); i++) {
			int yCorOfOriginDependOnColumns = heights[x + i] - piece.getSkirt()[i];
			maxCor = Integer.max(maxCor,yCorOfOriginDependOnColumns);
		}
		return maxCor; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");

		int result = PLACE_OK;

		committed = false;
		TPoint[] body = piece.getBody();


		saveBackupData();

		//modifying board
		boolean rowFilled = false;
		for(int i = 0; i < body.length; i++) {
			int blockX = x + body[i].x;
			int blockY = y + body[i].y;
			if(x < 0 || y < 0 || blockX > this.getWidth() - 1 || blockY > this.getHeight() - 1) {
				result = PLACE_OUT_BOUNDS;
				return result;
			}
			if(grid[blockX][blockY]) {
				result = PLACE_BAD;
				return result;
			}
			grid[blockX][blockY] = true;
			if(changeSecondaryStructures(blockX, blockY)) rowFilled = true;
		}
		if(rowFilled) {
			result = PLACE_ROW_FILLED;
		}

		sanityCheck();
		return result;
	}

	private void saveBackupData() {
		System.arraycopy(widths,0,xWidths,0,widths.length);
		System.arraycopy(heights,0,xHeights,0,heights.length);
		xMaxHeight = maxHeight;
		for(int i = 0; i < xGrid.length; i++) {
			System.arraycopy(grid[i],0,xGrid[i],0,grid[i].length);
		}
	}

	private boolean changeSecondaryStructures(int blockX, int blockY) {
		heights[blockX] = Integer.max(heights[blockX], blockY + 1);
		widths[blockY]++;
		maxHeight = Integer.max(maxHeight, heights[blockX]);
		if(widths[blockY] == this.width) {
			return true;
		}
		return false;
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if(committed) saveBackupData();   //Data is already saved.
		committed = false;
		int rowsCleared = 0;
		int to, from = 0;
		boolean atListOneRowIsCleared = false;
		boolean FROMIsOutOfBoundaries;
		for (int i = 0; i < maxHeight; i++) {
			to = i;

			FROMIsOutOfBoundaries = calculateIfFROMIsOutOfBoundaries(from);
			while(fromPointedRowIsFilled(from, FROMIsOutOfBoundaries)) {
				atListOneRowIsCleared = true;
				rowsCleared++;
				from++;
				FROMIsOutOfBoundaries = calculateIfFROMIsOutOfBoundaries(from);
			}

			//clear row
			if(atListOneRowIsCleared) clearRow(FROMIsOutOfBoundaries, to, from);

			from++;
		}

		changeHeightsArray();
		sanityCheck();
		return rowsCleared;
	}

	private void changeHeightsArray() {
		int maxHeightTmp = 0;
		for(int i = 0; i < grid.length; i++) {
			for(int j = grid[0].length - 1; j >= 0; j--) {
				heights[i] = 0;
				if(grid[i][j]) {
					heights[i] = j + 1;
					break;
				}
			}
			maxHeightTmp = Integer.max(maxHeightTmp,heights[i]);
		}

		maxHeight = maxHeightTmp;
	}

	private void clearRow(boolean FROMIsOutOfBoundaries, int to, int from) {
		for(int j = 0; j < width; j++) {
			if (FROMIsOutOfBoundaries) {
				grid[j][to] = false;
				widths[to] = 0;
			} else {
				grid[j][to] = grid[j][from];
				widths[to] = widths[from];
			}
		}
	}

	private boolean calculateIfFROMIsOutOfBoundaries(int from) {
		return from > height - 1;
	}

	private boolean fromPointedRowIsFilled(int from, boolean FROMIsOutOfBoundaries) {
		if(FROMIsOutOfBoundaries) return false;
		return widths[from] == width;
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		// YOUR CODE HERE
		if(!committed) {
			//width swap
			int[] temp1 = widths;
			widths = xWidths;
			xWidths = temp1;

			//height swap
			int[] temp2 = heights;
			heights = xHeights;
			xHeights = temp2;

			//grid swap
			boolean[][] temp3 = grid;
			grid = xGrid;
			xGrid = temp3;

			//max height swap
			int temp = maxHeight;
			maxHeight = xMaxHeight;
			xMaxHeight = temp;

			committed = true;

			sanityCheck();
		}
	}


	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
	 // ||Final Check OF Code||
}


