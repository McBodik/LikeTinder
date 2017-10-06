package com.mcbodik.liketinder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mcbodik.liketinder.utils.AnimationHelper;
import com.mcbodik.liketinder.utils.ImageProvider;
import com.mcbodik.liketinder.utils.SwipeListener;

public class MainActivity extends AppCompatActivity implements ISwipeCallback {

	private ImageView mImageView;
	private ProgressBar mProgressBar;
	private ImageProvider mImageProvider;

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
	}

	@Override
	protected void onStart() {
		super.onStart();
		setImage();
	}

	@Override
	public void onLeftSwipe(float x, float y) {
		animate(x, y);
	}

	@Override
	public void onRightSwipe(float x, float y) {
		animate(x, y);
	}

	@Override
	public void onTopSwipe(float x, float y) {
		animate(x, y);
	}

	@Override
	public void onDownSwipe(float x, float y) {
		animate(x, y);
	}

	private void setImage() {
		mProgressBar.setVisibility(View.VISIBLE);
		mImageView.setImageBitmap(null);
		mImageProvider.getNext(new ImageProvider.ImageObtainCallback() {
			@Override
			public void onObtain(Bitmap image) {
				mProgressBar.setVisibility(View.GONE);
				mImageView.setImageBitmap(image);
			}
		});
	}

	private void animate(float x, float y) {
		AnimationHelper animationHelper = new AnimationHelper();
		animationHelper.animateView(mImageView, x, y, 0, -1, new AnimationHelper.AnimationFinish() {
			@Override
			public void onFinish() {
				setImage();
			}
		});
	}
}
