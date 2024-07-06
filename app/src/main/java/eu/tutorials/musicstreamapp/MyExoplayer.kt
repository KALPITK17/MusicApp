package eu.tutorials.musicstreamapp

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.google.firebase.firestore.FirebaseFirestore
import eu.tutorials.musicstreamapp.models.songModel


object MyExoplayer {

    private var exoPlayer : ExoPlayer? = null
    private var currentSong : songModel? = null

    fun getCurrentSong() : songModel?{
        return currentSong
    }



    fun getInstance() : ExoPlayer?{
        return exoPlayer
    }

    fun startPlaying(context : Context, song : songModel){
        if(exoPlayer==null)
            exoPlayer = ExoPlayer.Builder(context).build()

        if(currentSong!=song){
            //Its a new song so start playing
            currentSong = song
            updateCount()  // Calling for most viewed section
            currentSong?.Url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoPlayer?.setMediaItem(mediaItem)
                exoPlayer?.prepare()
                exoPlayer?.play()

            }
        }


    }
   fun updateCount(){  // Logic for Most Viewed Section
       currentSong?.id?.let { id->
           FirebaseFirestore.getInstance().collection("Songs")
               .document(id)
               .get().addOnSuccessListener {
                   var latestCount = it.getLong("count")
                   if(latestCount==null){
                       latestCount = 1L
                   }else{
                       latestCount = latestCount + 1

                   }

                   FirebaseFirestore.getInstance().collection("Songs")
                       .document(id)
                       .update(mapOf("count" to latestCount))
               }
       }
   }
}


