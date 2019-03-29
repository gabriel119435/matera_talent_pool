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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
//  this was made to override basic json body error from Spring with valuable data https://www.baeldung.com/exception-handling-for-rest-with-spring
public class ErrorHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	ErrorHandlerProtected protectedMethods;

	// custom exception for validators
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<GenericError> handleBusinessException(BusinessException e) {
		GenericError error = protectedMethods.createGenericError(e.getCode(), e.getMessage());
		protectedMethods.printGenericError(error, log);
		return new ResponseEntity<GenericError>(error, HttpStatus.BAD_REQUEST);
	}
	
	// NPE
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<GenericError> npeHandler(NullPointerException e) {
		Throwable result = protectedMethods.getPrimaryCause(e);
		e.printStackTrace();
		return new ResponseEntity<GenericError>(
				protectedMethods.createGenericError(5L,
						Arrays.asList(result.getMessage(), "timestamp: " + protectedMethods.getTimeNow())),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// checking url params against javax.validation.constraints
	// like @Digits or @Size for future use
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<GenericError> handleParamNotValid(ConstraintViolationException e) {
		Set<ConstraintViolation<?>> errors = e.getConstraintViolations();
		List<String> codeErrors = errors.stream()
										.map(error -> 
											{ String[] pathParts = error.getPropertyPath().toString().split("\\.");
											  return error.getMessage() + " " + pathParts[pathParts.length - 1] + ": " + error.getInvalidValue();
											}).collect(Collectors.toList());
		GenericError error = protectedMethods.createGenericError(3L, codeErrors);
		protectedMethods.printGenericError(error, log);
		return new ResponseEntity<GenericError>(error, HttpStatus.BAD_REQUEST);
	}

	// used on mismatch types on url (ex: "abc" for Long)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<GenericError> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
		String message = e.getMessage().split(";")[0] + " " + e.getName() + ": " + e.getValue();
		GenericError error = protectedMethods.createGenericError(2L, message);
		protectedMethods.printGenericError(error, log);
		return new ResponseEntity<GenericError>(error, HttpStatus.BAD_REQUEST);
	}

	// used when body attributes mismatch. Ex: date has 'yyyy-MM-dd' and receives '31-12-2000' or
	// when json is invalid, or even missing
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable result = protectedMethods.getPrimaryCause(ex);
		String errorMessage = result.getMessage().split(" at ")[0].replaceAll("\n", "").replaceAll("\"", "");
		GenericError error = protectedMethods.createGenericError(6L, errorMessage);
		protectedMethods.printGenericError(error, log);
		return new ResponseEntity<Object>((Object) error, HttpStatus.BAD_REQUEST);
	}

	// used on @Valid objects (checking json body against object)
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
		
		List<String> codeErrors = fieldErrors.stream()
											 .map(error -> error.getField() + ": " + error.getDefaultMessage())
											 .collect(Collectors.toList());
		codeErrors.addAll(globalErrors.stream()
									  .map(ObjectError::getDefaultMessage)
									  .collect(Collectors.toList()));
		GenericError error = protectedMethods.createGenericError(4L, codeErrors);
		protectedMethods.printGenericError(error, log);
		return new ResponseEntity<Object>((Object) error, HttpStatus.BAD_REQUEST);
	}
}
