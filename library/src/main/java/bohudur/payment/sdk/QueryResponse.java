package bohudur.payment.sdk;

public interface VerifyResponse {
	void onPaymentFound(SuccessResponse response);
    void onPaymentError(FailureResponse error);
}
