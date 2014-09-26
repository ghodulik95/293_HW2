/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */
//Friend J UNIT TESTING
//Tests for Friend Class

import static org.junit.Assert.*;
import org.junit.Test;

public class FriendTest {
	Friend f;
	User u;
	
	@Test
	public void testFriend(){
		f = new Friend();
		u = new User();
		u.setID("01234");
		try{
			u.setFirstName("George");
			u.setLastName("Hodulik");
			u.setPhoneNumber("12334445");
		}catch(Exception e){
			fail();
		}
		
		assertTrue("First set of Friend should return true.",f.set(u, 0));
		assertFalse("After first set of Friend, set should return false,", f.set(u, 1));
		User u2 = null;
		int d = -1;
		try{
			u2 = f.getUser();
			d = f.getDistance();
		}catch(Exception e){
			fail();
		}
		assertEquals("Get user should return the same user.", u2, u);
		assertEquals("Distance should be 0.", d, 0);
		//Uncomment to test toString();
		//System.out.println(f.toString());
	}
}
