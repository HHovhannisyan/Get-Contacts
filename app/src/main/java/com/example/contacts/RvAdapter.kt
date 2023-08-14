package com.example.contacts

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.example.contacts.databinding.SingleItemBinding
import kotlin.random.Random


class RvAdapter(
    private val context: Context, ) : ListAdapter<Contact, RvAdapter.MyViewHolder>(
        ContactDiffUtilItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding,context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.bind(getItem(position))
    }

    class MyViewHolder(
        private val viewBinding: SingleItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(itemData: Contact) = with(viewBinding) {
            val rnd = Random.Default
            val color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200))
            val avatar = AvatarGenerator.AvatarBuilder(context)
                .setLabel(itemData.contactName)
                .setAvatarSize(80)
                .setTextSize(20)
                .toSquare()
                .toCircle()
                .setBackgroundColor(color)
                .build()

            viewBinding.apply {
                with(itemData) {
                    tvContactName.text = this.contactName
                    tvPhoneNumber.text = this.phoneNumber
                    if (this.img != null) {
                        ivContactImg.setImageBitmap(this.img)
                    } else {
                        ivContactImg.setImageDrawable(avatar)
                    }
                }
            }
        }
    }
}