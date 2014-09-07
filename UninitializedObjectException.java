
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
