//George Hodulik
//gmh73@case.edu
//SocialNetwork J UNIT TESTING
//These are the J unit tests for SocialNetwork.  Each function is named test{function Name}
//and tests that function name.

import static org.junit.Assert.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {
	SocialNetwork s;
	
	@Test
	public void testAddUser(){
		s = new SocialNetwork();
		User u = makeUser("Bob");
		assertTrue("Adding a user that is not already in the network should return true.", s.addUser(u));
		assertFalse("Adding a user that is already in the network should return false.", s.addUser(u));
		User u2 = makeUser("Bob");
		assertFalse("Adding a user that is already in the network should return false.", s.addUser(u2));
	}
	
	@Test
	public void testIsMember(){
		s = new SocialNetwork();
		User u1 = makeUser("Bob");
		User u3 = makeUser("Jobe");
		User u4 = makeUser("Mary");
		User u5 = makeUser("Grace");
		
		s.addUser(u1);
		assertTrue("Bob should be a member.", s.isMember("Bob"));
		assertFalse("Mary should not be a member.", s.isMember("Mary"));
		s.addUser(u5);
		assertTrue("Bob should be a member.", s.isMember("Bob"));
		assertTrue("Grace should be a member.", s.isMember("Grace"));
		assertFalse("Mary should not be a member.", s.isMember("Mary"));
		assertFalse("Jobe should not be a member.", s.isMember("Jobe"));
	}
	
	@Test
	public void testGetUser(){
		s = new SocialNetwork();
		User u1 = makeUser("Bob");
		User u3 = makeUser("Jobe");
		User u4 = makeUser("Mary");
		User u5 = makeUser("Grace");
		
		s.addUser(u1);
		s.addUser(u5);
		
		assertEquals("Get Bob should return the user with id Bob.", s.getUser("Bob").getID(), "Bob");
		assertEquals("Get Grace should return the user with id Grace.", s.getUser("Grace").getID(), "Grace");
		assertEquals("Get Jobe should return null because Jobe is not in the network.", s.getUser("Jobe"), null);
		assertEquals("Get Mary should return null because Mary is not in the network.", s.getUser("Mary"), null);
	}
	
	@Test
	public void testEstablishLinkAndTearDownLink(){
		s = new SocialNetwork();
		User u1 = makeUser("Bob");
		User u3 = makeUser("Jobe");
		User u4 = makeUser("Mary");
		User u5 = makeUser("Grace");
		
		s.addUser(u1);
		s.addUser(u3);
		s.addUser(u4);
		s.addUser(u5);
		
		Set<String> names = new HashSet<String>();
		names.add(u1.getID());
		names.add(u3.getID());
		names.add(u4.getID());
		names.add(u5.getID());
		
		assertFalse("Trying to establish a link of more than two users should return false.", s.establishLink(names, Date.valueOf("2012-11-13")));
		names.remove(u5.getID());
		names.remove(u4.getID());
		names.remove(u3.getID());
		assertFalse("Trying to establish a link of less than two users should return false.", s.establishLink(names, Date.valueOf("2012-11-13")));
		names.add(u3.getID());
		assertTrue("Tring to establish a link of two users should return true.", s.establishLink(names, Date.valueOf("2012-11-13")));
		assertFalse("Trying to establish a link when the link is already active should return false", s.establishLink(names, Date.valueOf("2012-11-15")));
		assertTrue("Trying to establish a link of two unlinked users should return true.", s.establishLink(makeUserStringSet(u1,u5), Date.valueOf("2012-12-25")));
		assertFalse("Trying to establish a link when the link is already active should return false.", s.establishLink(makeUserStringSet(u1,u5), Date.valueOf("2012-12-27")));
	
		//Now test tear down link
		assertTrue("Tearing down an active established link should return true", s.tearDownLink(makeUserStringSet(u3,u1), Date.valueOf("2012-11-20")));
		assertFalse("Tearing down an inactive link should return false", s.tearDownLink(makeUserStringSet(u1,u3), Date.valueOf("2014-12-31")));
		names.add(u5.getID());
		assertFalse("Given invalid input ( > 2 ids in set) to tear down should return false.", s.tearDownLink(names, Date.valueOf("2015-12-12")));
		names.remove(u5.getID());
		names.remove(u1.getID());
		assertFalse("Given invalid input ( < 2 ids in set) to tear down should return false.", s.tearDownLink(names, Date.valueOf("2015-12-12")));
		assertFalse("Trying to tear down a link that doesn't exist should return false.", s.tearDownLink(makeUserStringSet(u1, u4), Date.valueOf("2013-11-12")));
	}
	
	@Test
	public void testIsActive(){
		s = new SocialNetwork();
		assertFalse("A link that doesn't exist should not be active.", s.isActive(makeUserStringSet("Bobby", "Billy"), Date.valueOf("2004-12-3")));
		User u1 = makeUser("Bob");
		User u2 = makeUser("Billy");
		User u3 = makeUser("Goat");
		
		s.addUser(u1);
		s.addUser(u2);
		s.addUser(u3);
		
		s.establishLink(makeUserStringSet(u1, u2), Date.valueOf("2012-12-11"));
		s.tearDownLink(makeUserStringSet(u2, u1), Date.valueOf("2012-12-15"));
		s.establishLink(makeUserStringSet(u1, u2), Date.valueOf("2012-12-20"));
		s.tearDownLink(makeUserStringSet(u1, u2), Date.valueOf("2012-12-25"));
		assertFalse("This link should be inactive at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-09")));
		assertTrue("This link should be active at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-11")));
		assertTrue("This link should be active at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-14")));
		
		assertFalse("This link should be inactive at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-17")));
		assertFalse("This link should be inactive at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-15")));
		assertTrue("This link should be active at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-14")));
		assertTrue("This link should be active at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-20")));
		assertTrue("This link should be active at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-21")));

		assertFalse("This link should be inactive at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-25")));
		assertFalse("This link should be inactive at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-28")));
		assertTrue("This link should be active at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-20")));
		assertTrue("This link should be active at this time.", s.isActive(makeUserStringSet(u1, u2), Date.valueOf("2012-12-24")));
	}
	
	//makes a user from an id
	private User makeUser(String id){
		User u = new User();
		u.setID(id);
		return u;
	}
	
	//makes a user set from two string ids
	private Set<User> makeUserSet(String id1, String id2){
		User u1 = new User();
		u1.setID(id1);
		
		User u2 = new User();
		u2.setID(id2);
		
		Set<User> output = new HashSet<User>();
		output.add(u1);
		output.add(u2);
		
		return output;
	}
	
	//makes a set of id strings from two users
	private Set<String> makeUserStringSet(User u1, User u2){
		Set<String> output = new HashSet<String>();
		output.add(u1.getID());
		output.add(u2.getID());
		
		return output;
	}
	
	//makes a set of id string from two given strings
	private Set<String> makeUserStringSet(String id1, String id2){
		Set<String> output = new HashSet<String>();
		output.add(id1);
		output.add(id2);
		
		return output;
	}
}
