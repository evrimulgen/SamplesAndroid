package com.india.innovates.pucho.api;

import com.india.innovates.pucho.models.Answers;
import com.india.innovates.pucho.models.FeedResponse;
import com.india.innovates.pucho.models.FollowModel;
import com.india.innovates.pucho.models.LoginPost;
import com.india.innovates.pucho.models.LoginResponse;
import com.india.innovates.pucho.models.LoginViaServerPost;
import com.india.innovates.pucho.models.MyQuestionsFetched;
import com.india.innovates.pucho.models.PostAnswerContentModel;
import com.india.innovates.pucho.models.PostAnswerResponse;
import com.india.innovates.pucho.models.PostEditAnswer;
import com.india.innovates.pucho.models.PostGcmToken;
import com.india.innovates.pucho.models.PostQuestionContent;
import com.india.innovates.pucho.models.QuestionContentModel;
import com.india.innovates.pucho.models.SearchQueryResponse;
import com.india.innovates.pucho.models.SendTokenResponse;
import com.india.innovates.pucho.models.SignUpPost;
import com.india.innovates.pucho.models.SignupResponse;
import com.india.innovates.pucho.models.SuccessSignUpResponse;
import com.india.innovates.pucho.utils.UrlStrings;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Raghunandan on 03-01-2016.
 */
public interface Api {

    //@Headers({"Content-Type: application/json","charset=utf-8"})
    @Headers("Content-Type: application/json")
    @GET("/questions")
    Observable<LoginResponse> getQuestions(@Query("page") int page, @Query("per_page") int per_page);

    @Headers("Content-Type: application/json")
    @POST(UrlStrings.GOOGLE_OAUTH)
    Observable<LoginResponse> post_userdetails(@Body LoginPost loginPost);

    @Headers("Content-Type: application/json")
    @POST(UrlStrings.SIGN_UP)
    Observable<List<SuccessSignUpResponse>> post_signUpdetails_server(@Body SignUpPost signuppost);

    @Headers("Content-Type: application/json")
    @POST(UrlStrings.LOGIN_POST)
    Observable<SignupResponse> post_loginDetails(@Body LoginViaServerPost loginPost);

    @Headers("Content-Type: application/json")
    @POST(UrlStrings.POST_GCM_TOKEN)
    Observable<SendTokenResponse> post_GCMToken(@Body PostGcmToken postGcmToken);

    //@Headers("Content-Type: application/json")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(UrlStrings.ASK_QUESTION)
    Observable<List<QuestionContentModel>> postQuestion_server(@Body PostQuestionContent postQuestionContent);


    @Headers("Content-Type: application/json")
    @POST("/pucho/questions/{questionid}/answers")
    Observable<List<PostAnswerResponse>> postAnswer_server(@Path("questionid") int guestionId, @Body PostAnswerContentModel postAnswerContentModel);

    // (@Header("language")String value,
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET(UrlStrings.FETCH_FEED)
    Observable<FeedResponse> fetch_Feed(@Query("page") int page, @Query("per_page") int perpage,@Query("active") boolean active);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("/pucho/questions/{questionid}/answers")
    Observable<List<Answers>> fetch_Answers(@Path("questionid") int guestionId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST(UrlStrings.BASE_URL + UrlStrings.SEARCH)
    Observable<SearchQueryResponse> postSearchQUery(@Query("query") String queryparams, @Query("from") int from,@Query("active") boolean active);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET(UrlStrings.BASE_URL + UrlStrings.FETCH_USER_POSTED_QUESTIONS)
    Observable<List<MyQuestionsFetched>> fetch_question_userId(@Query("user_id") String queryparams,@Query("active") boolean active);

    @DELETE("/pucho/questions/{questionid}")
    Call<ResponseBody> delete_question(@Path("questionid") int guestionId);

    @POST("/pucho/wall/{followerUserId}/followQuestion/{followedQuestionId}")
    Observable<FollowModel> follow_question(@Path("followerUserId") int userid, @Path("followedQuestionId") int questionid);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT(UrlStrings.EDIT_QUESTION+"/{questionid}")
    Call<ResponseBody> edit_question(@Body PostQuestionContent postQuestionContent,@Path("questionid") int questionid);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT ("pucho/questions/{questionId}/answers/{answerId}")
    Call<ResponseBody> edit_answer(@Body PostEditAnswer postEditAnswer,@Path("answerId") int answerId,@Path("questionId") int questionId);
}
