package eu.tutorials.musicstreamapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import eu.tutorials.musicstreamapp.SongsListActivity.Companion.category
import eu.tutorials.musicstreamapp.adaptor.CategoryAdaptor
import eu.tutorials.musicstreamapp.adaptor.SectionSongListAdaptor
import eu.tutorials.musicstreamapp.databinding.ActivityMainBinding
import eu.tutorials.musicstreamapp.models.CategoryModel
import eu.tutorials.musicstreamapp.models.songModel


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var categoryAdapter: CategoryAdaptor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setupSection("section_1",binding.section1MainLayout,binding.section1Title,binding.section1RecyclerView)
        setupSection("section_2",binding.section2MainLayout,binding.section2Title,binding.section2RecyclerView)
        setupSection("section_3",binding.section3MainLayout,binding.section3Title,binding.section3RecyclerView)
        setupMostlyPlayed("mostly_played",binding.mostlyPlayedMainLayout,binding.mostlyPlayedTitle,binding.mostlyPlayedRecyclerView)


        binding.optionBtn.setOnClickListener {
           showPopupMenu()
        }
    }

     fun showPopupMenu(){
          val popupMenu = PopupMenu(this,binding.optionBtn)
         val inflator = popupMenu.menuInflater
         inflator.inflate(R.menu.option_menu,popupMenu.menu)
         popupMenu.show()
         popupMenu.setOnMenuItemClickListener {
             when(it.itemId){
                 R.id.logout -> { // when we will click logout on menu it will identify that logout is clicked
                     logout()
                     true
                 }
             }
             false
         }
     }


fun logout(){
    MyExoplayer.getInstance()?.release() // when we click on logout it stop the exoplayer
    FirebaseAuth.getInstance().signOut()
    startActivity(Intent(this,LoginActivity::class.java))
}

    override fun onResume() {
        super.onResume() // This override function is used to show player view when song is playing
        showPlayerView()
    }

    fun showPlayerView(){ // When is played in exoplayer then shown on player view
       binding.playerView.setOnClickListener{ // When clickon player view it goes on where it is playing
           startActivity(Intent(this,PlayerActivity::class.java))
       }

        MyExoplayer.getCurrentSong()?.let {
            // When music is playing in exoplayer the current song
            binding.playerView.visibility = View.VISIBLE
           binding.songTitleTextView.text = "Now Playing : " + it.title
            Glide.with(binding.songCoverImageView).load(it.coverUrl) // cover url of song
                .apply(
                    RequestOptions().transform(RoundedCorners(32)) // image be in rounded corner
                )
                .into(binding.songCoverImageView)
        }?: run { // When music is not playing on exo player
            binding.playerView.visibility = View.GONE
        }
    }

// CATEGORIES
    fun getCategories(){
        FirebaseFirestore.getInstance().collection("category")
            .get().addOnSuccessListener {
                val categoryList = it.toObjects(CategoryModel::class.java)
                setupCategoryRecyclerView(categoryList)
            }
    }

    fun setupCategoryRecyclerView(categoryList : List<CategoryModel>){
        categoryAdapter = CategoryAdaptor(categoryList)
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

// SECTIONS

    fun setupSection(id : String,mainLayout: RelativeLayout,titleView: TextView,recyclerView: RecyclerView){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val section = it.toObject(CategoryModel::class.java)  // As it it have same component as data class of categorymodel
                section?.apply {
                    mainLayout.visibility = View.VISIBLE
                    titleView.text = name
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                   recyclerView.adapter = SectionSongListAdaptor(songs)
                    mainLayout.setOnClickListener {
                        SongsListActivity.category = section
                       startActivity(Intent(this@MainActivity,SongsListActivity::class.java)) // When we click on category it go on songslistactivity
                    }
                }
            }
    }


    fun setupMostlyPlayed(id : String,mainLayout: RelativeLayout,titleView: TextView,recyclerView: RecyclerView){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {



                // get most played song
                FirebaseFirestore.getInstance().collection("Songs")
                    .orderBy("count",Query.Direction.DESCENDING)
                    .limit(10)
                    .get().addOnSuccessListener { songsListSnapshot ->
                        val songsModelList = songsListSnapshot.toObjects<songModel>()
                        val songsIdList = songsModelList.map {
                            it.id
                        }.toList()
                        val section = it.toObject(CategoryModel::class.java)  // As it it have same component as data class of categorymodel
                        section?.apply {
                            section.songs = songsIdList
                            mainLayout.visibility = View.VISIBLE
                            titleView.text = name
                            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                            recyclerView.adapter = SectionSongListAdaptor(songs)
                            mainLayout.setOnClickListener {
                                SongsListActivity.category = section
                                startActivity(Intent(this@MainActivity,SongsListActivity::class.java)) // When we click on category it go on songslistactivity
                            }
                        }
                    }


            }
    }


}