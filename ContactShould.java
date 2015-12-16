import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

//java org.junit.runner.JUnitCore ContactShould

public class ContactShould {
	private Contact contact;
	
	@Test
	public void createContact() {
		contact = new ContactImpl();
	}
}