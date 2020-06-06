package com.example.mytask

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytask.databinding.DesignBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL
import java.util.ArrayList
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {


    lateinit var recyclerview: RecyclerView
    val movielist = ArrayList<MovieModel>();
    var url: String =
            "https://api.themoviedb.org/3/movie/popular?api_key=10e20b7ec85d4192b956608a704a3db9"
    var conn: ConnectivityManager? = null
    var info: NetworkInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview = findViewById(R.id.recyclerView)

        recyclerview.layoutManager = GridLayoutManager(this, 2)

        if (connectNetworkd()) {
            getCoroutines_fun()
        } else {
            Toast.makeText(
                    this,
                    "Network failed!, Please connect to Internet:-)",
                    Toast.LENGTH_LONG
            ).show();
        }


    }

    private fun connectNetworkd(): Boolean {

        conn = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (conn != null) {
            info = conn!!.activeNetworkInfo

            if (info != null) {
                if (info!!.state == NetworkInfo.State.CONNECTED) {
                    return true
                }
            }
        }
        return false


    }

    private fun getCoroutines_fun() {

        CoroutineScope(Dispatchers.IO).launch {
            getDataofmovie()
        }

    }

    suspend fun getDataofmovie() {

        val url = URL(url)
        val httpsURLConnection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        val inputStream: InputStream = httpsURLConnection.inputStream
        val text = inputStream.bufferedReader().use(BufferedReader::readText)
        withContext(Dispatchers.Main) {

            val root = JSONObject(text);
            val jarry = root.getJSONArray("results")
            for (i in 0..jarry.length() - 1) {

                val po = jarry.getJSONObject(i);

                val poster_path = po.getString("poster_path")
                val title = po.getString("title")
                val overview = po.getString("overview")
                val mrelease: String = po.getString("release_date")

                val ding = MovieModel(poster_path, title, overview, mrelease)

                movielist.add(ding)
            }
            val adapterclass = Movieadapter(this@MainActivity, movielist);
            recyclerview.adapter = adapterclass

        }

    }
}

class Movieadapter(mainActivity: MainActivity, movielist: ArrayList<MovieModel>) :
        RecyclerView.Adapter<Movieadapter.ViewHolder>() {

    val mainActivity = mainActivity
    val mvlist = movielist


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        /*val v : View = LayoutInflater.from(mainActivity).inflate(R.layout.design,parent,false)
        return ViewHolder(v)*/
        val inflater = LayoutInflater.from(parent.context)
        val binding = DesignBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mvlist.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /* holder.titletext.text=mvlist.get(position).m_title
         Picasso.get().load("https://image.tmdb.org/t/p/w500/"+mvlist.get(position).m_poster).placeholder(R.drawable.download).into(holder.imgmve)*/
        holder.bind(mvlist[position])
        holder.itemView.setOnClickListener {

            val intent = Intent(mainActivity, Main2Activity::class.java)
            //intent.putStringArrayListExtra("datamve",mvlist[position])\
            intent.putExtra("i", mvlist[position].m_poster)
            intent.putExtra("t", mvlist[position].m_title)
            intent.putExtra("r", mvlist[position].m_res)
            intent.putExtra("d", mvlist[position].m_des)

            mainActivity.startActivity(intent)


        }
    }

    class ViewHolder(val binding: DesignBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MovieModel) {
            binding.movievar = item
            Log.d("asdf", item.m_poster)
            binding.executePendingBindings()
        }


    }

}

