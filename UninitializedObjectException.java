//George Hodulik EECS 293 HW2
//UninitializedObjectException is an exception that is thrown when
//an object is trying to get called to be used that is not initialized
//It extends exception, and calls on its super functions
public class UninitializedObjectException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UninitializedObjectException(){
		super();
	}
	
	public UninitializedObjectException(String message){
		super(message);
	}
	
	public UninitializedObjectException(String message, Throwable cause){
		super(message, cause);
	}
	
	public UninitializedObjectException(Throwable cause){
		super(cause);
	}
}
