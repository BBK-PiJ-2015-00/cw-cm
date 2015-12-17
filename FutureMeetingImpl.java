import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl implements FutureMeeting {
	
	private int fm_id;
	private Calendar fm_date;
	private Set<Contact> fm_contacts;
	
	public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		if(!date.isSet(0) || !date.isSet(1) || !date.isSet(2) || contacts.size() == 0) {
			throw new IllegalArgumentException();
		}
		
		fm_id = id;
		fm_date = date;
		fm_contacts = contacts;
	}	
	
	public int getId() {
		return fm_id;
	}
	
	public Calendar getDate() {
		return fm_date;
	}
	
	public Set<Contact> getContacts() {
		return fm_contacts;
	}
}