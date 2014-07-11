package com.racecondition.ribbitsdm.app.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.racecondition.ribbitsdm.app.R;


public class ForgotPasswordActivity extends Activity {

    protected EditText mResetEmail;
    protected Button mSubmitEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_forgot_password);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        mResetEmail = (EditText)findViewById(R.id.resetEmailField);
        mSubmitEmailButton = (Button)findViewById(R.id.resetPasswordButton);

        mSubmitEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resetEmail = mResetEmail.getText().toString();

                resetEmail = resetEmail.trim();

                if (TextUtils.isEmpty(resetEmail)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    builder.setMessage(R.string.reset_password_error_message)
                            .setTitle(R.string.reset_password_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    // reset the password
                    setProgressBarIndeterminateVisibility(true);
                    ParseUser.requestPasswordResetInBackground(resetEmail, new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                setProgressBarIndeterminateVisibility(false);
                                Toast successToast = Toast.makeText(ForgotPasswordActivity.this, "An email was successfully sent with reset instructions",
                                        Toast.LENGTH_LONG);
                                successToast.show();

                                // Take them back to the Login Screen
                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else {
                                mResetEmail.setText("");
                                AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.reset_password_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                setProgressBarIndeterminateVisibility(false);
                            }
                        }
                    });

                }
            }
        });

    }

}
