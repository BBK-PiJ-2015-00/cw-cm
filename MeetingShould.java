import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

//java org.junit.runner.JUnitCore MeetingShould

public class MeetingShould {
	
	private Meeting futureMeeting;
	private Meeting pastMeeting;
	private Meeting dud = null;
	private static Calendar date = Calendar.getInstance();
	private static Set<Contact> contacts;
	
	@BeforeClass
	public static void buildUp() {
		date.set(2015, 12, 10);
		contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		contacts.add(new ContactImpl(2, "Jenna"));
		contacts.add(new ContactImpl(3, "Arthur"));
		contacts.add(new ContactImpl(4, "Annie", "Oranges"));
	}
	
	@Before
	public void createMeetings() {		
		futureMeeting = new FutureMeetingImpl(1, date, contacts);
		pastMeeting = new PastMeetingImpl(2, date, contacts, "hello");
	}
	
	@Test
	public void notBeNull() {
		assertNotNull(futureMeeting);
		assertNotNull(pastMeeting);
	}
	
	@Test
	public void getTheId() {
		assertEquals(1, futureMeeting.getId());
		assertEquals(2, pastMeeting.getId());
	}
	
	@Test
	public void getTheDate() {
		Calendar expected = (Calendar) date.clone();
		assertEquals(expected, futureMeeting.getDate());
		assertEquals(expected, pastMeeting.getDate());
	}
	
	@Test
	public void getTheContacts() {
		assertEquals(contacts, futureMeeting.getContacts());
		assertEquals(contacts, pastMeeting.getContacts());
	}
	
	@Test
	public void throwIllegalArgumentExceptionWhenDateIsEmpty() {
		boolean isIllegal = false;
		Calendar empty = Calendar.getInstance();
		empty.clear();
		try {
			dud = new FutureMeetingImpl(2, empty, contacts);
		} catch (IllegalArgumentException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
		
		isIllegal = false;
		try {
			dud = new PastMeetingImpl(2, empty, contacts, "hello");
		} catch (IllegalArgumentException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
	}
	
	@Test
	public void throwIllegalArgumentExceptionWhenContactsIsEmpty() {
		boolean isIllegal = false;
		Set<Contact> empty = new HashSet<>();
		try {
			dud = new FutureMeetingImpl(2, date, empty);
		} catch (IllegalArgumentException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
		
		isIllegal = false;
		try {
			dud = new PastMeetingImpl(2, date, empty, "hello");
		} catch (IllegalArgumentException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
	}
	
	@Test
	public void throwNullPointerExceptionWhenDateIsNull() {
		boolean isIllegal = false;
		Calendar nullDate = null;
		try {
			dud = new FutureMeetingImpl(2, nullDate, contacts);
		} catch (NullPointerException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
		
		isIllegal = false;
		try {
			dud = new PastMeetingImpl(2, nullDate, contacts, "hello");
		} catch (NullPointerException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
	}
	
	@Test
	public void throwNullPointerExceptionWhenContactsIsNull() {
		boolean isIllegal = false;
		Set<Contact> nullContacts = null;
		try {
			dud = new FutureMeetingImpl(2, date, nullContacts);
		} catch (NullPointerException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
		
		isIllegal = false;
		try {
			dud = new PastMeetingImpl(2, date, nullContacts, "hello");
		} catch (NullPointerException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
	}
	
	@Test
	public void throwIllegalArgumentExceptionWhenIdIsLessThanOne() {
		boolean isIllegal = false;
		int num = 0;
		try {
			dud = new FutureMeetingImpl(num, date, contacts);
		} catch (IllegalArgumentException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
		
		isIllegal = false;
		num = -1;
		try {
			dud = new PastMeetingImpl(num, date, contacts, "hello");
		} catch (IllegalArgumentException ex){
			isIllegal = true;
		}		
		assertTrue(isIllegal);
		assertNull(dud);
	}

	@Test
	public void getTheNotes() {
		PastMeeting pm = new PastMeetingImpl(3, date, contacts, "notes");
		assertEquals("notes", pm.getNotes());
		
		pm = new PastMeetingImpl(3, date, contacts, "");
		assertEquals("", pm.getNotes());
	}
	
	@Test
	public void throwNullPointerExceptionWhenNotesIsNull() {
		boolean isIllegal = false;
		String nullNotes = null;
		try {
			dud = new PastMeetingImpl(2, date, contacts, nullNotes);
		} catch (NullPointerException ex) {
			isIllegal = true;
		}
		assertTrue(isIllegal);
		assertNull(dud);
	}
	
	@Test
	public void willSortMeetingsChronolically() {
		List<MeetingImpl> list = new LinkedList<MeetingImpl>();
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John"));
		
		Calendar futureDate1 = Calendar.getInstance();		
		futureDate1.clear();
		futureDate1.set(2500, 11, 10);
		list.add(new FutureMeetingImpl(1, futureDate1, contacts));
		
		Calendar futureDate2 = Calendar.getInstance();	
		futureDate2.clear();
		futureDate2.set(2900, 10, 5);
		list.add(new FutureMeetingImpl(2, futureDate2, contacts));
		
		Calendar futureDate3 = Calendar.getInstance();	
		futureDate3.clear();
		futureDate3.set(2800, 1, 1);
		list.add(new FutureMeetingImpl(3, futureDate3, contacts));
		
		
		Collections.sort(list, (m1, m2) -> m1.getDate().compareTo(m2.getDate()));
		
		assertEquals(1, list.get(0).getId());
		assertEquals(3, list.get(1).getId());
		assertEquals(2, list.get(2).getId());		
	}
}


















