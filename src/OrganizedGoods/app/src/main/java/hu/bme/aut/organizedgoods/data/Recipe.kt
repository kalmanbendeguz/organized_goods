package hu.bme.aut.organizedgoods.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long?,
    var recipeName: String
)