package domain.exception;

public class EntityNotFoundException extends BusinessException{

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String message) {
		// error code on errorMessages.properties
		// 1 = entity not found
		super(1L, message);
	}

}
