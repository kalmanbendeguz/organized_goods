package hu.bme.aut.organizedgoods.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.organizedgoods.R
import hu.bme.aut.organizedgoods.data.Ingredient

class IngredientAdapter : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {
    private val items = mutableListOf<Ingredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.ingredient_list_item, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.ingredientName
        holder.iconImageView.setImageResource(R.drawable.ingredient_icon)
        val quantityAndUnit = "${item.quantity} ${item.unit}"
        holder.quantityTextView.text = quantityAndUnit
        holder.item = item
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(ingredients: List<Ingredient>) {
        items.clear()
        items.addAll(ingredients)
        notifyDataSetChanged()
    }

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val iconImageView: ImageView = itemView.findViewById(R.id.IngredientIconImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.IngredientNameTextView)
        val quantityTextView: TextView = itemView.findViewById(R.id.IngredientQuantityTextView)

        var item: Ingredient? = null

        init {
            iconImageView.setImageResource(R.drawable.ingredient_icon)
        }
    }
}