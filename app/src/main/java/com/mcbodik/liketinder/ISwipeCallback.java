package com.mcbodik.liketinder;

public interface ISwipeCallback {

	void onLeftSwipe(float x ,float y);
	void onRightSwipe(float x ,float y);
	void onTopSwipe(float x ,float y);
	void onDownSwipe(float x ,float y);
}
