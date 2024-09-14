package com.foodscanner.app.firebase

import com.foodscanner.app.model.FoodItem
import com.google.firebase.database.*

class FirebaseHelper {

    private val database = FirebaseDatabase.getInstance().reference

    // Function to add food item to Firebase
    fun addFoodItem(userId: String, foodItem: FoodItem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child("Users").child(userId).child("foodItems").push().setValue(foodItem)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Function to get food items from Firebase
    fun getFoodItems(userId: String, callback: (List<FoodItem>) -> Unit, onError: (DatabaseError) -> Unit) {
        val userFoodRef = database.child("Users").child(userId).child("foodItems")
        userFoodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val foodItems = mutableListOf<FoodItem>()
                for (itemSnapshot in snapshot.children) {
                    val foodItem = itemSnapshot.getValue(FoodItem::class.java)
                    foodItem?.let { foodItems.add(it) }
                }
                callback(foodItems)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error)
            }
        })
    }

    // Function to update an existing food item
    fun updateFoodItem(userId: String, itemId: String, updatedFoodItem: FoodItem, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child("Users").child(userId).child("foodItems").child(itemId).setValue(updatedFoodItem)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Function to delete a food item from Firebase
    fun deleteFoodItem(userId: String, itemId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        database.child("Users").child(userId).child("foodItems").child(itemId).removeValue()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
