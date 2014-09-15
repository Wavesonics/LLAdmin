package com.darkrockstudios.apps.lladmin.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.darkrockstudios.apps.lladmin.R;
import com.darkrockstudios.apps.lladmin.TemporaryAuthToken;
import com.darkrockstudios.apps.lladmin.api.LLApiProvider;
import com.darkrockstudios.apps.lladmin.api.data.AuthResponse;
import com.darkrockstudios.apps.lladmin.api.data.AuthRequest;
import com.darkrockstudios.apps.lladmin.launches.LaunchListActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements RetrievalCallback<Credentials>
{
	private static final String TAG = LoginActivity.class.getSimpleName();

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	private Credentials m_savedCredentails;

	@InjectView(R.id.user)
	AutoCompleteTextView m_userView;

	@InjectView(R.id.password)
	EditText mPasswordView;

	@InjectView(R.id.login_progress)
	View mProgressView;

	@InjectView(R.id.login_form)
	View mLoginFormView;

	@Override
	protected void onCreate( final Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_login );
		ButterKnife.inject( this );

		mPasswordView.setOnEditorActionListener( new EditActionListener() );

		CredentailStorage.get( this, this );
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin()
	{
		if( mAuthTask != null )
		{
			return;
		}

		// Reset errors.
		m_userView.setError( null );
		mPasswordView.setError( null );

		// Store values at the time of the login attempt.
		String email = m_userView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if( !TextUtils.isEmpty( password ) && !isPasswordValid( password ) )
		{
			mPasswordView.setError( getString( R.string.error_invalid_password ) );
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if( TextUtils.isEmpty( email ) )
		{
			m_userView.setError( getString( R.string.error_field_required ) );
			focusView = m_userView;
			cancel = true;
		}
		else if( !isUserValid( email ) )
		{
			m_userView.setError( getString( R.string.error_invalid_user ) );
			focusView = m_userView;
			cancel = true;
		}

		if( cancel )
		{
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		}
		else
		{
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			showProgress( true );
			mAuthTask = new UserLoginTask( email, password );
			mAuthTask.execute( (Void) null );
		}
	}

	private boolean isUserValid( final String user )
	{
		return !TextUtils.isEmpty( user );
	}

	private boolean isPasswordValid( final String password )
	{
		return !TextUtils.isEmpty( password );
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	public void showProgress( final boolean show )
	{
		int shortAnimTime = getResources().getInteger( android.R.integer.config_shortAnimTime );

		mLoginFormView.setVisibility( show ? View.GONE : View.VISIBLE );
		mLoginFormView.animate().setDuration( shortAnimTime ).alpha(
				                                                           show ? 0 : 1 )
		              .setListener( new AnimatorListenerAdapter()
		              {
			              @Override
			              public void onAnimationEnd( final Animator animation )
			              {
				              mLoginFormView.setVisibility( show ? View.GONE : View.VISIBLE );
			              }
		              } );

		mProgressView.setVisibility( show ? View.VISIBLE : View.GONE );
		mProgressView.animate().setDuration( shortAnimTime ).alpha(
				                                                          show ? 1 : 0 )
		             .setListener( new AnimatorListenerAdapter()
		             {
			             @Override
			             public void onAnimationEnd( final Animator animation )
			             {
				             mProgressView.setVisibility( show ? View.VISIBLE : View.GONE );
			             }
		             } );
	}

	private void updateViews()
	{
		if( m_savedCredentails != null )
		{
			m_userView.setText( m_savedCredentails.userName );
			mPasswordView.setText( m_savedCredentails.password );
		}
	}

	@Override
	public void retrievedResults( final List<NoSQLEntity<Credentials>> noSQLEntities )
	{
		if( noSQLEntities != null && noSQLEntities.size() > 0 )
		{
			final NoSQLEntity<Credentials> entry = noSQLEntities.get( 0 );
			if( entry != null )
			{
				m_savedCredentails = entry.getData();
				updateViews();
			}
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
	{
		private final String m_user;
		private final String m_password;

		UserLoginTask( final String email, final String password )
		{
			m_user = email;
			m_password = password;
		}

		@Override
		protected Boolean doInBackground( final Void... params )
		{
			final AuthResponse authToken = LLApiProvider.get().auth( new AuthRequest( m_user, m_password ) );
			final boolean success = authToken != null && !TextUtils.isEmpty( authToken.token );

			if( success )
			{
				CredentailStorage.save( m_user, m_password, LoginActivity.this );

				TemporaryAuthToken.authToken = authToken.token;
				Log.d( TAG, "authToken " + authToken );
			}

			return success;
		}

		@Override
		protected void onPostExecute( final Boolean success )
		{
			mAuthTask = null;
			showProgress( false );

			if( success )
			{
				Intent intent = new Intent( LoginActivity.this, LaunchListActivity.class );
				startActivity( intent );
				finish();
			}
			else
			{
				mPasswordView.setError( getString( R.string.error_incorrect_password ) );
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled()
		{
			mAuthTask = null;
			showProgress( false );
		}
	}

	@OnClick(R.id.email_sign_in_button)
	public void onSignInClicked()
	{
		attemptLogin();
	}

	private class EditActionListener implements TextView.OnEditorActionListener
	{
		@Override
		public boolean onEditorAction( final TextView textView, final int id, final KeyEvent keyEvent )
		{
			final boolean success;

			if( id == R.id.login || id == EditorInfo.IME_NULL )
			{
				attemptLogin();
				success = true;
			}
			else
			{
				success = false;
			}

			return success;
		}
	}
}