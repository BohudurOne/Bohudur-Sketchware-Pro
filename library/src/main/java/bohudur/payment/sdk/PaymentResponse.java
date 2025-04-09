package bohudur.payment.sdk;

public interface PaymentResponse {
    void onPaymentSuccess(SuccessResponse response);
    void onPaymentCancelled(FailureResponse error);
}