package shobs.github.dynamodblocalspringboot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * All known errors for which there is a mapping must be thrown with this exception. 
 * The Payment Controller will then convert that to the appropriate response.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerControlsException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

    private CustomerControlsExceptionCode customerControlsExceptionCode;
    private String errorMessage;
}