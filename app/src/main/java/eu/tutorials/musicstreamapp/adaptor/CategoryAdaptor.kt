package eu.tutorials.musicstreamapp.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import eu.tutorials.musicstreamapp.SongsListActivity
import eu.tutorials.musicstreamapp.databinding.CategoryItemRecyclerRowBinding
import eu.tutorials.musicstreamapp.models.CategoryModel

class CategoryAdaptor(private val categoryList: List<CategoryModel>):
    RecyclerView.Adapter<CategoryAdaptor.MyViewHolder>() {


    class MyViewHolder(private val binding :CategoryItemRecyclerRowBinding):
          RecyclerView.ViewHolder(binding.root){
              // bind the data with views
              fun bindData(category:CategoryModel){  // Creating method  for geting position
            binding.nameTextView.text = category.name  // Name of song
             Glide.with(binding.coverImageView).load(category.coverUrl) // cover url of song
                 .apply(
                     RequestOptions().transform(RoundedCorners(32)) // image be in rounded corner
                 )
                 .into(binding.coverImageView)


                  //Start song list Activity
                 val context = binding.root.context
                  binding.root.setOnClickListener{
                      SongsListActivity.category = category
                      context.startActivity(Intent(context,SongsListActivity::class.java)) // When we click on category it go on songslstactivity
                  }
              }
          }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {  // in this we will view from category item recycler row and pass it to viewholder
        val binding = CategoryItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size   // from this we get the size of category list
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       holder.bindData(categoryList[position]) // Position of bind data in categorylist
    }

}
