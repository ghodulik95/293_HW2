//George Hodulik
//gmh73@case.edu
//USER J UNIT TESTING
//These are the J unit tests for user.  Each function is named test{function Name}
//and tests that function name.
//Some of them expect the correct error values.

import static org.junit.Assert.*;
import org.junit.Test;

public class UserTest {
	private User u;
	
	@Test
	public void testIsValid(){
		u = new User();
		assertFalse("A user that does not have any set id should be invalid", u.isValid());
		u.setID("Bob");
		assertTrue("A user that has a set id should be valid", u.isValid());
	}
	
	@Test
	public void testGetID(){
		u = new User();
		assertNull("A user that has not has an ID set should return getID of null.", u.getID());
		u.setID("Bob");
		assertEquals("The user has ID set to Bob.  GetID should return the String \"Bob\"", u.getID(), "Bob");
	}
	
	@Test(expected=NullPointerException.class)
	public void testSetIDNullInput(){
		u = new User();
		u.setID("");
	}
	
	@Test
	public void testSetIDValidInput(){
		u = new User();
		try{
			assertTrue("When the ID is successfully set, setID() should return true.", u.setID("Bob"));
			assertFalse("When the ID is already set, setID() should return false.", u.setID("Bobla"));
		}catch(NullPointerException e){
			fail("setID() thinks it was given null input.");
		}
	}
	
	@Test
	public void testToString(){
		u = new User();
		assertEquals("toString() should be an error string when the user is invalid.", u.toString(), "Invalid User: Uninitialized ID");
		u.setID("Bobby");
		assertEquals("toString() should return readable data (the user's id).", u.toString(), "Bobby");
		
	}
	
	@Test
	public void testEquals(){
		u = new User();
		u.setID("Bobbb");
		User u2 = new User();
		u2.setID("Bobbb");
		assertEquals("Users with the same id should be equal.", u, u2);
	}
	
	@Test
	public void testhashCode(){
		u = new User();
		u.setID("Bobbb");
		User u2 = new User();
		u2.setID("Bobbb");
		assertEquals("Users with the same id should be equal.", u.hashCode(), u2.hashCode());
	}
}
