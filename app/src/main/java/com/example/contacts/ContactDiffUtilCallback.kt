package com.example.contacts

import androidx.recyclerview.widget.DiffUtil

class ContactDiffUtilItemCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem.contactName == newItem.contactName

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
        oldItem == newItem
}