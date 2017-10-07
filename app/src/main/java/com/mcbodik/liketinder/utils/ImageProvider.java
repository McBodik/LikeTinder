package com.mcbodik.liketinder.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mcbodik.liketinder.R;
import com.mcbodik.liketinder.loader.ImageSetLoader;
import com.mcbodik.liketinder.loader.model.ImageModel;
import com.mcbodik.liketinder.loader.model.CallbackModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageProvider {

	private ArrayList<ImageModel> mImages;
	private Retrofit retrofit;
	private Bitmap mMainImage;
	private String mMainImageId;
	private Bitmap mCachedImage;
	private String mCachedImageId;

	private Context mContext;
	private ImageObtainCallback mCallback;

	private int mLoadedImageIndex = -1;

	private long photoSetId = 72157686914148170L;
	private String apiKey = "9af8a6a2c40dfc469d2bbad44bffeb2c";
	private String userId = "152745799@N02";

	public int ERROR_NO_ERRORS = 0;
	public int ERROR_NO_IMAGES = 1;
	public int ERROR_NO_IMAGE_YET = 2;
	private int mErrorCode = ERROR_NO_ERRORS;

	//https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
	private static final String IMAGE_URL = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

	public ImageProvider(Context context) {
		mContext = context;
		retrofit = new Retrofit.Builder()
				.baseUrl("https://api.flickr.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		mImages = new ArrayList<>();
		loadImageSet(false);
	}

	public boolean getNext(ImageObtainCallback callback) {
		if (mCallback != null) {
			mErrorCode = ERROR_NO_IMAGE_YET;
			return false;
		}
		mCallback = callback;
		if (mImages.size() == 0) {
			mErrorCode = ERROR_NO_IMAGES;
			return false;
		}
		if (mMainImage != null) {
			Bitmap bitmap = mMainImage;
			String id = mMainImageId;
			mMainImage = mCachedImage;
			mMainImageId = mCachedImageId;
			mCachedImage = null;
			mCachedImageId = null;
			mCallback = null;
			if (!loadNext(true)) {
				loadImageSet(true);
			}
			callback.onObtain(id, bitmap);
		}
		return true;
	}

	public int getError() {
		return mErrorCode;
	}

	private void loadImageSet(final boolean reload) {
		ImageSetLoader imageSetLoader = retrofit.create(ImageSetLoader.class);
		imageSetLoader.getImageSet(apiKey, String.valueOf(photoSetId), userId).enqueue(new Callback<CallbackModel>() {
			@Override
			public void onResponse(Call<CallbackModel> call, Response<CallbackModel> response) {
				synchronized (mImages) {
					mImages.clear();
					CallbackModel callbackModel = response.body();
					mImages.addAll(callbackModel.getPhotoset().getPhoto());
					Collections.sort(mImages, new Comparator<ImageModel>() {
						@Override
						public int compare(ImageModel imageModel, ImageModel t1) {
							return -imageModel.getDateupload().compareTo(t1.getDateupload());
						}
					});
					loadFirst(reload);
				}
			}

			@Override
			public void onFailure(Call<CallbackModel> call, Throwable t) {
				Toast.makeText(mContext, mContext.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private boolean loadFirst(boolean loadToCache) {
		mLoadedImageIndex = 0;
		if (mImages.size() > 0) {
			new ImageLoader(mImages.get(mLoadedImageIndex), loadToCache).execute();
			return true;
		} else {
			if (mCallback != null) {
				mCallback.onObtain(null, null);
				Toast.makeText(mContext, mContext.getString(R.string.error_message_no_images), Toast.LENGTH_SHORT).show();
			}
		}
		return false;
	}

	private boolean loadNext(boolean loadToCache) {
		mLoadedImageIndex++;
		if (mLoadedImageIndex < mImages.size()) {
			new ImageLoader(mImages.get(mLoadedImageIndex), loadToCache).execute();
			return true;
		} else {
			return false;
		}

	}

	public interface ImageObtainCallback {
		void onObtain(String id, Bitmap image);
	}

	private class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
		private ImageModel mImageModel;
		private String mImageURL;
		private boolean mCache;

		public ImageLoader(ImageModel model, boolean cache) {
			mImageModel = model;
			mImageURL = prepareUrl();
			mCache = cache;
		}

		@Override
		protected Bitmap doInBackground(Void... voids) {
			try {
				URL url = new URL(mImageURL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				InputStream input = connection.getInputStream();
				return BitmapFactory.decodeStream(input);
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap == null) {
				Toast.makeText(mContext, mContext.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
			} else {
				if (!mCache || mMainImage == null) {
					mMainImage = bitmap;
					mMainImageId = mImageModel.getId();
					if (mCallback != null) {
						mCallback.onObtain(mMainImageId, bitmap);
						mMainImage = null;
						mMainImageId = null;
						mCallback = null;
						loadNext(false);
					} else {
						loadNext(true);
					}
				} else {
					mCachedImage = bitmap;
					mCachedImageId = mImageModel.getId();
				}
			}
		}

		private String prepareUrl() {
			return String.format(IMAGE_URL,
					mImageModel.getFarm().toString(),
					mImageModel.getServer(),
					mImageModel.getId(),
					mImageModel.getSecret());
		}
	}
}
