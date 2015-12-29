import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
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
		
		MeetingImpl firstExpected = (MeetingImpl) expected.get(2);
		MeetingImpl secondExpected = (MeetingImpl) expected.get(0);
		
		assertTrue(firstExpected.equals(actual.get(0)));
		assertTrue(secondExpected.equals(actual.get(1)));
	}
	
	private List<Meeting> addMeetings() {
		List<Meeting> list = new LinkedList<Meeting>();
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John"));
		
		Set<Contact> contacts2 = new HashSet<>();
		contacts2.add(new ContactImpl(2, "Andrew"));
		
		Calendar futureDate = Calendar.getInstance();
		int id;
		
		Calendar futureDate1 = Calendar.getInstance();		
		futureDate1.clear();
		futureDate1.set(2500, 11, 10);
		id = contactManager.addFutureMeeting(contacts, futureDate1);
		list.add(new FutureMeetingImpl(id, futureDate1, contacts));
		
		Calendar futureDate2 = Calendar.getInstance();	
		futureDate2.clear();
		futureDate2.set(2900, 10, 5);
		id = contactManager.addFutureMeeting(contacts2, futureDate2);
		list.add(new FutureMeetingImpl(id, futureDate2, contacts2));
		
		Calendar futureDate3 = Calendar.getInstance();	
		futureDate3.clear();
		futureDate3.set(2300, 1, 1);
		id = contactManager.addFutureMeeting(contacts, futureDate3);
		list.add(new FutureMeetingImpl(id, futureDate3, contacts));
		
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
	public void getFutureMeetingListWillBeChronologicallySorted() {
		addMeetings();
		List<Meeting> originalList = contactManager.getFutureMeetingList(new ContactImpl(1, "John"));
		List<Meeting> sortedList = new LinkedList<Meeting>();
		sortedList.addAll(originalList);

		Collections.sort(sortedList, (m1, m2) -> m1.getDate().compareTo(m2.getDate()));
		
		assertTrue(originalList.equals(sortedList));
	}
	
	@Test
	public void getFutureMeetingListWillHaveNoDuplicates() {
		List<Meeting> list = addMeetings();
		List<Meeting> sortedList = contactManager.getFutureMeetingList(new ContactImpl(1, "John"));
		
		//add duplicate
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John"));
		Calendar futureDate1 = Calendar.getInstance();		
		futureDate1.clear();
		futureDate1.set(2500, 11, 10);
		int id = contactManager.addFutureMeeting(contacts, futureDate1);
		list.add(new FutureMeetingImpl(id, futureDate1, contacts));
		
		assertEquals(4, list.size());
		assertEquals(2, sortedList.size());
	}
	
	@Test
	public void getTheMeetingListOnGivenDate() {
		List<Meeting> fullList = addMeetingsOnDate();
		
		Calendar futureDate1 = Calendar.getInstance();
		futureDate1.clear();
		futureDate1.set(2500, 11, 10);
		List<Meeting> smallList = contactManager.getMeetingListOn(futureDate1);
		
		assertEquals(2, smallList.size());
		assertEquals(1, smallList.get(0).getId());
		assertEquals(3, smallList.get(1).getId());
	}
	
	private List<Meeting> addMeetingsOnDate() {
		List<Meeting> list = new LinkedList<Meeting>();
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John"));
		
		Calendar futureDate1 = Calendar.getInstance();
		futureDate1.clear();
		futureDate1.set(2500, 11, 10);
		
		Calendar futureDate2 = Calendar.getInstance();	
		futureDate2.clear();
		futureDate2.set(2900, 10, 5);		
		
		int id;
		id = contactManager.addFutureMeeting(contacts, futureDate1);
		list.add(new FutureMeetingImpl(id, futureDate1, contacts));
				
		id = contactManager.addFutureMeeting(contacts, futureDate2);
		list.add(new FutureMeetingImpl(id, futureDate2, contacts));
		
		id = contactManager.addFutureMeeting(contacts, futureDate1);
		list.add(new FutureMeetingImpl(id, futureDate1, contacts));
		
		return list;
	}
}











