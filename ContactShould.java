import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

//java org.junit.runner.JUnitCore ContactShould

public class ContactShould {
	private Contact contact;
	
	@Test
	public void createContact() {
		contact = new ContactImpl(1, "Sam", "Not nice");
		assertNotNull(contact);
		
		contact = new ContactImpl(2, "Jenna");
		assertNotNull(contact);
	}
	
	@Test
	public void throwIllegalArgumentWhenIDLessThanOrEqualToZeroWithThreePerams() {
		boolean isIllegal = false;		
		try {
			contact = new ContactImpl(0, "Sam", "Not nice");
		} catch (IllegalArgumentException ex) {
			isIllegal = true;
		}
		assertTrue(isIllegal);
	}
	
	@Test
	public void throwIllegalArgumentWhenIDLessThanOrEqualToZeroWithTwoPerams() {
		boolean isIllegal = false;		
		try {
			contact = new ContactImpl(-1, "Sam");
		} catch (IllegalArgumentException ex) {
			isIllegal = true;
		}
		assertTrue(isIllegal);
	}
	
	@Test
	public void getId() {
		contact = new ContactImpl(1, "Sam", "Not nice");
		assertEquals(1, contact.getId());
		
		contact = new ContactImpl(2, "Jenna");
		assertEquals(2, contact.getId());
	}
}