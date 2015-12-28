import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
	
	private String pm_notes;
	
	public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
		super(id, date, contacts);
		
		if(notes.equals(null)) {
			throw new NullPointerException();
		}
		pm_notes = notes;
	}
	
	public String getNotes() {
		return pm_notes;
	}
	
	public boolean equals(Meeting meeting) {
		if(meeting.getClass() != PastMeetingImpl.class) {
			return false;
		}
		
		PastMeetingImpl pm = (PastMeetingImpl) meeting;
		return (super.equals(meeting) && pm_notes.equals(pm.getNotes()));
	}
}