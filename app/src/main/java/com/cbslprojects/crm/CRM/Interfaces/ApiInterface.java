package com.cbslprojects.crm.CRM.Interfaces;


import com.cbslprojects.crm.CRM.Model.CompanyDetail;
import com.cbslprojects.crm.CRM.Model.GetFileType;
import com.cbslprojects.crm.CRM.Model.CommenResponse;
import com.cbslprojects.crm.CRM.Model.ItemDetail;
import com.cbslprojects.crm.CRM.Model.MachineInformation;
import com.cbslprojects.crm.CRM.Model.QuantityDetail;
import com.cbslprojects.crm.CRM.Model.ReloveComplaint;
import com.cbslprojects.crm.CRM.Model.RagisterComplaint;
import com.cbslprojects.crm.CRM.Model.Result;
import com.cbslprojects.crm.CRM.Model.ScheduleMachine;
import com.cbslprojects.crm.CRM.Model.UserCredential;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by user on 02/07/18.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("ValidateMobileUserLogin")
    Call<UserCredential> LoginUser(@Field("EmailId") String EmailId,
                                   @Field("Password") String Password);

    @GET("GetEmployeelocation")
    Call<CommenResponse> sendEmployeeLocation(@Query("userID") String userID,
                                              @Query("InLatitude") String Latitude,
                                              @Query("InLongitude") String Longitude);


    @POST("CheckInEmp")
    @FormUrlEncoded
    Call<CommenResponse> sendPunch(@Field("userID") String userID,
                                   @Field("InLatitude") String Latitude,
                                   @Field("InLongitude") String Longitude,
                                   @Field("InImageString") String InImageString,
                                   @Field("DeviceID") String DeviceID);

    @POST("SubmitConsumableOrders")
    @FormUrlEncoded
    Call<Result> SubmitConsumableOrders(@Field("BankId") int BankId,
                                        @Field("SoleId") String SoleId,
                                        @Field("ItemId") int ItemId,
                                        @Field("CompanyId") int CompanyId,
                                        @Field("ProjectId") int ProjectId,
                                        @Field("Machine") String Machine,
                                        @Field("User") String User,
                                        @Field("Quantity") String Quantity,
                                        @Field("RequiredDate") String RequiredDate,
                                        @Field("BankName") String BankName,
                                        @Field("ItemName") String ItemName,
                                        @Field("ContactPerson") String ContactPerson,
                                        @Field("ClientEmail") String ClientEmail,
                                        @Field("PostalCode") String PostalCode);



    @POST("CheckOutEmp")
    @FormUrlEncoded
    Call<CommenResponse> sendCheckOut(@Field("userID") String userID,
                                      @Field("OutLongitude") String Latitude,
                                      @Field("OutLatitude") String Longitude,
                                      @Field("ReferenceNo") String ReferenceNo,
                                      @Field("DeviceID") String DeviceID);

    @GET("BranchInformationWithUserid")
    Call<RagisterComplaint> RagisterComplaintList(@Query("UserId") String userID,
                                                  @Query("SoleId") String SoleId);

    @GET("GetCompanyDetail")
    Call<CompanyDetail> getCompanyDetail();

    @FormUrlEncoded
    @POST("ConsumableItemProjectwise")
    Call<ItemDetail> getConsumableItemProjectwise(@Field("BankId") int BankId,
                                                  @Field("ProjectId") int ProjectId);

    @FormUrlEncoded
    @POST("ItemQuantityInformation")
    Call<QuantityDetail> getItemQuantityInformation(@Field("ItemId") int ItemId,
                                                    @Field("BankId") int BankId,
                                                    @Field("ProjectId") int ProjectId);

    @GET("GetResolveComplaintList")
    Call<ReloveComplaint> ResolveComplaintList(@Query("UserId") String userID,
                                               @Query("SoleId") String SoleId);


    @GET("GetScheduledMachineListforInstallation")
    Call<ScheduleMachine> ScheduleMachineList(@Query("UserId") String userID,
                                              @Query("SoleId") String SoleId);

    @GET("GetScheduledMachineListforInstallation")
    Call<ScheduleMachine> ScheduledMachineListforInstallation(@Query("UserId") String userID,
                                                              @Query("SoleId") String SoleId);
    @FormUrlEncoded
    @POST("MachineInformation")
    Call<MachineInformation> MachineInfoDetails(@Field("MachineNo") String MachineNo);


    @GET("RegisterComplaint")
    Call<CommenResponse> RegisterComplaint(@Query("CityName") String CityName,
                                           @Query("StateName") String StateName,
                                           @Query("BranchName") String BranchName,
                                           @Query("LocationName") String LocationName,
                                           @Query("BankName") String BankName,
                                           @Query("MachineId") String MachineId,
                                           @Query("BankId") String BankId,
                                           @Query("StateId") String StateId,
                                           @Query("CityId") String CityId,
                                           @Query("BranchId") String BranchId,
                                           @Query("ProjectId") String ProjectId,
                                           @Query("ProjectName") String ProjectName,
                                           @Query("SoleId") String SoleId,
                                           @Query("ItemId") String ItemId,
                                           @Query("ComplaintType") String ComplaintType,
                                           @Query("ComplaintDetails") String ComplaintDetails,
                                           @Query("MachineNo") String MachineNo,
                                           @Query("UserId") String UserId);

    @GET("GetFileType")
    Call<GetFileType> getFileTypeList();


    @POST("FileUpload")
    @FormUrlEncoded
    Call<CommenResponse> fileUploadOnServer(@Field("Latitude") String Latitude,
                                            @Field("Longitude") String Longitude,
                                            @Field("userID") String userID,
                                            @Field("ImageString") String ImageString,
                                            @Field("DeviceID") String DeviceID,
                                            @Field("fileTypeID") String fileTypeID,
                                            @Field("bankID") String bankID,
                                            @Field("branchId") String branchId,
                                            @Field("projectId") String projectId);

    @POST("ResolveComplaint")
    @FormUrlEncoded
    Call<CommenResponse> ResolveComplaint(@Field("Latitude") String Latitude,
                                          @Field("Longitude") String Longitude,
                                          @Field("UserId") String userID,
                                          @Field("Image") String ImageString,
                                          @Field("MachineId") String MachineId,
                                          @Field("ComplaintNo") String ComplaintNo,
                                          @Field("ComplaintStatus") String ComplaintStatus,
                                          @Field("Comment") String Comment,
                                          @Field("ImageName") String ImageName);

//    @Multipart
//    @POST("ResolveComplaint")
//    Call<CommenResponse> ResolveComplaint1(@Part("Latitude") RequestBody Latitude,
//                                          @Part("Longitude") RequestBody Longitude,
//                                          @Part("UserId") RequestBody userID,
//
//                                          @Part("MachineId") RequestBody MachineId,
//                                          @Part("ComplaintNo") RequestBody ComplaintNo,
//                                          @Part("ComplaintStatus") RequestBody ComplaintStatus,
//                                          @Part("Comment") RequestBody Comment,
//                                           @Part MultipartBody.Part  ImageString);
    @Multipart
    @POST
    Call<CommenResponse> ResolveComplaint1(@Part MultipartBody.Part  ImageString);


    @POST("MachineInstallation")
    @FormUrlEncoded
    Call<CommenResponse> MachineInstallationComplete(@Field("Latitude") String Latitude,
                                                     @Field("Longitude") String Longitude,
                                                     @Field("BankID") String BankID,
                                                     @Field("UserId") String userID,
                                                     @Field("SoleId") String SoleId,
                                                     @Field("MachineId") String MachineId,
                                                     @Field("InstallationDate") String InstallationDate,
                                                     @Field("Image") String Image,
                                                     @Field("ImageName") String ImageName,
                                                     @Field("Comment") String Comment,
                                                     @Field("IpAddress") String IpAddress);

}

