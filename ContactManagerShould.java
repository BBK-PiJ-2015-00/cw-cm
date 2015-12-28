import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

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
		assertEquals(futureDate, contactManager.getFutureMeeting(id).getDate());
		assertEquals(contacts, contactManager.getFutureMeeting(id).getContacts());
	}
	
	@Test
	public void returnNullIfCannotFindFutureMeeting() {
		returnGreaterThanZeroWhenAddFutureMeeting();
		
		assertNull(contactManager.getFutureMeeting(-1));
	}
	
	@Test
	public void getTheMeeting() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		Calendar futureDate = Calendar.getInstance();
		futureDate.set(3000, 12, 10);
		
		int id = contactManager.addFutureMeeting(contacts, futureDate);
		Meeting expected = new FutureMeetingImpl(id, futureDate, contacts);
		
		assertEquals(id, contactManager.getMeeting(id).getId());
		assertEquals(futureDate, contactManager.getMeeting(id).getDate());
		assertEquals(contacts, contactManager.getMeeting(id).getContacts());
	}
	
	@Test
	public void returnNullIfCannotFindMeeting() {
		returnGreaterThanZeroWhenAddFutureMeeting();
		
		assertNull(contactManager.getMeeting(-1));
	}
	
	@Test
	public void getTheFutureMeetingListForJohn() {
		List<Meeting> expected = addMeetings();
		List<Meeting> actual = contactManager.getFutureMeetingList(new ContactImpl(1, "John"));
		
		MeetingImpl firstExpected = (MeetingImpl) expected.get(0);
		MeetingImpl secondExpected = (MeetingImpl) expected.get(2);
		
		assertTrue(firstExpected.equals(actual.get(0)));
		assertTrue(secondExpected.equals(actual.get(1)));
	}
	
	private List<Meeting> addMeetings() {
		List<Meeting> list = new ArrayList<Meeting>();
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John"));
		
		Set<Contact> contacts2 = new HashSet<>();
		contacts2.add(new ContactImpl(2, "Andrew"));
		
		Calendar futureDate = Calendar.getInstance();
		int id;
		
		futureDate.set(3000, 12, 10);
		id = contactManager.addFutureMeeting(contacts, futureDate);
		list.add(new FutureMeetingImpl(id, futureDate, contacts));
		
		futureDate.set(3000, 10, 5);
		id = contactManager.addFutureMeeting(contacts2, futureDate);
		list.add(new FutureMeetingImpl(id, futureDate, contacts));
		
		futureDate.set(3000, 1, 1);
		id = contactManager.addFutureMeeting(contacts, futureDate);	
		list.add(new FutureMeetingImpl(id, futureDate, contacts));
		
		return list;
	}
	
	@Test
	public void getFutureMeetingReturnsEmptyListWhenSusanHasNoFutureMeetings() {
		List<Meeting> initialList = addMeetings();
		List<Meeting> finalList = contactManager.getFutureMeetingList(new ContactImpl(3, "Susan"));
		
		assertEquals(3, initialList.size());
		assertEquals(0, finalList.size());
	}
	
	@Test
	public void willSortMeetingsChronolically() {
		List<Meeting> list = addMeetings();
		
		list.sort();
		assertEquals(3, list.get(0).getId());
		assertEquals(2, list.get(1).getId());
		assertEquals(1, list.get(2).getId());		
	}
	
	//@Test
	public void getFutureMeetingListWillBeChronologicallySorted() {
		addMeetings();
		List<Meeting> originalList = contactManager.getFutureMeetingList(new ContactImpl(1, "John"));
		List<Meeting> sortedList = originalList;
		
		
		assertTrue(originalList.equals(sortedList));
	}
}











