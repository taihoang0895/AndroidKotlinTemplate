package com.example.androidkotlintemplate

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.androidkotlintemplate.arch.StockLiveData


class PageFragment : Fragment() {
    var data: MutableLiveData<String> = MutableLiveData<String>()
    var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*(activity as MainActivity).data.observe(viewLifecycleOwner, Observer {
            Log.d("taih ", "PageFragment data changed ${it}")
        })*/
        StockLiveData.get().observe(viewLifecycleOwner, Observer<String> {
            MyObserver()
        })


    }

    fun sum(a: Int, b: Int):Int {
        return a + b;
    }

    inner class MyObserver : Observer<String> {
        override fun onChanged(price: String?) {
            sum(1, 2);
            price?.let { Log.d("taih ", "PageFragment data changed ${price}") }
        }

    }


    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = PageFragment()

    }

    override fun onStop() {
        super.onStop()
        Log.d("taih", "onStop PageFragment position ${position}")
    }
}
