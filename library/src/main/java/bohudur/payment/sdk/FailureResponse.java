package bohudur.payment.sdk;

import org.json.JSONException;
import org.json.JSONObject;

public class FailureResponse {
    private final String message;
    private final int responseCode;
    private final String status;

    public FailureResponse(JSONObject response) {
        String tempMessage = "";
        String tempStatus = "failed";
        int tempResponseCode = 0;

        try {
            tempMessage = response.getString("message");
            tempStatus = response.getString("status");
            tempResponseCode = response.getInt("responseCode");
        } catch (JSONException err) {}

        this.message = tempMessage;
        this.status = tempStatus;
        this.responseCode = tempResponseCode;
    }

    public String getMessage() {
        return message;
    }
    
    public int getErrorCode() {
        return responseCode;
    }

    public String getStatus() {
    	return status;
    }
}