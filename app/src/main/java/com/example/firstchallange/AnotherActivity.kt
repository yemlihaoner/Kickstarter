package com.example.firstchallange

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.ImageView
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageButton


class AnotherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        try { this.supportActionBar!!.hide() }
        catch (e: NullPointerException) { }

        setContentView(R.layout.activity_another)

        var custom_back_button = findViewById<ImageButton>(R.id.page2_getBack)
        custom_back_button.setOnClickListener{
            finish()
        }

        var blurb : String = intent.getStringExtra("blurb")
        var by : String = intent.getStringExtra("by")
        var currency : String = intent.getStringExtra("currency")
        var location : String = intent.getStringExtra("location")
        var state : String = intent.getStringExtra("state")
        var title : String = intent.getStringExtra("title")
        var type : String = intent.getStringExtra("type")
        var url : String = intent.getStringExtra("url")

        var endTime : String = intent.getStringExtra("end_time")
        var percentageFunded:String = intent.getStringExtra("percentage_funded")
        var numBackers:String = intent.getStringExtra("num_backers")
        var amtPledge:String = intent.getStringExtra("amt_pledge")
        var sNo:String = intent.getStringExtra("s_no")



        var txtBlurb = findViewById<TextView>(R.id.page2_blurb)
        var txtBy = findViewById<TextView>(R.id.page2_by)
        var txtCurrency = findViewById<TextView>(R.id.page2_currency)
        var txtLocation = findViewById<TextView>(R.id.page2_location)
        var txtState = findViewById<TextView>(R.id.page2_state)
        var txtTitle = findViewById<TextView>(R.id.page2_title)
        var txtType = findViewById<TextView>(R.id.page2_type)
        var txtUrl = findViewById<ImageView>(R.id.page2_url)

        var txtPledge = findViewById<TextView>(R.id.page2_pledge)
        var txtBackers = findViewById<TextView>(R.id.page2_backers)
        var txtsNo = findViewById<TextView>(R.id.page2_sNo)
        var txtTime = findViewById<TextView>(R.id.page2_time)
        var txtPercentage = findViewById<TextView>(R.id.page2_percentage)

        txtsNo.text = "Serial Number: " + sNo
        txtTime.text = "End Time: " + endTime
        txtPercentage.text = "Percentage: %" + percentageFunded
        txtBackers.text = numBackers
        txtPledge.text = amtPledge + " $"

        txtBlurb.text = "Blurb: " + blurb
        txtBy.text = by
        txtCurrency.text = "Currency: " + currency
        txtLocation.text = "Location: " + location
        txtState.text = "State: " + state
        txtTitle.text = title
        txtType.text = "Type: " + type
        txtUrl.tag = "https://www.kickstarter.com" +url

    }
    fun openBrowser(view: View) {

        //Get url from tag
        val url = view.getTag() as String

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)

        //pass the url to intent data
        intent.data = Uri.parse(url)

        startActivity(intent)
    }
}


