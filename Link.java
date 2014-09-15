//George Hodulik
//gmh73@case.edu
//LINK CLASS
//This is the Link class. It is a data structure containing a list of dates and the set of corresponding users.
//It contains all the required public functions. equals() is also overridden.

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Link {
	//isValidLink is set to true when the link is given users
	private boolean isValidLink;
	//the set of linked users
	private Set<User> linkedUsers;
	//isActiveAfterLastLink keeps track of whether the latest event left the link active or inactive
	private boolean isActiveAfterLastLink;
	//List of dates for the link
	private List<Date> dates;
	
	/**
	 * Constructor sets valid to false, active to false, and initializes the date list
	 */
	public Link(){
		isValidLink = false;
		isActiveAfterLastLink = false;
		dates = new ArrayList<Date>();
	}
	
	/**
	 * sets the users in the link
	 * @param users		a set of users
	 * @return	returns true if the given users are successfully set, and false if the link is already set
	 */
	public boolean setUsers(Set<User> users){
		//Check if the input(s) are null
		if(users == null)
			throw new NullPointerException("The given set of users is null.");
		//if the link is already set or the given set does not have 2 distinct users, return false
		if(this.isValidLink || users.size() != 2)
			return false;
		
		linkedUsers = users;
		this.isValidLink = true;
		return true;
	}
	
	/**
	 * 
	 * @return	returns if link is valid
	 */
	public boolean isValid(){
		return this.isValidLink;
	}
	
	/**
	 * 
	 * @return	returns users if valid, throws exception otherwise
	 * @throws UninitializedObjectException		if the link is not valid
	 */
	public Set<User> getUsers() throws UninitializedObjectException{
		if(this.isValidLink)
			return linkedUsers;
		else
			throw new UninitializedObjectException("Link user set not initialized");
	}
	
	/**
	 * establishes a link between set users
	 * @param date	date of establish event
	 * @return	returns true if the event is set; returns false if the link is already active or the
	 * 			date is not after the last event (equal dates are to be considered after)
	 * @throws UninitializedObjectException		throws if the link is not valid
	 */
	public boolean establish(Date date) throws UninitializedObjectException{
		//Check if the input(s) are null
		if(date == null)
			throw new NullPointerException("The given date is null.");
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		//if the user is active already, or the last date is before the given date, return false
		if(this.isActiveAfterLastLink || !dateIsAfterLastEvent(date))
			return false;
		
		//if there are no problems, add the given date to the date list, set active to true, and return true
		dates.add(date);
		this.isActiveAfterLastLink = true;
		return true;
	}
	
	/**
	 * tears down a link
	 * @param date	date of teardown event
	 * @return	true if the event is set; returns false if the link is already inactive or the
	 * 			date is not after the last event (equal dates are to be considered after)
	 * @throws UninitializedObjectException		throws if the link is not valid
	 */
	public boolean tearDown(Date date) throws UninitializedObjectException{
		//Check if the input(s) are null
		if(date == null)
			throw new NullPointerException("The given date is null.");
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		//if the user is already inactive, or the last date is before the given date, return false
		if(!this.isActiveAfterLastLink ||  !dateIsAfterLastEvent(date))
			return false;
		
		//if there are no problems, add the given date to the date list, set active to false, and return true
		dates.add(date);
		this.isActiveAfterLastLink = false;
		return true;
	}
	
	/**
	 * 
	 * @param date	a date
	 * @return	returns if the date is after the last recorded event, ie whether or not it
	 * 			is valid to set up establish or tear down
	 */
	private boolean dateIsAfterLastEvent(Date date){
		return dates.isEmpty() || !date.before(dates.get(dates.size() - 1));
	}
	
	/**
	 * checks whether a link was active at the given date
	 * @param date	a date
	 * @return	returns true if the link is active at the given date, false otherwise
	 * @throws UninitializedObjectException throws if the link is not valid
	 */
	public boolean isActive(Date date) throws UninitializedObjectException{
		//Check if the input(s) are null
		if(date == null)
			throw new NullPointerException("The given date is null.");
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		int indexOfNextDate = indexOfNextEvent(date);
		
		//The date is active if the index of the next event is odd (note that index will equal 0 if the given date
		//  is before the first date, so when i == 0, it is inactive at given date
		return indexOfNextDate % 2 == 1;
	}
	
	/**
	 * gets the index after the given date
	 * @param date	a date
	 * @return	returns the index of the nearest date that is after the given date.
	 * 			Will return 0 if the given date is before all the recorded dates, and the
	 * 			size of the list of dates if the given date is after all the recorded dates
	 */
	private int indexOfNextEvent(Date date){
		//while i is within bounds, and the date at i is either before or the same as the given date
		//iterate through to find the index of the next date
		int i = 0;
		while(i < dates.size() && (dates.get(i).before(date) || date.equals(dates.get(i)))){
			i++;
		}
		return i;
	}
	
	/**
	 * gets the first date
	 * @return	returns the first recorded date
	 * @throws UninitializedObjectException		throws if the list of dates is not initialized or if
	 * 											the link is not valid
	 */
	public Date firstEvent() throws UninitializedObjectException{
		//if invalid or the are no dates in our list, throw exception
		if(dates == null)
			throw new UninitializedObjectException("Link user set not initialized");
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link is not valid");
		if(dates.isEmpty())
			return null;
		
		//otherwise return the date at index 0
		return dates.get(0);
	}
	
	/**
	 * 
	 * @param date a date
	 * @return	returns the Date object that is nearest and after the given date
	 * 			in our list of recorded dates
	 * @throws UninitializedObjectException		throws if the list of dates is not initialized or if
	 * 											the link is not valid
	 */
	public Date nextEvent(Date date) throws UninitializedObjectException{
		//Check if the input(s) are null
		if(date == null)
			throw new NullPointerException("The given date is null.");
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		int indexOfNextDate = indexOfNextEvent(date);
		
		//if there is a date at i and the previous date is the given date, return it
		if(indexOfNextDate < dates.size()){
			return dates.get(indexOfNextDate);
		}
		//otherwise, there are no dates after the given date, or the given date was not a date we have, so we return null
		else{
			return null;
		}
	}
	
	
	/**
	 * Print Link
	 * The format I want is :
	 * List of events (ie. "event1, event2, event3") \n
	 * then a description of what is happening, like what is given in the hw
	 */
	public String toString(){
		//If this is not a valid line, return invalid message
		if(!this.isValidLink)
			return "Invalid Link: Uninitialized IDs";
		//if there are no dates in this link, return a message that says that
		if(dates.isEmpty())
			return "There are no events for this link.";
		
		//dateOutput is the list of dates, descrption is the description
		String dateOutput = dates.get(0).toString();
		String description = "The link was establised on " + dates.get(0).toString();
		for(int i = 1; i < dates.size(); i++){
			//ad date to list of dates
			dateOutput += ", " + dates.get(i).toString();
			
			//add a description that corresponds to whether the link is active
			if(i % 2 == 0){
				description += ", was re-establised on "+dates.get(i).toString();
			}else{
				description += ", was torn down on "+dates.get(i).toString();
			}
		}
		return dateOutput + "\n" + description;
	}
	
	/**
	 * return isActiveAfterLastLink
	 * @return	returns whether or not the link is active after the last recorded event
	 */
	public boolean isActiveAfterLastLink(){
		return this.isActiveAfterLastLink;
	}
	
	@Override
	/**
	 * overrides the equals function so that all that matters is the users (and there ids)
	 */
	public boolean equals(Object o){
		if(o instanceof Link){
			Link l = (Link) o;
			return this.linkedUsers.equals(l.linkedUsers);
		}else
			return false;
	}
}
