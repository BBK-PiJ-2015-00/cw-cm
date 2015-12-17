import java.util.Calendar;
import java.util.Set;
import java.util.HashSet;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

//java org.junit.runner.JUnitCore MeetingShould

public class MeetingShould {
	
	private Meeting futureMeeting;
	private Calendar date = Calendar.getInstance();
	private static Set<Contact> contacts;
	
	@BeforeClass
	public static void buildUp() {
		contacts = new HashSet<>();
		contacts.add(new ContactImpl(1, "Sam", "Not nice"));
		contacts.add(new ContactImpl(2, "Jenna"));
		contacts.add(new ContactImpl(3, "Arthur"));
		contacts.add(new ContactImpl(4, "Annie", "Oranges"));
	}
	
	@Before
	public void createMeetings() {
		futureMeeting = new FutureMeetingImpl(1, date, contacts);
	}
	
	@Test
	public void notBeNull() {
		assertNotNull(futureMeeting);
	}
	
	@Test
	public void getTheId() {
		assertEquals(1, futureMeeting.getId());
	}
	
	@Test
	public void getTheDate() {
		Calendar expected = (Calendar) date.clone();
		assertEquals(expected, futureMeeting.getDate());
	}
}