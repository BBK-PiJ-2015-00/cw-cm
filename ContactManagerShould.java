import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

//java org.junit.runner.JUnitCore ContactManagerShould

public class ContactManagerShould {
	
	private ContactManager contactManager;
	
	@Before
	public void createManager() {
		contactManager = new ContactManagerImpl();
	}
	
	@Test
	public void notBeNull() {
		assertNotNull(contactManager);
	}
	
	@Test
	public void returnGreaterThanZeroWhenAddFutureMeeting() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		Calendar futureDate = Calendar.getInstance();
		futureDate.set(3000, 12, 10);
		
		assertTrue(contactManager.addFutureMeeting(contacts, futureDate) > 0);
	}
	
	@Test
	public void returnNullIfCannotFindPastMeeting() {
		assertNull(contactManager.getPastMeeting(-1));
	}
	
	@Test
	public void getTheFutureMeeting() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		Calendar futureDate = Calendar.getInstance();
		futureDate.set(3000, 12, 10);
		
		int id = contactManager.addFutureMeeting(contacts, futureDate);
		FutureMeeting expected = new FutureMeetingImpl(id, futureDate, contacts);
		
		assertEquals(id, contactManager.getFutureMeeting(id).getId());
	}
}