package com.example.firstchallange.jsonClient

import com.example.firstchallange.items.Repo
import retrofit2.Call
import retrofit2.http.GET

interface RepoService{
    @GET("kickstarter")
    fun getRepos():Call<List<Repo>>
}

/*@Path("s.no",encoded = true)sNo:String,
  @Path("amt.pledged",encoded = true)amtPledged:String,
  @Path("end.time",encoded = true)endTime:String,
  @Path("percentage.funded",encoded = true)percentageFunded:String,
  @Path("num.backers",encoded = true)numBackers:String*/