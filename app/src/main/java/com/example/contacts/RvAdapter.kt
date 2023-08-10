package com.example.contacts

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.example.contacts.databinding.SingleItemBinding
import kotlin.random.Random


class RvAdapter(private val context: Context, private var contacts: MutableList<Contact>) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: SingleItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SingleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rnd = Random.Default
        val color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200))
        val avatar = AvatarGenerator.AvatarBuilder(context)
            .setLabel(contacts[position].contactName)
            .setAvatarSize(80)
            .setTextSize(20)
            .toSquare()
            .toCircle()
            .setBackgroundColor(color)
            .build()
        with(holder) {

            binding.apply {
                with(contacts[position]) {
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

    fun setData(newLanguageList : MutableList<Contact>){
        val diffUtil = ContactDiffUtilCallback(contacts , newLanguageList)
        // it calculates the different items of the oldLanguageList and newLanguageList
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        // assign oldLanguageList to newLanguageList
        contacts = newLanguageList
        diffResult.dispatchUpdatesTo(this)
    }
    override fun getItemCount(): Int = contacts.size
}