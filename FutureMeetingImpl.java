import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl implements FutureMeeting {
	private int fm_id;
	
	public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		fm_id = id;
	}
	
	
	public int getId() {
		return fm_id;
	}
	
	public Calendar getDate() {
		return null;
	}
	
	public Set<Contact> getContacts() {
		return null;
	}
}