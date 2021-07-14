package com.cbslprojects.crm.RMMS.Interface;

import com.cbslprojects.crm.RMMS.model.CommonResponse;
import com.cbslprojects.crm.RMMS.model.Login;
import com.cbslprojects.crm.RMMS.model.Machine;
import com.cbslprojects.crm.RMMS.model.RagisterComplaint;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiInterface {
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("Authenticateuser")
    Call<Login> Authenticateuser(@Body RequestBody body);


    @GET("GetDashboard/{bankId}")
    Call<Machine> getMachineDetail(@Path("bankId") String bankId);

    @GET("Getzonedetails/{bankId}")
    Call<Machine> getMachineDetailByBankID(@Path("bankId") String bankId);

//    @Headers({
//            "Content-Type: text/xml",
//            "Accept-Charset: utf-8",
//            "Accept: application/json"
//    })
//    @POST("Authenticateuser/")
//    Call<UserCredential> getLoginDetail(@Body RequestBody body);


    @Headers({
            "Content-Type: text/xml",
            "Accept-Charset: utf-8",
            "Accept: application/json"
    })
    @POST("SaveComplaint/")
    Call<RagisterComplaint> RegisterComplaint(@Body RequestBody body);


    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    @POST("Logout/{email}/")
    Call<CommonResponse>  logout(@Path("email") String email);

}


