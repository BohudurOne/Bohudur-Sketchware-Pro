package bohudur.payment.sdk;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.json.JSONException;
import org.json.JSONObject;

public class Bohudur {
    
    private final Context context;
    private String apiKey;
    
    private static final String REQUEST_URL = "https://request.bohudur.one/create/";
    private static final String EXECUTE_URL = "https://request.bohudur.one/execute/";
    private static final String VERIFY_URL = "https://request.bohudur.one/verify/";
    
    private final Map<String, Object> requestData = new HashMap<>();
    private final Map<String, String> webhookData = new HashMap<>();
    private final Map<String, Object> metadata = new HashMap<>();
    
    public static class Type {
        public static String POST() {
            return "POST";
        }
    
        public static String GET() {
            return "GET";
        }
    }
    
    public static class Redirect {
        public static String Default() {
            return "default";
        }
    }
    
    public void request(PaymentResponse response) {
        request(new Consumer<SuccessResponse>() {
            @Override
            public void accept(SuccessResponse success) {
                response.onPaymentSuccess(success);
            }
        }, new Consumer<FailureResponse>() {
            @Override
            public void accept(FailureResponse failure) {
                response.onPaymentCancelled(failure);
            }
        });
    }
    
    public void verify(String paymentkey, VerifyResponse response) {
        verify(paymentkey, new Consumer<SuccessResponse>() {
            @Override
            public void accept(SuccessResponse success) {
                response.onPaymentVerified(success);
            }
        }, new Consumer<FailureResponse>() {
            @Override
            public void accept(FailureResponse failure) {
                response.onPaymentError(failure);
            }
        });
    }
    
    // Constructor with Context and API Key
    public Bohudur(Context context, String apiKey) {
        this.context = context;
        this.apiKey = apiKey;
    }

    // Payment Data Methods
    public Bohudur setFullName(String fullName) {
        requestData.put("fullname", fullName);
        return this;
    }

    public Bohudur setEmail(String email) {
        requestData.put("email", email);
        return this;
    }

    public Bohudur setAmount(double amount) {
        requestData.put("amount", amount);
        return this;
    }

    public Bohudur setCurrency(String currency) {
        requestData.put("currency", currency);
        return this;
    }
        
    public Bohudur setCurrencyValue(double currency_value) {
        requestData.put("currency_value", currency_value);
        return this;
    }
        
    public Bohudur setReturnType(String type) {
        requestData.put("return_type", type);
        return this;
    }

    // Webhook Methods
    public Bohudur setWebhookSuccessUrl(String successUrl) {
        webhookData.put("success", successUrl);
        return this;
    }

    public Bohudur setWebhookCancelUrl(String cancelUrl) {
        webhookData.put("cancel", cancelUrl);
        return this;
    }

    // Metadata Methods
    public Bohudur addMetadata(String key, Object value) {
        metadata.put(key, value);
        return this;
    }

    public Bohudur addMetadata(Map<String, Object> metadataMap) {
        metadata.putAll(metadataMap);
        return this;
    }

    // Execute Request using Internal Request Executor
    public void request(Consumer<SuccessResponse> onSuccess, Consumer<FailureResponse> onCancel) {
        
        requestData.put("webhooks", webhookData);
        requestData.put("metadata", metadata);
        requestData.put("redirect_url","default");
        requestData.put("cancelled_url","default");
        
        BohudurPaymentDialog paymentDialog = new BohudurPaymentDialog(context);
        paymentDialog.show();
        paymentDialog.showLoading();
        paymentDialog.hideWebView();
        
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST, REQUEST_URL, new JSONObject(requestData),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        try {
                            if (response.has("responseCode") && response.getInt("responseCode") == 200) {
                                paymentDialog.hideLoading();
                                paymentDialog.showWebView();
                                paymentDialog.loadWebViewAndTakePayment(
                                    response.getString("payment_url"),
                                    new BohudurPaymentDialog.RedirectResponse() {
                                        @Override
                                        public void onRedirect(String url) {
                                            if(url.contains("payments.blogx.top/checkout/cancelled/")){
                                                try{
                                                    String jsonString = "{\"responseCode\":2003, \"message\": \"Payment Cancelled!\", \"status\": \"failed\"}";
                                                    JSONObject jsonObject = new JSONObject(jsonString);
                                                    onCancel.accept(new FailureResponse(jsonObject));
                                                    paymentDialog.dismiss();
                                                }catch(JSONException e){}
                                            }else if(url.contains("payments.blogx.top/checkout/redirect/")){
                                                    
                                                JSONObject jsonObject = new JSONObject();
                                                try {
                                                    jsonObject.put("paymentkey", response.getString("paymentkey"));
                                                } catch (JSONException err) {}
                                                    
                                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(
                                                    Request.Method.POST, EXECUTE_URL, jsonObject,
                                                        new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response2) {
                                                                if (response != null) {
                                                                    try {
                                                                        if (!response2.has("responseCode") && response2.has("Status") && response2.getString("Status").equals("EXECUTED")) {
                                                                            paymentDialog.dismiss();
                                                                            onSuccess.accept(new SuccessResponse(response2));
                                                                        } else if(response2.has("responseCode")){
                                                                            paymentDialog.dismiss();
                                                                            onCancel.accept(new FailureResponse(response2));
                                                                        }
                                                                    } catch (JSONException exception) {
                                                                        paymentDialog.dismiss();
                                                                        onCancel.accept(new FailureResponse(response2));
                                                                    }
                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {
                                                                try {
                                                                    String jsonString = "{\"responseCode\":2002, \"message\": \"internal error!\", \"status\": \"failed\"}";
                                                                    JSONObject jsonObject = new JSONObject(jsonString);
                                                                    onCancel.accept(new FailureResponse(jsonObject));
                                                                    paymentDialog.dismiss();
                                                                } catch (JSONException err) {}
                                                            }
                                                        }) {
                                                        @Override
                                                        public java.util.Map<String, String> getHeaders() {
                                                            java.util.Map<String, String> headers = new java.util.HashMap<>();
                                                            headers.put("Content-Type", "application/json");
                                                            headers.put("AH-BOHUDUR-API-KEY", apiKey);
                                                            return headers;
                                                        }
                                                };
                                                    
                                                RequestQueue requestQueue = Volley.newRequestQueue(context);
                                                requestQueue.add(jsonObjectRequest2);
                                            }
                                        }
                                    });
                            } else {
                                paymentDialog.dismiss();
                                onCancel.accept(new FailureResponse(response));
                            }
                        } catch (JSONException exception) {
                            paymentDialog.dismiss();
                            onCancel.accept(new FailureResponse(response));
                        }
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String jsonString = "{\"responseCode\":2001, \"message\": \"internal error!\", \"status\": \"failed\"}";
                        JSONObject jsonObject = new JSONObject(jsonString);
                        onCancel.accept(new FailureResponse(jsonObject));
                        paymentDialog.dismiss();
                    } catch (JSONException err) {}
                }
            }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("AH-BOHUDUR-API-KEY", apiKey);
                return headers;
            }
        };
        
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
        
    public void verify(String paymentKey, Consumer<SuccessResponse> onSuccess, Consumer<FailureResponse> onCancel) {
        BohudurPaymentDialog paymentDialog = new BohudurPaymentDialog(context);
        paymentDialog.show();
        paymentDialog.hideWebView();
        paymentDialog.showLoading();
        
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("paymentkey", paymentKey);
        } catch (JSONException err) {}
        
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.POST, VERIFY_URL, jsonObject,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        try {
                            if (!response.has("responseCode") && response.has("Status") && response.getString("Status").equals("EXECUTED")) {
                                paymentDialog.dismiss();
                                onSuccess.accept(new SuccessResponse(response));
                            } else if(response.has("responseCode")){
                                paymentDialog.dismiss();
                                onCancel.accept(new FailureResponse(response));
                            }
                        } catch (JSONException exception) {
                            paymentDialog.dismiss();
                            onCancel.accept(new FailureResponse(response));
                        }
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try {
                        String jsonString = "{\"responseCode\":2004, \"message\": \"internal error!\", \"status\": \"failed\"}";
                        JSONObject jsonObject = new JSONObject(jsonString);
                        onCancel.accept(new FailureResponse(jsonObject));
                        paymentDialog.dismiss();
                    } catch (JSONException err) {}
                }
            }) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("AH-BOHUDUR-API-KEY", apiKey);
                return headers;
            }
        };
        
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
}
