package com.example.latihanretrofit1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var disposable: Disposable? = null
    private val wikiApiServices by lazy {
        WikiApiServices.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_search.setOnClickListener {
            if (edit_search.text.toString().isNotEmpty()){
                beginSearch(edit_search.text.toString())
            }else{
                Toast.makeText(this,
                    "Data tidak boleh kosong",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun beginSearch(searchString: String) {
        disposable = wikiApiServices.hitCountCheck(
            "query", "json", "search", searchString
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    txt_search_result.text =
                        "${result.query.searchinfo.totalhits} ditemukan"
                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                }
            )
    }
}
