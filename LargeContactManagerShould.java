import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;
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

//java org.junit.runner.JUnitCore LargeContactManagerShould

public class LargeContactManagerShould {
	
	private ContactManager contactManager;
		
	@Before
	public void createManager() {
		File file = new File("contacts.txt");
		try {
			file.delete();
			file.createNewFile();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		contactManager = new ContactManagerImpl();
		//add contacts
		Set<Contact> team1 = new HashSet<>();
		Set<Contact> team2 = new HashSet<>();
		Set<Contact> team3 = new HashSet<>();
		Set<Contact> team4 = new HashSet<>();
		int id = contactManager.addNewContact("Henry", "abc");
		Contact hen1 = new ContactImpl(id, "Henry", "abc");
		
		id = contactManager.addNewContact("Janet", "aaa");
		Contact jan2 = new ContactImpl(id, "Janet", "aaa");
		
		id = contactManager.addNewContact("John", "bbb");
		Contact joh3 = new ContactImpl(id, "John", "bbb");
		
		id = contactManager.addNewContact("Henry", "abdddc");
		Contact hen4 = new ContactImpl(id, "Henry", "abdddc");
		
		id = contactManager.addNewContact("Janet", "ccc");
		Contact jan5 = new ContactImpl(id, "Janet", "ccc");
		
		id = contactManager.addNewContact("Susan", "desa");
		Contact sus6 = new ContactImpl(id, "Susan", "desa");
		
		id = contactManager.addNewContact("Henry", "hhrd");
		Contact hen7 = new ContactImpl(id, "Henry", "hhrd");
		
		team1.add(hen1);
		team1.add(hen4);
		team1.add(joh3);
		
		team2.add(hen1);
		team2.add(jan2);
		team2.add(sus6);
		team2.add(hen7);
		
		team3.add(hen1);
		team3.add(jan2);
		team3.add(sus6);
		team3.add(hen7);
		
		team4.add(jan2);
		team4.add(jan5);
		team4.add(joh3);
		team4.add(hen4);
		team4.add(hen1);
		
		//add PastMeetings
		Calendar pastDate1 = Calendar.getInstance();
		pastDate1.clear();
		pastDate1.set(2000,4,4);
		
		Calendar pastDate2 = Calendar.getInstance();
		pastDate2.clear();
		pastDate2.set(2001,4,3);
		
		Calendar pastDate3 = Calendar.getInstance();
		pastDate3.clear();
		pastDate3.set(2001,4,4);
		
		Calendar pastDate4 = Calendar.getInstance();
		pastDate4.clear();
		pastDate4.set(1950,4,4);
		
		contactManager.addNewPastMeeting(team1, pastDate1, "notes");
		contactManager.addNewPastMeeting(team2, pastDate2, "");
		contactManager.addNewPastMeeting(team3, pastDate3, "more notes");
		contactManager.addNewPastMeeting(team4, pastDate4, "");
	
		//add FutureMeetings
		Calendar futureDate1 = Calendar.getInstance();
		futureDate1.clear();
		futureDate1.set(3000,4,4);
		
		Calendar futureDate2 = Calendar.getInstance();
		futureDate2.clear();
		futureDate2.set(2900,4,4);
		
		Calendar futureDate3 = Calendar.getInstance();
		futureDate3.clear();
		futureDate3.set(2900,4,5);
		
		Calendar futureDate4 = Calendar.getInstance();
		futureDate4.clear();
		futureDate4.set(3050,4,4);
		
		contactManager.addFutureMeeting(team1, futureDate1);
		contactManager.addFutureMeeting(team2, futureDate2);
		contactManager.addFutureMeeting(team3, futureDate3);
		contactManager.addFutureMeeting(team4, futureDate4);
	}
	
	@Test
	public void getTheMeetings() {
		for(int i = 1; i < 9; i++) {
			assertNotNull(i + "", contactManager.getMeeting(i));
		}
		assertNull(contactManager.getMeeting(9));
	}
	
	@Test
	public void onlyReturnPastMeetingWhenGettingPastMeeting() {
		for(int i = 1; i < 5; i++) {
			PastMeeting meeting = contactManager.getPastMeeting(i);
			assertNotNull(i + ": not null", meeting);
			assertTrue(i + ": true", meeting.getClass() == PastMeetingImpl.class);			
		}
		for(int i = 5; i < 9; i++) {
			boolean hasError = false;
			try {
				PastMeeting meeting = contactManager.getPastMeeting(i);
			} catch (IllegalStateException ex) {
				hasError = true;
			}
			assertTrue(i + "", hasError);
		}
	}
	
	@Test
	public void onlyReturnFutureMeetingWhenGettingFutureMeeting() {
		for(int i = 1; i < 5; i++) {
			boolean hasError = false;
			try {
				FutureMeeting meeting = contactManager.getFutureMeeting(i);
			} catch (IllegalStateException ex) {
				hasError = true;
			}
			assertTrue(i + "", hasError);			
		}
		for(int i = 5; i < 9; i++) {			
			FutureMeeting meeting = contactManager.getFutureMeeting(i);
			assertNotNull(i + ": not null", meeting);
			assertTrue(i + ": true", meeting.getClass() == FutureMeetingImpl.class);
		}
	}
	
	@Test
	public void onlyReturnFutureMeetingsWhenGettingFutureMeetingList() {
		Contact hen4 = new ContactImpl(4, "Henry", "abdddc");
		List<Meeting> list = contactManager.getFutureMeetingList(hen4);
		
		int index = 0;
		for(Iterator<Meeting> it = list.iterator(); it.hasNext(); index++) {
			Meeting current = it.next();
			assertTrue(index + "", current.getClass() == FutureMeetingImpl.class);
		}
	}
	
	@Test
	public void onlyReturnPastMeetingsWhenGettingPastMeetingList() {
		Contact hen4 = new ContactImpl(4, "Henry", "abdddc");
		List<PastMeeting> list = contactManager.getPastMeetingListFor(hen4);
		
		int index = 0;
		for(Iterator<PastMeeting> it = list.iterator(); it.hasNext(); index++) {
			Meeting current = it.next();
			assertTrue(index + "", current.getClass() == PastMeetingImpl.class);
		}
	}
	
	@Test
	public void notThrowErrorsWhenFlushIsCalled() {
		contactManager.flush();
		contactManager = null;
	}
	
	@Test
	public void readTxtFileToGetContactsOnOpen() {
		contactManager.flush();
		contactManager = new ContactManagerImpl();
		
		ContactImpl hen1 = new ContactImpl(1, "Henry", "abc");		
		ContactImpl jan2 = new ContactImpl(2, "Janet", "aaa");		
		ContactImpl joh3 = new ContactImpl(3, "John", "bbb");		
		ContactImpl hen4 = new ContactImpl(4, "Henry", "abdddc");		
		ContactImpl jan5 = new ContactImpl(5, "Janet", "ccc");		
		ContactImpl sus6 = new ContactImpl(6, "Susan", "desa");		
		ContactImpl hen7 = new ContactImpl(7, "Henry", "hhrd");
		
		Set<Contact> list = contactManager.getContacts("");
		Iterator<Contact> it = list.iterator();
		
		assertTrue("list is empty", it.hasNext());
		assertTrue("hen1", hen1.equals(it.next()));
		assertTrue("jan2", jan2.equals(it.next()));
		assertTrue("joh3", joh3.equals(it.next()));
		assertTrue("hen4", hen4.equals(it.next()));
		assertTrue("jan5", jan5.equals(it.next()));
		assertTrue("sus6", sus6.equals(it.next()));
		assertTrue("hen7", hen7.equals(it.next()));
	}
		
	@Test
	public void readTxtFileToGetMeetingsOnOpen() {
		
		readTxtFileToGetContactsOnOpen();
		List<Meeting> expectedList = makeMeetings();
		
		int i = 1;
		for(Iterator<Meeting> it = expectedList.iterator(); it.hasNext(); i++) {
			MeetingImpl expected = (MeetingImpl) it.next();
			Meeting actual = contactManager.getMeeting(i);
			
			assertTrue("Meeting " + i, expected.equals(actual));
		}
		
	}
	
	private List<Meeting> makeMeetings() {
		List<Meeting> list = new LinkedList<Meeting>();
		Set<Contact> team1 = new HashSet<>();
		Set<Contact> team2 = new HashSet<>();
		Set<Contact> team3 = new HashSet<>();
		Set<Contact> team4 = new HashSet<>();
		Contact hen1 = new ContactImpl(1, "Henry", "abc");		
		Contact jan2 = new ContactImpl(2, "Janet", "aaa");		
		Contact joh3 = new ContactImpl(3, "John", "bbb");		
		Contact hen4 = new ContactImpl(4, "Henry", "abdddc");		
		Contact jan5 = new ContactImpl(5, "Janet", "ccc");		
		Contact sus6 = new ContactImpl(6, "Susan", "desa");		
		Contact hen7 = new ContactImpl(7, "Henry", "hhrd");
		team1.add(hen1);
		team1.add(hen4);
		team1.add(joh3);		
		team2.add(hen1);
		team2.add(jan2);
		team2.add(sus6);
		team2.add(hen7);		
		team3.add(hen1);
		team3.add(jan2);
		team3.add(sus6);
		team3.add(hen7);		
		team4.add(jan2);
		team4.add(jan5);
		team4.add(joh3);
		team4.add(hen4);
		team4.add(hen1);
		Calendar pastDate1 = Calendar.getInstance();
		pastDate1.clear();
		pastDate1.set(2000,4,4);		
		Calendar pastDate2 = Calendar.getInstance();
		pastDate2.clear();
		pastDate2.set(2001,4,3);		
		Calendar pastDate3 = Calendar.getInstance();
		pastDate3.clear();
		pastDate3.set(2001,4,4);		
		Calendar pastDate4 = Calendar.getInstance();
		pastDate4.clear();
		pastDate4.set(1950,4,4);
		list.add(new PastMeetingImpl(1,pastDate1,  team1, "notes"));
		list.add(new PastMeetingImpl(2,pastDate2, team2, ""));
		list.add(new PastMeetingImpl(3,pastDate3, team3, "more notes"));
		list.add(new PastMeetingImpl(4,pastDate4, team4, ""));
		Calendar futureDate1 = Calendar.getInstance();
		futureDate1.clear();
		futureDate1.set(3000,4,4);		
		Calendar futureDate2 = Calendar.getInstance();
		futureDate2.clear();
		futureDate2.set(2900,4,4);		
		Calendar futureDate3 = Calendar.getInstance();
		futureDate3.clear();
		futureDate3.set(2900,4,5);		
		Calendar futureDate4 = Calendar.getInstance();
		futureDate4.clear();
		futureDate4.set(3050,4,4);
		list.add(new FutureMeetingImpl(5,futureDate1, team1));
		list.add(new FutureMeetingImpl(6,futureDate2, team2));
		list.add(new FutureMeetingImpl(7,futureDate3, team3));
		list.add(new FutureMeetingImpl(8,futureDate4, team4));
		return list;
	}
	
}






















