import java.util.HashSet;
import java.util.Set;


/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */
 /*
 * USER CLASS
 * This is the user class. It is a data structure containing a unique user id and boolean of if the user is valid.
 * It contains all the required public functions. equals() and hashCode() is also overridden.

 *
 */
public class User{
	//The unique id
	private String id;
	//I thought about having the validity of the user just be whether or not id was null,
	//but the description of the assignment made it sound like it wanted a boolean value for it.
	private boolean isValidUser;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String phoneNumber;
	
	/**
	 * Constructor sets user validity to false
	 */
	public User(){
		isValidUser = false;
	}
	
	/**
	 * 
	 * @param fName		The user's first name
	 * @return	returns this user
	 * @throws UninitializedObjectException
	 */
	public User setFirstName(String fName) throws UninitializedObjectException{
		//check input : throw exception if the input is bad or the user isn't valid
		checkInputAndUser(fName);
		firstName = fName;
		return this;
	}
	
	/**
	 * Sets the user's middle name
	 * @param mName	the user's middle name
	 * @return	returns this user
	 * @throws UninitializedObjectException
	 */
	public User setMiddleName(String mName) throws UninitializedObjectException{
		//check input : throw exception if the input is bad or the user isn't valid
		checkInputAndUser(mName);
		middleName = mName;
		return this;
	}
	
	/**
	 * Sets the user's last name
	 * @param lName	the user's last name
	 * @return	returns this user
	 * @throws UninitializedObjectException
	 */
	public User setLastName(String lName) throws UninitializedObjectException{
		//check input : throw exception if the input is bad or the user isn't valid
		checkInputAndUser(lName);
		lastName = lName;
		return this;
	}
	
	/**
	 * Sets the user's email address
	 * @param e		the user's email address
	 * @return	returns this user
	 * @throws UninitializedObjectException
	 */
	public User setEmail(String e) throws UninitializedObjectException{
		//check input : throw exception if the input is bad or the user isn't valid
		checkInputAndUser(e);
		email = e;
		return this;
	}
	
	/**
	 * Sets the user's phone number
	 * @param phone	the user's phone number
	 * @return	returns this user
	 * @throws UninitializedObjectException
	 */
	public User setPhoneNumber(String phone) throws UninitializedObjectException{
		//check input : throw exception if the input is bad or the user isn't valid
		checkInputAndUser(phone);
		phoneNumber = phone;
		return this;
	}
	
	/**
	 * Checks if an input string is null or empty, and if the user is valid
	 * @param input		given input
	 * @throws UninitializedObjectException		thrown if this user is not valid
	 */
	private void checkInputAndUser(String input) throws UninitializedObjectException{
		if(!this.isValidUser)
			throw new UninitializedObjectException("User is not valid.");
		if(this.id == null)
			throw new NullPointerException("User ID is null");
		checkInput(input);
	}
	
	private void checkInput(String input){
		if(input == null || input.compareTo("") == 0)
			throw new NullPointerException("Given input string is empty");
	}
	
	/**
	 * Gets the user's first name
	 * @return returns the user's first name
	 */
	public String getFirstName(){
		if(this.firstName == null)
			return "Not given";
		else
			return this.firstName;
	}
	
	/**
	 * Gets the user's middle name
	 * @return returns the user's middle name
	 */
	public String getMiddleName(){
		if(this.middleName == null)
			return "Not given";
		else
			return this.middleName;
	}
	
	/**
	 * Gets the user's last name
	 * @return returns the user's last name
	 */
	public String getLastName(){
		if(this.lastName == null)
			return "Not given";
		else
			return this.lastName;
	}
	
	/**
	 * Gets the user's email
	 * @return returns the user's email address
	 */
	public String getEmail(){
		if(this.email == null)
			return "Not given";
		else
			return this.email;
	}
	
	/**
	 * Gets the user's first name
	 * @return returns the user's phone number
	 */
	public String getPhoneNumber(){
		if(this.phoneNumber == null)
			return "Not given";
		else
			return this.phoneNumber;
	}
	
	
	/**
	 * 
	 * @return	returns the validity of the user
	 */
	public boolean isValid(){
		return isValidUser;
	}
	
	/**
	 * 
	 * @return	returns the user id
	 */
	public String getID(){
		return id;
	}
	
	/**
	 * sets the user id, throwing nullpointer exception if the given string is null
	 * @param id_in		the inputted user id
	 * @return	returns true if the user was successfully set; false if the user was already valid
	 * @throws NullPointerException		throws if the given string is null
	 */
	public boolean setID(String id_in) throws NullPointerException{
		//throw NullPointerException if input string is null
		checkInput(id_in);
		
		//if the id is not initialized and the user is not valid, set it to the given id
		if(id == null && !this.isValidUser){
			id = id_in;
			isValidUser = true;
			return true;
		}
		//if the user is already valid, return false
		else{
			return false;
		}
	}
	
	/**
	 * toString() is the user id and all its information.  Note that information that is not
	 * given will say "Not given".
	 */
	public String toString(){
		if(this.isValid())
			return "User "+getID()+" :\nFirst name - "+getFirstName()+"\nMiddle name - "+getMiddleName()+"\n" +
					"Last name - "+getLastName()+"\nEmail - "+getEmail()+"\nPhone Number - "+getPhoneNumber();
		else
			return "Invalid User: Uninitialized ID";
	}
	
	
	@Override
	/**
	 * Two users with the same unique id must be equal, so the equal function
	 * is overridden to have this functionality
	 */
	public boolean equals(Object o){
		// TODO Auto-generated method stub
		//Check if the input(s) are null
		if(o == null)
			throw new NullPointerException("Given object is null.");
		//if the object is a user, return if the ids of that user and this are equal
		if(o instanceof User)
			return this.getID().compareTo(((User)o).getID()) == 0;
		else
			return false;
	}
	
	@Override
	/**
	 * So that checking Set<User>.contains(User u) works correctly,
	 * I had to override the hashCode() function so that it only takes the
	 * id into account
	 */
	public int hashCode(){
		return this.getID().hashCode();
	}
	
	/**
	 * Makes a user set from a set of two strings
	 * @param ids a set of two strings
	 * @return	returns the set of users with ids given in ids.
	 * 			returns null if there is a problem
	 */
	public static Set<User> makeUserSetFromStringSet(Set<String> ids) {
		Object[] idArray = ids.toArray();
		//If we were not given two user ids, return null
		if(idArray.length != 2){
			return null;
		}
		
		//If both users are ones that we have, make a userSet for them, and return it
		return makeUserSet( (String) idArray[0], (String) idArray[1]);
	}


	/**
	 * Makes a set of two users from two strings that are the user's ids
	 * @param id1 the id of user 1
	 * @param id2 the id of user 2
	 * @return	returns the set of users containing both ids, or null if the ids are the same
	 */
	public static Set<User> makeUserSet(String id1, String id2) {
		if(id1.compareTo(id2) == 0)
			return null;
		//build first user
		User u1 = new User();
		u1.setID(id1);
		
		//build second user
		User u2 = new User();
		u2.setID(id2);
		
		//build set and return it
		Set<User> output = new HashSet<User>();
		output.add(u1);
		output.add(u2);
		
		return output;
	}



	/**
	 * Makes a user set from a set of ids
	 * @param ids a set containing two user ids
	 * @param status	a status 
	 * @return returns the user set from the ids in ids
	 */
	public static Set<User> makeUserSetFromStringSet(Set<String> ids, SocialNetworkStatus status){
		Object[] idArray = ids.toArray();
		//If we were not given two user ids, return null
		if(idArray.length != 2 ){
			status.setStatus(ErrorStatus.INVALID_USERS);
			return null;
		}
		
		//If both users are ones that we have, make a userSet for them, and return it
		return makeUserSet( (String) idArray[0], (String) idArray[1] , status);
	}
	

	/**
	 * Makes a set of users from two strings
	 * @param id1	id of first user
	 * @param id2	id of second user
	 * @param status an error status
	 * @return	returns a set of users with id1 and id2
	 */
	public static Set<User> makeUserSet(String id1, String id2, SocialNetworkStatus status){
		if(id1.compareTo(id2) == 0){
			status.setStatus(ErrorStatus.INVALID_USERS);
			return null;
		}
		//build first user
		User u1 = new User();
		u1.setID(id1);
		
		//build second user
		User u2 = new User();
		u2.setID(id2);
		
		//build set and return it
		Set<User> output = new HashSet<User>();
		output.add(u1);
		output.add(u2);
		
		status.setStatus(ErrorStatus.SUCCESS);
		return output;
	}
	
}
