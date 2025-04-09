package bohudur.payment.sdk;

import org.json.JSONObject;

public class SuccessResponse {
    private final JSONObject responseData;

    public SuccessResponse(JSONObject responseData) {
        this.responseData = responseData;
    }

    public String getName() {
        try {
            return responseData.getString("Full Name");
        } catch (Exception e) {
            return "Unknown Name";  // Default value in case of error
        }
    }

    public String getEmail() {
        try {
            return responseData.getString("Email");
        } catch (Exception e) {
            return "Unknown Email";  // Default value in case of error
        }
    }

    public double getAmount() {
        try {
            return responseData.getDouble("Amount");
        } catch (Exception e) {
            return 0.0;  // Default value in case of error
        }
    }

    public double getConvertedAmount() {
        try {
            return responseData.getDouble("Converted Amount");
        } catch (Exception e) {
            return 0.0;  // Default value in case of error
        }
    }

    public double getTotalAmount() {
        try {
            return responseData.getDouble("Total Amount");
        } catch (Exception e) {
            return 0.0;  // Default value in case of error
        }
    }

    public String getCurrency() {
        try {
            return responseData.getString("Currency");
        } catch (Exception e) {
            return "Unknown Currency";  // Default value in case of error
        }
    }

    public double getCurrencyValue() {
        try {
            return responseData.getDouble("Currency Value");
        } catch (Exception e) {
            return 0.0;  // Default value in case of error
        }
    }

    public String getRedirectUrl() {
        try {
            return responseData.getString("redirect_url");
        } catch (Exception e) {
            return "Unknown Redirect URL";  // Default value in case of error
        }
    }

    public String getCancelledUrl() {
        try {
            return responseData.getString("cancelled_url");
        } catch (Exception e) {
            return "Unknown Cancelled URL";  // Default value in case of error
        }
    }

    public String getMethod() {
        try {
            return responseData.getString("Method");
        } catch (Exception e) {
            return "Unknown Method";  // Default value in case of error
        }
    }

    public String getMFS() {
        try {
            return responseData.getString("MFS");
        } catch (Exception e) {
            return "Unknown MFS";  // Default value in case of error
        }
    }

    public String getNumber() {
        try {
            return responseData.getString("Number");
        } catch (Exception e) {
            return "Unknown Number";  // Default value in case of error
        }
    }

    public String getTrx() {
        try {
            return responseData.getString("Transaction ID");
        } catch (Exception e) {
            return "Unknown Transaction ID";  // Default value in case of error
        }
    }

    public String getTime() {
        try {
            return responseData.getString("Payment Time");
        } catch (Exception e) {
            return "Unknown Payment Time";  // Default value in case of error
        }
    }

    public String getStatus() {
        try {
            return responseData.getString("Status");
        } catch (Exception e) {
            return "Unknown Status";  // Default value in case of error
        }
    }

    public JSONObject getMetadata() {
        try {
            return responseData.getJSONObject("Metadata");
        } catch (Exception e) {
            return null;  // Return null in case of error
        }
    }
}
