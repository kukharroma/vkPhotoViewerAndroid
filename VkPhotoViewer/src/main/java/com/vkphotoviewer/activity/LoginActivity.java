package com.vkphotoviewer.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.vk.sdk.*;
import com.vk.sdk.api.VKError;
import com.vkphotoviewer.R;
import com.vkphotoviewer.util.VkPhotoViewerConst;

public class LoginActivity extends Activity {

    private Button loginBtn, logoutBtn, returnToAccountBtn;

    private static final String[] scope = new String[]{
            VKScope.PHOTOS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VKUIHelper.onCreate(this);
        setContentView(R.layout.login_layout);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(requestCode, resultCode, data);
    }

    private final VKSdkListener listener = new VKSdkListener() {
        @Override
        public void onCaptchaError(VKError captchaError) {

        }

        @Override
        public void onTokenExpired(VKAccessToken expiredToken) {

        }

        @Override
        public void onAccessDenied(VKError authorizationError) {

        }

        @Override
        public void onReceiveNewToken(VKAccessToken newToken) {
            super.onReceiveNewToken(newToken);
            Intent intent = new Intent(getApplicationContext(), AlbumListActivity.class);
            startActivity(intent);
        }

        @Override
        public void onAcceptUserToken(VKAccessToken token) {
            super.onAcceptUserToken(token);
        }
    };

    public void onLoginBtnClick(View view) {
        VKSdk.initialize(listener, VkPhotoViewerConst.API_ID);
        if (!VKSdk.isLoggedIn()) {
            VKSdk.authorize(scope);
        }
    }

    public void onReturnToAccountBtnClick(View view) {
        Intent intent = new Intent(this, AlbumListActivity.class);
        startActivity(intent);
    }

    public void onLogoutBtnClick(View view) {
        VKSdk.logout();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void initView() {
        setContentView(R.layout.login_layout);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        returnToAccountBtn = (Button) findViewById(R.id.returnToAccountBtn);

        VKSdk.initialize(listener, VkPhotoViewerConst.API_ID);
        if (VKSdk.isLoggedIn()) {
            loginBtn.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
            returnToAccountBtn.setVisibility(View.VISIBLE);
        } else {
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
            returnToAccountBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
