package name.nepavel.foodlist.model

data class FoodItem(val name: String,
                    val prot: Float,
                    val fat: Float,
                    val carb: Float,
                    val cal: Int,
                    val cell: Int?,
                    val category: String?,
                    val link: String? = null) {

}