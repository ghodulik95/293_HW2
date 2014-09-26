
/**
 * 
 * @author George Hodulik
 *	gmh73@case.edu
 */
 /*
 *	The SocialNetworkStatus class contains an enum with an error status
 */
public class SocialNetworkStatus {
		private ErrorStatus status;
		
		/**
		 * Constructor sets status to s
		 * @param s		An error Status
		 */
	    public SocialNetworkStatus(ErrorStatus s){
	    	status = s;
	    }
	    
	    /**
	     * set status to s
	     * @param s		An ErrorStatus
	     * @return returns this SocialNetworkStatus after status change
	     */
	    public SocialNetworkStatus setStatus(ErrorStatus s){
	    	status = s;
	    	return this;
	    }
	    
	    /**
	     * returns the current status
	     * @return returns the current error status
	     */
	    public ErrorStatus getStatus(){
	    	return status;
	    }
	    
	    /**
	     * Checks if the value of a SocialNetworkStatus or ErrorStatus is the same as
	     * this status.
	     * Note: Was going to override equals, but decided not to because then
	     * 	a SocialNetworkStatus could equal an ErrorStatus, but an ErrorStatus
	     * 	could not equal a SocialNetworkStatus, which is not the definition of equals
	     * @param o
	     * @return	returns true if the parameter o is either a SocialNetworkStatus with the same
	     * 			status value, or an ErrorStatus with the same value as this status
	     */
	    public boolean is(Object o){
	    	if(o instanceof SocialNetworkStatus){
	    		return ((SocialNetworkStatus) o).status == this.status;
	    	}else if (o instanceof ErrorStatus){
	    		return ((ErrorStatus) o) == this.status;
	    	}else
	    		return false;
	    }
	    
	    /**
	     * returns if the current status is SUCCESS
	     * @return	returns true if the current status is SUCCESS
	     */
	    public boolean isSuccess(){
	    	return status == ErrorStatus.SUCCESS;
	    }
}
