import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Period;
import java.util.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	protected void setUp() throws Exception {
		super.setUp();

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		initForTests();
	}


	// Here are some sample tests to get you started

	
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}
	
	
	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));
		
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));
	}

	private Piece stk1, stk2;
	private Piece LOne1, LOne2, LOne3, LOne4;
	private Piece LTwo1, LTwo2, LTwo3, LTwo4;
	private Piece SOne1, SOne2;
	private Piece STwo1, STwo2;
	private Piece SQR;
	private Piece PRM1, PRM2, PRM3, PRM4;

	public void initForTests(){
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

		PRM1 = new Piece(Piece.PYRAMID_STR);
		PRM2 = PRM1.computeNextRotation();
		PRM3 = PRM2.computeNextRotation();
		PRM4 = PRM3.computeNextRotation();
	}

	public void testStick() {
		//Fast Rotation
		Piece stk1Same = stk2.fastRotation();
		Piece stk2Same = stk1.fastRotation();

		//Equals
		assertTrue(stk1.equals(stk1Same));
		assertTrue(stk2.equals(stk2Same));

		//Width Check
		assertEquals(1,stk1.getWidth());
		assertEquals(4,stk2Same.getWidth());
		assertEquals(stk2.getWidth(),stk2Same.getWidth());

		//Height Check
		assertEquals(4,stk1Same.getHeight());
		assertEquals(1,stk2.getHeight());
		assertEquals(stk1.getHeight(), stk1Same.getHeight());

		//Skirt Check
		assertTrue(Arrays.equals(new int[] {0}, stk1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stk2.getSkirt()));
	}

	public void testL1() {
		//Fast Rotation
		Piece LOne2same = LOne1.fastRotation();
		Piece LOne3same = LOne2.fastRotation();
		Piece LOne4same = LOne3same.fastRotation();
		Piece LOne1Same = LOne4same.fastRotation();

		//Equals
		assertTrue(LOne2same.equals(LOne2));
		assertTrue(LOne3.equals(LOne3same));
		assertTrue(LOne4.equals(LOne4same));
		assertTrue(LOne1Same.equals(LOne1));

		//Width Check
		assertEquals(2,LOne1.getWidth());
		assertEquals(3,LOne2same.getWidth());
		assertEquals(2,LOne3same.getWidth());
		assertEquals(3,LOne4.getWidth());


		//Height Check
		assertEquals(3,LOne1Same.getHeight());
		assertEquals(2,LOne2.getHeight());
		assertEquals(3,LOne3.getHeight());
		assertEquals(2,LOne4same.getHeight());


		//Skirt Check
		assertTrue(Arrays.equals(new int[] {0, 0}, LOne1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, LOne2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {2, 0}, LOne3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1, 1}, LOne4.getSkirt()));
	}

	public void testL2() {
		//Fast Rotation
		Piece LTwo2same = LTwo1.fastRotation();
		Piece LTwo3same = LTwo2.fastRotation();
		Piece LTwo4same = LTwo3same.fastRotation();
		Piece LTwo1Same = LTwo4same.fastRotation();

		//Equals
		assertTrue(LTwo2same.equals(LTwo2));
		assertTrue(LTwo3.equals(LTwo3same));
		assertTrue(LTwo4.equals(LTwo4same));
		assertTrue(LTwo1Same.equals(LTwo1));

		//Width Check
		assertEquals(2,LTwo1.getWidth());
		assertEquals(3,LTwo2same.getWidth());
		assertEquals(2,LTwo3same.getWidth());
		assertEquals(3,LTwo4.getWidth());


		//Height Check
		assertEquals(3,LTwo1Same.getHeight());
		assertEquals(2,LTwo2.getHeight());
		assertEquals(3,LTwo3.getHeight());
		assertEquals(2,LTwo4same.getHeight());


		//Skirt Check
		assertTrue(Arrays.equals(new int[] {0, 0}, LTwo1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 1, 0}, LTwo2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 2}, LTwo3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, LTwo4.getSkirt()));
	}

	public void testS1() {

		//Fast Rotation
		Piece SOne2same = SOne1.fastRotation();
		Piece SOne1same = SOne2.fastRotation();

		//Equals
		assertTrue(SOne1same.equals(SOne1));
		assertTrue(SOne2same.equals(SOne2));


		//Width Check
		assertEquals(3,SOne1same.getWidth());
		assertEquals(2,SOne2.getWidth());


		//Height Check
		assertEquals(2,SOne1.getHeight());
		assertEquals(3,SOne2same.getHeight());


		//Skirt Check
		assertTrue(Arrays.equals(new int[] {0, 0, 1}, SOne1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, SOne2.getSkirt()));
	}

	public void testS2() {
		//Fast Rotation
		Piece STwo2same = STwo1.fastRotation();
		Piece STwo1same = STwo2.fastRotation();

		//Equals
		assertTrue(STwo1.equals(STwo1same));
		assertTrue(STwo2same.equals(STwo2));


		//Width Check
		assertEquals(3,STwo1.getWidth());
		assertEquals(2,STwo2same.getWidth());


		//Height Check
		assertEquals(2,STwo1same.getHeight());
		assertEquals(3,STwo2.getHeight());


		//Skirt Check
		assertTrue(Arrays.equals(new int[] {1, 0, 0}, STwo1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1}, STwo2.getSkirt()));
	}

	public void testSquare() {
		//Fast Rotation
		Piece SQRsame = SQR.fastRotation();

		//Equals
		assertTrue(SQR.equals(SQRsame));


		//Width Check
		assertEquals(2,SQR.getWidth());
		assertEquals(2,SQRsame.getWidth());


		//Height Check
		assertEquals(2,SQR.getHeight());
		assertEquals(2,SQRsame.getHeight());


		//Skirt Check
		assertTrue(Arrays.equals(new int[] {0, 0}, SQR.getSkirt()));
	}

	public void testPyramid() {
		//Fast Rotation
		Piece PRM2same = PRM1.fastRotation();
		Piece PRM3same = PRM2same.fastRotation();
		Piece PRM4same = PRM3.fastRotation();
		Piece PRM1same = PRM4same.fastRotation();

		//Equals
		assertTrue(PRM1same.equals(PRM1));
		assertTrue(PRM2.equals(PRM2same));
		assertTrue(PRM3same.equals(PRM3));
		assertTrue(PRM4.equals(PRM4same));
		assertFalse(PRM1.equals(PRM3));

		//Width Check
		assertEquals(3,PRM1.getWidth());
		assertEquals(2,PRM2same.getWidth());
		assertEquals(3,PRM3same.getWidth());
		assertEquals(2,PRM4.getWidth());


		//Height Check
		assertEquals(2,PRM1same.getHeight());
		assertEquals(3,PRM2.getHeight());
		assertEquals(2,PRM3.getHeight());
		assertEquals(3,PRM4same.getHeight());


		//Skirt Check
		assertEquals(0,PRM1.getSkirt()[0]);
		assertEquals(0,PRM1.getSkirt()[2]);
		assertEquals(1,PRM2.getSkirt()[0]);
		assertEquals(0,PRM2.getSkirt()[1]);
		assertEquals(1,PRM3.getSkirt()[0]);
		assertEquals(0,PRM3.getSkirt()[1]);
		assertEquals(1,PRM3.getSkirt()[2]);
		assertEquals(1,PRM4.getSkirt()[1]);
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, PRM1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, PRM2.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, PRM3.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 1}, PRM4.getSkirt()));
	}

	public void testComplicated() {
		assertTrue(stk1.equals(stk1.fastRotation().fastRotation()));
		assertTrue(PRM1.equals(PRM1.fastRotation().fastRotation().fastRotation().fastRotation()));

		//not requested in assignment, but for line coverage
		assertTrue(STwo2.equals(Piece.getPieces()[4].fastRotation()));

		//not requested in assignment, but for line coverage
		TPoint[] bodyTmp = new TPoint[4];
		bodyTmp[0] = new TPoint(0,0);
		bodyTmp[1] = new TPoint(0,1);
		bodyTmp[2] = new TPoint(1,0);
		bodyTmp[3] = new TPoint(1,1);
		assertTrue(bodyTmp[1].equals(SQR.getBody()[1]));
		assertTrue(bodyTmp[3].equals(SQR.getBody()[3]));
		assertTrue(bodyTmp[0].equals(SQR.getBody()[0]));

		//This should be true, because they are same object of cycle.
		assertTrue(SOne1 == SOne1.fastRotation().fastRotation());

		String string = "n o t c o r r e c t";
		Assert.assertThrows(RuntimeException.class, () -> new Piece(string));
	}
	// ||Final Check OF Code||
}
