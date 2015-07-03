package leclerc.zapper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewAnimator;

import leclerc.gridder.R;


public class LoginActivity extends HeaderView {

    private TextView txtUsername;
    private TextView txtPassword;
    private ImageButton btnConnect;
    private TextView txtNewUser;

    public LoginActivity() {
        super(ELayoutType.TitleOnly);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //setContentView(R.layout.activity_login);

        setContent(R.layout.activity_login);

        txtUsername = (TextView)findViewById(R.id.txtUsername);
        txtPassword = (TextView)findViewById(R.id.txtPassword);
        btnConnect = (ImageButton)findViewById(R.id.btnConnect);
        txtNewUser = (TextView)findViewById(R.id.txtNewUser);

        txtUsername.addTextChangedListener(new CredentialsUpdater());
        txtPassword.addTextChangedListener(new CredentialsUpdater());

        btnConnect.setOnClickListener(new ConnectListener(this, getHeader()));
        txtNewUser.setOnClickListener(new NewUserListener(this, getHeader()));

        updateConnectionValidity();

    }

    private boolean canConnect() {
        return true;//txtUsername.getText().length() >= 3 && txtPassword.getText().length() >= 3;
    }

    private void updateConnectionValidity() {
        btnConnect.setEnabled(canConnect());
    }

    private class CredentialsUpdater implements TextWatcher {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            updateConnectionValidity();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class ConnectListener implements View.OnClickListener {
        private Activity CurrentActivity;
        private FrameLayout CurrentHeader;

        public ConnectListener(Activity activity, FrameLayout header) {
            super();

            CurrentActivity = activity;
            CurrentHeader = header;
        }

        @Override
        public void onClick(View v) {
            /* TODO: Verify Credentials
               If success-> Switch to next view
               If error-> Indicate error to user
            */

            Intent intent = new Intent(CurrentActivity, ConversationsActivity.class);

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
                CurrentActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CurrentActivity, CurrentHeader, "header").toBundle());
            else
                CurrentActivity.startActivity(intent);
        }
    }

    private class NewUserListener implements View.OnClickListener {
        private Activity CurrentActivity;
        private FrameLayout CurrentHeader;

        public NewUserListener(Activity activity, FrameLayout header) {
            super();

            CurrentActivity = activity;
            CurrentHeader = header;
        }

        @Override
        public void onClick(View v) {
            // TODO: Switch to registration view

            Intent intent = new Intent(CurrentActivity, NewUserActivity.class);

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP)
                CurrentActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(CurrentActivity, CurrentHeader, "header").toBundle());
            else
                CurrentActivity.startActivity(intent);
        }
    }
}
