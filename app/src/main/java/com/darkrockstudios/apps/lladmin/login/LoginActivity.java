package com.darkrockstudios.apps.lladmin.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.colintmiller.simplenosql.NoSQLEntity;
import com.colintmiller.simplenosql.RetrievalCallback;
import com.darkrockstudios.apps.lladmin.R;
import com.darkrockstudios.apps.lladmin.TemporaryAuthToken;
import com.darkrockstudios.apps.lladmin.api.LLApiProvider;
import com.darkrockstudios.apps.lladmin.api.data.AuthRequest;
import com.darkrockstudios.apps.lladmin.api.data.AuthResponse;
import com.darkrockstudios.apps.lladmin.launches.LaunchListActivity;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements RetrievalCallback<Credentials>
{
	private static final String TAG = LoginActivity.class.getSimpleName();

	private Credentials m_savedCredentails;

	@InjectView(R.id.user)
	AutoCompleteTextView m_userView;

	@InjectView(R.id.password)
	EditText mPasswordView;

	@InjectView(R.id.login_progress)
	View mProgressView;

	@InjectView(R.id.login_form)
	View mLoginFormView;

	@InjectView( R.id.LOGIN_remember_credentials )
	CheckBox m_rememberCredentialsView;

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

			final AuthRequest authRequest = new AuthRequest( email, password );
			final Observable<AuthResponse> authObservable = LLApiProvider.get().auth( authRequest );

			authObservable.subscribeOn( Schedulers.newThread() )
			              .observeOn( AndroidSchedulers.mainThread() )
			              .subscribe( new LoginObserver( email, password, m_rememberCredentialsView.isChecked() ) );
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
			m_userView.setText( m_savedCredentails.userName() );
			mPasswordView.setText( m_savedCredentails.password() );
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
				m_rememberCredentialsView.setChecked( true );

				updateViews();
			}
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

	private class LoginObserver implements Observer<AuthResponse>
	{
		private final String m_user;
		private final String m_password;
		private final boolean m_saveCredentials;

		public LoginObserver( final String user, final String password, final boolean saveCredentials )
		{

			m_user = user;
			m_password = password;
			m_saveCredentials = saveCredentials;
		}

		@Override
		public void onCompleted()
		{
			showProgress( false );
		}

		@Override
		public void onError( final Throwable e )
		{
			showProgress( false );
		}

		@Override
		public void onNext( final AuthResponse authResponse )
		{
			final boolean success = authResponse != null
			                        && authResponse.token != null
			                        && !TextUtils.isEmpty( authResponse.token );
			showProgress( false );

			if( success )
			{
				Log.d( TAG, "authToken " + authResponse.token );
				if( m_saveCredentials )
				{
					CredentailStorage.save( m_user, m_password, LoginActivity.this );
				}

				TemporaryAuthToken.authToken = authResponse.token;

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
	}
}