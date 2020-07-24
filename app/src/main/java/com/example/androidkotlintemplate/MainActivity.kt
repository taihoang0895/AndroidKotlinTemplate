package com.example.androidkotlintemplate

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.example.androidkotlintemplate.arch.BaseActivity
import com.example.androidkotlintemplate.arch.ConfirmDialog
import com.example.androidkotlintemplate.arch.MyObserver

class MainActivity : BaseActivity() {
    lateinit var fragment1: PageFragment
    lateinit var fragment2: PageFragment

    init {
        lifecycle.addObserver(MyObserver())

    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main;
    }

    override fun bindEvents() {

    }

    override fun findViewById() {

    }

    override fun onPostCreate() {
        super.onPostCreate()
    }

    var data: MutableLiveData<String> = MutableLiveData<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /* val pageAdapter = PageAdapter(supportFragmentManager);
         val viewPage:ViewPager = findViewById(R.id.view_pager)
         viewPage.adapter = pageAdapter*/
        val btn_change_data: Button = findViewById(R.id.btn_change_data)
        btn_change_data.setOnClickListener({ view ->
            data.value = "data changed"
        })

        fragment1 = PageFragment.newInstance()
        fragment1.position = 1

        var transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment1)
        transaction.commit()
        /* data.observe(fragment1, Observer {name ->
            Log.d("taih", "data changed ${name}")
        })*/
        btn_change_data.postDelayed({
            transaction = supportFragmentManager.beginTransaction()
            fragment2 = PageFragment.newInstance()
            fragment2.position = 2
            transaction.add(R.id.container, fragment2)
            transaction.addToBackStack(null)
            transaction.commit()
            /* data.observe(fragment2, Observer {name ->
                 Log.d("taih", "data changed ${name}")
             })*/
        }, 1000)

    }
}
