package com.mcbodik.liketinder.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mcbodik.liketinder.R;
import com.mcbodik.liketinder.loader.ImageSetLoader;
import com.mcbodik.liketinder.loader.model.ImageModel;
import com.mcbodik.liketinder.loader.model.ImageSetCallbackModel;

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
	private Bitmap mCachedImage;

	private Context mContext;
	private ImageObtainCallback mCallback;

	private boolean mImageSetLoaded = false;
	private int mLoadedImageIndex = -1;

	private long photoSetId = 72157686914148170L;
	private String apiKey = "9af8a6a2c40dfc469d2bbad44bffeb2c";
	private String userId = "152745799@N02";

	public int ERROR_NO_ERRORS = 0;
	public int ERROR_NO_IMAGES = 1;
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
		loadImageSet();
	}

	public boolean getNext(ImageObtainCallback callback) {
		if (!mImageSetLoaded) {
			mCallback = callback;
		} else {
			if (mImages.size() == 0) {
				mErrorCode = ERROR_NO_IMAGES;
				return false;
			}
		}
		if (mMainImage != null) {
			Bitmap result = mMainImage;
			mMainImage = mCachedImage;
			if (!loadNext(true)) {
				loadFirst(true);
			}
			callback.onObtain(result);
		} else {
			mCallback = callback;
		}
		return true;
	}

	public int getError() {
		return mErrorCode;
	}

	private void loadImageSet() {
		ImageSetLoader imageSetLoader = retrofit.create(ImageSetLoader.class);
		imageSetLoader.getImageSet(apiKey, String.valueOf(photoSetId), userId).enqueue(new Callback<ImageSetCallbackModel>() {
			@Override
			public void onResponse(Call<ImageSetCallbackModel> call, Response<ImageSetCallbackModel> response) {
				synchronized (mImages) {
					mImages.clear();
					ImageSetCallbackModel callbackModel = response.body();
					mImages.addAll(callbackModel.getPhotoset().getPhoto());
					Collections.sort(mImages, new Comparator<ImageModel>() {
						@Override
						public int compare(ImageModel imageModel, ImageModel t1) {
							return -imageModel.getDateupload().compareTo(t1.getDateupload());
						}
					});
					mImageSetLoaded = true;
					loadFirst(false);
					loadNext(true);
				}
			}

			@Override
			public void onFailure(Call<ImageSetCallbackModel> call, Throwable t) {
				Toast.makeText(mContext, mContext.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private boolean loadFirst(boolean loadToCache) {
		mLoadedImageIndex = 0;
		if (mImages.size() > 0) {
			new ImageLoader(mImages.get(mLoadedImageIndex), loadToCache).execute();
			return true;
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
		void onObtain(Bitmap image);
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
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected Bitmap doInBackground(Void... voids) {
			try {
				URL url = new URL(mImageURL);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap result = BitmapFactory.decodeStream(input);
				return result;
			} catch (IOException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap == null) {
				Toast.makeText(mContext, mContext.getString(R.string.error_message), Toast.LENGTH_SHORT).show();
			} else {
				if (!mCache) {
					mMainImage = bitmap;
					if (mCallback != null) {
						mCallback.onObtain(bitmap);
						mMainImage = null;
						loadNext(false);
						mCallback = null;
					}
				} else {
					mCachedImage = bitmap;
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
