package com.example.firstchallange.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.example.firstchallange.items.Repo
import com.example.firstchallange.RepoViewHolder

class RepoAdapter(repoList:List<Repo>, val clickListener:(Repo)->Unit): RecyclerView.Adapter<RepoViewHolder>() {

    var repoList=repoList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bindTo(repoList[position],clickListener)
    }

    override fun getItemCount(): Int {
        return repoList.size
    }
}