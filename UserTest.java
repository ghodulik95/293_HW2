import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest {
	private User u;
	
	@Test
	public void testIsValid(){
		bla();
		assertFalse("A user that does not have any set id should be invalid", u.isValid());
		u.setID("Bob");
		assertTrue("A user that has a set id should be valid", u.isValid());
	}
	public void bla(){
		u = new User();
	}
}
