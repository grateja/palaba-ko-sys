package com.csi.palabakosys.util

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import com.csi.palabakosys.R

abstract class FilterActivity : AppCompatActivity(), FilterActivityInterface {
    protected var searchBar: SearchView? = null
    private lateinit var toolbar: Toolbar
    protected lateinit var addEditLauncher: ActivityLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        addEditLauncher = ActivityLauncher(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)

        searchBar = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        searchBar?.apply {
            maxWidth = Integer.MAX_VALUE
            queryHint = filterHint
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

        menu.findItem(R.id.menu_advanced_option).apply {
            setOnMenuItemClickListener {
                onAdvancedSearchClicked()
            }
            isVisible = enableAdvancedSearch
        }

        return super.onCreateOptionsMenu(menu)
    }
}