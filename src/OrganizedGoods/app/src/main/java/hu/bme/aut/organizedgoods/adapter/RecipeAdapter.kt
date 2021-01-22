package hu.bme.aut.organizedgoods.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.organizedgoods.R
import hu.bme.aut.organizedgoods.data.Recipe

class RecipeAdapter(private val listener: RecipeClickListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val items = mutableListOf<Recipe>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recipe_list_item, parent, false)
        return RecipeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.recipeName
        holder.item = item
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Recipe) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun removeRecipe(index: Int){
        val item = items[index]
        items.removeAt(index)
        listener.onItemRemoved(item)
        notifyItemRemoved(index)
    }

    fun update(recipes: List<Recipe>) {
        items.clear()
        items.addAll(recipes)
        notifyDataSetChanged()
    }

    interface RecipeClickListener {
        fun onItemChanged(item: Recipe)
        fun onItemRemoved(item: Recipe)
        fun onItemEdited(item: Recipe)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val iconImageView: ImageView = itemView.findViewById(R.id.RecipeIconImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.RecipeNameTextView)
        private val editButton: ImageButton = itemView.findViewById(R.id.RecipeItemEditButton)
        private val removeButton: ImageButton = itemView.findViewById(R.id.RecipeItemRemoveButton)

        var item: Recipe? = null

        init {
            iconImageView.setImageResource(R.drawable.recipe_icon)
            editButton.setImageResource(R.drawable.edit_icon)
            removeButton.setImageResource(R.drawable.delete_icon)

            removeButton.setOnClickListener {
                removeRecipe(adapterPosition)
            }

            editButton.setOnClickListener {
                listener.onItemEdited(item!!)
            }
        }
    }
}