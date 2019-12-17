package com.ikecin.demo;

import android.os.Bundle;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private RequestContext mRequestContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestContext = RequestContext.create(this.getBaseContext());
        mRequestContext.registerListener(mAuthorizeListener);

        findViewById(R.id.login).setOnClickListener(v ->
            AuthorizationManager.authorize(new AuthorizeRequest
                .Builder(mRequestContext)
                .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
                .build()));

        findViewById(R.id.logout).setOnClickListener(v ->
            AuthorizationManager.signOut(getApplicationContext(), new Listener<Void, AuthError>() {
                @Override
                public void onSuccess(Void response) {
                    // Set logged out state in UI
                }

                @Override
                public void onError(AuthError authError) {
                    // Log the error
                }
            }));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Scope[] scopes = {
            ProfileScope.profile(),
            ProfileScope.postalCode()
        };
        AuthorizationManager.getToken(this, scopes, new Listener<AuthorizeResult, AuthError>() {

            @Override
            public void onSuccess(AuthorizeResult result) {
                if (result.getAccessToken() != null) {
                    /* The user is signed in */
                } else {
                    /* The user is not signed in */
                }
            }

            @Override
            public void onError(AuthError ae) {
                /* The user is not signed in */
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRequestContext.onResume();
    }

    private AuthorizeListener mAuthorizeListener = new AuthorizeListener() {
        /* Authorization was completed successfully. */
        @Override
        public void onSuccess(AuthorizeResult result) {
            /* Your app is now authorized for the requested scopes */
        }

        /* There was an error during the attempt to authorize the
        application. */
        @Override
        public void onError(AuthError ae) {
            /* Inform the user of the error */
        }

        /* Authorization was cancelled before it could be completed. */
        @Override
        public void onCancel(AuthCancellation cancellation) {
            /* Reset the UI to a ready-to-login state */
        }
    };

}
