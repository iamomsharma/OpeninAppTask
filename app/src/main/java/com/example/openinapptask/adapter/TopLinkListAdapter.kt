package com.example.openinapptask.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.openinapptask.databinding.AdapterItemOpeninLinksBinding
import com.example.openinapptask.model.DashboardModel
import com.squareup.picasso.Picasso


class TopLinkListAdapter(val mLinkArrayList: ArrayList<DashboardModel.Data.TopLink>) :
    RecyclerView.Adapter<TopLinkListAdapter.MyViewHolder>() {

    private lateinit var binding: AdapterItemOpeninLinksBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding =
            AdapterItemOpeninLinksBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val links = mLinkArrayList[position]
        holder.bind(links)
    }

    override fun getItemCount(): Int {
        return 4
    }

    inner class MyViewHolder(private val binding: AdapterItemOpeninLinksBinding) :
        ViewHolder(binding.root) {

        fun bind(linkArrayList: DashboardModel.Data.TopLink) {
            Picasso.get()
                .load(linkArrayList.original_image)
                .into(binding.ivProfile)

            binding.tvLink.text = linkArrayList.web_link
        }

    }

}


