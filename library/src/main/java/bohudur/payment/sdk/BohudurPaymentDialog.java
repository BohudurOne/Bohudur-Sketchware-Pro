package bohudur.payment.sdk;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import bohudur.payment.sdk.R;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BohudurPaymentDialog {
    
    private Dialog dialog;
    private RedirectResponse redirectResponse;
    
    private WebView webView;
    private ImageView loader;
    
    public interface RedirectResponse {
        void onRedirect(String url);
    }
    
    public BohudurPaymentDialog(Context context/*, RedirectResponse redirectResponse*/) {
        this.redirectResponse = redirectResponse;
        showFullScreenDialog(context);
    }
    
    public void loadWebViewAndTakePayment(String url, RedirectResponse redirectResponse) {
        webView.loadUrl(url);
        
    	webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("payments.blogx.top/checkout/redirect/") || url.contains("payments.blogx.top/checkout/cancelled/")) {
                    redirectResponse.onRedirect(url);
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });
    }
    
    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
    
    public void hideLoading(){
        loader.setVisibility(View.GONE);
    }
    
    public void showWebView(){
        webView.setVisibility(View.VISIBLE);
    }
    
    public void showLoading(){
        loader.setVisibility(View.VISIBLE);
    }
    
    public void hideWebView() {
    	webView.setVisibility(View.GONE);
    }
    
    
    private void showFullScreenDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(createDialogLayout(context));
    
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    
            // Start the animation on the loader
            Animation bounceAnim = AnimationUtils.loadAnimation(context, R.anim.loading);
            loader.startAnimation(bounceAnim);
    
            // Use modern API for system bars
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            dialog.getWindow().setDimAmount(0.2f);
        }
    }
    
    private LinearLayout createDialogLayout(Context context) {
        // Create the main LinearLayout for the dialog
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Create a child LinearLayout
        LinearLayout childLayout = new LinearLayout(context);
        childLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        childLayout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        childLayout.setOrientation(LinearLayout.VERTICAL);
        
        // Create ImageView (loader) and set it as global variable
        loader = new ImageView(context);
        loader.setId(View.generateViewId()); // Dynamically generated ID
        loader.setLayoutParams(new LinearLayout.LayoutParams(150, 150)); // Set the width and height (100dp)
        loader.setImageResource(R.drawable.ic_loader); // Set the loader image (adjust resource as needed)
        
        GradientDrawable background = new GradientDrawable();
        background.setColor(Color.WHITE); // Background color
        background.setCornerRadius(10); // Corner radius
        background.setStroke(0, Color.LTGRAY); // Optional border
        background.setPadding(10,10,10,10);
        loader.setBackground(background);

        // Create WebView and set it as global variable
        webView = new WebView(context);
        webView.setId(View.generateViewId()); // Dynamically generated ID
        webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        webView.setVisibility(View.GONE); // Set WebView visibility to gone initially
        
        WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.setWebViewClient(new WebViewClient());

        // Add ImageView and WebView to the child layout
        childLayout.addView(loader);
        childLayout.addView(webView);

        // Add the child layout to the main layout
        mainLayout.addView(childLayout);

        return mainLayout;
    }
}