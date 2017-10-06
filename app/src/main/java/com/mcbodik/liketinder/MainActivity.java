package com.mcbodik.liketinder;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mcbodik.liketinder.utils.AnimationHelper;
import com.mcbodik.liketinder.utils.ImageProvider;
import com.mcbodik.liketinder.utils.SwipeListener;

public class MainActivity extends AppCompatActivity implements ISwipeCallback {

	private ImageView mImageView;
	private TextView mLikesField;
	private TextView mDislikesField;
	private ProgressBar mProgressBar;
	private ImageProvider mImageProvider;
	private SharedPreferences mSharedPreferences;
	private String mImageId;

	private static final String LIKE = "_LIKE";
	private static final String DISLIKE = "_DISLIKE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mImageProvider = new ImageProvider(this);

		mProgressBar = findViewById(R.id.progress);
		mProgressBar.setIndeterminate(true);
		DrawableCompat.setTint(mProgressBar.getIndeterminateDrawable(), ContextCompat.getColor(this, android.R.color.holo_blue_dark));

		mImageView = findViewById(R.id.main_image);
		mImageView.setOnTouchListener(new View.OnTouchListener() {
			SwipeListener swipeListener = new SwipeListener(MainActivity.this);

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return swipeListener.handleEvent(event);
			}
		});
		mLikesField = findViewById(R.id.like);
		mDislikesField = findViewById(R.id.dislike);
		mSharedPreferences = getPreferences(MODE_PRIVATE);
		updateImageData();
	}

	@Override
	public void onLeftSwipe(float x, float y) {
		animate(x, y);
		updateLikes(false);
	}

	@Override
	public void onRightSwipe(float x, float y) {
		animate(x, y);
		updateLikes(true);
	}

	private void updateImageData() {
		mProgressBar.setVisibility(View.VISIBLE);
		mImageView.setImageBitmap(null);
		mLikesField.setText("");
		mDislikesField.setText("");
		mImageId = null;
		mImageProvider.getNext(new ImageProvider.ImageObtainCallback() {
			@Override
			public void onObtain(String id, Bitmap image) {
				if (id == null && image == null) {
					mProgressBar.setVisibility(View.GONE);
					return;
				}
				mImageId = id;
				mProgressBar.setVisibility(View.GONE);
				mImageView.setImageBitmap(image);
				mLikesField.setText(String.valueOf(mSharedPreferences.getInt(mImageId + LIKE, 0)));
				mDislikesField.setText(String.valueOf(mSharedPreferences.getInt(mImageId + DISLIKE, 0)));
			}
		});
	}

	private void updateLikes(boolean like) {
		int count;
		String id;
		if (like) {
			id = mImageId + LIKE;
			count = mSharedPreferences.getInt(id, 0);
			mSharedPreferences.edit().putInt(id, count + 1).commit();
		} else {
			id = mImageId + DISLIKE;
			count = mSharedPreferences.getInt(id, 0);
			mSharedPreferences.edit().putInt(id, count + 1).commit();
		}
	}

	private void animate(float x, float y) {
		AnimationHelper animationHelper = new AnimationHelper();
		animationHelper.animateView(mImageView, x, y, 0, -1, new AnimationHelper.AnimationFinish() {
			@Override
			public void onFinish() {
				updateImageData();
			}
		});
	}
}
