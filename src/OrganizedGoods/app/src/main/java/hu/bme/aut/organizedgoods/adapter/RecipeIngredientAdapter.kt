package hu.bme.aut.organizedgoods.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.organizedgoods.R
import hu.bme.aut.organizedgoods.data.Ingredient

class RecipeIngredientAdapter(private val listener: RecipeIngredientClickListener):
    RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientViewHolder>() {

    private val items = mutableListOf<Ingredient?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeIngredientViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.ingredient_registration_item, parent, false)
        return RecipeIngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecipeIngredientViewHolder, position: Int) {
        val item = items[position]
        holder.item = item
        if(item!!.ingredientName.isNotBlank() ){
            holder.ingredientNameEditText.setText(item.ingredientName)
            holder.ingredientQuantityEditText.setText(item.quantity.toString())
            holder.ingredientUnitEditText.setText(item.unit)
        } else {
            holder.ingredientNameEditText.setText("")
            holder.ingredientQuantityEditText.setText("")
            holder.ingredientUnitEditText.setText("")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Ingredient){
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun addEmptyItem() {
        val ingredient = Ingredient(
            ingredientId = null,
            recipeParentId = null,
            ingredientName = "",
            quantity = 0,
            unit = ""
        )
        items.add(ingredient)
        notifyItemInserted(items.size - 1)
    }

    fun removeIngredient(index: Int){
        val item = items[index]
        items.removeAt(index)
        listener.onItemRemoved(item!!)
        notifyItemRemoved(index)
    }

    interface RecipeIngredientClickListener {
        fun onItemChanged(item: Ingredient)
        fun onItemRemoved(item: Ingredient)
    }

    inner class RecipeIngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ingredientNameEditText: EditText = itemView.findViewById(R.id.IngredientItemNameEditText)
        val ingredientQuantityEditText: EditText = itemView.findViewById(R.id.IngredientQuantityEditText)
        val ingredientUnitEditText: EditText = itemView.findViewById(R.id.IngredientUnitEditText)
        private val ingredientRemoveButton: ImageButton = itemView.findViewById(R.id.RecipeRegistrationRemoveButton)

        var item: Ingredient? = null

        init {
            ingredientRemoveButton.setOnClickListener {
                removeIngredient(adapterPosition)
            }
        }
    }
}