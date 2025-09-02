package bohudur.payment.sdk;

import org.json.JSONObject;

public class SuccessResponse {
    private final JSONObject responseData;

    public SuccessResponse(JSONObject responseData) {
        this.responseData = responseData;
    }

    public String getFullName() {
        try {
            return responseData.getString("full_name");
        } catch (Exception e) {
            return "Unknown Name";
        }
    }

    public String getEmail() {
        try {
            return responseData.getString("email");
        } catch (Exception e) {
            return "Unknown Email";
        }
    }

    public double getAmount() {
        try {
            return responseData.getDouble("amount");
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getConvertedAmount() {
        try {
            return responseData.getDouble("converted_amount");
        } catch (Exception e) {
            return 0.0;
        }
    }

    public double getTotalAmount() {
        try {
            return responseData.getDouble("total_amount");
        } catch (Exception e) {
            return 0.0;
        }
    }

    public String getCurrency() {
        try {
            return responseData.getString("currency");
        } catch (Exception e) {
            return "Unknown Currency";
        }
    }

    public double getCurrencyValue() {
        try {
            return responseData.getDouble("currency_value");
        } catch (Exception e) {
            return 0.0;
        }
    }

    public String getRedirectUrl() {
        try {
            return responseData.getString("redirect_url");
        } catch (Exception e) {
            return "Unknown Redirect URL";
        }
    }

    public String getCancelledUrl() {
        try {
            return responseData.getString("cancel_url");
        } catch (Exception e) {
            return "Unknown Cancelled URL";
        }
    }
    
    public String getTime() {
    	try {
            return responseData.getString("time");
        } catch (Exception e) {
            return "Unknown Time";
        }
    }

    public String getPaymentMethod() {
        try {
            return responseData.getString("payment_method");
        } catch (Exception e) {
            return "Unknown MFS";
        }
    }
    
    public String getTransactionMethod() {
        try {
            return responseData.getString("transaction_method");
        } catch (Exception e) {
            return "Unknown Method";
        }
    }

    public String getSenderNumber() {
        try {
            return responseData.getString("sender_number");
        } catch (Exception e) {
            return "Unknown Number";
        }
    }

    public String getTransactionID() {
        try {
            return responseData.getString("transaction_id");
        } catch (Exception e) {
            return "Unknown Transaction ID";
        }
    }

    public String getPaymentDateAndTime() {
        try {
            return responseData.getString("payment_date");
        } catch (Exception e) {
            return "Unknown Payment Time";
        }
    }

    public String getStatus() {
        try {
            return responseData.getString("status");
        } catch (Exception e) {
            return "Unknown Status";
        }
    }

    public JSONObject getMetadata() {
        try {
            return responseData.getJSONObject("metadata");
        } catch (Exception e) {
            return null;
        }
    }
}
