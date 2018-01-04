package net.codysehl.www.reader.Search.View

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup


abstract class ItemAdapter<I, V: View>(var items: List<I> = listOf()): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        fun <I, V: View>create(update: (V, I) -> Unit, viewConstructor: () -> V): ItemAdapter<I, V> {
            return object: ItemAdapter<I, V>() {

                override fun getItemCount(): Int = items.size

                override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
                    return object: RecyclerView.ViewHolder(viewConstructor()) {}
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    val item = items[position]
                    @Suppress("UNCHECKED_CAST")
                    update(holder.itemView as V, item)
                }

            }
        }
    }
}
