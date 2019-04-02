package controller.error;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import domain.error.GenericError;
import domain.exception.BusinessException;
import domain.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
//  this was made to override basic json body error from Spring with only valuable data 
//  https://www.baeldung.com/exception-handling-for-rest-with-spring
public class ErrorHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private ErrorHandlerUtils utils;

	// used on mismatch types on url (ex: "abc" for Long)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<GenericError> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		final String message = e.getMessage().split(";")[0] + " " + e.getName() + ": " + e.getValue();
		// 2 = value with different type
		final GenericError error = utils.createGenericError(2L, message);
		utils.printGenericError(error, log);
		return new ResponseEntity<GenericError>(error, HttpStatus.BAD_REQUEST);
	}

	// checking url params against javax.validation.constraints
	// like @Digits or @Size for future use
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<GenericError> handleParamNotValid(ConstraintViolationException e) {
		final Set<ConstraintViolation<?>> errors = e.getConstraintViolations();
		final List<String> codeErrors = errors.stream()
										      .map(error -> 
											    { String[] pathParts = error.getPropertyPath().toString().split("\\.");
											      return error.getMessage() + " " + pathParts[pathParts.length - 1] + ": " + error.getInvalidValue();})
										      .collect(Collectors.toList());
		// 3 = url value not valid
		final GenericError error = utils.createGenericError(3L, codeErrors);
		utils.printGenericError(error, log);
		return new ResponseEntity<GenericError>(error, HttpStatus.BAD_REQUEST);
	}
	
	// used on @Valid objects (checking json body against object)
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		final List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
		
		final List<String> codeErrors = fieldErrors.stream()
												   .map(error -> error.getField() + ": " + error.getDefaultMessage())
											       .collect(Collectors.toList());
		codeErrors.addAll(globalErrors.stream()
									  .map(ObjectError::getDefaultMessage)
									  .collect(Collectors.toList()));
		// 4 = request body not valid
		final GenericError error = utils.createGenericError(4L, codeErrors);
		utils.printGenericError(error, log);
		return new ResponseEntity<Object>((Object) error, HttpStatus.BAD_REQUEST);
	}
	
	// NPE
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<GenericError> npeHandler(NullPointerException e) {
		final Throwable result = utils.getPrimaryCause(e);
		e.printStackTrace();
		return new ResponseEntity<GenericError>(
				// 5 = null pointer exception, send logs to support
				utils.createGenericError(5L,Arrays.asList(result.getMessage(), "timestamp: " + utils.getTimeNow())),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	// used when body attributes mismatch. Ex: date has 'yyyy-MM-dd' and receives '31-12-2000' or when json is invalid, or even missing
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		final Throwable result = utils.getPrimaryCause(ex);
		final String errorMessage = result.getMessage().split(" at ")[0].replaceAll("\n", "").replaceAll("\"", "");
		// 6 = bad/missing json. Ex: dates should use 'yyyy-MM-dd' format
		final GenericError error = utils.createGenericError(6L, errorMessage);
		utils.printGenericError(error, log);
		return new ResponseEntity<Object>((Object) error, HttpStatus.BAD_REQUEST);
	}

	// custom exception for validators
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<GenericError> handleBusinessException(BusinessException e) {
		final GenericError error = utils.createGenericError(e.getCode(), e.getMessage());
		utils.printGenericError(error, log);
		return new ResponseEntity<GenericError>(error, HttpStatus.BAD_REQUEST);
	}
	
	// custom exception for EntityNotFoundException
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<GenericError> handleEntityNotFoundException(EntityNotFoundException e) {
		final GenericError error = utils.createGenericError(e.getCode(), e.getMessage());
		utils.printGenericError(error, log);
		return new ResponseEntity<GenericError>(error, HttpStatus.NOT_FOUND);
	}
	
	
}