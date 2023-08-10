package com.example.contacts

import androidx.recyclerview.widget.DiffUtil

class ContactDiffUtilCallback(
    private val oldList: List<Contact>,
    private val newList: List<Contact>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].contactName == newList[newItemPosition].contactName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].contactName == newList[newItemPosition].contactName &&
                oldList[oldItemPosition].phoneNumber == newList[newItemPosition].phoneNumber &&
                oldList[oldItemPosition].img == newList[newItemPosition].img
    }

}