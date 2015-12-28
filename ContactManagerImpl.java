import java.util.Calendar;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.Iterator;

public class ContactManagerImpl implements ContactManager {
	
	private List<Meeting> cm_meetings;
	private int cm_uniqueId;
	
	public ContactManagerImpl() {
		cm_meetings = new LinkedList<Meeting>();
		cm_uniqueId = 0;
	}
	
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		cm_uniqueId++;
		cm_meetings.add(new FutureMeetingImpl(cm_uniqueId, date, contacts));
		return cm_uniqueId;
	}
	
	public PastMeeting getPastMeeting(int id) {
		return null;
	}
	
	public FutureMeeting getFutureMeeting(int id) {
		return (FutureMeeting) getMeeting(id);
	}
	
	public Meeting getMeeting(int id) {
		Meeting current = cm_meetings.get(0);
		
		if(id == current.getId()) {
			return current;
		}
		
		return null;
	}
	
	public List<Meeting> getFutureMeetingList(Contact contact) {
		List<Meeting> result = new LinkedList<Meeting>();
		
		for(Iterator<Meeting> meetingsIt = cm_meetings.iterator(); meetingsIt.hasNext(); ) {
			Meeting currentMeeting = meetingsIt.next();
			Set<Contact> contacts = currentMeeting.getContacts();
			
			for(Iterator<Contact> contactsIt = contacts.iterator(); contactsIt.hasNext(); ) {
				ContactImpl currentContact = (ContactImpl) contactsIt.next();
				if (currentContact.equals(contact)) {
					result.add(currentMeeting);
					break;
				}
			}
		}
		
		return result;
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