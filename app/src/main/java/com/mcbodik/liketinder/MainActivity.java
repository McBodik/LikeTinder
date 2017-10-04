package com.mcbodik.liketinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mcbodik.liketinder.loader.ImageSetLoader;
import com.mcbodik.liketinder.loader.model.ImageSetCallbackModel;
import com.mcbodik.liketinder.loader.model.ImageModel;

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

public class MainActivity extends AppCompatActivity {

	private Retrofit retrofit;
	private long photoSetId = 72157686914148170L;
	private String apiKey = "9af8a6a2c40dfc469d2bbad44bffeb2c";
	private String userId = "152745799@N02";

	//https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
	private static final String IMAGE_URL = "https://farm%s.staticflickr.com/%s/%s_%s.jpg";

	private ArrayList<ImageModel> images;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		images = new ArrayList<>();
		retrofit = new Retrofit.Builder()
				.baseUrl("https://api.flickr.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		ImageSetLoader imageSetLoader = retrofit.create(ImageSetLoader.class);
		imageSetLoader.getImageSet(apiKey, String.valueOf(photoSetId), userId).enqueue(new Callback<ImageSetCallbackModel>() {
			@Override
			public void onResponse(Call<ImageSetCallbackModel> call, Response<ImageSetCallbackModel> response) {
				synchronized (images) {
					images.clear();
					ImageSetCallbackModel callbackModel = response.body();
					images.addAll(callbackModel.getPhotoset().getPhoto());
					Collections.sort(images, new Comparator<ImageModel>() {
						@Override
						public int compare(ImageModel imageModel, ImageModel t1) {
							return -imageModel.getDateupload().compareTo(t1.getDateupload());
						}
					});
					new ImageLoader(images.get(0)).execute();
				}
			}

			@Override
			public void onFailure(Call<ImageSetCallbackModel> call, Throwable t) {
				Toast.makeText(MainActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private class ImageLoader extends AsyncTask<Void, Void, Bitmap> {
		private ImageModel mImageModel;
		private String mImageURL;

		public ImageLoader(ImageModel model) {
			super();
			mImageModel = model;
			mImageURL = prepareUrl();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
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
				Toast.makeText(MainActivity.this, getString(R.string.error_message), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(bitmap);
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
