import java.util.Calendar;
import java.util.Set;

public abstract class MeetingImpl implements Meeting {
	
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
}