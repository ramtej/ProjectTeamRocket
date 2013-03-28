package edu.gatech.oad.rocket.findmythings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

/**
 * CS 2340 - FindMyStuff Android App
 *
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 *
 * @author TeamRocket
 */
public class ItemDetailActivity extends FragmentActivity {

	/**
	 * The class of Item displayed.
	 */
	private Type mType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);


		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle extraInfo = getIntent().getExtras();
		if (extraInfo != null && extraInfo.containsKey(Type.ID)) {
			int value = extraInfo.getInt(Type.ID);
			mType = EnumHelper.forInt(value, Type.class);
		}

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putAll(getIntent().getExtras());
			ItemDetailFragment fragment = new ItemDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this,
					new Intent(this, ItemListActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A read-only getter for the kinds of Item displayed in this view.
	 * @return An enumerated Type value.
	 */
	public Type getItemType() {
		return mType;
	}

	/**
	 * Method called when the location button is clicked
	 * Goes to MapsActivity
	 * @param LocationButton
	 */
	public void toMap (View LocationButton) {
		if(hasInternet()) {
			Intent next = new Intent(getApplicationContext(), MapsActivity.class);
			finish();
			startActivity(next);
		} else {
		AlertDialog.Builder noConnection = new AlertDialog.Builder(this);
			noConnection.setMessage("Error: no active internet connection.");
			noConnection.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
			    public void onClick(DialogInterface dialog, int id) {
                	//close	
                }
			});
			noConnection.show();
		}
	}
	
	/**
	 * Checks to see if the user has an active network connection 
	 * @return
	 */
	public boolean hasInternet() {
		boolean hasWifi = false;
		boolean hasMobile = false;
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if ( ni != null ) {
		    if (ni.getType() == ConnectivityManager.TYPE_WIFI)
		        if (ni.isConnectedOrConnecting())
		        	hasWifi = true;
		    if (ni.getType() == ConnectivityManager.TYPE_MOBILE)
		        if (ni.isConnectedOrConnecting())
		        	hasMobile = true;
		}
		if(hasWifi || hasMobile)
			return true;
		return false;
		
	}
}
