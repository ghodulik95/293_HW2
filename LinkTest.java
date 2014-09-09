import static org.junit.Assert.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class LinkTest {
	Link l;
	
	@Test
	public void testIsValid(){
		l = new Link();
		assertFalse("A new link should not be valid.", l.isValid());
		Set<User> s = makeUserSet("Bob", "Mary");
		assertTrue("Setting users should return true when successful", l.setUsers(s));
		assertTrue("A link with users should be valid.", l.isValid());
	}
	
	@Test
	public void testSetUser(){
		l = new Link();
		assertFalse("setUsers should return false when set of users does not have size 2", l.setUsers(new HashSet<User>()));
		String[] ids = {"Billy", "Joe", "Mary", "Sue", "Marty" };
		assertFalse("setUsers should return false when the set of users does not have size 2", l.setUsers(makeUserSet(ids)));
		assertTrue("setUsers should return true when given a set of users size 2, and the link is previously invalid", l.setUsers(makeUserSet("Boe", "Joe")));
		assertTrue("Successfully setting the users should make the link valid", l.isValid());
		assertFalse("Trying to set the users after the link is valid should return false", l.setUsers(makeUserSet(ids)));
		assertFalse("Trying to set the users after the link is valid should return false", l.setUsers(makeUserSet("Boe", "Joe")));
	}
	
	@Test
	public void testGetUsers(){
		l = new Link();
		Set<User> userSet = makeUserSet("Bob", "Mary");
		l.setUsers(userSet);
		try{
			Set<User> s = l.getUsers();
			assertTrue("A set containing the original two users should be returned.", s.equals(userSet));
		}catch(UninitializedObjectException e){
			fail("Users in l should have been initialized.");
		}
	}
	
	@Test(expected=UninitializedObjectException.class)
	public void testGetUsersUninitializedSet() throws UninitializedObjectException{
		l = new Link();
		l.getUsers();
	}
	
	//Not a test, because this builds a good event to test toString()
	public Link testEstablishAndTearDown(){
		l = new Link();
		l.setUsers(makeUserSet("Bob", "Mary"));
		try{
			//First Event
			assertFalse("Tearing down as the first event should return false", l.tearDown(Date.valueOf("2013-11-2")));
			assertTrue("Establishing a date as the first event should return true.", l.establish(Date.valueOf("2013-11-2")));
			
			for(int i = 3; i < 30; i++){
				if( i % 2 == 1){
					assertFalse("Establishing a date as the "+(i-1)+"th event should return false", l.establish(Date.valueOf("2013-11-"+i)));
					assertFalse("Tearing down a link before the most recent event should return false",l.tearDown(Date.valueOf("2013-11-"+(i-2))));
					assertTrue("Tearing down a link after the most recent date in a link should return true.", l.tearDown(Date.valueOf("2013-11-"+i)));
				}else{
					assertFalse("Tearing down a date as the "+(i-1)+"th event should return false", l.tearDown(Date.valueOf("2013-11-"+i)));
					assertFalse("Establishing a link before the most recent event should return false",l.establish(Date.valueOf("2013-11-"+(i-2))));
					assertTrue("Eastablishing a link after the most recent date in a link should return true.", l.establish(Date.valueOf("2013-11-"+i)));
				}
			}
			
			return l;
		}catch(UninitializedObjectException e){
			fail("establish() thinks that the link is not valid.");
		}
		return null;
	}
	
	@Test(expected=UninitializedObjectException.class)
	public void testEstablishWhenInvalid() throws UninitializedObjectException{
		l = new Link();
		l.establish(Date.valueOf("2011-3-3"));
	}
	
	@Test(expected=UninitializedObjectException.class)
	public void testTearDownWhenInvalid() throws UninitializedObjectException{
		l = new Link();
		l.tearDown(Date.valueOf("2011-3-3"));
	}
	
	//testIsActive calls testEstablishAndTearDown()
	@Test
	public void testIsActive(){
		l = testEstablishAndTearDown();
		try{
			//In the returned link, even days should be active, and odd inactive
			for(int i = 2; i <= 29; i++){
				if(i % 2 == 0){
					assertTrue("Day "+i+" should be active.", l.isActive(Date.valueOf("2013-11-"+i)));
				}else{
					assertFalse("Day "+i+" should not be active", l.isActive(Date.valueOf("2013-11-"+i)));
				}
			}
		}catch(UninitializedObjectException e){
			fail("isActive() thinks the link is not valid");
		}
	}
	
	@Test(expected=UninitializedObjectException.class)
	public void testIsActiveWhenInvalid() throws UninitializedObjectException{
		l = new Link();
		l.isActive(Date.valueOf("2014-9-20"));
	}
	
	@Test
	public void testFirstEvent(){
		l = testEstablishAndTearDown();
		try {
			assertEquals("The first event should be the first added event.", l.firstEvent(), Date.valueOf("2013-11-2"));
		} catch (UninitializedObjectException e) {
			fail("firstEvent() thinks the link is invalid.");
		}
	}
	
	@Test(expected=UninitializedObjectException.class)
	public void testFirstEventWhenInvalid() throws UninitializedObjectException{
		l = new Link();
		l.firstEvent();
	}
	
	@Test
	public void testNextEvent(){
		l = testEstablishAndTearDown();
		try{
			for(int i = 1; i <= 28; i++){
				assertEquals("The date after "+i+" should be "+(i+1)+".", l.nextEvent(Date.valueOf("2013-11-"+i)), Date.valueOf("2013-11-"+(i+1)));
			}
			assertTrue("nextEvent() should return null when there is no next event.", l.nextEvent(Date.valueOf("2013-11-29")) == null);
			assertTrue("nextEvent() should return null when there is no next event.", l.nextEvent(Date.valueOf("2013-12-10")) == null);
			assertEquals("When given a date you do not have, nextEvent() should return the date of the event after.", 
					l.nextEvent(Date.valueOf("2013-10-15")), Date.valueOf("2013-11-2"));
			
		}catch(UninitializedObjectException e){
			fail("NextEvent() thinks the link is invalid.");
		}
	}
	
	//testToString calls testEstablishAndTearDown()
	@Test
	public void testToString(){
		l = testEstablishAndTearDown();
		//UNCOMMENT TO SEE WHAT toString() RETURNS
		//System.out.println(l.toString());
	}
	
	@Test
	public void testEquals(){
		l = testEstablishAndTearDown();
		Link l2 = new Link();
		l2 = testEstablishAndTearDown();
		
		assertTrue("l should equal l2.", l.equals(l2));
	}
	
	//makes a set of users with the given ids
	private Set<User> makeUserSet(String id1, String id2){
		User u1 = new User();
		User u2 = new User();
		
		if(!u1.setID(id1)) fail("setID() thinks id1 is null.");
		if(!u2.setID(id2)) fail("setID() thinks id2 is null.");
		
		Set<User> s = new HashSet<User>();
		s.add(u1);
		s.add(u2);
		return s;
	}
	
	//make a set of users with the given string array of ids
	private Set<User> makeUserSet(String[] ids){
		Set<User> s = new HashSet<User>();
		for(int i = 0; i < ids.length; i++){
			User u = new User();
			if(!u.setID(ids[i])) 
				fail("setID thinks id"+i+" is null.");
			s.add(u);
		}
		return s;
	}
	
}
