package com.mcbodik.liketinder.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.View;

public class AnimationHelper {

	private boolean xDone = false;
	private boolean yDone = false;
	private AnimationFinish mCallback;

	public void animateView(View view, float x, float y, float speed, long time, AnimationFinish callback) {
		animateX(view, x, speed, time);
		animateY(view, y, speed, time);
		mCallback = callback;
	}

	public static float[] normalization(float x, float y) {
		float[] result = new float[2];
		float distance = (float) Math.sqrt(x * x + y * y);
		result[0] = x / distance;
		result[1] = y / distance;
		return result;
	}

	private void animateX(final View view, float x, float speed, long time) {
		float velocity = speed > 0 ? x * speed : x;
		ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(view, View.X, velocity);
		objectAnimatorX.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(final Animator animation) {
				animation.removeListener(this);
				view.post(new Runnable() {
					@Override
					public void run() {
						animation.setDuration(0);
						((ObjectAnimator) animation).reverse();
						xDone = true;
						finish();
					}
				});

			}
		});
		if (time >= 0) {
			objectAnimatorX.setDuration(time);
		}
		objectAnimatorX.start();
	}

	private void animateY(final View view, float y, float speed, long time) {
		float velocity = speed > 0 ? y * speed : y;
		ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(view, View.Y, velocity);
		objectAnimatorY.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(final Animator animation) {
				animation.removeListener(this);
				view.post(new Runnable() {
					@Override
					public void run() {
						animation.setDuration(0);
						((ObjectAnimator) animation).reverse();
						yDone = true;
						finish();
					}
				});
			}
		});
		if (time >= 0) {
			objectAnimatorY.setDuration(time);
		}
		objectAnimatorY.start();
	}

	private void finish() {
		if (xDone && yDone) {
			mCallback.onFinish();
		}
	}

	public interface AnimationFinish {
		void onFinish();
	}
}
