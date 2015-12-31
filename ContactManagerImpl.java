import java.util.Calendar;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;

public class ContactManagerImpl implements ContactManager {
	
	private List<Meeting> cm_meetings;
	private Set<Contact> cm_contacts;
	private int cm_meetingId;
	private int cm_contactId;	
	
	public ContactManagerImpl() {
		cm_meetings = new LinkedList<Meeting>();
		cm_contacts = new HashSet<>();
		cm_meetingId = 0;
		cm_contactId = 0;
	}
	
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		
		Calendar dateNow = Calendar.getInstance();
		if(date.before(dateNow) || contacts.contains(null)) {
			throw new IllegalArgumentException();
		}
		
		cm_meetingId++;
		cm_meetings.add(new FutureMeetingImpl(cm_meetingId, date, contacts));
		return cm_meetingId;
	}
	
	public PastMeeting getPastMeeting(int id) {
		
		Meeting meeting = getMeeting(id);
		if(meeting != null && meeting.getClass() == FutureMeetingImpl.class) {
			throw new IllegalStateException();
		}
		
		return (PastMeeting) meeting;
	}
	
	public FutureMeeting getFutureMeeting(int id) {
		
		Meeting meeting = getMeeting(id);
		if(meeting != null && meeting.getClass() == PastMeetingImpl.class) {
			throw new IllegalStateException();
		}
		
		return (FutureMeeting) meeting;
	}
	
	public Meeting getMeeting(int id) {
		for(Iterator<Meeting> meetingsIt = cm_meetings.iterator(); meetingsIt.hasNext(); ) {
			
			Meeting current = meetingsIt.next();
			
			if(id == current.getId()) {
				return current;
			}
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
		
		Collections.sort(result, (m1, m2) -> m1.getDate().compareTo (m2.getDate()));
		return result;
	}
	
	public List<Meeting> getMeetingListOn(Calendar date) {
		List<Meeting> result = new LinkedList<Meeting>();
		
		for(Iterator<Meeting> meetingsIt = cm_meetings.iterator(); meetingsIt.hasNext(); ) {
			Meeting currentMeeting = meetingsIt.next();
			Calendar currentDate = currentMeeting.getDate();
			
			if(sameDate(currentDate, date)) {
				result.add(currentMeeting);
			}
		}
		
		Collections.sort(result, (m1, m2) -> m1.getDate().compareTo (m2.getDate()));
		return result;
	}
	
	private boolean sameDate(Calendar date1, Calendar date2) {
		int year = date1.get(Calendar.YEAR) - date2.get(Calendar.YEAR);
		int month = date1.get(Calendar.MONTH) - date2.get(Calendar.MONTH);
		int day = date1.get(Calendar.DAY_OF_MONTH) - date2.get(Calendar.DAY_OF_MONTH);
		
		return year==0 && month==0 && day==0;
	}
	
	public List<PastMeeting> getPastMeetingListFor(Contact contact) {
		List<PastMeeting> result = new LinkedList<PastMeeting>();
		
		for(Iterator<Meeting> meetingsIt = cm_meetings.iterator(); meetingsIt.hasNext(); ) {
			
			Meeting currentMeeting = meetingsIt.next();
			if(currentMeeting.getClass() != PastMeetingImpl.class) {
				continue;
			}	
			Set<Contact> contacts = currentMeeting.getContacts();
			
			for(Iterator<Contact> contactsIt = contacts.iterator(); contactsIt.hasNext(); ) {
				ContactImpl currentContact = (ContactImpl) contactsIt.next();
				if (currentContact.equals(contact)) {
					result.add((PastMeeting) currentMeeting);
					break;
				}
			}
		}
		
		Collections.sort(result, (m1, m2) -> m1.getDate().compareTo (m2.getDate()));
		return result;
	}
	
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
		
		if(contacts.contains(null)) {
			throw new IllegalArgumentException();
		}
		cm_meetingId++;
		cm_meetings.add(new PastMeetingImpl(cm_meetingId, date, contacts, text));
	}
	
	public PastMeeting addMeetingNotes(int id, String text) {
		
		if(text.equals(null)) {
			throw new NullPointerException();
		}	
		Meeting temp = getMeeting(id);
		if(temp == null) {
			throw new IllegalArgumentException();
		}
		
		Calendar dateNow = Calendar.getInstance();
		Calendar date = temp.getDate();
		if(dateNow.before(date)) {
			throw new IllegalStateException();
		}		
		Set<Contact> contacts = temp.getContacts();
		String notes = "";
		if(temp.getClass() == PastMeetingImpl.class) {
			PastMeeting pTemp = (PastMeeting) temp;
			notes += pTemp.getNotes();
		}
		notes += text;
		
		PastMeeting result = new PastMeetingImpl(id, date, contacts, notes);
		
		//find meeting index
		int index = -1;
		for(Iterator<Meeting> meetingsIt = cm_meetings.iterator(); meetingsIt.hasNext(); ) {
			index++;
			Meeting current = meetingsIt.next();
			
			if(id == current.getId()) {
				break;
			}
		}
		cm_meetings.set(index, result);
		
		return result;
	}
	
	public int addNewContact(String name, String notes) {
		
		//also throws NullPointerException if name or notes are null
		if(name.equals("") || notes.equals("")) {
			throw new IllegalArgumentException();
		}
		cm_contactId++;
		cm_contacts.add(new ContactImpl(cm_contactId, name, notes));
		return cm_contactId;
	}
	
	public Set<Contact> getContacts(String name) {
		
		//throws NullPointerException if name is null.
		if(name.equals("")) {
			return cm_contacts;
		}
		Set<Contact> result = new HashSet<>();
		for(Iterator<Contact> contactsIt = cm_contacts.iterator(); contactsIt.hasNext(); ) {
			Contact current = contactsIt.next();
			if(current.getName().equals(name)) {
				result.add(current);
			}
		}
		
		return result;
	}
	
	public Set<Contact> getContacts(int... ids) {
		
		if(ids.length == 0) {
			throw new IllegalArgumentException();
		}
		
		Set<Contact> result = new HashSet<>();
		for(int i = 0; i < ids.length; i++) {
			int id = ids[i];			
			for(Iterator<Contact> contactsIt = cm_contacts.iterator(); contactsIt.hasNext(); ) {
				Contact current = contactsIt.next();
				if(current.getId() == id) {
					result.add(current);
					break;
				}
			}
		}
		
		return result;
	}
	
	public void flush() {
		
	}
}















