package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.data.network.models.ElectionResponse
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(ElectionAdapter())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    @GET("elections")
    suspend fun elections(): ElectionResponse

    @GET("voterinfo")
    suspend fun voterInfo(
            @Query("address") address: String,
            @Query("officialOnly") officialOnly: Boolean = false,
    ): VoterInfoResponse
    @GET("voterinfo")
    suspend fun voterInfo(
            @Query("address") address: String,
            @Query("electionId") electionId: Long,
            @Query("officialOnly") officialOnly: Boolean = false,
    ): VoterInfoResponse

    //TODO: Add representatives API Call
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}