package edu.gatech.oad.rocket.findmythings.NonActivity;

import java.util.ArrayList;

/**
 * CS 2340 - FindMyStuff Android App
 * This class creates a new User object
 *
 * @author TeamRocket
 * */
public class User extends Member {

	/**
	 * Whether or not a user has exceeded their login attempts.
	 */
	private boolean locked = false;

	/**
	 * The number of login attempts.
	 */
	private int attempts = 0;
	
	/**
	 * Store all items the user has submitted
	 */
	private ArrayList<Item> currSubmits; //TODO: this

	public User(String user, String pass, String phone) {
		super(user, pass, phone);
		
	}

	public User(String user, String pass) {
		super(user, pass);
		
	}

	@Override
	public boolean locked(){
		return locked;
	}

	/**
	 * locks or unlocks user account
	 * @param b
	 */
	public void setLock(boolean b) {
		locked = b;
	}

	/**
	 * Increments the number of unsuccessful attempts a User has made to log in
	 */
	public void incrment() {
		attempts++;

		if(attempts==3)
			setLock(true);
	}

	/**
	 * sets the number of attempts that user has used to log in
	 * @param i
	 */
	public void setAttempts(int i) {
		attempts=i;
		if(attempts==3)
			setLock(true);
	}

	/**
	 * returns the number of wrong attempts at login by user
	 * @return int attempts
	 */
	public int getAttempts() {
		return attempts;
	}

	/**
	 * returns if the member (user) is a admin
	 * @return False
	 */
	@Override
	public boolean isAdmin() {
		return false;
	}

}
