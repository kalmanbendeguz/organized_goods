package hu.bme.aut.organizedgoods.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Long?,
    var recipeParentId: Long?,
    val ingredientName: String,
    var quantity: Int,
    val unit: String
)