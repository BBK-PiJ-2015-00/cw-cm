/*
import java.util.Calendar;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collections;
import java.util.Arrays;
import java.util.Comparator;
*/
import java.util.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class ContactManagerImpl implements ContactManager {
	
	private List<Meeting> cm_meetings;
	private Set<Contact> cm_contacts;
	private int cm_meetingId;
	private int cm_contactId;
	private final String seperator = ";";
	
	private Comparator<Contact> contactComparator = new Comparator<Contact>() {
		@Override
		public int compare(Contact c1, Contact c2) {
			return c1.getId() - c2.getId();
		}
	};
	
	public ContactManagerImpl() {		
		
		cm_meetings = new LinkedList<Meeting>();
		cm_contacts = new TreeSet<>(contactComparator);
		cm_meetingId = 1;
		cm_contactId = 0;
		
		String filename = "contacts.txt";
		File file = new File(filename);
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));		
			cm_contacts.addAll(readContacts(in));
			readMeetings(in);
		} catch (FileNotFoundException ex) {
			System.out.println("File " + file + " does not exists.");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} 
		}
	}
	
	private Set<Contact> readContacts(BufferedReader in) throws IOException {
		Set<Contact> result = new HashSet<>();
		String line;
		line = in.readLine();
		if(line == null) {
			return result;
		}
		String[] values = line.split(seperator);
		int maxId = 0;
		
		for(int i = 0; i < values.length; i+=3) {
			int id = Integer.parseInt(values[i]);
			String name = values[i+1];
			String notes = values[i+2];
			
			result.add (new ContactImpl(id, name, notes));
			maxId = Math.max(maxId, id);
		}
		cm_contactId = maxId;		
		return result;
	}
	
	private void readMeetings(BufferedReader in) throws IOException {		
		
		int maxId = 0;
		String line;
		while((line = in.readLine()) != null) {
			
			String[] values = line.split(seperator);			
			String token = values[0];
			int id = Integer.parseInt(values[1]);
			Calendar date = stringToDate(values[2]);			
			Set<Contact> contacts = readContacts(in);
			
			Meeting meeting;
			if(token.equals("F")) {
				meeting = new FutureMeetingImpl(id, date, contacts);
			} else {
				String notes = "";
				if(values.length > 3) {
					notes = values[3];
				}
				meeting = new PastMeetingImpl(id, date, contacts, notes);
			}
			cm_meetings.add(meeting);
			maxId = Math.max(maxId, id);
		}
		cm_meetingId = maxId + 1;
	}
	
	private Calendar stringToDate(String str) {
		
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM d H:m:s z y");
			Date date = formatter.parse(str);
			
			Calendar result = Calendar.getInstance();
			result.setTime(date);
			return result;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		} 
	}
	
	private boolean contactExists(Set<Contact> contacts) {
		
		for(Iterator<Contact> contactsIt = contacts.iterator(); contactsIt.hasNext(); ) {			
			if(!contactExists(contactsIt.next())) {
				return false;
			}
		}		
		return true;
	}
	
	private boolean contactExists(Contact contact) {
		
		ContactImpl currentContact = (ContactImpl) contact;
		for(Iterator<Contact> cm_contactsIt = cm_contacts.iterator(); cm_contactsIt.hasNext(); ) {
			Contact compare = cm_contactsIt.next();
			if(currentContact.equals(compare)) {
				return true;
			}
		}			
		return false;
	}
	
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
		
		Calendar dateNow = Calendar.getInstance();
		if(date.before(dateNow) || contacts.contains(null) || !contactExists(contacts)) {
			throw new IllegalArgumentException();
		}
		int result = cm_meetingId;
		cm_meetings.add(new FutureMeetingImpl(cm_meetingId, date, contacts));
		cm_meetingId++;
		return result;
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
		
		if(!contactExists(contact)) {
			throw new IllegalArgumentException();
		}
		
		List<Meeting> result = new LinkedList<Meeting>();		
		for(Iterator<Meeting> meetingsIt = cm_meetings.iterator(); meetingsIt.hasNext(); ) {
			
			Meeting currentMeeting = meetingsIt.next();
			
			if(currentMeeting.getClass() != FutureMeetingImpl.class) {
				continue;
			}
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
		
		if(date.equals(null)) {
			throw new NullPointerException();
		}
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
		
		if(!contactExists(contact)) {
			throw new IllegalArgumentException();
		}
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
		
		if(contacts.contains(null) || !contactExists(contacts)) {
			throw new IllegalArgumentException();
		}			
		cm_meetings.add(new PastMeetingImpl(cm_meetingId, date, contacts, text));
		cm_meetingId++;	
	}
	
	public PastMeeting addMeetingNotes(int id, String text) {
		
		if(text.equals(null)) {
			throw new NullPointerException();
		}
		//find meeting index
		int index = 0;
		Meeting temp = null;
		for(Iterator<Meeting> meetingsIt = cm_meetings.iterator(); meetingsIt.hasNext(); index++) {
			
			Meeting current = meetingsIt.next();			
			if(id == current.getId()) {
				temp = current;
				break;
			}
		}
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
		//convert meeting to a past meeting
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
		LinkedList<Contact> contactsCopy = new LinkedList<Contact>(cm_contacts);
		Arrays.sort(ids);
		
		for(int i = 0; i < ids.length; i++) {
			boolean foundId = false;
			int id = ids[i];			
			for(int j = 0; j < contactsCopy.size(); j++) {
				Contact current = contactsCopy.pop();
				if(current.getId() == id) {
					result.add(current);
					foundId = true;
					break;
				}
			}
			if(!foundId) {
				throw new IllegalArgumentException();
			}
		}
		
		return result;
	}
	
	public void flush() {
		String filename = "contacts.txt";		
		File file = new File(filename);		
		PrintWriter out = null;
		try {
			out = new PrintWriter(file);
			writeContacts(out, cm_contacts);
			writeMeetings(out);
		} catch (FileNotFoundException ex) {
			System.out.println("Cannot write to file " + file + ".");
		} finally {
			out.close();
		}
	}
	
	private void writeContacts(PrintWriter out, Set<Contact> contacts) {
		StringBuffer sb = new StringBuffer("");
		
		for(Iterator<Contact> it = contacts.iterator(); it.hasNext(); ) {
			
			Contact current = it.next();			
			sb.append(current.getId() + seperator);
			sb.append(current.getName() + seperator);
			sb.append(current.getNotes() + seperator);
		}
		
		out.println(sb.toString());
	}
	
	private void writeMeetings(PrintWriter out) {
		
		for(Iterator<Meeting> it = cm_meetings.iterator(); it.hasNext(); ) {
			
			Meeting current = it.next();
			StringBuffer sb = new StringBuffer("");
			String token = "";
			String notes = "";
			if(current.getClass() == FutureMeetingImpl.class) {
				token = "F";
			} else {
				PastMeeting temp = (PastMeeting) current;
				token = "P";
				notes = temp.getNotes();
			}
			sb.append(token + seperator);
			sb.append(current.getId() + seperator);
			sb.append(current.getDate().getTime() + seperator);
			sb.append(notes + seperator);
			
			out.println(sb.toString());
			writeContacts(out, current.getContacts());
		}
	}
}















