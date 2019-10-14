package com.example.firstchallange

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.example.firstchallange.adapters.RepoAdapter
import com.example.firstchallange.items.Repo
import com.example.firstchallange.jsonClient.RepoService
import com.example.firstchallange.jsonClient.RetrofitClient
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast




class MainActivity : AppCompatActivity() {
    var searchlist = ArrayList<Repo>()
    var filterlist = ArrayList<Repo>()
    var showerList = ArrayList<Repo>()
    private lateinit var filterRange : Array<Int>
    lateinit var mainList:ArrayList<Repo>
    var rv: RecyclerView? = null
    private lateinit var firstRange :TextView
    private lateinit var secondRange :TextView
    private lateinit var lastRange :TextView
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
     //   window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        try { this.supportActionBar!!.hide() }
        catch (e: NullPointerException) { }
        setContentView(R.layout.activity_main)
        rv = findViewById(R.id.recyclerView)

        val filterButton = findViewById<ImageButton>(R.id.filter_button)
        val sortButton = findViewById<ImageButton>(R.id.sort_button)
        val searchView = findViewById<SearchView>(R.id.searchBar)


        val searcherText = findViewById<TextView>(R.id.searcher_text)
        val searcherCloser = findViewById<ImageButton>(R.id.close_searcher)
        val searcherLayout = findViewById<CardView>(R.id.searcher_layout)
        val orderText = findViewById<TextView>(R.id.order_text)
        val orderCloser = findViewById<ImageButton>(R.id.close_order)
        val orderLayout = findViewById<CardView>(R.id.order_layout)
        val filterLayout = findViewById<CardView>(R.id.filter_layout)
        val filterCloser = findViewById<ImageButton>(R.id.close_filter)


        val filterOkey = findViewById<ImageButton>(R.id.filter_button_OK)
        val filterBar = findViewById<LinearLayout>(R.id.filter_operations)
        val firstRange = findViewById<LinearLayout>(R.id.first_filter_L)
        val secondRange = findViewById<LinearLayout>(R.id.second_filter_L)
        val thirdRange = findViewById<LinearLayout>(R.id.third_filter_L)
        val selectedFilters = arrayOf(false,false,false)

        searchView.queryHint = "Search Projects.."
        searchView.setOnSearchClickListener {
            filterButton.visibility = View.GONE
            sortButton.visibility = View.GONE
        }
        searchView.setOnCloseListener {
            filterButton.visibility = View.VISIBLE
            sortButton.visibility = View.VISIBLE
            false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                filterButton.visibility = View.VISIBLE
                sortButton.visibility = View.VISIBLE
                searchView.onActionViewCollapsed()
                if (query.isNotEmpty()) {
                    Toast.makeText(this@MainActivity,query, Toast.LENGTH_SHORT).show()
                    search(query)

                    linear_operations.visibility = View.VISIBLE
                    searcherLayout.visibility = View.VISIBLE
                    searcherText.text = query.capitalize()
                    // handle search here
                    return true
                }
                return false
            }
        })

        sortButton.setOnClickListener {
            if(orderLayout.visibility == View.INVISIBLE){
                sortList()
                linear_operations.visibility = View.VISIBLE
                orderLayout.visibility = View.VISIBLE
                orderText.text = "SORTED"
            }
            else{
                if( filterLayout.visibility == View.INVISIBLE && searcherLayout.visibility == View.INVISIBLE ) linear_operations.visibility = View.GONE
                orderLayout.visibility = View.INVISIBLE
                orderText.text = ""
                updateShowList()
            }
        }
        orderCloser.setOnClickListener {
            if( filterLayout.visibility == View.INVISIBLE && searcherLayout.visibility == View.INVISIBLE ) linear_operations.visibility = View.GONE
            orderLayout.visibility = View.INVISIBLE
            orderText.text = ""
            updateShowList()
        }

        searcherCloser.setOnClickListener{
            if( filterLayout.visibility == View.INVISIBLE && orderLayout.visibility == View.INVISIBLE ) linear_operations.visibility = View.GONE
            searcherLayout.visibility = View.INVISIBLE
            endTheSearch()
        }
        filterCloser.setOnClickListener{
            if( searcherLayout.visibility == View.INVISIBLE && orderLayout.visibility == View.INVISIBLE ) linear_operations.visibility = View.GONE
            filterLayout.visibility = View.INVISIBLE
            firstRange.tag = "Grey"
            secondRange.tag = "Grey"
            thirdRange.tag = "Grey"
            firstRange.setBackgroundColor(Color.GRAY)
            secondRange.setBackgroundColor(Color.GRAY)
            thirdRange.setBackgroundColor(Color.GRAY)
            for (i in 0..2) selectedFilters[i] = false
            filter(selectedFilters,filterRange)
        }

        firstRange.setOnClickListener{
            if(firstRange.tag == "Grey"){
                firstRange.tag = "colorPrimary"
                firstRange.setBackgroundColor(Color.parseColor("#008577"))
                selectedFilters[0] = true
                filter(selectedFilters,filterRange)
            }
            else{
                selectedFilters[0] = false
                firstRange.tag = "Grey"
                firstRange.setBackgroundColor(Color.GRAY)
                filter(selectedFilters,filterRange)
            }
        }
        secondRange.setOnClickListener {
            if (secondRange.tag == "Grey") {
                secondRange.tag = "colorPrimary"
                secondRange.setBackgroundColor(Color.parseColor("#008577"))
                selectedFilters[1] = true
                filter(selectedFilters,filterRange)
            }
            else{
                selectedFilters[1] = false
                secondRange.tag = "Grey"
                secondRange.setBackgroundColor(Color.GRAY)
                filter(selectedFilters,filterRange)
            }
        }
        thirdRange.setOnClickListener{
            if(thirdRange.tag == "Grey"){
                thirdRange.tag = "colorPrimary"
                thirdRange.setBackgroundColor(Color.parseColor("#008577"))
                selectedFilters[2] = true
                filter(selectedFilters,filterRange)
            }
            else{
                selectedFilters[2] = false
                thirdRange.tag = "Grey"
                thirdRange.setBackgroundColor(Color.GRAY)
                filter(selectedFilters,filterRange)
            }
        }

        filterButton.setOnClickListener{
            setFilterRange()
            filterButton.visibility = View.GONE
            filterOkey.visibility = View.VISIBLE
            filterBar.visibility = View.VISIBLE
        }
        filter_button_OK.setOnClickListener{
            filterBar.visibility = View.GONE
            filterOkey.visibility = View.GONE
            filterButton.visibility = View.VISIBLE
            if(firstRange.tag == "colorPrimary" || secondRange.tag == "colorPrimary"  || thirdRange.tag == "colorPrimary" ){
                filterLayout.visibility = View.VISIBLE
                linear_operations.visibility = View.VISIBLE
            }
        }

        rv?.layoutManager= LinearLayoutManager( this)

        rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) {
                    // Recycle view scrolling down...
                    if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                        Toast.makeText(applicationContext, "Reached the end of recycler view", Toast.LENGTH_LONG).show()
                    }

                }
            }
        })
        RetrofitClient.getClient().create(RepoService::class.java)
                .getRepos().enqueue(object: Callback<List<Repo>>{

                override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                    animation_layout.visibility = View.GONE
                    load_animation.cancelAnimation()
                    Thread.sleep(500)  // wait for 1 second

                    mainList = ArrayList(response.body())
                    filterlist.apply { addAll(mainList) }
                    searchlist.apply { addAll(mainList) }
                    showerList.apply { addAll(mainList) }
                    Toast.makeText(this@MainActivity,"Succes", Toast.LENGTH_SHORT).show()
                    rv?.adapter= RepoAdapter(showerList
                    ) { repoItem: Repo -> repoItemClicked(repoItem) }
                }

                override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Failure",Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun sortList(){
        val sortingList = ArrayList<Repo>()
        sortingList.addAll(showerList)
        showerList.clear()
        showerList.addAll(sortingList.sortedWith(compareBy { it.title }))
        rv?.adapter?.notifyDataSetChanged()
    }
    private fun setFilterRange(){
        var minBackers = 99999999
        var maxBackers = 0
        firstRange = findViewById(R.id.F_range)
        secondRange = findViewById(R.id.S_range)
        lastRange = findViewById(R.id.L_range)
        mainList.forEach {
            try{
                if(it.num_backers!!.toInt() < minBackers){
                    minBackers = it.num_backers!!.toInt()
                }
                if(it.num_backers!!.toInt() > maxBackers) {
                    maxBackers = it.num_backers!!.toInt()
                }
            }
            catch (e: Exception){
                //          it.num_backers is not an integer at 54. Data and 60. Data
                it.num_backers = "Non-Integer Data"
            }
        }
        filterRange = arrayOf(minBackers,minBackers + (maxBackers-minBackers)/3,minBackers + 2*(maxBackers-minBackers)/3,maxBackers)

        firstRange.text = "From :" + filterRange[0] + "    To :" + filterRange[1]
        secondRange.text = "From :" + filterRange[1] + "    To :"  + filterRange[2]
        lastRange.text = "From :" + filterRange[2] + "    To :"  + filterRange[3]
    }
    private fun filter(filters:Array<Boolean>, ranges:Array<Int>){
        filterlist.clear()
        val filterText = findViewById<TextView>(R.id.filter_text)

        if(filters[0] && filters [1] && filters[2]){
            filterText.text = ranges[0].toString() + " - " + ranges[3]
            mainList.forEach{ try{ filterlist.add(it) } catch(E:Exception){ } }}

        else if(filters[0] && filters [1] && !filters[2]){
            filterText.text = ranges[0].toString() + " - " + ranges[2]
            mainList.forEach{ try{  if( it.num_backers!!.toInt() < ranges[2] ){filterlist.add(it) } } catch(E:Exception){ } }}

        else if(filters[0] && !filters [1] && filters[2]){
            filterText.text = ranges[0].toString() + " - " + ranges[1] + " & " + ranges[2] + " - " + ranges[3]
            mainList.forEach{ try{ if( it.num_backers!!.toInt() < ranges[1] || it.num_backers!!.toInt() >= ranges[2] ){filterlist.add(it) }} catch(E:Exception){ } } }

        else if(!filters[0] && filters [1] && filters[2]){
            filterText.text = ranges[1].toString() + " - " + ranges[3]
            mainList.forEach{ try{ if( it.num_backers!!.toInt() >= ranges[1] ){filterlist.add(it) } } catch(E:Exception){ } }}

        else if(!filters[0] && !filters [1] && filters[2]){
            filterText.text = ranges[2].toString() + " - " + ranges[3]
            mainList.forEach{ try{ if( it.num_backers!!.toInt() >= ranges[2] ){filterlist.add(it) }} catch(E:Exception){ } } }

        else if(!filters[0] && filters [1] && !filters[2]){
            filterText.text = ranges[1].toString() + " - " + ranges[2]
            mainList.forEach{ try{ if( it.num_backers!!.toInt() >= ranges[1] && it.num_backers!!.toInt() < ranges[2] ){filterlist.add(it) }} catch(E:Exception){ } } }

        else if(filters[0] && !filters [1] && !filters[2]){
            filterText.text = ranges[0].toString() + " - " + ranges[1]
            mainList.forEach{ try{ if( it.num_backers!!.toInt() < ranges[1] ){filterlist.add(it) }} catch(E:Exception){ } } }

        else if(!filters[0] && !filters [1] && !filters[2]){
            filterText.text = ranges[0].toString() + " - " + ranges[3]
            mainList.forEach{ try{  filterlist.add(it)} catch(E:Exception){ } } }

        updateShowList()
    }
    private fun endTheSearch(){
        searchlist.clear()
        searchlist.apply { addAll(mainList) }
        updateShowList()
    }
    fun search(title: String) {
        searchlist.clear()
        mainList.forEach {
            if(it.title.toLowerCase().contains(title.toLowerCase())){
                searchlist.add(it)
            }
        }
        updateShowList()
    }
    private fun updateShowList(){
        val orderLayout = findViewById<CardView>(R.id.order_layout)
        showerList.clear()
        showerList.addAll(searchlist.intersect(filterlist).toList())
        if(orderLayout.visibility == View.VISIBLE) sortList()
        rv?.adapter?.notifyDataSetChanged()
    }
    private fun repoItemClicked(repoItem: Repo){
        Toast.makeText(this,"Clicked: ${repoItem.title}",Toast.LENGTH_LONG).show()
        val intent = Intent(this, AnotherActivity::class.java)
        intent.putExtra("blurb",repoItem.blurb)
        intent.putExtra("by",repoItem.by)
        intent.putExtra("country",repoItem.country)
        intent.putExtra("location",repoItem.location)
        intent.putExtra("currency",repoItem.currency)
        intent.putExtra("end_time",repoItem.end_time)
        intent.putExtra("s_no",repoItem.s_no)
        intent.putExtra("amt_pledge",repoItem.amt_pledged)
        intent.putExtra("percentage_funded",repoItem.percentage_funded)
        intent.putExtra("num_backers",repoItem.num_backers)
        intent.putExtra("state",repoItem.state)
        intent.putExtra("title",repoItem.title)
        intent.putExtra("type",repoItem.type)
        intent.putExtra("url",repoItem.url)
        startActivity(intent)
    }
}
