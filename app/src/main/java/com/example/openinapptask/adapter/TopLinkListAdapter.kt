package com.example.openinapptask.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.openinapptask.R
import com.example.openinapptask.databinding.AdapterItemOpeninLinksBinding
import com.example.openinapptask.model.DashboardModel
import java.text.SimpleDateFormat
import java.util.Locale

class TopLinkListAdapter(
    private val mLinkArrayList: ArrayList<DashboardModel.Data.TopLink>,
    private val isCallFromDashboard: Boolean,
    private val clipboardManager: ClipboardManager
) : RecyclerView.Adapter<TopLinkListAdapter.MyViewHolder>() {

    private lateinit var binding: AdapterItemOpeninLinksBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = AdapterItemOpeninLinksBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val links = mLinkArrayList[position]
        holder.bind(links, holder)
    }

    override fun getItemCount(): Int {
        // if Adapter call from dashboard then show 4 data otherwise
        if (isCallFromDashboard) {
            return 4
        }
        return mLinkArrayList.size
    }

    inner class MyViewHolder(private val binding: AdapterItemOpeninLinksBinding) :
        ViewHolder(binding.root) {

        fun bind(linkArrayList: DashboardModel.Data.TopLink, holder: MyViewHolder) {

         /*
          I used the original_image because the
          image is not showing in the thumbnail
          */

            if (linkArrayList.original_image.isEmpty()) {
                Glide.with(holder.itemView)
                    .load(R.mipmap.dummy_image)
                    .into(binding.ivProfile)

            } else {
                Glide.with(holder.itemView)
                    .load(linkArrayList.original_image)
                    .into(binding.ivProfile)

            }

            binding.tvLink.text = linkArrayList.web_link
            binding.tvLinkTitle.text = linkArrayList.title
            binding.tvClickCount.text = linkArrayList.total_clicks.toString()

            // Set Date
            val convertDate = dateFormat(linkArrayList.created_at)
            binding.tvDate.text = convertDate

            // Click for Copy Link
            binding.ivCopy.setOnClickListener {
                clipboardManager.setPrimaryClip(ClipData.newPlainText("Openin App - ", binding.tvLink.text))
            }

        }

        private fun dateFormat(createdAt: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(createdAt)
            val formattedDate = outputFormat.format(date)
            return formattedDate
        }

    }

}


