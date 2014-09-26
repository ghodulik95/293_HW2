/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */
 /*
 * Friend Class contains a user and a distance
 *
 */
public class Friend {
	private User user;
	private int distance;
	private boolean isValidFriend;
	
	/**
	 * Constructor creates an invalid Friend
	 */
	public Friend(){
		isValidFriend = false;
	}
	
	/**
	 * Sets a user with given distance.  Returns true if successful, false if the Friend was already set
	 * @param user	A user
	 * @param distance	the minimum number of links it takes to reach this user
	 * @return	returns true if successfully set, false if the Friend was already set
	 */
	public boolean set(User user, int distance){
		checkInput(user);
		if(!this.isValidFriend){
			this.user = user;
			this.distance = distance;
			this.isValidFriend = true;
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Gets the user for this friend.  Throws Exception if the Friend is not valid.
	 * @return	returns the User this Friend relates
	 * @throws UninitializedObjectException		thrown if this Friend is not valid
	 */
	public User getUser() throws UninitializedObjectException{
		checkFriend();
		return user;
	}
	
	/**
	 * Gets the distance for this friend.  Throws Exception if the Friend is not valid.
	 * @return	the distance of the User this Friend relates
	 * @throws UninitializedObjectException thrown if this Friend is not valid
	 */
	public int getDistance() throws UninitializedObjectException{
		checkFriend();
		return distance;
	}
	
	/**
	 * Returns a readable string that is basically the user toString with the distance.
	 */
	public String toString(){
		if(this.isValidFriend){
			String output = "--:Friend Information:--\n"+user.toString()+"\nDistance: ";
			try{
				String d = String.valueOf(getDistance());
				output += d;
			}catch(Exception e){
				//This should never happen, as a valid Friend should
				//always have a distance
			}
			return output;
		}else{
			return "Invalid Friend.";
		}
	}
	
	/**
	 * Throws exception if this Friend is not valid.
	 * @throws UninitializedObjectException		thrown if this is not valid.
	 */
	private void checkFriend() throws UninitializedObjectException{
		if(!this.isValidFriend)
			throw new UninitializedObjectException("This Friend is invalid.");
	}
	
	/**
	 * Checks if the user is null
	 * @param user	a user to be checked if null
	 */
	private void checkInput(User user){
		if(user == null){
			throw new NullPointerException("Given user is null.");
		}
	}
	
	@Override
	public boolean equals(Object o){
		try{
			if(o instanceof Friend){
				Friend f = (Friend) o;
				return f.getUser().getID().compareTo(user.getID()) == 0;
			}else
				return false;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return user.hashCode();
	}
	
}
