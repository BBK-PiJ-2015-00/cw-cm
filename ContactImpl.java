public class ContactImpl implements Contact {
	
	
	public ContactImpl(int id, String name, String notes) {
		if(id<=0) {
			throw new IllegalArgumentException();
		}
		
	}
	
	public ContactImpl(int id, String name) {
		
	}
	
	public int getId() {
		return 0;
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