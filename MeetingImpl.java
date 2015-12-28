import java.util.Calendar;
import java.util.Set;
import java.util.Collections;

public abstract class MeetingImpl implements Meeting, Comparable<Meeting>{
	
	private int m_id;
	private Calendar m_date;
	private Set<Contact> m_contacts;
	
	public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		//Will also throw NullPointerException if date or contacts is Null.
		if(id <= 0 || !date.isSet(0) || !date.isSet(1) || !date.isSet(2) || contacts.size() == 0) {
			throw new IllegalArgumentException();
		}
		
		m_id = id;
		m_date = date;
		m_contacts = contacts;
	}
	
	public int getId() {
		return m_id;
	}
	
	public Calendar getDate() {
		return m_date;
	}
	
	public Set<Contact> getContacts() {
		return m_contacts;
	}
	
	public boolean equals(Meeting meeting) {
		return (m_id == meeting.getId() &&
				m_date.equals(meeting.getDate()) &&
				m_contacts.equals(meeting.getContacts())
				);
	}
	
	@Override
	public int compareTo(Meeting m) {
		Calendar comparedDate = m.getDate();
		if (m_date.after(comparedDate)) {
			return 1;
		} else if (m_date.before(comparedDate)) {
			return -1;
		} else {
			return 0;
		}
	}
}