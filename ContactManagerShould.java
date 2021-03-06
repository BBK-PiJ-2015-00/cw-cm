import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import java.util.*;
import java.io.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

//java org.junit.runner.JUnitCore ContactManagerShould

public class ContactManagerShould {
	
	private ContactManager contactManager;
	
	@Before
	public void createManager() {
		File file = new File("contacts.txt");
		file.delete();
		/*
		try {
			file.delete();
			file.createNewFile();
		} catch (IOException ex) {
			ex.printStackTrace();
		}*/
		contactManager = new ContactManagerImpl();
		addContacts();
	}
	
	@Test
	public void notBeNull() {
		assertNotNull(contactManager);
	}
	
	@Test
	public void returnGreaterThanZeroWhenAddFutureMeeting() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "John", "Short"));
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
		contacts.add(new ContactImpl(1, "John", "Short"));
		Calendar futureDate = Calendar.getInstance();
		futureDate.set(3000, 12, 10);
		
		int id = contactManager.addFutureMeeting(contacts, futureDate);
		FutureMeeting expected = new FutureMeetingImpl(id, futureDate, contacts);
		FutureMeeting actual = contactManager.getFutureMeeting(id);
		
		assertNotNull(actual);
		assertEquals(id, actual.getId());
		assertEquals(futureDate, actual.getDate());
		assertEquals(contacts, actual.getContacts());
	}
	
	@Test
	public void returnNullIfCannotFindFutureMeeting() {
		returnGreaterThanZeroWhenAddFutureMeeting();
		
		assertNull(contactManager.getFutureMeeting(-1));
	}
	
	@Test
	public void getTheMeeting() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "John", "Short"));
		Calendar futureDate = Calendar.getInstance();
		futureDate.set(3000, 12, 10);
		
		int id = contactManager.addFutureMeeting(contacts, futureDate);
		Meeting expected = new FutureMeetingImpl(id, futureDate, contacts);
		Meeting actual = contactManager.getMeeting(id);
		
		assertNotNull(actual);
		assertEquals(id, actual.getId());
		assertEquals(futureDate, actual.getDate());
		assertEquals(contacts, actual.getContacts());
	}
	
	@Test
	public void returnNullIfCannotFindMeeting() {
		returnGreaterThanZeroWhenAddFutureMeeting();
		
		assertNull(contactManager.getMeeting(-1));
	}
	
	@Test
	public void getTheFutureMeetingListForJohn() {
		List<Meeting> expected = addMeetings();
		List<Meeting> actual = contactManager.getFutureMeetingList(new ContactImpl(1, "John", "Short"));
		
		MeetingImpl firstExpected = (MeetingImpl) expected.get(2);
		MeetingImpl secondExpected = (MeetingImpl) expected.get(0);
		
		assertTrue(firstExpected.equals(actual.get(0)));
		assertTrue(secondExpected.equals(actual.get(1)));
	}
	
	private List<Meeting> addMeetings() {
		List<Meeting> list = new LinkedList<Meeting>();
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John", "Short"));
		
		Set<Contact> contacts2 = new HashSet<>();
		contacts2.add(new ContactImpl(2, "Janet", "Tall"));
		
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
		List<Meeting> finalList = contactManager.getFutureMeetingList(new ContactImpl(4, "Andrew", "Calm"));
		
		assertEquals(3, initialList.size());
		assertEquals(0, finalList.size());
	}
	
	
	@Test
	public void getFutureMeetingListWillBeChronologicallySorted() {
		addMeetings();
		List<Meeting> originalList = contactManager.getFutureMeetingList(new ContactImpl(1, "John", "Short"));
		List<Meeting> sortedList = new LinkedList<Meeting>();
		sortedList.addAll(originalList);

		Collections.sort(sortedList, (m1, m2) -> m1.getDate().compareTo(m2.getDate()));
		
		assertTrue(originalList.equals(sortedList));
	}
	
	@Test
	public void getFutureMeetingListWillHaveNoDuplicates() {
		List<Meeting> list = addMeetings();
		List<Meeting> sortedList = contactManager.getFutureMeetingList(new ContactImpl(1, "John", "Short"));
		
		//add duplicate
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John", "Short"));
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
		assertEquals(3, smallList.get(0).getId());
		assertEquals(1, smallList.get(1).getId());
	}
	
	private List<Meeting> addMeetingsOnDate() {
		List<Meeting> list = new LinkedList<Meeting>();
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John", "Short"));
		
		Calendar futureDate1 = Calendar.getInstance();
		futureDate1.clear();
		futureDate1.set(2500, 11, 10);
		futureDate1.set(Calendar.HOUR_OF_DAY, 15);
		
		Calendar futureDate2 = Calendar.getInstance();	
		futureDate2.clear();
		futureDate2.set(2900, 10, 5);
		futureDate2.set(Calendar.HOUR_OF_DAY, 12);
		
		Calendar futureDate3 = Calendar.getInstance();
		futureDate3.clear();
		futureDate3.set(2500, 11, 10);
		futureDate3.set(Calendar.HOUR_OF_DAY, 12);
		
		int id;
		id = contactManager.addFutureMeeting(contacts, futureDate1);
		list.add(new FutureMeetingImpl(id, futureDate1, contacts));
				
		id = contactManager.addFutureMeeting(contacts, futureDate2);
		list.add(new FutureMeetingImpl(id, futureDate2, contacts));
		
		id = contactManager.addFutureMeeting(contacts, futureDate3);
		list.add(new FutureMeetingImpl(id, futureDate3, contacts));
		
		return list;
	}
	
	@Test
	public void getTheMeetingListOnGivenDateIsSorted() {
		addMeetingsOnDate();
		
		Calendar futureDate1 = Calendar.getInstance();
		futureDate1.clear();
		futureDate1.set(2500, 11, 10);
		List<Meeting> originalList = contactManager.getMeetingListOn(futureDate1);
		
		List<Meeting> sortedList = new LinkedList<Meeting>();
		sortedList.addAll(originalList);
		Collections.sort(sortedList, (m1, m2) -> m1.getDate().compareTo (m2.getDate()));		
		
		assertTrue(sortedList.equals(originalList));
	}
	
	@Test
	public void getTheMeetingListOnGivenDateIsEmpty() {
		addMeetingsOnDate();
		
		Calendar futureDate4 = Calendar.getInstance();
		futureDate4.clear();
		futureDate4.set(9000, 11, 10);
		List<Meeting> list = contactManager.getMeetingListOn(futureDate4);
		
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void addTheNewPastMeeting() {
		Calendar pastDate = Calendar.getInstance();
		pastDate.clear();
		pastDate.set(2000, 11, 10);
		pastDate.set(Calendar.HOUR_OF_DAY, 15);
		
		Set<Contact> contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "John", "Short"));
		
		String notes = "Hello";
		
		contactManager.addNewPastMeeting(contacts, pastDate, notes);
	}
	
	@Test
	public void getThePastMeetingListForJanet() {
		List<Meeting> fullList = addPastMeetings();
		List<PastMeeting> smallList = contactManager.getPastMeetingListFor(new ContactImpl(2, "Janet", "Tall"));
		
		assertTrue(smallList.size() != fullList.size());
		assertEquals(3, smallList.get(0).getId());
		assertEquals(1, smallList.get(1).getId());
	}
	
	private List<Meeting> addPastMeetings() {
		Calendar pastDate1 = Calendar.getInstance();
		pastDate1.clear();
		pastDate1.set(2000, 11, 10);
		pastDate1.set(Calendar.HOUR_OF_DAY, 15);
		
		Calendar pastDate2 = Calendar.getInstance();
		pastDate2.clear();
		pastDate2.set(2000, 5, 10);
		pastDate2.set(Calendar.HOUR_OF_DAY, 4);
		
		Calendar pastDate3 = Calendar.getInstance();
		pastDate3.clear();
		pastDate3.set(1990, 3, 10);
		pastDate3.set(Calendar.HOUR_OF_DAY, 15);
		
		Calendar pastDate4 = Calendar.getInstance();
		pastDate3.clear();
		pastDate3.set(1990, 3, 10);
		pastDate3.set(Calendar.HOUR_OF_DAY, 12);
		
		Contact john = new ContactImpl(1, "John", "Short");
		Contact janet = new ContactImpl(2, "Janet", "Tall");
		Contact andrew = new ContactImpl(4, "Andrew", "Calm");
		
		Set<Contact> contacts1 = new HashSet<>();
		contacts1.add(john);
		contacts1.add(janet);
		contacts1.add(andrew);
		
		Set<Contact> contacts2 = new HashSet<>();
		contacts2.add(john);
		contacts2.add(andrew);
		
		List<Meeting> result = new LinkedList<Meeting>();
		
		contactManager.addNewPastMeeting(contacts1, pastDate1, "");		
		contactManager.addNewPastMeeting(contacts2, pastDate2, "");
		contactManager.addNewPastMeeting(contacts1, pastDate3, "What?");
		contactManager.addNewPastMeeting(contacts2, pastDate4, "Hello");
		
		result.add(new PastMeetingImpl(1, pastDate1, contacts1, ""));
		result.add(new PastMeetingImpl(2, pastDate2, contacts2, ""));
		result.add(new PastMeetingImpl(3, pastDate3, contacts1, "What?"));
		result.add(new PastMeetingImpl(4, pastDate4, contacts2, "Hello"));
		
		return result;
	}
	
	@Test
	public void getThePastMeetingListForArnoldIsEmpty() {
		addPastMeetings();
		List<PastMeeting> smallList = contactManager.getPastMeetingListFor(new ContactImpl(3, "John", "Angry"));
		
		assertTrue(smallList.isEmpty());
	}
	
	@Test
	public void getThePastMeetingListForJohnIsSorted() {
		addPastMeetings();
		List<PastMeeting> originalList = contactManager.getPastMeetingListFor(new ContactImpl(1, "John", "Short"));
		
		List<PastMeeting> sortedList = new LinkedList<PastMeeting>();
		sortedList.addAll(originalList);
		
		Collections.sort(sortedList, (m1, m2) -> m1.getDate().compareTo (m2.getDate()));
		
		assertTrue(sortedList.equals(originalList));		
	}
	
	@Test
	public void addMeetingNotesReturnsAPastMeeting() {
		addPastMeetings();
		PastMeeting example = contactManager.addMeetingNotes(1, "example note");
		
		assertNotNull(example);
		assertTrue(example.getClass() == PastMeetingImpl.class);
	}
	
	@Test
	public void addMeetingNotesReturnsAPastMeetingWithNotesAdded() {
		addPastMeetings();
		PastMeeting example = contactManager.addMeetingNotes(1, "hello world");
		
		assertEquals("hello world", example.getNotes());
	}

	@Test
	public void getThePastMeeting() {
		List<Meeting> list = addPastMeetings();
		PastMeetingImpl expected = (PastMeetingImpl) list.get(1);
		PastMeetingImpl actual = (PastMeetingImpl) contactManager.getPastMeeting(2);
		
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void addMeetingNotesAddsNotesToAnExistingPastMeeting() {
		List<Meeting> list = addPastMeetings();
		PastMeetingImpl before = (PastMeetingImpl) list.get(0);
		PastMeetingImpl actual = (PastMeetingImpl) contactManager.addMeetingNotes(1, "hello world");
		PastMeetingImpl after = (PastMeetingImpl) contactManager.getPastMeeting(1);
		
		assertFalse("before, after", before.equals(after));
		assertTrue("after, actual", after.equals(actual));
	}
	
	//@Test
	public void addMeetingNotesConvertsFutureMeetingToPastMeeting() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(2000, 10, 10);
		
		contactManager.addFutureMeeting(contacts, date);
		assertTrue("is future meeting", contactManager.getMeeting(1).getClass() == FutureMeetingImpl.class);
		
		PastMeetingImpl actual = (PastMeetingImpl) contactManager.addMeetingNotes(1, "what?");
		assertTrue("now past meeting", contactManager.getMeeting(1).getClass() == PastMeetingImpl.class);
		
		PastMeetingImpl expected = (PastMeetingImpl) contactManager.getPastMeeting(1);
		assertTrue("is the same", expected.equals(actual));
	}
	
	@Test
	public void addMeetingNotesThrowsIllegalArgumentExceptionIfMeetingDoesNotExist() {
		addPastMeetings();
		
		boolean hasError = false;
		try {
			contactManager.addMeetingNotes(100, "Hello");
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void addMeetingNotesThrowsNullPointerExceptionIfNoteIsNull() {
		addPastMeetings();
		
		boolean hasError = false;
		try {
			contactManager.addMeetingNotes(1, null);
		} catch (NullPointerException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void addMeetingNotesThrowsIllegalStateExceptionIfDateIsNotPast() {
		addMeetings();
		
		boolean hasError = false;
		try {
			contactManager.addMeetingNotes(1, "hello");
		} catch (IllegalStateException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void addFutureMeetingThrowsIllegalArgumentExceptionIfDateIsInPast() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(2000, 10, 10);
		
		boolean hasError = false;
		try {
			contactManager.addFutureMeeting(contacts, date);
		} catch (IllegalArgumentException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addFutureMeetingThrowsNullPointerExceptionIfDateIsNull() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		Calendar date = null;
		
		boolean hasError = false;
		try {
			contactManager.addFutureMeeting(contacts, date);
		} catch (NullPointerException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addFutureMeetingThrowsIllegalArgumentExceptionIfAContactIsNull() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		contacts.add(null);
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(3000, 10, 10);		
		
		boolean hasError = false;
		try {
			contactManager.addFutureMeeting(contacts, date);
		} catch (IllegalArgumentException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addFutureMeetingThrowsNullPointerExceptionIfContactsNull() {
		Set<Contact> contacts = null;
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(3000, 10, 10);		
		
		boolean hasError = false;
		try {
			contactManager.addFutureMeeting(contacts, date);
		} catch (NullPointerException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addFutureMeetingThrowsIllegalArgumentExceptionIfContactsIsEmpty() {
		Set<Contact> contacts = new HashSet<>();
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(3000, 10, 10);		
		
		boolean hasError = false;
		try {
			contactManager.addFutureMeeting(contacts, date);
		} catch (IllegalArgumentException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void getPastMeetingThrowsIllegalStateExceptionIfMeetingIsInFuture() {
		addMeetings();
		
		boolean hasError = false;
		try {
			contactManager.getPastMeeting(1);
		} catch (IllegalStateException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void getFutureMeetingThrowsIllegalStateExceptionIfMeetingIsInPast() {
		addPastMeetings();
		
		boolean hasError = false;
		try {
			contactManager.getFutureMeeting(1);
		} catch (IllegalStateException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void addNewPastMeetingThrowsNullPointerExceptionIfDateIsNull() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "John", "Short"));
		Calendar date = null;
		
		boolean hasError = false;
		try {
			contactManager.addNewPastMeeting(contacts, date, "");
		} catch (NullPointerException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addNewPastMeetingThrowsNullPointerExceptionIfNoteIsNull() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "John", "Short"));
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(2000, 10, 10);
		
		boolean hasError = false;
		try {
			contactManager.addNewPastMeeting(contacts, date, null);
		} catch (NullPointerException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addNewPastMeetingThrowsNullPointerExceptionIfContactsIsNull() {
		Set<Contact> contacts = null;
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(2000, 10, 10);
		
		boolean hasError = false;
		try {
			contactManager.addNewPastMeeting(contacts, date, "hello");
		} catch (NullPointerException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addNewPastMeetingThrowsIllegalArgumentExceptionIfAContactIsNull() {
		Set<Contact> contacts = new HashSet<>(); 
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		contacts.add(null);
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(2000, 10, 10);
		
		boolean hasError = false;
		try {
			contactManager.addNewPastMeeting(contacts, date, "hello");
		} catch (IllegalArgumentException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addNewPastMeetingThrowsIllegalArgumentExceptionIfContactIsEmpty() {
		Set<Contact> contacts = new HashSet<>();
		Calendar date = Calendar.getInstance();
		date.clear();
		date.set(2000, 10, 10);
		
		boolean hasError = false;
		try {
			contactManager.addNewPastMeeting(contacts, date, "hello");
		} catch (IllegalArgumentException ex){
			hasError = true;
		}
		
		assertTrue(hasError);
	}
	
	@Test
	public void addNewContactReturnsIntGreaterThanZero() {
		assertTrue(0 < contactManager.addNewContact("name", "notes"));
	}
	
	@Test
	public void addNewContactThrowsIllegalArgumentExceptionIfNameOrNotesIsEmptyString() {
		boolean hasError = false;
		try {
			contactManager.addNewContact("", "notes");
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue("Name is empty", hasError);
		
		hasError = false;
		try {
			contactManager.addNewContact("name", "");
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue("Notes is empty", hasError);
	}
	
	@Test
	public void addNewContactThrowsNullPointerExceptionIfNameOrNotesIsNull() {
		boolean hasError = false;
		try {
			contactManager.addNewContact(null, "notes");
		} catch (NullPointerException ex) {
			hasError = true;
		}
		assertTrue("Name is null", hasError);
		
		hasError = false;
		try {
			contactManager.addNewContact("name", null);
		} catch (NullPointerException ex) {
			hasError = true;
		}
		assertTrue("Notes is null", hasError);
	}
	
	@Test
	public void getContactsStringWillReturnTheWholeListWhenEmpty() {
		contactManager = new ContactManagerImpl();
		Set<Contact> longSet = addContacts();
		
		Set<Contact> set = contactManager.getContacts("");
		
		assertEquals("longList", 4, longSet.size());
		assertEquals("full list", 4, set.size());
		
		List<Contact> list = new LinkedList<Contact>(set);
		List<Contact> longList = new LinkedList<Contact>(longSet);
		
		Collections.sort(list, (c1, c2) -> c1.getId() - c2.getId());
		Collections.sort(longList, (c1, c2) -> c1.getId() - c2.getId());
		
		Iterator<Contact> listIt = list.iterator();
		Iterator<Contact> longListIt = longList.iterator();
		int i = 0;
		while(listIt.hasNext()) {
			ContactImpl current = (ContactImpl) listIt.next();
			Contact compare = (Contact) longListIt.next();
			assertTrue(i + " loop", current.equals(compare));
		}
	}
	
	private Set<Contact> addContacts() {
		Set<Contact> list = new HashSet<>();
		
		int id1 = contactManager.addNewContact("John", "Short");
		list.add(new ContactImpl(id1, "John", "Short"));
		
		int id2 = contactManager.addNewContact("Janet", "Tall");
		list.add(new ContactImpl(id2, "Janet", "Tall"));
		
		int id3 = contactManager.addNewContact("John", "Angry");
		list.add(new ContactImpl(id3, "John", "Angry"));
		
		int id4 = contactManager.addNewContact("Andrew", "Calm");
		list.add(new ContactImpl(id4, "Andrew", "Calm"));
		
		return list;
	}
	
	@Test
	public void getContactsStringReturnsSetForGivenName() {
		contactManager = new ContactManagerImpl();
		List<Contact> fullList = new LinkedList<Contact>(addContacts());
		LinkedList<Contact> johnList = new LinkedList<Contact>(contactManager.getContacts("John"));
		LinkedList<Contact> janetList = new LinkedList<Contact>(contactManager.getContacts("Janet"));
		
		Collections.sort(fullList, (c1, c2) -> c1.getId() - c2.getId());
		Collections.sort(johnList, (c1, c2) -> c1.getId() - c2.getId());
		Collections.sort(janetList, (c1, c2) -> c1.getId() - c2.getId());
		
		assertEquals("john", 2, johnList.size());
		assertEquals("janet", 1, janetList.size());
		
		assertEquals(1, johnList.pop().getId());
		assertEquals(3, johnList.pop().getId());
		assertTrue(johnList.isEmpty());
		
		assertEquals(2, janetList.pop().getId());
		assertTrue(janetList.isEmpty());
		
		LinkedList<Contact> emptyList = new LinkedList<Contact>(contactManager.getContacts("Gertrud"));
		assertTrue(emptyList.isEmpty());
	}
	
	@Test
	public void getContactsStringThrowsNullPointerExceptionWhenNameIsNull() {
		boolean hasError = false;
		try {
			String dud = null;
			contactManager.getContacts(dud);
		} catch (NullPointerException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void getContactsIdsReturnsOneContactFromOneId() {
		contactManager = new ContactManagerImpl();
		LinkedList<Contact> fullList = new LinkedList<Contact>(addContacts());
		Collections.sort(fullList, (c1, c2) -> c1.getId() - c2.getId());
		
		LinkedList<Contact> johnList = new LinkedList<Contact>(contactManager.getContacts(1));		
		assertEquals("size", 1, johnList.size());
		
		ContactImpl expected = (ContactImpl) fullList.pop();
		ContactImpl actual = (ContactImpl) johnList.pop();
		assertTrue(expected.equals(actual));
	}
	
	@Test
	public void getContactsIdsReturnsTwoContactsFromTwoIds() {
		contactManager = new ContactManagerImpl();
		LinkedList<Contact> fullList = new LinkedList<Contact>(addContacts());
		Collections.sort(fullList, (c1, c2) -> c1.getId() - c2.getId());
		
		LinkedList<Contact> smallList = new LinkedList<Contact>(contactManager.getContacts(1, 3));
		Collections.sort(smallList, (c1, c2) -> c1.getId() - c2.getId());
		assertEquals("size",2, smallList.size());
		
		ContactImpl expected = (ContactImpl) fullList.pop();
		ContactImpl actual = (ContactImpl) smallList.pop();
		assertTrue("first", expected.equals(actual));
		
		fullList.pop();
		expected = (ContactImpl) fullList.pop();
		actual = (ContactImpl) smallList.pop();
		assertTrue("second", expected.equals(actual));
	}
	
	@Test
	public void getContactsIdsThrowsIllegalArgumentExceptionWhenNoIds() {
		boolean hasError = false;
		try {
			contactManager.getContacts();
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void getContactsIdsThrowsIllegalArgumentExceptionWhenAnIdNotFound() {
		boolean hasError = false;
		try {
			contactManager.getContacts(2, 300);
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test 
	public void addFutureMeetingThrowsIllegalArgumentExceptionIfAContactIsUnknown() {
		boolean hasError = false;
		try {
			Set<Contact> list = new HashSet<>();		
			list.add(new ContactImpl(6, "Arnold", "Short"));
			list.add(new ContactImpl(1, "John", "Short"));
			Calendar date = Calendar.getInstance();
			date.set(3000, 2, 2);
			contactManager.addFutureMeeting(list, date);
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test 
	public void getFutureMeetingListThrowsIllegalArgumentExceptionIfAContactIsUnknown() {
		boolean hasError = false;
		try {			
			contactManager.getFutureMeetingList(new ContactImpl(6, "Arnold", "Short"));
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test 
	public void getFutureMeetingListThrowsNullPointerExceptionIfContactIsNull() {
		boolean hasError = false;
		try {
			contactManager.getFutureMeetingList(null);
		} catch (NullPointerException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test 
	public void getFutureMeetingListThrowsNullPointerExceptionIfDateIsNull() {
		boolean hasError = false;
		try {
			contactManager.getMeetingListOn(null);
		} catch (NullPointerException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test 
	public void getPastMeetingListThrowsIllegalArgumentExceptionIfAContactIsUnknown() {
		boolean hasError = false;
		try {			
			contactManager.getPastMeetingListFor(new ContactImpl(6, "Arnold", "Short"));
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test 
	public void getPastMeetingListThrowsNullPointerExceptionIfAContactIsNull() {
		boolean hasError = false;
		try {			
			contactManager.getPastMeetingListFor(null);
		} catch (NullPointerException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void addNewPastMeetingThrowsIllegalArgumentExceptionIfAContactNotExists() {
		boolean hasError = false;
		try {
			Set<Contact> contacts = new HashSet<>();
			contacts.add(new ContactImpl(6, "Arnold", "Short"));
			Calendar date = Calendar.getInstance();
			date.set(2000, 2, 2);
			contactManager.addNewPastMeeting(contacts, date, "hello");
		} catch (IllegalArgumentException ex) {
			hasError = true;
		}
		assertTrue(hasError);
	}
	
	@Test
	public void getThePastMeetingTwo() {
		addPastMeetings();
		assertNotNull(contactManager.getPastMeeting(1));
	}
	
	@Test
	public void shouldNotChangeExistingContactsWhenGetContactsIsCalled() {
		Set<Contact> before = new TreeSet<>();
		before.addAll(contactManager.getContacts(""));
		contactManager.getContacts(1,2);
		Set<Contact> after = new TreeSet<>();		
		after.addAll(contactManager.getContacts(""));
		
		assertEquals(before, after);
	}
}
















