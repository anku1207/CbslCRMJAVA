package com.cbslprojects.crm.RMMS.Util;

import com.cbslprojects.crm.RMMS.Interface.ApiInterface;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by user on 02/07/18.
 */

public class ApiClient {

    private static Retrofit json_retrofit = null;


    public static ApiInterface callJsonRetrofit() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient m_client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        if (json_retrofit == null) {
            json_retrofit = new Retrofit.Builder()
                    .baseUrl(Constraints.base_url)
                    .client(m_client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return json_retrofit.create(ApiInterface.class);
    }

    public static RequestBody javaObjectToXml(String ApiName, HashMap<String, String> ApiParameter) {
        StringBuilder param = new StringBuilder();

        for (String key : ApiParameter.keySet()) {
            param.append("<").append(key).append(">").append(ApiParameter.get(key))
                    .append("</").append(key).append(">\n");
            // System.out.println("key: " + key + " value: " + ApiParameter.get(key));
        }
        String requestBodyText = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <" + ApiName + " xmlns=\"http://crm.cbslprojects.com/\">\n" +
                param.toString() +
                "    </" + ApiName + ">\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope> ";

        return RequestBody.create(MediaType.parse("text/xml"), requestBodyText);

    }

}
