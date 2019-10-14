package com.example.firstchallange

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.firstchallange.items.Repo


class RepoViewHolder(viewGroup:ViewGroup): RecyclerView.ViewHolder
    (LayoutInflater.from(viewGroup.context).inflate
    (R.layout.repo_list_item,viewGroup,false))
{/*
    private  val txtcountry by lazy { itemView.findViewById<TextView>(R.id.country) }
    private  val txtcurrency by lazy { itemView.findViewById<TextView>(R.id.currency) }
    private  val txtlocation by lazy { itemView.findViewById<TextView>(R.id.location) }
    private  val txtstate by lazy { itemView.findViewById<TextView>(R.id.state) }
   */
    private  val txtby by lazy { itemView.findViewById<TextView>(R.id.by) }

    private  val txttitle by lazy { itemView.findViewById<TextView>(R.id.title) }
    private  val txtbackers by lazy { itemView.findViewById<TextView>(R.id.backers) }
    private  val txtamtpledge by lazy { itemView.findViewById<TextView>(R.id.pledge) }

    // private  val txturl by lazy { itemView.findViewById<TextView>(R.id.url) }

    fun bindTo(repoDto: Repo, clickListener:(Repo)->Unit){
/*
        txtcountry.text=repoDto.country + "             ->"
        txtcurrency.text=repoDto.currency
        txtlocation.text=repoDto.location + "       ->"
        txtstate.text=repoDto.state
  */
        txtby.text=repoDto.by
        txttitle.text=repoDto.title
        txtbackers.text =  repoDto.num_backers
        txtamtpledge.text =  repoDto.amt_pledged + " $"
        //txturl.text=repoDto.url
        itemView.setOnClickListener{clickListener(repoDto)}
    }

}
