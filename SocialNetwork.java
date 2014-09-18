

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */
 /*
 * SOCIALNETWORK CLASS
 * This is the SocialNetwork class. It is a data structure containing sets of users and links.
 * It contains all the required public functions. 
 *
 */
public class SocialNetwork {
	//I decided to represent the set of users and links as Maps
	//I didn't use a Set because there is no get function,
	//and I wanted constant time for .contains(), so a list would not be optimal
	private Map<String, User> users;
	private Map<Set<User>, Link> links;
	
	/**
	 * Constructor instantiates users and links
	 */
	public SocialNetwork(){
		users = new HashMap<String, User>();
		links = new HashMap<Set<User>, Link>();
	}
	
	
	
	/**
	 * adds a user to the users Map
	 * @param user	a User
	 * @return	returns false if the user is already in the Map, true otherwise
	 */
	public boolean addUser(User user){
		//Check if the input(s) are null
		if(user == null)
			throw new NullPointerException("The given user is null.");
		if(!user.isValid())
			return false;
		if(users.containsKey(user.getID()))
			return false;
		
		users.put(user.getID(), user);
		return true;
	}
	
	/**
	 * 
	 * @param id	a user's id
	 * @return returns whether the given string is the id of a member
	 */
	public boolean isMember(String id){
		//Check if the input(s) are null
		if( id == null || id.compareTo("") == 0 )
			throw new NullPointerException("The given id is null");
		return users.containsKey( id );
	}
	
	/**
	 * 
	 * @param id	a user's id
	 * @return	Returns the user in the network with the given id, null otherwise
	 */
	public User getUser(String id){
		//Check if the input(s) are null
		if( id == null || id.compareTo("") == 0 )
			throw new NullPointerException("The given id is null");
		if(users.containsKey(id))
			return users.get(id);
		else
			return null;
	}
	
	/**
	 * Establishes a link between the two users with the ids in the ids set on the given date
	 * @param ids a set of user ids
	 * @param date	a date
	 */
	public void establishLink(Set<String> ids, Date date, SocialNetworkStatus status){
		checkParams(ids, date, status);
		//Make a user set from the given set
		Set<User> userSet = makeUserSetFromStringSet( ids , status);
		if(!status.isSuccess())
			return;
		
		//If this link is already in the network, get it, otherwise make it
		Link l = getLinkForEstablish(userSet, status);
		
		if(status.isSuccess()){
			//establish the date
			try{
				l.establish(date, status);
			}catch(UninitializedObjectException e){
				
			}	
		}
	}
	
	/**
	 * Gets the link for an establish event.  Sets status to corresponding
	 * values when there are errors.
	 * @param userSet
	 * @param status
	 * @return returns the link, or null if the activity of the link does not work
	 */
	private Link getLinkForEstablish(Set<User> userSet, SocialNetworkStatus status){
		Link l;
		if(links.containsKey(userSet)){
			l = links.get(userSet);
			//If the link is active after the last link, we cannot establish it, so return false
			if(l.isActiveAfterLastLink())
				status.setStatus(ErrorStatus.ALREADY_ACTIVE);
			else { /* do nothing -- we expect this */ }
		}else{
			l = new Link();
			l.setUsers(userSet, status);
			links.put(userSet, l);
		}
		return l;
	}
	
	/**
	 * Check if parameters are null
	 * @param ids
	 * @param date
	 * @param status
	 * @return returns true if the parameters are not null
	 */
	private boolean checkParams(Set<String> ids, Date date, SocialNetworkStatus status){
		//Check if the input(s) are null
		if(ids == null)
			throw new NullPointerException("The given set of ids is null");
		if(date == null)
			throw new NullPointerException("The given date is null");
		if(status == null)
			throw new NullPointerException("The given status is null");
		status.setStatus(ErrorStatus.SUCCESS);
		return true;
	}
	
	/**
	 * Check if parameters are null
	 * @param ids
	 * @param date
	 * @return	returns true if the parameters are not null
	 */
	private boolean checkParams(Set<String> ids, Date date){
		//Check if the input(s) are null
		if(ids == null)
			throw new NullPointerException("The given set of ids is null");
		if(date == null)
			throw new NullPointerException("The given date is null");
		return true;
	}
	
	/**
	 * Tears down a link between the two users with the ids in the ids set on the given date
	 * @param ids	a set of user ids
	 * @param date	a date
	 */
	public void tearDownLink(Set<String> ids, Date date, SocialNetworkStatus status){
		checkParams(ids, date, status);
		
		//Make a user set from the given set
		Set<User> userSet = makeUserSetFromStringSet( ids, status );
		if(!status.isSuccess())
			return;
		
		//If this link is already in the network, get it, otherwise make it
		Link l = getLinkForTearDown(userSet, status);
		
		if(status.isSuccess()){
			//tear down the date, and return false if there is a problem or exception
			try{
				l.tearDown(date, status);
			}catch(UninitializedObjectException e){
				
			}	
		}
	}
	
	/**
	 * Gets the link for a tear down event.  Sets status to corresponding
	 * values when there are errors.
	 * @param userSet
	 * @param status
	 * @return returns the link, or null if hte activity of the link does not work
	 */
	private Link getLinkForTearDown(Set<User> userSet, SocialNetworkStatus status){
		Link l;
		if(links.containsKey(userSet)){
			l = links.get(userSet);
			//If the link is inactive after the last link, we cannot tear it down, so return false
			if(!l.isActiveAfterLastLink())
				status.setStatus(ErrorStatus.ALREADY_INACTIVE);
			else { /* do nothing -- we expect this*/ }
		}else{
			//return false, because we cannot tear down a link that has never been established
			status.setStatus(ErrorStatus.ALREADY_INACTIVE);
			l = null;
		}
		return l;
	}
	
	/**
	 * returns whether a link of the users with the given ids is active at a given date
	 * @param ids	a set of user ids
	 * @param date	a date
	 * @return	returns whether the link is active at the given date
	 */
	public boolean isActive(Set<String> ids, Date date){
		//Check if the input(s) are null
		checkParams(ids, date);
		
		//Make a user set from the given set
		Set<User> userSet = makeUserSetFromStringSet( ids );
		//if there is a problem making the userSet, return false
		if(userSet == null)
			return false;
		
		//If this link is already in the network, get it, otherwise make it
		Link l;
		if(links.containsKey(userSet)){
			l = links.get(userSet);
		}else{
			//return false, because the link does not exist
			return false;
		}
		
		//tear down the date, and return false if there is a problem or exception
		try{
			return l.isActive(date);
		}catch(UninitializedObjectException e){
			return false;
		}	
	}
	
	/**
	 * Makes user set from a set of id Strings using User static function
	 * @param ids
	 * @return returns a set of users with the ids in the set ids
	 */
	private Set<User> makeUserSetFromStringSet(Set<String> ids) {
		Object[] idArray = ids.toArray();
		//If we were not given two user ids, or if one or more of the given users are not one of our users, return false
		if(idArray.length != 2 || !(users.containsKey((String)idArray[0]) && users.containsKey((String)idArray[1]))){
			return null;
		}
		
		//If both users are ones that we have, make a userSet for them, and return it
		return User.makeUserSet( (String) idArray[0], (String) idArray[1]);
	}

	/**
	 * Makes a user set from a set of ids
	 * @param ids a set containing two user ids
	 * @return returns the user set from the ids in ids
	 */
	private Set<User> makeUserSetFromStringSet(Set<String> ids, SocialNetworkStatus status){
		Object[] idArray = ids.toArray();
		//If we were not given two user ids, or if one or more of the given users are not one of our users, return false
		if(idArray.length != 2 || !(users.containsKey((String)idArray[0]) && users.containsKey((String)idArray[1]))){
			status.setStatus(ErrorStatus.INVALID_USERS);
			return null;
		}
		
		//If both users are ones that we have, make a userSet for them, and return it
		return User.makeUserSet( (String) idArray[0], (String) idArray[1] , status);
	}
	
}
