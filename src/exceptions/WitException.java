package exceptions;

import javax.servlet.ServletException;

public class WitException extends ServletException{
	public WitException(String message) {
		super(message);
	}	
	public WitException(String message, Throwable t) {
		super(message, t);
	}
}
