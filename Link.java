import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


public class Link {
	private boolean isValidLink;
	private Set<User> linkedUsers;
	private boolean isActiveAfterLastLink;
	private List<Date> dates;
	
	//Constructor sets valid to false, active to false, and initializes the date list
	public void Link(){
		isValidLink = false;
		isActiveAfterLastLink = false;
		dates = new ArrayList<Date>();
	}
	
	//sets the users in the link
	public boolean setUsers(Set<User> users){
		//if the link is already set or the given set does not have 2 users, return false
		if(this.isValidLink || users.size() != 2)
			return false;
		
		linkedUsers = users;
		return true;
	}
	
	//returns if link is valid
	public boolean isValid(){
		return this.isValidLink;
	}
	
	//returns users if valid, throws exception otherwise
	public Set<User> getUsers() throws UninitializedObjectException{
		if(this.isValidLink)
			return linkedUsers;
		else
			throw new UninitializedObjectException("Link user set not initialized");
	}
	
	//establishes a link
	public boolean establish(Date date) throws UninitializedObjectException{
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		//if the user is active already, or the last date is before the given date, return false
		if(this.isActiveAfterLastLink ||  
				(!dates.isEmpty() && dates.get(dates.size() - 1).before(date)))
			return false;
		
		//if there are no problems, add the given date to the date list, set active to true, and return true
		dates.add(date);
		this.isActiveAfterLastLink = true;
		this.isValidLink = true;
		return true;
	}
	
	//tears down a link
	public boolean tearDown(Date date) throws UninitializedObjectException{
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		//if the user is already inactive, or the last date is before the given date, return false
		if(!this.isActiveAfterLastLink ||  
				(!dates.isEmpty() && dates.get(dates.size() - 1).before(date)))
			return false;
		
		//if there are no problems, add the given date to the date list, set active to false, and return true
		dates.add(date);
		this.isActiveAfterLastLink = false;
		return true;
	}
	
	//checks whether a link was active at the given date
	public boolean isActive(Date date) throws UninitializedObjectException{
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		//loop through the date list until there is a date after the given date
		//keep track of whether or not the link is active at index i, and return the result
		int i = 0;
		//boolean active = false;
		//while i is within bounds, and the date at i is either before or the same as the given date
		while(i < dates.size() && (dates.get(i).before(date) || date.equals(dates.get(i)))){
			i++;
			//active = !active;
		}
			
		return i % 2 == 0;
	}
	
	//gets the first date
	public Date firstEvent() throws UninitializedObjectException{
		//if invalid or the are no dates in our list, throw exception
		if(!this.isValidLink || dates.size() == 0)
			throw new UninitializedObjectException("Link user set not initialized");
		if(dates.isEmpty())
			throw new UninitializedObjectException("There are no dates recorded");
		
		return dates.get(0);
	}
	
	public Date nextEvent(Date date) throws UninitializedObjectException{
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		int i = 0;
		//while i is within bounds, and the date at i is either before or the same as the given date
		while(i < dates.size() && (dates.get(i).before(date) || date.equals(dates.get(i)))){
			i++;
		}
		
		//if there is a date at i, return it
		if(i < dates.size()){
			return dates.get(i);
		}
		//otherwise, there are no dates after the given date, so we return null
		else{
			return null;
		}
	}
	
	//Print Link
	public String toString(){
		if(!this.isValidLink)
			return "Invalid Link: Uninitialized IDs";
		if(dates.isEmpty())
			return "There are no events for this link.";
		
		String dateOutput = dates.get(0).toString();
		String description = "The link was establised on " + dates.get(0).toString();
		for(int i = 1; i < dates.size() - 1; i++){
			dateOutput += ", " + dates.get(i).toString();
			
			if(i % 2 == 0){
				description += ", was re-establised on "+dates.get(i).toString();
			}else{
				description += ", was torn down on "+dates.get(i).toString();
			}
		}
		return dateOutput + "\n" + description;
		
	}
}
