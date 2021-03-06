

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */
 /*
 * LINK CLASS
 * This is the Link class. It is a data structure containing a list of dates and the set of corresponding users.
 * It contains all the required public functions. equals() is also overridden.
 *
 */

public class Link {
	/** isValidLink is set to true when the link is given users */
	private boolean isValidLink;
	/** the set of linked users */
	private Set<User> linkedUsers;
	/** isActiveAfterLastLink keeps track of whether the latest event left the link active or inactive */
	private boolean isActiveAfterLastLink;
	/** List of dates for the link */
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
	 */
	public void setUsers(Set<User> users, SocialNetworkStatus status){
		//check if the link is valid
		if(this.isValidLink) 
			status.setStatus(ErrorStatus.ALREADY_VALID);
		//if the link is already set or the given set does not have 2 distinct users, return false
		else if(users == null || users.size() != 2)
			status.setStatus(ErrorStatus.INVALID_USERS);
		//else the link is good for setUsers
		else{
			linkedUsers = users;
			this.isValidLink = true;
			status.setStatus(ErrorStatus.SUCCESS);
		}
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
	 * @throws UninitializedObjectException		throws if the link is not valid
	 */
	public void establish(Date date, SocialNetworkStatus status) throws UninitializedObjectException{
		checkIfLinkIsReady(date, status, status);
		boolean isEstablish = true;
		createEvent(isEstablish, date, status);
	}
	
	/**
	 * Create the given event if the link is valid for that kind of event
	 * @param isEstablish	true if this is an establishment event, false for teardown
	 * @param date	the date of the event
	 * @param status	error status
	 */
	private void createEvent(boolean isEstablish, Date date, SocialNetworkStatus status) {
		//Check if the link is ready for this kind of event
		checkIfLinkValidForNewEvent(isEstablish,date, status);
		//if it is, add it
		if(status.isSuccess()){
			addEvent(isEstablish, date, status);
		}
	}
	
	/**
	 * Check if the date is after the last event and if the activity of this link
	 * is a match for this type of event
	 * @param isEstablish	true if this is an establishment event, false for teardown
	 * @param date	the date of the event
	 * @param status	error status
	 */
	private void checkIfLinkValidForNewEvent(boolean isEstablish, Date date, SocialNetworkStatus status){
		checkAfterLastDate(date, status);
		checkIfActiveMatchesEventType(isEstablish, status);
	}
	
	/**
	 * Changes the error status if the latest activity of this link does not match
	 * this kind of event
	 * @param isEstablish true if establish, false for teardown
	 * @param status	error status
	 */
	private void checkIfActiveMatchesEventType(boolean isEstablish,
			SocialNetworkStatus status) {
		//If this is establish and the link is already active, set error
		if(establishAndActive(isEstablish)){
			status.setStatus(ErrorStatus.ALREADY_ACTIVE);
		}
		//id the link is teardown and the link is already inactive, set error
		else if(tearDownAndInActive(isEstablish)){
			status.setStatus(ErrorStatus.ALREADY_INACTIVE);
		}
	}
	
	private boolean tearDownAndInActive(boolean isEstablish) {
		return !isEstablish && !this.isActiveAfterLastLink;
	}

	private boolean establishAndActive(boolean isEstablish) {
		return isEstablish && this.isActiveAfterLastLink;
	}

	/**
	 * Change the error status if the given date is not after the last event
	 * @param date	given date for event
	 * @param status	error status
	 */
	private void checkAfterLastDate(Date date, SocialNetworkStatus status) {
		if(!dateIsAfterLastEvent(date))
			status.setStatus(ErrorStatus.INVALID_DATE);
		
	}
	
	/**
	 *	Add an event to the event list --- input at this point is assumed to be completely valid 
	 * @param isEstablish	true if this is an establishment event, false for teardown
	 * @param date	the date of the event
	 * @param status	error status
	 */
	private void addEvent(boolean isEstablish, Date date, SocialNetworkStatus status){
		dates.add(date);
		this.isActiveAfterLastLink = isEstablish;
		status.setStatus(ErrorStatus.SUCCESS);
	}
	
	/**
	 * tears down a link
	 * @param date	date of teardown event
	 * @throws UninitializedObjectException		throws if the link is not valid
	 */
	public void tearDown(Date date, SocialNetworkStatus status) throws UninitializedObjectException{
		checkIfLinkIsReady(date, status, status);
		boolean isEstablish = false;
		createEvent(isEstablish, date, status);
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
		checkIfLinkIsReady(date);
		
		//The index of the date is the index before the next date's index
		int index = indexOfNextEvent(date) - 1;
		
		//The date is active if the index of the next event is odd (note that index will equal 0 if the given date
		//  is before the first date, so when i == 0, it is inactive at given date
		return isActiveIndex(index);
	}
	
	/**
	 * Returns if the index is an active index.  An index is active if it is even
	 * @param index the index of a date
	 * @return	returns true if the index is an active one.
	 */
	private boolean isActiveIndex(int index){
		return index % 2 == 0;
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
		checkIfLinkIsReady(dates);
		
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
		checkIfLinkIsReady(date);
		
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
	 * Checks if parameters are null, and also checks if link is valid
	 * @param param parameter to check if null
	 * @return	returns true if param is not null and the link is valid
	 * @throws UninitializedObjectException		thrown if link is invalid
	 */
	private boolean checkIfLinkIsReady(Object param) throws UninitializedObjectException{
		//Check if the input(s) are null
		if(param == null)
			throw new NullPointerException("The given parameter is null.");
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		
		return true;
	}
	
	/**
	 * Checks if parameters are null, and also checks if link is valud
	 * @param param1 parameter to check if null
	 * @param param2 parameter to check if null
	 * @return	returns true if params are not null and the link is valid
	 * @throws UninitializedObjectException		thrown if link is invalid
	 */
	private boolean checkIfLinkIsReady(Object param1, Object param2, SocialNetworkStatus status) throws UninitializedObjectException{
		//Check if the input(s) are null
		if(param1 == null || param2 == null)
			throw new NullPointerException("The given parameter is null.");
		//if not valid, throw exception
		if(!this.isValidLink)
			throw new UninitializedObjectException("Link user set not initialized");
		status.setStatus(ErrorStatus.SUCCESS);
		return true;
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
		
		Object[] users = linkedUsers.toArray();
		String userInfo = "Link betweek Users "+((User)users[0]).getID()+" and " + ((User)users[1]).getID() + "\n";
		//dateOutput is the list of dates, descrption is the description
		String dateOutput = dates.get(0).toString();
		String description = "The link was establised on " + dates.get(0).toString();
		for(int i = 1; i < dates.size(); i++){
			//ad date to list of dates
			dateOutput += ", " + dates.get(i).toString();
			
			//add a description that corresponds to whether the link is active
			if(isActiveIndex(i)){
				description += ", was re-establised on "+dates.get(i).toString();
			}else{
				description += ", was torn down on "+dates.get(i).toString();
			}
		}
		return userInfo + dateOutput + "\n" + description;
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
			return this.linkedUsers.containsAll(l.linkedUsers) && l.linkedUsers.containsAll(this.linkedUsers);
		}else
			return false;
	}
}
