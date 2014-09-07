
public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		User u = new User();
		System.out.println("Valid: "+u.isValid()+"\tGetID:"+u.getID()+"\tsetID:"+u.setID("Bobby")+"\ttoString:"+u.toString());
		System.out.println("Valid: "+u.isValid()+"\tGetID:"+u.getID()+"\tsetID:"+u.setID("Bobby")+"\ttoString:"+u.toString());
		
		u = new User();
		System.out.println("Valid: "+u.isValid()+"\tGetID:"+u.getID()+"\ttoString:"+u.toString()+"\tsetID:"+u.setID("Bobby"));
		System.out.println("Valid: "+u.isValid()+"\tGetID:"+u.getID()+"\tsetID:"+u.setID("Bobby")+"\ttoString:"+u.toString());
		
		System.out.println("\n\n");
		
		User m = new User();
		m.setID("Mary");
		Link l = new Link();
		
		
	}

}
