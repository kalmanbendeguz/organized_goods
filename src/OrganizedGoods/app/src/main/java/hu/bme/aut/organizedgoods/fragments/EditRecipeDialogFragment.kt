package hu.bme.aut.organizedgoods.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.organizedgoods.R
import hu.bme.aut.organizedgoods.adapter.RecipeIngredientAdapter
import hu.bme.aut.organizedgoods.data.Ingredient
import hu.bme.aut.organizedgoods.data.Recipe
import kotlinx.android.synthetic.main.ingredient_registration_item.view.*
import kotlin.concurrent.thread


class EditRecipeDialogFragment(recipe: Recipe, ingredientsOfRecipe: List<Ingredient>) : DialogFragment(),
    View.OnClickListener, RecipeIngredientAdapter.RecipeIngredientClickListener {

    private lateinit var recipeNameEditText: EditText
    private lateinit var addNewIngredientButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeIngredientAdapter
    private lateinit var contentView: View
    private lateinit var listener: EditRecipeDialogListener
    private var currentRecipe: Recipe = recipe
    private var ingredients = ingredientsOfRecipe.toMutableList()

    interface EditRecipeDialogListener {
        fun onRecipeEdited(editedRecipe: Recipe, editedIngredients: List<Ingredient>)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? EditRecipeDialogListener
            ?: throw RuntimeException("Activity must implement the EditRecipeDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Edit Recipe")
            .setView(getContentView())
            .setPositiveButton("OK"){ _, _ ->
                if (isValid()){
                    listener.onRecipeEdited(getRecipe(), getIngredients())
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    private fun initRecyclerView() {
        recyclerView = contentView.findViewById(R.id.DialogRecyclerView)
        adapter = RecipeIngredientAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.adapter = adapter
    }

    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.edit_recipe_dialog, null)

        recipeNameEditText = contentView.findViewById(R.id.RecipeNameEditText)
        addNewIngredientButton = contentView.findViewById(R.id.AddNewIngredientButton)
        addNewIngredientButton.setOnClickListener(this)

        this.contentView = contentView
        initRecyclerView()
        return contentView
    }

    private fun loadItemsInBackground() {
        thread {
            recipeNameEditText.setText(currentRecipe.recipeName)
            for(item in ingredients){
                adapter.addItem(item)
            }
        }
    }

    private fun isValid() : Boolean {
        return true
    }

    private fun getRecipe() : Recipe {
        currentRecipe.recipeName = recipeNameEditText.text.toString()
        return currentRecipe
    }

    private fun getIngredients(): List<Ingredient>{
        val ingredientsList = mutableListOf<Ingredient>()

        for(i in 0 until adapter.itemCount){
            if(recyclerView[i].IngredientItemNameEditText.text.toString().isNotBlank() && recyclerView[i].IngredientUnitEditText.text.toString().isNotBlank()){
                val ingredientItem = Ingredient(
                    ingredientId = null,
                    recipeParentId = null,
                    ingredientName = recyclerView[i].IngredientItemNameEditText.text.toString(),
                    quantity = recyclerView[i].IngredientQuantityEditText.text.toString().toInt(),
                    unit = recyclerView[i].IngredientUnitEditText.text.toString()
                )
                ingredientsList.add(ingredientItem)
            }
        }
        return ingredientsList
    }

    companion object {
        const val TAG = "EditRecipeDialogFragment"
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.AddNewIngredientButton -> {
                adapter.addEmptyItem()
            }
        }
    }

    override fun onItemChanged(item: Ingredient) {}
    override fun onItemRemoved(item: Ingredient) {}
}