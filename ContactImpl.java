public class ContactImpl implements Contact {
	private int c_id;
	
	public ContactImpl(int id, String name, String notes) {
		if(id<=0) {
			throw new IllegalArgumentException();
		}
		
		this.c_id = id;
	}
	
	public ContactImpl(int id, String name) {
		if(id<=0) {
			throw new IllegalArgumentException();
		}
		
		this.c_id = id;
	}
	
	public int getId() {
		return c_id;
	}
	
	public String getName() {
		return "";
	}
	
	public String getNotes() {
		return "";
	}
	
	public void addNotes(String note) {
		return;
	}
}