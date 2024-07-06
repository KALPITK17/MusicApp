package eu.tutorials.musicstreamapp

import eu.tutorials.musicstreamapp.adaptor.SongsListAdaptor
import eu.tutorials.musicstreamapp.databinding.ActivitySongsListBinding
import eu.tutorials.musicstreamapp.models.CategoryModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class SongsListActivity : AppCompatActivity() {

    companion object{
        lateinit var category : CategoryModel
    }

    lateinit var  binding: ActivitySongsListBinding
    lateinit var songsListAdaptor: SongsListAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameTextView.text  = category.name
        Glide.with(binding.coverImageView).load(category.coverUrl)
            .apply(
                RequestOptions().transform(RoundedCorners(32))
            )
            .into(binding.coverImageView)

// change
        setupSongsListRecyclerView()
    }

    fun setupSongsListRecyclerView(){
        songsListAdaptor = SongsListAdaptor(category.songs)
        binding.songsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsListRecyclerView.adapter = songsListAdaptor
    }

}