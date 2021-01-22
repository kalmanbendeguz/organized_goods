package hu.bme.aut.organizedgoods.data

import androidx.room.*

@Dao
interface IngredientDao {
    @Transaction
    @Query("SELECT * FROM ingredients")
    fun getAllIngredients(): List<Ingredient>

    @Insert
    fun insertIngredient(ingredient: Ingredient): Long

    @Query("SELECT * FROM ingredients WHERE recipeParentId=:recipeId")
    fun getIngredientsByRecipeId(recipeId: Long): List<Ingredient>

    @Query("DELETE FROM ingredients WHERE recipeParentId=:recipeId")
    fun deleteIngredientsByRecipeId(recipeId : Long)
}