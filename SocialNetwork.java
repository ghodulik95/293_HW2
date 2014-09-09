import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class SocialNetwork {
	private Map<String, User> users;
	private Map<Set<User>, Link> links;
	
	public SocialNetwork(){
		users = new HashMap<String, User>();
		links = new HashMap<Set<User>, Link>();
	}
	
	public boolean addUser(User user){
		if(users.containsKey(user.getID()))
			return false;
		
		users.put(user.getID(), user);
		return true;
	}
	
	public boolean isMember(String id){
		return users.containsKey( id );
	}
	
	public User getUser(String id){
		if(users.containsKey(id))
			return users.get(id);
		else
			return null;
	}
	
	public boolean establishLink(Set<String> ids, Date date){
		//Make a user set from the given set
		Set<User> userSet = makeUserSetFromStringSet( ids );
		//if there is a problem making the userSet (the given set was not valid), return false
		if(userSet == null)
			return false;
		
		//If this link is already in the network, get it, otherwise make it
		Link l;
		if(links.containsKey(userSet)){
			l = links.get(userSet);
			//If the link is active after the last link, we cannot establish it, so return false
			if(l.isActiveAfterLastLink())
				return false;
			else { /* do nothing -- we expect this */ }
		}else{
			l = new Link();
			l.setUsers(userSet);
			links.put(userSet, l);
		}
		
		//establish the date, and return false if there is a problem or exception
		try{
			return l.establish(date);
		}catch(UninitializedObjectException e){
			return false;
		}	
	}
	
	public boolean tearDownLink(Set<String> ids, Date date){
		//Make a user set from the given set
		Set<User> userSet = makeUserSetFromStringSet( ids );
		//if there is a problem making the userSet, return false
		if(userSet == null)
			return false;
		
		//If this link is already in the network, get it, otherwise make it
		Link l;
		if(links.containsKey(userSet)){
			l = links.get(userSet);
			//If the link is inactive after the last link, we cannot tear it down, so return false
			if(!l.isActiveAfterLastLink())
				return false;
			else { /* do nothing -- we expect this*/ }
		}else{
			//return false, because we cannot tear down a link that has never been established
			return false;
		}
		
		//tear down the date, and return false if there is a problem or exception
		try{
			return l.tearDown(date);
		}catch(UninitializedObjectException e){
			return false;
		}	
	}
	
	public boolean isActive(Set<String> ids, Date date){
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
	
	private Set<User> makeUserSetFromStringSet(Set<String> ids){
		Object[] idArray = ids.toArray();
		//If we were not given two user ids, or if one or more of the given users are not one of our users, return false
		if(idArray.length != 2 || !(users.containsKey(idArray[0]) && users.containsKey(idArray[1])))
			return null;
		
		//If both users are ones that we have, make a userSet for them, and return ut
		return makeUserSet( (String) idArray[0], (String) idArray[1] );
	}
	
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
	
	
}
