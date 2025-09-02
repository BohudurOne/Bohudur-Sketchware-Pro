package bohudur.payment.sdk;

public interface QueryResponse {
	void onPaymentFound(SuccessResponse response);
    void onPaymentError(FailureResponse error);
}
