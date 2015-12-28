public class ContactImpl implements Contact {
	private int c_id;
	private String c_name;
	private String c_notes;	
	
	public ContactImpl(int id, String name, String notes) {
		if(id<=0) {
			throw new IllegalArgumentException();
		} else if (name.equals(null) || notes.equals(null)) {
			throw new NullPointerException();
		}
		
		this.c_id = id;
		this.c_name = name;
		this.c_notes = notes;
	}
	
	public ContactImpl(int id, String name) {
		this(id, name, "");
	}
	
	public int getId() {
		return c_id;
	}
	
	public String getName() {
		return c_name;
	}
	
	public String getNotes() {
		return c_notes;
	}
	
	public void addNotes(String note) {
		c_notes += note;
	}
	
	public boolean equals(Contact contact) {
		return (c_id == contact.getId() &&
				c_name.equals (contact.getName()) &&
				c_notes.equals (contact.getNotes())
				);
	}
}