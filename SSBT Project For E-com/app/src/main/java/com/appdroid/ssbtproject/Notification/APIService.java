package com.appdroid.ssbtproject.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA56B06ho:APA91bFO1-nvVeM8EKfFrYwYpJfCJDN3kZC2coI4yYkNsM_dY-inv0qRQe5n_OCS2Cmo0ctsiiZUYGqsB8M0js7k6WQHLGQzMD31SZCXcdIxUeYktv2p8rSF9nrq9YSZeAVDDGdU9HcL"
    })

    @POST("fcm/send")
        Call<Responce> sendNotification(@Body Sender body);


}
