import java.util.Calendar;
import java.util.Set;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

//java org.junit.runner.JUnitCore MeetingShould

public class MeetingShould {
	
	private Meeting futureMeeting;
	private Calendar date = new Calendar();
	private Set<Contact> contacts;
	
	@BeforeClass
	public static void buildUp() {
		contacts.add(new Contact(1, "Sam", "Not nice"));
		contacts.add(new Contact(2, "Jenna"));
		contacts.add(new Contact(3, "Arthur"));
		contacts.add(new Contact(4, "Annie", "Oranges"));
	}
	
	@Before
	public void createMeetings() {
		futureMeeting = new FutureMeetingImpl(1, date, contacts);
	}
}