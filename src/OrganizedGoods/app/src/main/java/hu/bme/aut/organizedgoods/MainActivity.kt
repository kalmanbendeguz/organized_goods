package hu.bme.aut.organizedgoods

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.navigation.NavigationView
import hu.bme.aut.organizedgoods.adapter.RecipeAdapter
import hu.bme.aut.organizedgoods.data.Ingredient
import hu.bme.aut.organizedgoods.data.Recipe
import hu.bme.aut.organizedgoods.data.RecipeDatabase
import hu.bme.aut.organizedgoods.fragments.EditRecipeDialogFragment
import hu.bme.aut.organizedgoods.fragments.NewRecipeDialogFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), RecipeAdapter.RecipeClickListener,
    NewRecipeDialogFragment.NewRecipeDialogListener, EditRecipeDialogFragment.EditRecipeDialogListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer : DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private lateinit var database: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        database = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java,
            "recipe-list"
        ).build()

        fab.setOnClickListener {
            NewRecipeDialogFragment().show(
                supportFragmentManager,
                NewRecipeDialogFragment.TAG
            )
        }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter = RecipeAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.recipeDao().getAllRecipes()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: Recipe) {
        thread {
            database.recipeDao().update(item)
        }
    }

    override fun onItemRemoved(item: Recipe) {
        thread {
            database.recipeDao().deleteRecipe(item)
            database.ingredientDao().deleteIngredientsByRecipeId(item.recipeId!!)
        }
    }

    override fun onItemEdited(item: Recipe) {
        thread {
            database.ingredientDao().getIngredientsByRecipeId(item.recipeId!!)
            val ingredientsOfRecipe = database.ingredientDao().getIngredientsByRecipeId(item.recipeId)
            EditRecipeDialogFragment(item, ingredientsOfRecipe).show(
                supportFragmentManager,
                EditRecipeDialogFragment.TAG
            )
        }
    }

    override fun onRecipeCreated(newRecipe: Recipe, newIngredients: List<Ingredient>) {
        thread {
            val newId = database.recipeDao().insertRecipe(newRecipe)
            val newRecipeItem = newRecipe.copy(
                recipeId = newId
            )
            for(ni in newIngredients){
                ni.recipeParentId = newId
                database.ingredientDao().insertIngredient(ni)
            }
            runOnUiThread {
                adapter.addItem(newRecipeItem)
            }
        }
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onRecipeEdited(editedRecipe: Recipe, editedIngredients: List<Ingredient>) {
        thread{
            database.recipeDao().update(editedRecipe)
            database.ingredientDao().deleteIngredientsByRecipeId(editedRecipe.recipeId!!)
            for(ei in editedIngredients){
                ei.recipeParentId = editedRecipe.recipeId
                database.ingredientDao().insertIngredient(ei)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_ingredients -> {
                val ingredientsIntent = Intent(this, IngredientsActivity::class.java)
                startActivity(ingredientsIntent)
                this.overridePendingTransition(0, 0)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}