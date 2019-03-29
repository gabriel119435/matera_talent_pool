package domain.exception;

import lombok.Getter;

@Getter
//RuntimeException = unchecked exception
//exception created to throw business logic exceptions, not related to Java Exceptions
public class BusinessException extends RuntimeException {

	private long code;

	private static final long serialVersionUID = 1L;

	public BusinessException(long code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(long code) {
		super();
		this.code = code;
	}

}