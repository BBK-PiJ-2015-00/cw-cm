import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl implements FutureMeeting {
	private int fm_id;
	private Calendar fm_date;
	
	public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		fm_id = id;
		fm_date = date;
	}
	
	
	public int getId() {
		return fm_id;
	}
	
	public Calendar getDate() {
		return fm_date;
	}
	
	public Set<Contact> getContacts() {
		return null;
	}
}