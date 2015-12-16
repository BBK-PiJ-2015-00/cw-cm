import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

//java org.junit.runner.JUnitCore ContactShould

public class ContactShould {
	private Contact contact1;
	private Contact contact2;
	
	@Before
	public void createContact() {
		contact1 = new ContactImpl(1, "Sam", "Not nice");		
		contact2 = new ContactImpl(2, "Jenna");		
	}
	
	@Test
	public void notBeNull() {
		assertNotNull(contact1);
		assertNotNull(contact2);
	}
	
	@Test
	public void throwIllegalArgumentWhenIDLessThanOrEqualToZeroWithThreePerams() {
		boolean isIllegal = false;
		Contact dud = null;
		try {
			dud = new ContactImpl(0, "Sam", "Not nice");
		} catch (IllegalArgumentException ex) {
			isIllegal = true;
		}
		assertTrue(isIllegal);
		assertNull(dud);
	}
	
	@Test
	public void throwIllegalArgumentWhenIDLessThanOrEqualToZeroWithTwoPerams() {
		boolean isIllegal = false;
		Contact dud = null;		
		try {
			dud = new ContactImpl(-1, "Sam");
		} catch (IllegalArgumentException ex) {
			isIllegal = true;
		}
		assertTrue(isIllegal);
		assertNull(dud);
	}
	
	@Test
	public void getId() {
		assertEquals(1, contact1.getId());
		assertEquals(2, contact2.getId());
	}
	
	@Test
	public void getTheName() {
		assertEquals("Sam", contact1.getName());
		assertEquals("Jenna", contact2.getName());
	}
	
	@Test
	public void getTheNotes() {
		assertEquals("Not nice", contact1.getNotes());
		assertEquals("", contact2.getNotes());
	}
}












