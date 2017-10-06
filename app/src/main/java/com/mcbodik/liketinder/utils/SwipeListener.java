package com.mcbodik.liketinder.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.mcbodik.liketinder.ISwipeCallback;

public class SwipeListener {
	private GestureDetector gestureDetector;
	private Context mContext;

	public SwipeListener(Context context) {
		mContext = context;
		gestureDetector = new GestureDetector(mContext, new GestureListener());
	}

	public boolean handleEvent(MotionEvent motionEvent) {
		return gestureDetector.onTouchEvent(motionEvent);
	}

	private class GestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final int SWIPE_MIN_DISTANCE = 120;
		private static final int SWIPE_THRESHOLD_VELOCITY = 200;

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			ISwipeCallback callback = null;
			if (mContext instanceof ISwipeCallback) {
				callback = (ISwipeCallback) mContext; 
			}
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (callback != null) {
					callback.onLeftSwipe(velocityX, velocityY);
				}
				return false; //<-
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				if (callback != null) {
					callback.onRightSwipe(velocityX, velocityY);
				}
				return false; //->
			}
			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				if (callback != null) {
					callback.onTopSwipe(velocityX, velocityY);
				}
				return false; //up
			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				if (callback != null) {
					callback.onDownSwipe(velocityX, velocityY);
				}
				return false; //down
			}
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return true;
		}
	}
}
