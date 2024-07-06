package eu.tutorials.musicstreamapp.adaptor


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.musicstreamapp.MyExoplayer
import eu.tutorials.musicstreamapp.PlayerActivity
import eu.tutorials.musicstreamapp.databinding.SectionSongListRecyclerRowBinding
import eu.tutorials.musicstreamapp.databinding.SongListItemRecyclerRowBinding
import eu.tutorials.musicstreamapp.models.songModel

class SectionSongListAdaptor (private val songIdList: List<String>):
    RecyclerView.Adapter<SectionSongListAdaptor.MyViewHolder>() {


    class MyViewHolder(private val binding : SectionSongListRecyclerRowBinding):
        RecyclerView.ViewHolder(binding.root){
        // bind the data with views
        fun bindData(songId: String ){
            FirebaseFirestore.getInstance().collection("Songs")
                .document(songId).get()
                .addOnSuccessListener {
                    val song = it.toObject(songModel::class.java)  // We will get the object of this song like title subtitle etc
                    song?.apply {
                        binding.songTitleTextView.text = title
                        binding.songSubtitleTextView.text = subtitle
                        Glide.with(binding.songCoverImageView).load(coverUrl) // cover url of song
                            .apply(
                                RequestOptions().transform(RoundedCorners(32)) // image be in rounded corner
                            )
                            .into(binding.songCoverImageView)
                        binding.root.setOnClickListener{
                            MyExoplayer.startPlaying(binding.root.context,song)
                            it.context.startActivity(Intent(it.context,PlayerActivity::class.java))
                        }
                    }
                }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {  // in this we will view from category item recycler row and pass it to viewholder
        val binding = SectionSongListRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songIdList.size   // from this we get the size of category list
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(songIdList[position]) // Position of bind data in categorylist
    }

}