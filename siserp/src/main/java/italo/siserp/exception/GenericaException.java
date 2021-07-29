package italo.siserp.exception;

public class GenericaException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private String[] params;
		
	public String[] getParams() {
		return params;
	}
	
	public void setParams( String... params ) {
		this.params = params;
	}
	
}
