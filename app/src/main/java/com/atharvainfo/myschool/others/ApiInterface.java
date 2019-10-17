package com.atharvainfo.myschool.others;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface<Staffemail> {

    @FormUrlEncoded
    @POST("api.php?apicall=addNewClassName")
        // @POST("getuserlogin.php")
    Call<ServerResponse> addnewclassname(@Field("classname") String classname,
                                         @Field("addedby") String addedby,
                                         @Field("date") String date);

    @FormUrlEncoded
    @POST("api.php?apicall=addNewStudentName")
    Call<ServerResponse> addnewstudent(@Field("Admissionno") String Admissionno,
                                       @Field("Spinner") String Spinner,
                                       @Field("Gender") String Gender,
                                       @Field("Studentname") String Studentname,
                                       @Field("Fathername") String Fathername,
                                       @Field("Mothername") String Mothername,
                                       @Field("Emailadress") String Emailadress,
                                       @Field("Mobilenooffather") String Mobilenooffather,
                                       @Field("Mobilenoofmother") String Mobilenoofmother,
                                       @Field("DateofRegistartion") String DateofRegistartion,
                                       @Field("Dateofeffectiveform") String Dateofeffectiveform);

    @FormUrlEncoded
    @POST("api.php?apicall=addNewStaffName")
    Call<ServerResponse> addnewstaffname(@Field("staffname") String staffname,
                                         @Field("staffemail") String staffemail,
                                         @Field("staffaddress") String staffaddress,
                                         @Field("staffmobileno") String staffmobileno,
                                         @Field("designation") String designation,
                                         @Field("dateofjoining") String dateofjoining);

    @FormUrlEncoded
    @POST("api.php?apicall=addNewSubjectName")
    Call<ServerResponse> addnewsubjectname(@Field("subjectname") String subjectname);

    @FormUrlEncoded
    @POST("api.php?apicall=addNewBookName")
    Call<ServerResponse> addbookmaster (@Field("Bookno") String bookno,
                                        @Field("Bookname") String bookname,
                                        @Field("Bookauther") String bookauther,
                                        @Field(" Bookdateofissued.") String bookdateofissued,
                                        @Field("Booksuppliername") String booksuppliername,
                                        @Field("Bookpublisher") String bookpublisher,
                                        @Field("Bookprice") String bookprice,
                                        @Field("Booksubject") String booksubject,
                                        @Field("Bookbarcode") String bookbarcode);

}
