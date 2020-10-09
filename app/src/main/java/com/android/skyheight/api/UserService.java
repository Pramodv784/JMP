package com.android.skyheight.api;



import android.content.Context;

import com.android.skyheight.model.ActiveUserModel;
import com.android.skyheight.model.AddressModel;
import com.android.skyheight.model.BookingModel;
import com.android.skyheight.model.DeleteModel;
import com.android.skyheight.model.ImageModel;
import com.android.skyheight.model.LoginModel;
import com.android.skyheight.model.PlotExpenseModel;
import com.android.skyheight.model.PlotListModel;
import com.android.skyheight.model.PlotModel;
import com.android.skyheight.model.PlotSummaryModel;
import com.android.skyheight.model.PlotUpdateModel;
import com.android.skyheight.model.PlotsModel;
import com.android.skyheight.model.SiteExpenseModel;
import com.android.skyheight.model.SiteListModel;
import com.android.skyheight.model.SiteModel;
import com.android.skyheight.model.SiteSummaryModel;
import com.android.skyheight.model.UserDetail;
import com.android.skyheight.model.UserList;
import com.android.skyheight.utils.Prefrence;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface UserService {


    String BASE_URL = "https://testappecom.herokuapp.com";
    @POST("user/register/")
    Call<UserDetail> createUser(@Body UserDetail userDetail);
    @FormUrlEncoded
    @POST("user/login/")
    Call<LoginModel> user_login(@Field("mobile_number") String mobile_number,
                                @Field("password") String password);
    @GET("user")
    Call<ArrayList<UserList>> allusers(@Header("Authorization") String token);
    @DELETE("user/{id}")
    Call<DeleteModel> delete(@Header("Authorization") String token,@Path("id") String id);

    @PUT("user/{id}")
    Call<UserDetail> update(@Header("Authorization") String token,@Path("id") String id,@Body UserDetail userDetail);
    @POST("site/address")
    Call<SiteModel> addaddress(@Header("Authorization") String token, @Body AddressModel addressModel);
    @Multipart
    @POST("site/")
    Call<SiteModel> addsite(@Header("Authorization") String token,
                               @Part("name") RequestBody name,
                               @Part("site_location") RequestBody site_location,
                               @Part("area") RequestBody site_area,
                               @Part("price") RequestBody site_price,
                               @Part("description") RequestBody site_description,
                               @Part("owner") RequestBody owner,
                               @Part MultipartBody.Part image,
                               @Part MultipartBody.Part file);

    @Multipart
    @PUT("site/{id}")
    Call<SiteModel> updatesite(@Header("Authorization") String token,
                           @Part("name") RequestBody name,
                           @Part("site_location") RequestBody site_location,
                           @Part("area") RequestBody site_area,
                           @Part("price") RequestBody site_price,
                           @Part("description") RequestBody site_description,
                               @Path("id") String  site_id
                          );
    @GET("site/list")
    Call<ArrayList<SiteListModel>> sitelist(@Header("Authorization") String token);
    @GET("site/list")
    Call<ArrayList<SiteListModel>> usersitelist();
    @GET("user")
    Call<ArrayList<UserList>> allbroker(@Header("Authorization") String token,
                                        @Query("type") String type);
    @GET("site/plot/list")
    Call<ArrayList<PlotListModel>> getplot(@Header("Authorization") String token,@Query("site") String site);
    @POST("site/{id}/plots/{plots_count}")
    Call<PlotsModel> plots(@Header("Authorization") String token,
                           @Path("id") String id,@Path("plots_count") String plots_count);
    @DELETE("site/{id}")
    Call<DeleteModel> deletesite(@Header("Authorization") String token,@Path("id") String delete);
    @GET("site/plot/list")
    Call<ArrayList<PlotListModel>> siteplot(@Header("Authorization") String token
            ,@Query("site") String site);
    @GET("site/plot/list")
    Call<ArrayList<PlotListModel>> usersiteplot(
            @Query("site") String site);
  @POST("site/plot")
    Call<PlotModel> addplot(@Header("Authorization") String token
          ,@Body PlotModel plotModel);

  @PUT("site/plot/{id}")
    Call<PlotUpdateModel> update_plot(@Header("Authorization") String token
          , @Path("id") String id , @Body PlotUpdateModel plotUpdateModel);

@DELETE("site/plot/{id}")
Call<PlotUpdateModel> deleteplot(@Header("Authorization") String token,@Path("id") String id);

  @GET("user/activate-request")
  Call<ArrayList<UserList>> activateRequest(@Header("Authorization") String token);
  @GET("user/activate-request")
  Call<ArrayList<UserList>> deactive(@Header("Authorization") String token,@Query("deactivate") String deactivate);


  @POST("user/activate/{id}")
    Call<ActiveUserModel> active(@Header("Authorization") String token,@Path("id") String id);

  @POST("user/deactivate/{id}")
  Call<ActiveUserModel> deactivate(@Header("Authorization") String token,@Path("id") String id);

  @GET("user")
    Call<ArrayList<UserList>> allowner(@Header("Authorization")
                                               String token,@Query("type") String owner);

  @GET("user")
    Call<ArrayList<UserList>> allpartner(@Header("Authorization")
                                                 String token,@Query("type") String partner);
@POST("user/{user_id}/site/{site_id}")
    Call<SiteListModel> addpartner_in_site(@Header("Authorization")
                                                   String token,@Path("user_id") String user_id
        ,@Path("site_id") String site_id,@Query("add") String add);


@GET("site/site-image/{site_id}")
Call<ArrayList<ImageModel>> allimages(@Header("Authorization")
                                              String token,@Path("site_id") String site_id);
@Multipart
@POST("site/site-image")
Call<ImageModel> upload(@Header("Authorization") String token, @Part MultipartBody.Part image,
                        @Part("Site") RequestBody site_id);
    @Multipart
    @POST("site/site-file")
    Call<ImageModel> fileupload(@Header("Authorization") String token, @Part MultipartBody.Part file,
                            @Part("Site") RequestBody site_id);
@GET("site/list/{id}")
    Call<SiteListModel> getsinglesite(@Header("Authorization")
                                              String token,@Path("id") String id);

@POST("site/site-expense")
    Call<SiteExpenseModel> siteexpense(@Header("Authorization")
                                               String token,@Body SiteExpenseModel siteExpenseModel);

    @POST("site/plot-expense")
    Call<PlotExpenseModel> plotexpense(@Header("Authorization")
                                               String token,@Body PlotExpenseModel plotExpenseModel);

    @GET("site/site-expense")
    Call<ArrayList<SiteSummaryModel>> sitesummary(@Header("Authorization") String token,@Query("site_id") String site);

    @GET("site/plot-expense")
    Call<ArrayList<PlotSummaryModel>> plotsummary(@Header("Authorization") String token,@Query("plot_id")String plot);
    @POST("site/multiple_plot_book")
    Call<PlotListModel> plotbook(@Header("Authorization") String token, @Body BookingModel bookingModel);

    @GET("site/book")
    Call<ArrayList<BookingModel>> bookinglist(@Header("Authorization") String token);

    @DELETE("site/book/{id}")
    Call<BookingModel> delete_book_summary(@Header("Authorization") String token,@Path("id") String id);

}
