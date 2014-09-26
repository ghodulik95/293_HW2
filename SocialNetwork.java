

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
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
	/** Map of users in network */
	private Map<String, User> users;
	/** Map of links in network */
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
		if(isInSocialNetwork(user.getID()))
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
		checkUserId(id);
		return isInSocialNetwork( id );
	}
	
	private boolean isInSocialNetwork(String id){
		return users.containsKey(id);
	}
	
	/**
	 * 
	 * @param id	a user's id
	 * @return	Returns the user in the network with th
			return this.linkedUsers.conte given id, null otherwise
	 */
	public User getUser(String id){
		//Check if the input(s) are null
		checkUserId(id);
		if(isInSocialNetwork(id))
			return users.get(id);
		else
			return null;
	}
	
	/**
	 * Checks if a user id is valid (does not check if the user is in this network)
	 * @param id A user id
	 */
	private void checkUserId(String id){
		if( id == null || id.compareTo("") == 0 )
			throw new NullPointerException("The given id is null");
	}
	
	/**
	 * Establishes a link between the two users with the ids in the ids set on the given date
	 * @param ids a set of user ids
	 * @param date	a date
	 */
	public void establishLink(Set<String> ids, Date date, SocialNetworkStatus status){
		checkParamsNotNull(ids, date, status);
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
	 * @param userSet	a ser of Users
	 * @param status	a SocialNetworkStatus
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
	 * @param ids	a set of string ids
	 * @param date	a date
	 * @param status	a SocialNetworkStatus
	 * @return returns true if the parameters are not null
	 */
	private boolean checkParamsNotNull(Object ids, Date date, SocialNetworkStatus status){
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
	 * @param ids	a set of user ids
	 * @param date	a date
	 * @return	returns true if the parameters are not null
	 */
	private boolean checkParamsNotNull(Set<String> ids, Date date){
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
		checkParamsNotNull(ids, date, status);
		
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
	 * @param userSet	a set of users
	 * @param status	a SocialNetworkStatus
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
		checkParamsNotNull(ids, date);
		
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
	 * Checks if the given id is valid and in the network
	 * @param id	given id to check
	 * @param status	sets status to INVALID_USER if user id is invalid
	 */
	private void checkId(String id, SocialNetworkStatus status){
		//check id id is null or not in network
		checkUserId(id);
		if(!isInSocialNetwork(id)){
			status.setStatus(ErrorStatus.INVALID_USERS);
		}
	}
	
	/**
	 * Checks if the maximum distance specified is valid
	 * @param distance_max	max distance
	 * @param status	set status to INVALID_DISTANCE if it is invalid
	 */
	private void checkMaxDistance(int distance_max, SocialNetworkStatus status){
		//check if max distance is positive
		if(distance_max < 0){
			status.setStatus(ErrorStatus.INVALID_DISTANCE);
		}
	}
	
	/**
	 * Finds the neighborhood of user with id id
	 * @param id	The user id
	 * @param date	A date
	 * @param status	status variable
	 * @return	returns a set of Friends related to this user with an active
	 * 			direct or indirect connection to the given user at date date
	 */
	public Set<Friend> neighborhood(String id, Date date, SocialNetworkStatus status){
		//Just call the other neighborhood function with the max value set to be Integer's max value
		return this.neighborhood(id, date, Integer.MAX_VALUE,status);
	}
	
	/**
	 * Finds the neighborhood of user with id id and a specified max distance
	 * @param id	The user id
	 * @param date	A date
	 * @param distance_max the specified maximum distance a Friend can have
	 * @param status	status variable
	 * @return	returns a set of Friends related to this user with an active
	 * 			direct or indirect connection to the given user at date date
	 */
	public Set<Friend> neighborhood(String id, Date date, int distance_max, SocialNetworkStatus status){
		checkParamsNotNull(id, date, status);
		checkId(id, status);
		checkMaxDistance(distance_max, status);
		
		if(!status.isSuccess()){
			return null;
		}
		
		//Instantiate our output set
		Set<Friend> friends = new HashSet<Friend>();
		
		//Add the given user to the neighborhood with distance 0
		//as specified by the assignment
		User u = users.get(id);
		Friend origUser = new Friend();
		origUser.set(u, 0);
		friends.add(origUser);
		
		//Make a queue of all the direct links to the given user
		Queue<Friend> newFriends = new LinkedList<Friend>();
		newFriends.addAll(getAllImmediateFriends(id, date, friends, 1, status));
		
		//Add the other, indirect links to neighborhood
		//there is no maximum distance in this method, so make the max be Integer's max
		findAllNonImmediateFriends(friends, newFriends, date, distance_max, status);
		
		//if there was a problem, return null
		if(!status.isSuccess()){
			return null;
		}
		
		//return the neighborhood
		return friends;
	}
	
	/**
	 * Given a set of immediate friends and a date, finds all the non-immediate friends
	 * @param friends	The set of friends that the new friends are added to
	 * @param immediateFriends	a queue of immediate friends of the user (original user is not known by this function)
	 * @param date	the date
	 * @param status	status variable
	 */
	private void findAllNonImmediateFriends(Set<Friend> friends, Queue<Friend> immediateFriends, Date date, int distance_max,
			SocialNetworkStatus status){
		try{
			//Get the first friend in the queue
			Friend tempF = immediateFriends.poll();
			
			//while there are still friends in the queue, the status is success, and we are within max distance
			int distance = -1;
			while(neighborhoodIsNotFinished(tempF, distance, distance_max, status)){
				//if the set of friends contains the polled friend in queue
				if(!friends.contains(tempF)){
					//add the friend to our set
					friends.add(tempF);
					
					//find all the immediate friends to this user
						//these immediate friends have a distance to our original user of 1 more than this friend
					distance = tempF.getDistance() + 1;
					String friendID = tempF.getUser().getID();
					immediateFriends.addAll(getAllImmediateFriends(friendID, date, friends, distance, status));
				}else{
					//May need to add a way to check if this friends distance
					//is less than the current, but due to the nature of this 
					//algorithm, I think the current distance could only be
					//greater than or equal to the recorded distance
				}
				
				//Do the same to the next friend in the queue
				tempF = immediateFriends.poll();
			}
		}catch(Exception e){
			status.setStatus(ErrorStatus.INVALID_USERS);
		}
	}
	
	private boolean neighborhoodIsNotFinished(Friend f, int distance, int distance_max, SocialNetworkStatus status){
		return f != null && status.isSuccess() && distance <= distance_max;
	}
	
	/**
	 * Returns a LinkedList of all the immediate friends of the user with id id
	 * @param id	The user's id
	 * @param date	A date to test if active link
	 * @param friends	The set of friends we already have found
	 * @param distance	the distance that these found friends will have assigned to them
	 * @param status	the status variable
	 * @return	Returns a LinkedList of all the immediate friends of the user with id id
	 */
	private LinkedList<Friend> getAllImmediateFriends(String id, Date date, Set<Friend> friends, int distance,SocialNetworkStatus status){
		//Get all the links in the network as a collection so we can iterate it
		Collection<Link> allLinks = links.values();
		//Initialize our output
		LinkedList<Friend> foundFriends = new LinkedList<Friend>();
		try{
			//for each of our links, add the other user in the link whenever
			//the link is active AND one of the users is the given one
			for(Link l : allLinks){
				Set<User> usersInLink = l.getUsers();
				if( l.isActive(date) && oneUserHasId(id, usersInLink))
					addFoundFriend(id, usersInLink, distance, foundFriends);
			}
			status.setStatus(ErrorStatus.SUCCESS);
		}catch(UninitializedObjectException e){
			status.setStatus(ErrorStatus.INVALID_USERS);
		}
		return foundFriends;
	}
	
	/**
	 * Adds the user that in the given set that does not have the given id to foundFriends with given distance
	 * @param id	id not to include (the original id)
	 * @param usersInLink	the users from a link
	 * @param distance	the distance to assign this friend
	 * @param foundFriends	the list of friends to add this new friend to
	 * @throws UninitializedObjectException		thrown if the friend is invalid
	 */
	private void addFoundFriend(String id, Set<User> usersInLink, int distance, LinkedList<Friend> foundFriends) throws UninitializedObjectException{
		Friend tempF = new Friend();
		OTHER_USER_FOUND:
		for(User u : usersInLink){
			if(u.getID().compareTo(id) != 0){
				tempF.set(u, distance);
				break OTHER_USER_FOUND;
			}
		}
		foundFriends.add(tempF);
	}
	
	/**
	 * Returns true when the given string id is the id of one of the users in the given set
	 * @param id	the id we are checking
	 * @param userSet	the set of users we are checking
	 * @return	Returns true when the given string id is the id of one of the users in the given set
	 */
	private boolean oneUserHasId(String id, Set<User> userSet){
		User u = new User();
		u.setID(id);
		return userSet.contains(u);
	}	
	
	/**
	 * Makes user set from a set of id Strings using User static function
	 * @param ids
	 * @return returns a set of users with the ids in the set ids
	 */
	private Set<User> makeUserSetFromStringSet(Set<String> ids) {
		String[] idArray = ids.toArray(new String[0]);
		//If we were not given two user ids, or if one or more of the given users are not one of our users, return false
		if(idsAreInvalid(idArray)){
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
		String[] idArray = ids.toArray(new String[0]);
		//If we were not given two user ids, or if one or more of the given users are not one of our users, return false
		if(idsAreInvalid(idArray)){
			status.setStatus(ErrorStatus.INVALID_USERS);
			return null;
		}
		
		//If both users are ones that we have, make a userSet for them, and return it
		return User.makeUserSet( (String) idArray[0], (String) idArray[1] , status);
	}
	
	/**
	 * Returns true if an array of user id strings is of length 2, and both users are in this social network,
	 * and both ids are not the same.
	 * @param idArray an array of user id strings
	 * @return	Returns true if an array of user id strings is of length 2, and both users are in this social network,
	 * 			and both ids are not the same.
	 */
	private boolean idsAreInvalid(String[] idArray){
		return idArray.length != 2 || !((isInSocialNetwork(idArray[0]) && isInSocialNetwork(idArray[1])))
				|| idArray[0].compareTo(idArray[1]) == 0;
	}
	
}
