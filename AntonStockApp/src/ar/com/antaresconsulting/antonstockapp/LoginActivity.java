package ar.com.antaresconsulting.antonstockapp;

import com.openerp.ConnectAsyncTask;
import com.openerp.OpenErpHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import ar.com.antaresconsulting.antonstockapp.popup.ServerPopupFragment;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity implements LoginActivityInterface {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private ServerPopupFragment popconf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setActivities();

		setContentView(R.layout.activity_login);
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.edtUser);
		mEmailView.setText(mEmail);
		

		
		mPasswordView = (EditText) findViewById(R.id.edtPassword);
	
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int id,
					KeyEvent keyEvent) {
				if (id == R.id.login_form || id == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		findViewById(R.id.login_form);

		findViewById(R.id.btnLogin).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
							attemptLogin();
					}
				});
		findViewById(R.id.btnCancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						doExit();
					}
				});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_setting),Context.MODE_PRIVATE);
		String mServerDBName = sharedPref.getString(getString(R.string.server_db),AntonConstants.DEFAULT_DB);
		String mServerHost = sharedPref.getString(getString(R.string.server_url),AntonConstants.DEFAULT_URL);
		Integer mServerPort = Integer.valueOf(sharedPref.getString(getString(R.string.server_port),AntonConstants.DEFAULT_PORT));

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} 

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			ConnectAsyncTask conAsT = new ConnectAsyncTask(this);
			conAsT.execute(mServerHost, mServerPort.toString(), mServerDBName, mEmail, mPassword);
		}
	}

	public void showPopup(MenuItem v) {
		popconf = new ServerPopupFragment();
		popconf.show(getFragmentManager(),"Server_Config");
	}

	/**
	 * Exit the app if user select yes.
	 */
	@Override
	public void onBackPressed() {

		doExit();
	}

	private void doExit() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				LoginActivity.this);

		alertDialog.setPositiveButton(getString(R.string.sYes), new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		alertDialog.setNegativeButton(getString(R.string.sNo), null);

		alertDialog.setMessage(getString(R.string.sCloseQ));
		alertDialog.setTitle(R.string.app_name);
		alertDialog.show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_setting),Context.MODE_PRIVATE);
		String user  = sharedPref.getString(getString(R.string.credential_user), "");
		String pass  = sharedPref.getString(getString(R.string.credential_pass),"");
		String mServerHost  = sharedPref.getString(getString(R.string.server_url),AntonConstants.DEFAULT_URL);
		String mServerPort  = sharedPref.getString(getString(R.string.server_port),AntonConstants.DEFAULT_PORT);
		String mServerDBName  = sharedPref.getString(getString(R.string.server_db),AntonConstants.DEFAULT_DB);
		if(!user.equalsIgnoreCase("") && !pass.equalsIgnoreCase("")){
			this.mEmail = user;
			this.mPassword = pass;
			ConnectAsyncTask conAsT = new ConnectAsyncTask(this);
			conAsT.execute(mServerHost, mServerPort.toString(), mServerDBName, user, pass);
		}		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_setting),Context.MODE_PRIVATE);
		String user  = sharedPref.getString(getString(R.string.credential_user), "");
		String pass  = sharedPref.getString(getString(R.string.credential_pass),"");
		String mServerHost  = sharedPref.getString(getString(R.string.server_url),AntonConstants.DEFAULT_URL);
		String mServerPort  = sharedPref.getString(getString(R.string.server_port),AntonConstants.DEFAULT_PORT);
		String mServerDBName  = sharedPref.getString(getString(R.string.server_db),AntonConstants.DEFAULT_DB);
		if(!user.equalsIgnoreCase("") && !pass.equalsIgnoreCase("")){
			this.mEmail = user;
			this.mPassword = pass;
			ConnectAsyncTask conAsT = new ConnectAsyncTask(this);
			conAsT.execute(mServerHost, mServerPort.toString(), mServerDBName, user, pass);
		}			
	}

	@Override
	public void connectionResolved(Boolean result) {
		if(result){
			SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_setting),Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(getString(R.string.credential_user), this.mEmail);
			editor.putString(getString(R.string.credential_pass), this.mPassword);
			editor.commit();

			Intent i = new Intent(this, OpenErpHolder.getInstance().getMainClass());
			startActivity(i);
			finish();
		}
		else{
			String failMsg = this.getString(R.string.sCheckSettings);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(this.getString(R.string.sFailTitle))
			.setMessage(failMsg)
			.setCancelable(false)
			.setNegativeButton("OK",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int id) {
					dialog.cancel();
					// viewSettings();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}

	@Override
	public void setActivities() {
		OpenErpHolder.getInstance().setMainClass(AntonLauncherActivity.class);
	}

	@Override
	public void dbList(Object[] dbs) {
		popconf.dbList(dbs);
		
	}
	public void searchDBs(View view) {
		popconf.searchDBs(view);
	}
}
