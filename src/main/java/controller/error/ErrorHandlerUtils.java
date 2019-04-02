package controller.error;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import domain.error.GenericError;

@Component
// some methods to only be used at ErrorHandler.java
final class ErrorHandlerUtils {
	
	@Autowired
	private ErrorConfig errorConfig;

	protected void printGenericError(GenericError e, Logger log) {
		log.error("code: " + e.getCode() + " message: " + e.getStandardMessage());
		log.error("customMessage: " + e.getCustomMessage().stream().map(Object::toString).collect(Collectors.joining(",")));
	}

	protected GenericError createGenericError(Long code, String customMessage) {
		return createGenericError(code, customMessage != null ? Arrays.asList(customMessage) : null);
	}
	
	protected GenericError createGenericError(Long code, List<String> customMessages) {
		return GenericError.builder()
				.code(code)
				.standardMessage(errorConfig.getStandardText(code))
				.customMessage(customMessages).build();
	}

	// https://stackoverflow.com/a/28565320/3026886
	protected Throwable getPrimaryCause(Throwable e) {
		Throwable cause = null;
		Throwable result = e;
		while (null != (cause = result.getCause()) && (result != cause))
			result = cause;
		return result;
	}
	
	protected String getTimeNow() {
		return LocalDateTime.now().withNano(0).toString();
	}

}