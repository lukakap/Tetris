import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;
	private Piece stk1, stk2;
	private Piece LOne1, LOne2, LOne3, LOne4;
	private Piece LTwo1, LTwo2, LTwo3, LTwo4;
	private Piece SOne1, SOne2;
	private Piece STwo1, STwo2;
	private Piece SQR;
	Board b1, b2, b3, b4, b5, b6, b7;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.

	@Before
	protected void setUp() throws Exception {
		b = new Board(3, 6);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		b.place(pyr1, 0, 0);
		restOfInitForTests();
	}

	private void restOfInitForTests() {
		stk1 = new Piece(Piece.STICK_STR);
		stk2 = stk1.computeNextRotation();

		LOne1 = new Piece(Piece.L1_STR);
		LOne2 = LOne1.computeNextRotation();
		LOne3 = LOne2.computeNextRotation();
		LOne4 = LOne3.computeNextRotation();

		LTwo1 = new Piece(Piece.L2_STR);
		LTwo2 = LTwo1.computeNextRotation();
		LTwo3 = LTwo2.computeNextRotation();
		LTwo4 = LTwo3.computeNextRotation();

		SOne1 = new Piece(Piece.S1_STR);
		SOne2 = SOne1.computeNextRotation();

		STwo1 = new Piece(Piece.S2_STR);
		STwo2 = STwo1.computeNextRotation();

		SQR = new Piece(Piece.SQUARE_STR);

		b1 = new Board(3,6);
		b2 = new Board(5,10);
		b3 = new Board(3,6);
		b4 = new Board(6,10);
		b5 = new Board(3,5);
		b6 = new Board(4,1);
		b7 = new Board(4,7);
	}

	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	public void testTwoPyrAndAStick(){
		b1.place(pyr1,0,0);

		b1.commit();
		b1.place(pyr4,0,1);

		assertEquals(3,b1.dropHeight(pyr2,0));
		assertEquals(4,b1.getColumnHeight(0));
		assertEquals(1, b1.getRowWidth(3));

		b1.commit();
		int result = b1.place(stk1,2,1);

		assertEquals(Board.PLACE_ROW_FILLED,result);
		assertEquals(4,b1.dropHeight(pyr4,1));
		assertEquals(5,b1.getColumnHeight(2));
		assertEquals(2,b1.getRowWidth(3));
		assertEquals(5,b1.getMaxHeight());

		b1.undo();

		assertEquals(1,b1.getColumnHeight(2));
		assertEquals(2,b1.getRowWidth(2));
		assertEquals(3,b1.dropHeight(pyr4,1));

		int res = b1.place(pyr1,1,3);
		assertEquals(Board.PLACE_OUT_BOUNDS,res);

		b1.undo();

		int resClearRows = b1.clearRows();
		assertEquals(1,resClearRows);

		assertEquals(2,b1.getColumnHeight(1));
		assertEquals(3,b1.getMaxHeight());

		assertEquals(3,b1.getWidth());
		assertEquals(6,b1.getHeight());

		assertFalse(b1.getGrid(1,2));
	}


	public void testOfTwoS() {
		b2.place(SOne2,1,0);

		assertEquals(2,b2.dropHeight(STwo2,0));
		assertEquals(3,b2.getMaxHeight());
		assertEquals(3,b2.getColumnHeight(1));
		assertEquals(2,b2.getColumnHeight(2));

		b2.commit();
		int res = b2.place(pyr4,0,0);
		assertEquals(Board.PLACE_BAD,res);


		b2.undo();

		b2.place(STwo2,0,2);

		assertEquals(5,b2.getMaxHeight());
		assertEquals(2,b2.getRowWidth(1));
		assertEquals(2,b2.getRowWidth(3));

		assertEquals(5,b2.getWidth());
		assertEquals(10,b2.getHeight());

		assertFalse(b2.getGrid(1,0));
		assertTrue(b2.getGrid(1,4));
	}

	public void testAdvancedLikeInAssignment(){
		int result = b3.place(pyr1,0,0);
		assertEquals(Board.PLACE_ROW_FILLED, result);

		b3.undo();

		b3.place(stk1,0,0);

		assertEquals(4,b3.getMaxHeight());
		b3.commit();

		b3.place(stk1,2,0);

		assertEquals(2, b3.getRowWidth(3));
		b3.commit();

		b3.place(stk1,1,0);

		int res = b3.clearRows();
		assertEquals(4,res);
		assertEquals(0,b3.getColumnHeight(1));
		assertEquals(0,b3.getRowWidth(1));

		b3.commit();

		b3.place(pyr1,0,0);

		assertEquals(2,b3.getColumnHeight(1));
		assertEquals(2,b3.getMaxHeight());
		assertEquals(1,b3.getRowWidth(1));
		b3.clearRows();
		assertEquals(1,b3.getColumnHeight(1));
		assertEquals(1,b3.getRowWidth(0));

		b3.undo();

		//board should be empty
		Board EmptyBoard = new Board(b3.getWidth(),b3.getHeight());
		assertEquals(b3.toString(),EmptyBoard.toString());
		assertEquals(0, b3.getMaxHeight());
		assertEquals(0 ,b3.getRowWidth(0));
		assertFalse(b3.getGrid(0,0));

		b3.place(SQR,1,0);

		assertEquals(2,b3.getColumnHeight(2));
		assertEquals(2,b3.getRowWidth(0));

		b3.commit();
		assertEquals(2,b3.getMaxHeight());
		assertTrue(b3.getGrid(1,1));

		b3.place(LOne1,1,2);

		assertEquals(5,b3.getMaxHeight());
		assertEquals(3,b3.getColumnHeight(2));

		int resOfCR = b3.clearRows();
		assertEquals(0,resOfCR);
		assertEquals(3,b3.getColumnHeight(2));
		assertEquals(2,b3.getRowWidth(2));

		assertEquals(3,b3.getWidth());
		assertEquals(6,b3.getHeight());
	}

	public void testMostlyDropHeight() {
		b4.place(stk2,0,0);

		b4.commit();
		b4.place(SQR,0,1);

		assertEquals(4,b4.getRowWidth(0));
		assertEquals(3,b4.getColumnHeight(1));

		assertEquals(2,b4.dropHeight(SOne2,1));

		b4.commit();
		b4.place(SOne2,1,2);

		assertEquals(5,b4.getColumnHeight(1));
		assertEquals(2,b4.getRowWidth(3));
		assertFalse(b4.getGrid(2,1));
		assertEquals(4,b4.getColumnHeight(2));

		b4.commit();
		b4.place(SQR,4,0);
		b4.clearRows();
		b4.commit();

		assertEquals(4,b4.getRowWidth(0));
		assertFalse(b4.getGrid(2,0));
		assertFalse(b4.getGrid(3,0));
		assertEquals(3,b4.getColumnHeight(2));

		assertEquals(4,b4.dropHeight(pyr2,0));
		assertEquals(4,b4.getColumnHeight(1));

		assertEquals(1,b4.dropHeight(LOne3,2));
	}

	public void testClearRows(){
		b5.place(stk1,0,0);

		b5.commit();
		b5.place(stk1,1,0);

		b5.commit();
		b5.place(stk1,2,0);

		assertEquals(4,b5.clearRows());

		assertEquals(b5.getMaxHeight(),0);

		b6.place(stk2,0,0);

		assertEquals(1,b6.clearRows());
		assertEquals(0,b6.getMaxHeight());
	}

	public void testForHeightsCheck() {
		b7.place(SQR, 0, 0);
		b7.commit();
		b7.place(pyr1, 0, 2);
		b7.commit();
		b7.place(stk1,3,0);
		b7.clearRows();

		assertEquals(0,b7.getColumnHeight(2));
	}

	// ||Final Check OF Code||
}
