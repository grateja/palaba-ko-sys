package com.csi.palabakosys.util

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.csi.palabakosys.R

open class FilterActivity : AppCompatActivity() {
    private var searchBar: SearchView? = null
    private lateinit var toolbar: Toolbar
    protected open lateinit var queryHint: String

//    var onQuery: ((keyword: String?) -> Unit) ? = null

    protected open fun onQuery(keyword: String?) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val hint = this.queryHint
        menuInflater.inflate(R.menu.menu_search, menu)
        searchBar = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchBar?.apply {
            maxWidth = Integer.MAX_VALUE
            queryHint = hint
            setOnQueryTextFocusChangeListener { _, b ->
                if(b) {
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.white))
                } else {
                    toolbar.setBackgroundColor(applicationContext.getColor(R.color.teal_700))
                }
            }
        }
        searchBar?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar?.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                onQuery(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }
}