package com.lecet.app.data.api.service;

import com.lecet.app.data.api.request.ProjectNotifyRequest;
import com.lecet.app.data.api.response.ProjectsNearResponse;
import com.lecet.app.data.models.Jurisdiction;
import com.lecet.app.data.models.NotePost;
import com.lecet.app.data.models.PhotoPost;
import com.lecet.app.data.models.Project;
import com.lecet.app.data.models.ProjectNote;
import com.lecet.app.data.models.ProjectPhoto;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * File: ProjectService Created: 10/5/16 Author: domandtom
 *
 * This code is copyright (c) 2016 Dom & Tom Inc.
 */

public interface ProjectService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects")
    Call<List<Project>> projects(@Header("Authorization") String authorization, @Query("filter") String filter);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects/near")
    Call<ProjectsNearResponse> projectsNear(@Header("Authorization") String authorization, @Query("lat") double lat, @Query("lng") double lng, @Query("dist") int dist, @Query("filter") String filter);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects/{projectID}")
    Call<Project> project(@Header("Authorization") String authorization, @Path("projectID") long projectID, @Query("filter") String filter);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects/{projectID}/jurisdiction")
    Call<List<Jurisdiction>> projectJurisdiction(@Header("Authorization") String authorization, @Path("projectID") long projectID);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("Projects/{projectID}/hide")
    Call<ResponseBody> hide(@Header("Authorization") String authorization, @Path("projectID") long projectID);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("Projects/{projectID}/unhide")
    Call<ResponseBody> unhide(@Header("Authorization") String authorization, @Path("projectID") long projectID);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("LecetUsers/{user_id}/hiddenProjects")
    Call<List<Project>> hiddenProjects(@Header("Authorization") String authorization, @Path("user_id") long user_id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("Projects/{projectID}/userNotes")
    Call<ProjectNote> addNote (@Header("Authorization") String authorization, @Path("projectID") long projectID, @Body NotePost notePost);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("Notes/{noteID}")
    Call<ProjectNote> updateNote (@Header("Authorization") String authorization, @Path("noteID") long noteID, @Body NotePost notePost);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects/{projectID}/userNotes")
    Call<List<ProjectNote>> projectNotes (@Header("Authorization") String authorization, @Path("projectID") long projectID);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("Projects/{projectID}/uploadImage")
    Call<ProjectPhoto> addPhoto (@Header("Authorization") String authorization, @Path("projectID") long projectID, @Body PhotoPost photoPost);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("Images/{photoID}")
    Call<ProjectPhoto> updatePhoto (@Header("Authorization") String authorization, @Path("photoID") long photoID, @Body PhotoPost photoPost);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("Projects/{projectID}/images")
    Call<List<ProjectPhoto>> projectImages (@Header("Authorization") String authorization, @Path("projectID") long projectID);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("Projects/Project_notify")
    Call<ResponseBody> projectNotify(@Body ProjectNotifyRequest notifyRequest);
}
