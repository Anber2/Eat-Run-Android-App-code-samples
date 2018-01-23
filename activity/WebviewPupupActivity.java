package com.mawaqaa.eatandrun.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mawaqaa.eatandrun.R;
import com.mawaqaa.eatandrun.Utilities.PreferenceUtil;

/**
 * Created by HP on 7/19/2017.
 */

public class WebviewPupupActivity extends Activity {
    int index = -1;
    String url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        final ProgressDialog pd = ProgressDialog.show(this, "", "Please wait...", true);


        webView = new WebView(this);




        webView = (WebView) findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);


        //webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(PaymentUrl.this, description, Toast.LENGTH_SHORT).show();
                Log.e("onReceivedError", "  " + failingUrl);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("onPageStarted", "  " + url);
                pd.show();
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("onPageFinished", "  " + url);
                pd.dismiss();

            }

        });
        webView.loadUrl(PreferenceUtil.getProfileUri(WebviewPupupActivity.this));

        //  goBackInWebView();

        /*while (webView.canGoBackOrForward(index)) {
            if (!history.getItemAtIndex(history.getCurrentIndex() + index).getUrl().equals("about:blank")) {
                webView.goBackOrForward(index);
                url = history.getItemAtIndex(-index).getUrl();
            }

            if (!history.getItemAtIndex(history.getCurrentIndex() + index).getUrl().equals("about:blank")) {
                webView.goBackOrForward(index);
                url = history.getItemAtIndex(-index).getUrl();
                Log.e("tag", "first non empty" + url);
                break;
            }
        }*/

        try {

         /*   WebBackForwardList mWebBackForwardList = webView.copyBackForwardList();
            String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
            Log.d("historyUrl ", "****" + historyUrl);*/

           /* String webUrl = webView.getOriginalUrl();
            Log.d("webUrl","---"+webUrl);

            String webUrl2 = webView.getUrl();
            Log.d("webUrl2 ","+++"+webUrl2);*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void goBackInWebView() {
        WebBackForwardList history = webView.copyBackForwardList();
        int index = -1;
        String url = null;

        while (webView.canGoBackOrForward(index)) {
            if (!history.getItemAtIndex(history.getCurrentIndex() + index).getUrl().equals("about:blank")) {
                webView.goBackOrForward(index);
                url = history.getItemAtIndex(-index).getUrl();
                Log.e("goBackInWebView ", "first non empty" + url);
                break;
            }
            index--;

        }
        // no history found that is not empty
        if (url == null) {
            finish();
        }
    }
}
