package uz.sanjar.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.sanjar.pagination.databinding.ItemDataBinding

class DataAdapter : RecyclerView.Adapter<DataAdapter.ViewHolder>() {
    public var COUNT = 20

    class ViewHolder(private val binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int) {
            binding.itemTxt.text = position.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemDataBinding =
            ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemDataBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return COUNT
    }

}