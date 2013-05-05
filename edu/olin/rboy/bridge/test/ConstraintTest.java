package edu.olin.rboy.bridge.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.olin.rboy.bridge.constraints.Constraint;
import edu.olin.rboy.bridge.constraints.HandConstraint;
import edu.olin.rboy.bridge.constraints.SuitsConstraint;

public class ConstraintTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHandConstraintGetDivision() {
		HandConstraint constraint = new SuitsConstraint();
		List<Constraint> split = constraint.getDivision(2);
		assertTrue(split.size()==2);
		assertTrue(split.get(0) instanceof SuitsConstraint);
		HandConstraint c1 = (HandConstraint) split.get(0);
		HandConstraint c2 = (HandConstraint) split.get(1);
		assertTrue(c1.getRange().get(0) == 1);
		assertTrue(c1.getRange().get(1) == 2);
		assertTrue(c2.getRange().get(0) == 3);
		assertTrue(c2.getRange().get(1) == 3);
	}

	@Test
	public void testGetPossDivisions() {
		HandConstraint constraint = new SuitsConstraint();
		List<List<Constraint>> split = constraint.getPossDivisions(1);
		assertTrue(split.size()==3);
		
		List<Constraint> first = split.get(0);
		assertTrue(first.size() == 1);
		List<Constraint> second = split.get(1);
		assertTrue(second.size() == 2);
		
		assertTrue(first.get(0).equals(constraint));
		
		HandConstraint split1a = (HandConstraint) second.get(0);
		HandConstraint split1b = (HandConstraint) second.get(1);
		assertTrue(split1a.getRange().get(0) == 1);
		assertTrue(split1a.getRange().get(1) == 1);
		assertTrue(split1b.getRange().get(0) == 2);
		assertTrue(split1b.getRange().get(1) == 3);
		
		HandConstraint split2a = (HandConstraint) split.get(2).get(0);
		HandConstraint split2b = (HandConstraint) split.get(2).get(1);
		assertTrue(split2a.getRange().get(0) == 1);
		assertTrue(split2a.getRange().get(1) == 2);
		assertTrue(split2b.getRange().get(0) == 3);
		assertTrue(split2b.getRange().get(1) == 3);
	}

}
 