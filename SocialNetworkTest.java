/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */

//SocialNetwork J UNIT TESTING
//These are the J unit tests for SocialNetwork.  Each function is named test{function Name}
//and tests that function name.

import static org.junit.Assert.*;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {
	SocialNetwork s;
	SocialNetworkStatus status;
	
	@Test
	public void testNeighborhoodTrend(){
		boolean viewToStrings = false;
		s = new SocialNetwork();
		status = new SocialNetworkStatus(ErrorStatus.SUCCESS);
		Map<Date, Integer> nTrendB = new HashMap<Date, Integer>();
		User u1 = new User();
		u1.setID("1");
		User u2 = new User();
		u2.setID("2");
		User u3 = new User();
		u3.setID("3");
		s.addUser(u1); s.addUser(u2); s.addUser(u3);
		Set<String> u1Andu2 = new HashSet<String>();
		u1Andu2.add("1"); u1Andu2.add("2");
		//Test if a neighborhood of 2 is formed
		Date date1 = Date.valueOf("2013-11-12");
		s.establishLink(u1Andu2, date1, status);
		nTrendB.put(date1, 2);
		Map<Date, Integer> nTrendA = s.neighborhoodTrend("1", status);
		if(viewToStrings) System.out.println(nTrendA+" "+status.getStatus());
		assertTrue(status.isSuccess());
		assertEquals(nTrendA, nTrendB);
		
		Set<String> u2Andu3 = new HashSet<String>();
		u2Andu3.add("2"); u2Andu3.add("3");
		//Test if a neighborhood of 2 is formed
		date1 = Date.valueOf("2013-11-13");
		s.establishLink(u2Andu3, date1, status);
		nTrendB.put(date1, 3);
		nTrendA = s.neighborhoodTrend("1", status);
		if(viewToStrings) System.out.println(nTrendA+" "+status.getStatus());
		assertTrue(status.isSuccess());
		assertEquals(nTrendA, nTrendB);
		
		date1 = Date.valueOf("2013-11-14");
		s.tearDownLink(u2Andu3, date1, status);
		nTrendB.put(date1, 2);
		nTrendA = s.neighborhoodTrend("1", status);
		if(viewToStrings) System.out.println(nTrendA+" "+status.getStatus());
		assertTrue(status.isSuccess());
		assertEquals(nTrendB, nTrendA);
		
		Set<String> u1Andu3 = new HashSet<String>();
		u1Andu3.add("1"); u1Andu3.add("3");
		date1 = Date.valueOf("2013-11-14");
		s.establishLink(u1Andu3, date1, status);
		nTrendB.remove(date1);
		nTrendA = s.neighborhoodTrend("1", status);
		if(viewToStrings) System.out.println(nTrendA+" "+status.getStatus());
		assertTrue(status.isSuccess());
		assertEquals(nTrendB, nTrendA);
	}
	
	@Test
	public void testNeighborhood(){
		boolean viewToStrings = false;
		User u1 = new User();
		u1.setID("1");
		User u2 = new User();
		u2.setID("2");
		User u3 = new User();
		u3.setID("3");
		
		s = new SocialNetwork();
		status = new SocialNetworkStatus(ErrorStatus.SUCCESS);
		
		s.addUser(u1); s.addUser(u2); s.addUser(u3);
		Set<String> u1Andu2 = new HashSet<String>();
		u1Andu2.add("1"); u1Andu2.add("2");
		
		//Test if a neighborhood of 2 is formed
		Date date1 = Date.valueOf("2013-11-12");
		s.establishLink(u1Andu2, date1, status);
		Set<Friend> neighborhood = s.neighborhood("1", date1, status);
		assertTrue("The neighborhood should have been successful.", status.isSuccess());
		for(Friend f : neighborhood){
			if(viewToStrings) System.out.println(f.toString());
		}
		
		//Test if a neighborhood of 2 is formed
		Set<String> u2Andu3 = new HashSet<String>();
		u2Andu3.add("2"); u2Andu3.add("3");
		s.establishLink(u2Andu3, date1, status);
		neighborhood = s.neighborhood("1", date1, status);
		assertTrue("The neighborhood should have been successful.", status.isSuccess());
		if(viewToStrings) System.out.println("\nTest 2-----");
		for(Friend f : neighborhood){
			if(viewToStrings) System.out.println(f.toString());
		}
		
		//Test if a neighborhood of 2 is formed, but this time both should have distances of 0.
		Set<String> u1Andu3 = new HashSet<String>();
		u1Andu3.add("1"); u1Andu3.add("3");
		s.establishLink(u1Andu3, date1, status);
		neighborhood = s.neighborhood("1", date1, status);
		assertTrue("The neighborhood should have been successful.", status.isSuccess());
		if(viewToStrings) System.out.println("\nTest 3-----");
		for(Friend f : neighborhood){
			if(viewToStrings) System.out.println(f.toString());
			try{
				if(f.getUser().getID().compareTo("1") != 0)
					assertTrue("Distance of friends should all be 1 here.", f.getDistance() == 1);
			}catch(Exception e){
				fail();
			}
		}
		
		//Test if a neighborhood of 2 is formed, but this time one distance should be 2 again
		//because we tear down a link, and torn down links should not count.
		s.tearDownLink(u1Andu3, date1, status);
		neighborhood = s.neighborhood("1", date1, status);
		assertTrue("The neighborhood should have been successful.", status.isSuccess());
		if(viewToStrings) System.out.println("\nTest 4-----");
		for(Friend f : neighborhood){
			if(viewToStrings) System.out.println(f.toString());
		}
		//s.establishLink(u1Andu3, date1, status);
		
		//Now I will make a longer link and I want to see if the distance is correct
		for(int i = 4; i < 10; i++){
			User user = new User();
			user.setID(String.valueOf(i));
			s.addUser(user);
			Set<String> idSet = new HashSet<String>();
			idSet.add(String.valueOf(i));
			idSet.add(String.valueOf(i - 1));
			s.establishLink(idSet, date1, status);
		}
		
		neighborhood = s.neighborhood("1", date1, status);
		assertTrue("The neighborhood should have been successful.", status.isSuccess());
		if(viewToStrings) System.out.println("\nTest 5-----");
		for(Friend f : neighborhood){
			if(viewToStrings) System.out.println(f.toString());
			try{
				int id = Integer.valueOf(f.getUser().getID());
				assertTrue(f.getDistance() == id - 1);
			}catch(Exception e){
				fail();
			}
		}
		
		//Now I will test with a specified max distance
		neighborhood = s.neighborhood("1", date1, 3, status);
		assertTrue("The neighborhood should have been successful.", status.isSuccess());
		if(viewToStrings) System.out.println("\nTest 5-----");
		for(Friend f : neighborhood){
			if(viewToStrings) System.out.println(f.toString());
			try{
				int id = Integer.valueOf(f.getUser().getID());
				assertTrue(f.getDistance() == id - 1);
				assertTrue(f.getDistance() <= 3);
			}catch(Exception e){
				fail();
			}
		}
		
		//Check invalidin input
		s.neighborhood("1", date1, -1,  status);
		assertTrue(status.is(ErrorStatus.INVALID_DISTANCE));
		s.neighborhood("12342", date1, status);
		assertTrue(status.is(ErrorStatus.INVALID_USERS));
	}
	
	@Test
	public void testAddUser(){
		s = new SocialNetwork();
		User u = makeUser("Bob");
		assertTrue("Adding a user that is not already in the network should return true.", s.addUser(u));
		assertFalse("Adding a user that is already in the network should return false.", s.addUser(u));
		User u2 = makeUser("Bob");
		assertFalse("Adding a user that is already in the network should return false.", s.addUser(u2));
		User u3 = new User();
		assertFalse("Adding an invalid user should return false", s.addUser(u3));
	}
	
	@Test
	public void testIsMember(){
		s = new SocialNetwork();
		User u1 = makeUser("Bob");
		//User u3 = makeUser("Jobe");
		//User u4 = makeUser("Mary");
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
		//User u3 = makeUser("Jobe");
		//User u4 = makeUser("Mary");
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
		status = new SocialNetworkStatus(ErrorStatus.SUCCESS);
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
		
		s.establishLink(names, Date.valueOf("2012-11-13"), status);
		assertTrue("Trying to establish a link of more than two users should set status to INVALID_USERS.", 
				status.is( ErrorStatus.INVALID_USERS));
		names.remove(u5.getID());
		names.remove(u4.getID());
		names.remove(u3.getID());
		s.establishLink(names, Date.valueOf("2012-11-13"), status);
		assertTrue("Trying to establish a link of less than two users should set status to INVALID_USERS.", 
				status.is( ErrorStatus.INVALID_USERS));
		names.add(u3.getID());
		s.establishLink(names, Date.valueOf("2012-11-13"), status);
		assertTrue("Tring to establish a link of two users should set status to success.", status.isSuccess());
		s.establishLink(names, Date.valueOf("2012-11-15"), status);
		assertTrue("Trying to establish a link when the link is already active should set status to ALREADY_ACTIVE",
				status.is( ErrorStatus.ALREADY_ACTIVE));
		s.establishLink(makeUserStringSet(u1,u5), Date.valueOf("2012-12-25"), status);
		assertTrue("Trying to establish a link of two unlinked users should set status to SUCCESS.", status.isSuccess());
		s.establishLink(makeUserStringSet(u1,u5), Date.valueOf("2012-12-27"), status);
		assertTrue("Trying to establish a link when the link is already active should set status to ALREADY_ACTIVE.",
				status.is( ErrorStatus.ALREADY_ACTIVE));
	
		//Now test tear down link
		s.tearDownLink(makeUserStringSet(u3,u1), Date.valueOf("2012-11-20"), status);
		assertTrue("Tearing down an active established link should set status to SUCCESS", 
				status.isSuccess());
		s.tearDownLink(makeUserStringSet(u1,u3), Date.valueOf("2014-12-31"), status);
		assertTrue("Tearing down an inactive link should set status to ALREADY_INACTIVE", 
				status.is(ErrorStatus.ALREADY_INACTIVE));
		names.add(u5.getID());
		s.tearDownLink(names, Date.valueOf("2015-12-12"), status);
		assertTrue("Given invalid input ( > 2 ids in set) to tear down should set status to INVALID_USERS.", 
				status.is(ErrorStatus.INVALID_USERS));
		names.remove(u5.getID());
		names.remove(u1.getID());
		s.tearDownLink(names, Date.valueOf("2015-12-12"), status.setStatus(ErrorStatus.SUCCESS));
		assertTrue("Given invalid input ( < 2 ids in set) to tear down should set status to INVALID_USERS.", 
				status.is(ErrorStatus.INVALID_USERS));
		s.tearDownLink(makeUserStringSet(u1, u4), Date.valueOf("2013-11-12"), status);
		assertTrue("Trying to tear down a link that doesn't exist should set status to ALREADY_INACTIVE.", 
				status.is(ErrorStatus.ALREADY_INACTIVE));
	}
	
	@Test
	public void testIsActive(){
		s = new SocialNetwork();
		status = new SocialNetworkStatus(ErrorStatus.SUCCESS);
		assertFalse("A link that doesn't exist should not be active.", s.isActive(makeUserStringSet("Bobby", "Billy"), Date.valueOf("2004-12-3")));
		User u1 = makeUser("Bob");
		User u2 = makeUser("Billy");
		User u3 = makeUser("Goat");
		
		s.addUser(u1);
		s.addUser(u2);
		s.addUser(u3);
		
		s.establishLink(makeUserStringSet(u1, u2), Date.valueOf("2012-12-11"), status);
		s.tearDownLink(makeUserStringSet(u2, u1), Date.valueOf("2012-12-15"), status);
		s.establishLink(makeUserStringSet(u1, u2), Date.valueOf("2012-12-20"), status);
		s.tearDownLink(makeUserStringSet(u1, u2), Date.valueOf("2012-12-25"), status);
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
