package com.mcbodik.liketinder.loader;

import com.mcbodik.liketinder.loader.model.ImageSetCallbackModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageSetLoader {

	@GET("services/rest/?method=flickr.photosets.getPhotos&extras=date_upload&format=json&nojsoncallback=1")
	Call<ImageSetCallbackModel> getImageSet(@Query("api_key") String apiKey, @Query("photoset_id") String photoSetId, @Query("user_id") String userId);
}
