//George Hodulik
//gmh73@case.edu
//USER CLASS
//This is the user class. It is a data structure containing a unique user id and boolean of if the user is valid.
//It contains all the required public functions. equals() and hashCode() is also overridden.

public class User{
	//The unique id
	private String id;
	//I thought about having the validity of the user just be whether or not id was null,
	//but the description of the assignment made it sound like it wanted a boolean value for it.
	private boolean isValidUser;
	
	//Constructor sets user validity to false
	public User(){
		isValidUser = false;
	}
	
	//returns the validity of the user
	public boolean isValid(){
		return isValidUser;
	}
	
	//returns the user id
	public String getID(){
		return id;
	}
	
	//sets the user id, throwing nullpointer exception if the given string is null
	public boolean setID(String id_in) throws NullPointerException{
		//throw NullPointerException if input string is null
		if(id_in.compareTo("") == 0){
			throw new NullPointerException("Input string must not be null.");
		}
		
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
	
	//toString() is just the user's id
	public String toString(){
		if(this.isValid())
			return this.getID();
		else
			return "Invalid User: Uninitialized ID";
	}
	
	//Two users with the same unique id must be equal, so the equal function
	//is overridden to have this functionality
	@Override
	public boolean equals(Object o){
		// TODO Auto-generated method stub
		//if the object is a user, return if the ids of that user and this are equal
		if(o instanceof User)
			return this.getID().compareTo(((User)o).getID()) == 0;
		else
			return false;
	}
	
	//So that checking Set<User>.contains(User u) works correctly,
	//I had to override the hashCode() function so that it only takes the
	//id into account
	@Override
	public int hashCode(){
		return this.getID().hashCode();
	}

}
