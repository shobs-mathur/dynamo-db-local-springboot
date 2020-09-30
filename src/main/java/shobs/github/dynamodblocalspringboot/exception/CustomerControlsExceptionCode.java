package shobs.github.dynamodblocalspringboot.exception;

public enum CustomerControlsExceptionCode {
	CC_1001("CC_1001", ""),
	CC_1002("CC_1002", ""),
	CC_1003("CC_1003", "");

	private String responseCode;
	private String errorMessage;

	CustomerControlsExceptionCode(String responseCode, String errorMessage) {
		this.responseCode = responseCode;
		this.errorMessage = errorMessage;
	}
}