package name.nepavel.foodlist.model

data class FoodItem(val name: String,
                    val protein: Float,
                    val fat: Float,
                    val carbs: Float,
                    val cal: Int,
                    val cellulose: Int?,
                    val category: String?,
                    val link: String? = null) {

}