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
import hu.bme.aut.organizedgoods.adapter.IngredientAdapter
import hu.bme.aut.organizedgoods.data.Ingredient
import hu.bme.aut.organizedgoods.data.RecipeDatabase
import kotlinx.android.synthetic.main.content_ingredients.*
import kotlin.concurrent.thread

class IngredientsActivity: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer : DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: IngredientAdapter
    private lateinit var database: RecipeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_ingredients)
        val toolbar: Toolbar = findViewById(R.id.toolbar_ingredients)
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout_ingredients)
        val navigationView = findViewById<NavigationView>(R.id.nav_view_ingredients)
        navigationView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        database = Room.databaseBuilder(
            applicationContext,
            RecipeDatabase::class.java,
            "recipe-list"
        ).build()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = IngredientsRecyclerView
        adapter = IngredientAdapter()
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.ingredientDao().getAllIngredients()
            val itemsSum = sumIngredients(items)
            runOnUiThread {
                adapter.update(itemsSum)
            }
        }
    }

    private fun sumIngredients(items: List<Ingredient>): List<Ingredient>{
        val itemsSum = mutableListOf<Ingredient>()
        for(i in items){
            val index = isIngredientInList(i,itemsSum)
            if(index != -1){
                itemsSum[index].quantity += i.quantity
            } else {
                itemsSum.add(i)
            }
        }
        return itemsSum
    }

    private fun isIngredientInList(item: Ingredient, list: List<Ingredient>): Int{
        for(i in list){
            if(item.ingredientName == i.ingredientName && item.unit == i.unit){
                return list.indexOf(i)
            }
        }
        return -1
    }

    override fun onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_recipes -> {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                this.overridePendingTransition(0, 0)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

}