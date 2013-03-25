package net.matmas.unknowncallerlookup.unused;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import net.matmas.unknowncallerlookup.App;

import android.os.AsyncTask;
import android.widget.Toast;

public class LookupTask extends AsyncTask<Void, Void, String> {
	
	private Exception exception = null;
	private SuccessCallback onSuccessCallback = null;
	private FailureCallback onFailureCallback = null;
	private String URL;
	
	public LookupTask(String URL, SuccessCallback onSuccessCallback, FailureCallback onFaiulureCallback) {
		this.URL = URL;
		this.onSuccessCallback = onSuccessCallback;
		this.onFailureCallback = onFaiulureCallback;
	}
	
	protected String doInBackground(Void... none) {
		URL url;
		HttpURLConnection urlConnection = null;
		String response = "";
		try {
			url = new URL(URL);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoInput(true);
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
    		response = Tools.convertStreamToString(in);
		} catch (ProtocolException e) {
			exception = e;
		} catch (MalformedURLException e) {
			exception = e;
		} catch (IOException e) {
			exception = e;
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return response;
	}
	
	private boolean isSilent() {
		return false; // override this if needed
	}
	
	protected void onPostExecute(String response) {
		if (exception != null) {
			if ( !isSilent()) {
				Toast.makeText(App.getContext(), exception.toString(), Toast.LENGTH_LONG).show();
			}
			onFailure();
			return;
		}
		onSuccess(response);
	}
	
	private void onFailure() {
		if (this.onFailureCallback != null) {
			this.onFailureCallback.onFailure();
		}
	}
	
	private void onSuccess(String response) {
		if (this.onSuccessCallback != null) {
			this.onSuccessCallback.onSuccess(response);
		}
	}
}
