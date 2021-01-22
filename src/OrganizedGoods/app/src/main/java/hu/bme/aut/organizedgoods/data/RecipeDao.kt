package hu.bme.aut.organizedgoods.data

import androidx.room.*

@Dao
interface RecipeDao {
    @Transaction
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): List<Recipe>

    @Insert
    fun insertRecipe(recipe: Recipe): Long

    @Update
    fun update(recipe: Recipe)

    @Delete
    fun deleteRecipe(recipe: Recipe)

}