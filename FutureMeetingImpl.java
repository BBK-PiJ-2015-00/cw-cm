import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {
	
	public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts) {
		super(id, date, contacts);
	}	
	
	@Override
	public int getId() {
		return super.getId();
	}
	
	@Override
	public Calendar getDate() {
		return super.getDate();
	}
	
	@Override
	public Set<Contact> getContacts() {
		return super.getContacts();
	}
}