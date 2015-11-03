package exceptions;

import javax.servlet.ServletException;

public class NotImplementedException extends ServletException {

	public NotImplementedException(String message) {
		super(message);
	}

}
