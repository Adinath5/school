package com.atharvainfo.myschool.others;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api.php?apicall=addNewClassName")
        // @POST("getuserlogin.php")
    Call<ServerResponse> addnewclassname(@Field("classname") String classname,
                                     @Field("addedby") String addedby,
                                     @Field("date") String date);


}
