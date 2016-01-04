import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

public abstract class MeetingImpl implements Meeting{
	
	private int m_id;
	private Calendar m_date;
	private Set<Contact> m_contacts;
	
	public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		//Will also throw NullPointerException if date or contacts is Null.
		if(id <= 0 || !date.isSet(Calendar.YEAR) || !date.isSet(Calendar.MONTH) || !date.isSet(Calendar.DAY_OF_MONTH) || contacts.size() == 0) {
			throw new IllegalArgumentException();
		}
		
		m_id = id;
		m_date = date;
		m_contacts = new TreeSet<>();
		m_contacts.addAll(contacts);
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
}