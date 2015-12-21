import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;

//java org.junit.runner.JUnitCore ContactManagerShould

public class ContactManagerShould {
	private ContactManager contactManager;
	
	@Before
	public void createManager() {
		contactManager = new ContactManagerImpl();
	}
	
	@Test
	public void notBeNull() {
		assertNotNull(contactManager);
	}
	
}