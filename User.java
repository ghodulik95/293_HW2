
public class User{
	private String id;
	private boolean isValidUser;
	
	public User(){
		isValidUser = false;
	}
	
	public boolean isValid(){
		return isValidUser;
	}
	
	public String getID(){
		return id;
	}
	
	public boolean setID(String id_in) throws NullPointerException{
		if(id_in.compareTo("") == 0){
			throw new NullPointerException("Input string must not be null.");
		}
		
		if(id == null){
			id = id_in;
			isValidUser = true;
			return true;
		}else{
			return false;
		}
	}
	
	public String toString(){
		if(this.isValid())
			return this.getID();
		else
			return "Invalid User: Uninitialized ID";
	}

	@Override
	public boolean equals(Object o){
		// TODO Auto-generated method stub
		if(o instanceof User)
			return this.getID().compareTo(((User)o).getID()) == 0;
		else
			return false;
	}
	
	@Override
	public int hashCode(){
		return this.getID().hashCode();
	}

}
