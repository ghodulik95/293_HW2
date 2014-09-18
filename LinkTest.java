/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */
//Link J UNIT TESTING
//These are the J unit tests for Link.  Each function is named test{function Name}
//and tests that function name.
//Some of them expect the correct error values.

import static org.junit.Assert.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class LinkTest {
	Link l;
	SocialNetworkStatus status;
	
	@Test
	public void testIsValid(){
		l = new Link();
		status = new SocialNetworkStatus(ErrorStatus.INVALID_DATE);
		assertFalse("A new link should not be valid.", l.isValid());
		Set<User> s = makeUserSet("Bob", "Mary");
		l.setUsers(s, status);
		assertTrue("Setting users should set status to success when successful", status.getStatus() == ErrorStatus.SUCCESS);
		assertTrue("A link with users should be valid.", l.isValid());
	}
	
	@Test
	public void testSetUser(){
		l = new Link();
		status = new SocialNetworkStatus(ErrorStatus.INVALID_DATE);
		l.setUsers(new HashSet<User>(), status);
		assertTrue("setUsers should set status to INVALID_USERS when set of users does not have size 2",
				status.getStatus() == ErrorStatus.INVALID_USERS);
		String[] ids = {"Billy", "Joe", "Mary", "Sue", "Marty" };
		l.setUsers(makeUserSet(ids), status.setStatus(ErrorStatus.SUCCESS));
		assertTrue("setUsers should set status to INVALID_USERS when set of users does not have size 2",
				status.getStatus() == ErrorStatus.INVALID_USERS);
		l.setUsers(makeUserSet("Boe", "Joe"), status);
		assertTrue("setUsers should set status to SUCCESS when given a set of users size 2, and the link is previously invalid", 
				status.getStatus() == ErrorStatus.SUCCESS);
		assertTrue("Successfully setting the users should make the link valid", l.isValid());
		l.setUsers(makeUserSet(ids), status);
		assertTrue("Trying to set the users after the link is valid should set status to ALREADY_VALID",
				status.getStatus() == ErrorStatus.ALREADY_VALID);
		l.setUsers(makeUserSet("Boe", "Joe"), status);
		assertTrue("Trying to set the users after the link is valid should set status to ALREADY_VALID",
				status.getStatus() == ErrorStatus.ALREADY_VALID);
	}
	
	@Test
	public void testGetUsers(){
		l = new Link();
		Set<User> userSet = makeUserSet("Bob", "Mary");
		l.setUsers(userSet, new SocialNetworkStatus(ErrorStatus.SUCCESS));
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
		status = new SocialNetworkStatus(ErrorStatus.SUCCESS);
		l.setUsers(makeUserSet("Bob", "Mary"), status);
		try{
			//First Event
			l.tearDown(Date.valueOf("2013-11-2"), status);
			assertEquals("Tearing down as the first event should set status to ALREADY_INCACTIVE", status.getStatus(), ErrorStatus.ALREADY_INACTIVE);
			l.establish(Date.valueOf("2013-11-2"), status);
			assertTrue("Establishing a date as the first event should set status to SUCCESS", status.is( ErrorStatus.SUCCESS ));
			
			for(int i = 3; i < 30; i++){
				if( i % 2 == 1){
					l.establish(Date.valueOf("2013-11-"+i), status);
					assertTrue("Establishing a date as the "+(i-1)+"th event should set status to ALREADY_ACTIVE", status.is( ErrorStatus.ALREADY_ACTIVE));
					l.tearDown(Date.valueOf("2013-11-"+(i-2)), status);
					assertTrue("Tearing down a link before the most recent event should set status to INVALID_DATE", status.is( ErrorStatus.INVALID_DATE));
					l.tearDown(Date.valueOf("2013-11-"+i), status);
					assertTrue("Tearing down a link after the most recent date in a link should set status to SUCCESS.", status.is( ErrorStatus.SUCCESS ));
				}else{
					l.tearDown(Date.valueOf("2013-11-"+i), status);
					assertTrue("Tearing down a date as the "+(i-1)+"th event should set status to ALREADY_INACTIVE", status.is( ErrorStatus.ALREADY_INACTIVE));
					l.establish(Date.valueOf("2013-11-"+(i-2)), status);
					assertTrue("Establishing a link before the most recent event should set status to INVALID_DATE", status.is( ErrorStatus.INVALID_DATE));
					l.establish(Date.valueOf("2013-11-"+i), status);
					assertTrue("Eastablishing a link after the most recent date in a link should set status to SUCCESS.", status.is( ErrorStatus.SUCCESS ));
				}
			}
			
			return l;
		}catch(UninitializedObjectException e){
			fail("establish() thinks that the link is not valid.");
		}
		return null;
	}
	
	@Test 
	public void testEstablishAndTearDownHere(){
		testEstablishAndTearDown();
	}
	
	@Test
	public void testEstablishAndTearDownWithSameDate(){
		//Events with the same date should be allowed, and when finding
		//the next date or testing if a date is active, the last date
		//should be used when there are multiple events with the same date
		
		l = new Link();
		status = new SocialNetworkStatus(ErrorStatus.SUCCESS);
		l.setUsers(makeUserSet("Bob", "Bill"), status);
		Date date = Date.valueOf("2013-12-12");
		try{
			l.establish(date, status);
			assertTrue("Establishing the first date should set status to SUCCESS", status.is( ErrorStatus.SUCCESS));
			l.tearDown(date,status);
			assertTrue("TearingDown on the same date should set status to SUCCESS",status.is( ErrorStatus.SUCCESS));
			l.establish(date, status);
			assertTrue("Establishing the first date should set status to SUCCESS", status.is( ErrorStatus.SUCCESS));
			assertTrue("The link should be active since the last event was establish", l.isActive(date));
			l.tearDown(date,status);
			assertTrue("TearingDown on the same date should set status to SUCCESS",status.is( ErrorStatus.SUCCESS));
			assertFalse("The link should not be active since the last date was tear down", l.isActive(date));
			assertEquals("The next date should be null since it is the same as the given date", l.nextEvent(date), null);
		}catch(UninitializedObjectException e){
			fail("Establishing and tearing down on the same date should not throw an exception");
		}
		
	}
	
	@Test(expected=UninitializedObjectException.class)
	public void testEstablishWhenInvalid() throws UninitializedObjectException{
		l = new Link();
		l.establish(Date.valueOf("2011-3-3"), new SocialNetworkStatus(ErrorStatus.SUCCESS));
	}
	
	@Test(expected=UninitializedObjectException.class)
	public void testTearDownWhenInvalid() throws UninitializedObjectException{
		l = new Link();
		l.tearDown(Date.valueOf("2011-3-3"), new SocialNetworkStatus(ErrorStatus.SUCCESS));
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
