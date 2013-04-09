package edu.gatech.oad.rocket.findmythings;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import edu.gatech.oad.rocket.findmythings.control.*;
import edu.gatech.oad.rocket.findmythings.model.Category;
import edu.gatech.oad.rocket.findmythings.model.Item;
import edu.gatech.oad.rocket.findmythings.model.Type;
import edu.gatech.oad.rocket.findmythings.util.*;

/**
 * CS 2340 - FindMyStuff Android App
 * activity that deals with submitting a new item
 *
 * @author TeamRocket
 * */
public class SubmitActivity extends Activity {

	//UI references
	private EditText description;
	private EditText location;
	private EditText reward;
	private EditText iName;

	private View focusView;

	//Hold strings from the UI
	private String desc, loc, name;
	private int rward;

	/**
	 * Data source we submit to.
	 */
	private Controller control = Controller.shared();

	/**
	 * The list to submit this item to.
	 */
	private Type mType = Type.LOST;

	/**
	 * Category for this item, helper for {@link SubmitFragment}.
	 */
	private Category mCategory = Category.MISC;

	/**
	 * creates new window with correct layout
	 * @param Bundle savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit);

		//References the layout in activity_submit
		iName = (EditText) findViewById(R.id.search_name);
		description = (EditText) findViewById(R.id.description);
		location = (EditText) findViewById(R.id.locationtext);
		reward = (EditText) findViewById(R.id.rewardtext);

		Bundle extraInfo = getIntent().getExtras();
		if (extraInfo != null && extraInfo.containsKey(Type.ID)) {
			int value = extraInfo.getInt(Type.ID);
			mType = EnumHelper.forInt(value, Type.class);
		}

		// Hide the Up button in the action bar.
		setupActionBar();

		setTitle("SubmitActivity an Item");

		SubmitFragment frag = (SubmitFragment) getFragmentManager().findFragmentById(R.id.submit_fragment);
		frag.syncTypePref(mType);
		frag.syncCatPref(mCategory);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	/**
	 * creates the options menu 
	 * @param Menu menu
	 * @return boolean true when done
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submit, menu);
		return true;
	}

	/**
	 * deals with action to do once a key is pressed down
	 * @param int keyCode - key pressed
	 * @param KeyEvent event - event to do in case of pressed
	 * @return boolean 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		//Tells Activity what to do when back key is pressed
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    	super.onBackPressed();
			return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Checks for errors
	 * @return boolean false(no errors) or true(there are errors)
	 */
	public boolean checkforErrors() {
		boolean cancel = false;
		focusView = null;

		desc = description.getText().toString();
		name = iName.getText().toString();

		//Check to see if name is empty
		if (TextUtils.isEmpty(name.trim())) {
			iName.setError(getString(R.string.error_field_required));
			focusView = iName;
			cancel = true;
		}

		//Check to see if description is empty
		if (TextUtils.isEmpty(desc.trim())) {
			description.setError(getString(R.string.error_field_required));
			focusView = description;
			cancel = true;
		}
		return cancel;

	}
	
	/**
	 * deals with action when an options button is selected
	 * @param MenuItem item
	 * @return boolean  
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

			switch (item.getItemId()) {
			case R.id.submit_ok:
				if (checkforErrors()) { //There was an error
					focusView.requestFocus(); //Show error
					return false;
				}

				else {
				loc = location.getText().toString();
				rward = reward.getText().length() == 0 ? 0:Integer.parseInt(reward.getText().toString());

				Item temp = new Item(name,rward);
				temp.setCategory(mCategory);
				temp.setType(mType);
				temp.setDescription(desc);
				temp.setLoc(loc);

				control.addItem(temp);
				//ItemListFragment.update(control.getItem(temp.getType()));

				return toItemList();
				}
			case R.id.submit_cancel:
				finish();
				return true;
			case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//

			return true;
			}


			return super.onOptionsItemSelected(item);

	}

	/**
	 * Sets the item type for this submission, i.e., the list
	 * the item will be put on.
	 * @param type An Item Type enumerated value.
	 */
	public void setItemType(Type type) {
		mType = type;
	}

	/**
	 * Returns the list the item will be put on.
	 * @return An Item Type enumerated value.
	 */
	public Type getItemType() {
		return mType;
	}

	/**
	 * Sets the item category for this submission, used for filtering
	 * @param type An Item Category enumerated value.
	 */
	public void setItemCategory(Category type) {
		mCategory = type;
	}

	/**
	 * Returns the category for the item.
	 * @return An Item Category enumerated value.
	 */
	public Category getItemCategory() {
		return mCategory;
	}

	/**
	 * Returns to Item List activity. Animation and ID helper.
	 * @return boolean
	 */
	public boolean toItemList() {
		Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
		goToNextActivity.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
		goToNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		goToNextActivity.putExtra(Type.ID, mType.ordinal());
		finish();
		startActivity(goToNextActivity);
		overridePendingTransition(R.anim.hold, R.anim.slide_down_modal);
		return true;
	}
	
	/**
	 * Called to pop the login window from the navigation stack
	 */
	@Override 
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.slide_down_modal);
    }

}
