package com.zzoru.pathview;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class PathViewActivity extends Activity {



	private static final int ERASE_MENU_ID = Menu.FIRST;

	private PathDeletableView mPathView;
	private boolean isPathViewDeletable = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mPathView = (PathDeletableView) findViewById(R.id.deletablePathView1);


	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);


		menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');


		/****   Is this the mechanism to extend with filter effects?
         Intent intent = new Intent(null, getIntent().getData());
         intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
         menu.addIntentOptions(
                               Menu.ALTERNATIVE, 0,
                               new ComponentName(this, NotesList.class),
                               null, intent, 0, null);
		 *****/
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ERASE_MENU_ID:
			isPathViewDeletable = !isPathViewDeletable;
			mPathView.setDeleteMode(isPathViewDeletable);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}