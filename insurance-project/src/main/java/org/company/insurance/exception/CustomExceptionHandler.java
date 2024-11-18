package org.company.insurance.exception;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.company.insurance.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {
    @Autowired
    private EmailService emailService;

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(AgentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAgentAlreadyExistsException(AgentAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(AutoInsuranceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAutoInsuranceAlreadyExistsException(AutoInsuranceAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(ClaimAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleClaimAlreadyExistsException(ClaimAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(ClaimAssessmentAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleClaimAssessmentAlreadyExistsException(ClaimAssessmentAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(HealthInsuranceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleHealthInsuranceAlreadyExistsException(HealthInsuranceAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(InsurancePolicyAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleInsurancePolicyAlreadyExistsException(InsurancePolicyAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(PolicyHolderAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePolicyHolderAlreadyExistsException(PolicyHolderAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(PropertyInsuranceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handlePropertyInsuranceAlreadyExistsException(PropertyInsuranceAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }
    @ExceptionHandler(TravelInsuranceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTravelInsuranceAlreadyExistsException(TravelInsuranceAlreadyExistsException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(AgentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAgentNotFoundException(AgentNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(AutoInsuranceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAutoInsuranceNotFoundException(AutoInsuranceNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(ClaimNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClaimNotFoundException(ClaimNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(ClaimAssessmentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClaimAssessmentNotFoundException(ClaimAssessmentNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(HealthInsuranceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHealthInsuranceNotFoundException(HealthInsuranceNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(InsurancePolicyNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleInsurancePolicyNotFoundException(InsurancePolicyNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(PolicyHolderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePolicyHolderNotFoundException(PolicyHolderNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(PropertyInsuranceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePropertyInsuranceNotFoundException(PropertyInsuranceNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }

    @ExceptionHandler(TravelInsuranceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTravelInsuranceNotFoundException(TravelInsuranceNotFoundException exception) throws MessagingException, UnsupportedEncodingException {
        return getErrorResponseResponseEntity(exception.getMessage(), exception);
    }


    private ResponseEntity<ErrorResponse> getErrorResponseResponseEntity(String message, Object exception) throws MessagingException, UnsupportedEncodingException {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        errors.put("message", message);
        emailService.sendEmail("sanyabryndio@gmail.com", message);
        log.error(message);
        return new ResponseEntity<>(new ErrorResponse(httpStatus,errors),httpStatus);
    }
}