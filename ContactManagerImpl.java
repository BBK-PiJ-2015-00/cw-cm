import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class ContactManagerImpl implements ContactManager {
	
	private List<Meeting> meetings;
	private int uniqueId;
	
	public ContactManagerImpl() {
		meetings = new ArrayList<Meeting>();
		uniqueId = 0;
	}
	
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		uniqueId++;
		meetings.add(new FutureMeetingImpl(uniqueId, date, contacts));
		return uniqueId;
	}
	
	public PastMeeting getPastMeeting(int id) {
		return null;
	}
	
	public FutureMeeting getFutureMeeting(int id) {
		return (FutureMeeting) getMeeting(id);
	}
	
	public Meeting getMeeting(int id) {
		Meeting current = meetings.get(0);
		
		if(id == current.getId()) {
			return current;
		}
		
		return null;
	}
	
	public List<Meeting> getFutureMeetingList(Contact contact) {
		return null;
	}
	
	public List<Meeting> getMeetingListOn(Calendar date) {
		return null;
	}
	
	public List<PastMeeting> getPastMeetingListFor(Contact contact) {
		return null;
	}
	
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
		
	}
	
	public PastMeeting addMeetingNotes(int id, String text) {
		return null;
	}
	
	public int addNewContact(String name, String notes) {
		return -1;
	}
	
	public Set<Contact> getContacts(String name) {
		return null;
	}
	
	public Set<Contact> getContacts(int... ids) {
		return null;
	}
	
	public void flush() {
		
	}
}