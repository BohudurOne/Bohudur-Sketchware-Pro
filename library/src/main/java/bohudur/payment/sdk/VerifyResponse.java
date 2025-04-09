package bohudur.payment.sdk;

public interface VerifyResponse {
	void onPaymentVerified(SuccessResponse response);
    void onPaymentError(FailureResponse error);
}