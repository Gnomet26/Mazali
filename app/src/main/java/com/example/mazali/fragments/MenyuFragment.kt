package com.example.mazali.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.adapters.MenuAdapter
import com.example.mazali.data.FoodItem

class MenyuFragment : Fragment() {
    lateinit var view__: View

    private lateinit var menuAdapter: MenuAdapter
    private lateinit var recyclerMenu: RecyclerView
    private lateinit var categoryContainer: LinearLayout
    private lateinit var editSearch: EditText
    private var currentCategory = "Barchasi"
    private var currentQuery = ""
    val foodList = listOf(
        FoodItem(
            name = "Sinfdosh Combo 1",
            price = "25 000 so'm",
            imageResId = R.drawable.img1,
            category = "Combo",
            description = "Issiq va xushbo'y xot-dog, qarsildoq kartoshka fri, shirali naggetslar 2 ta va bir stakan soflantiruvchi Pepsi (0,3 l). Har bir maktab o'quvchisini xursand qiladigan kombo."
        ),FoodItem(
            name = "Sinfdosh Combo 2",
            price = "27 000 so'm",
            imageResId = R.drawable.img2,
            category = "Combo",
            description = "Issiq va xushbo'y xot-dog, qarsildoq kartoshka fri, shirali naggetslar 2 ta va bir stakan soflantiruvchi Pepsi (0,3 l). Har bir maktab o'quvchisini xursand qiladigan kombo."
        ),FoodItem(
            name = "Lavash Time Combo 1",
            price = "25 000 so'm",
            imageResId = R.drawable.img3,
            category = "Combo",
            description = "Tovuqli ishtaha ochar mini lavash, qarsildoq mini kartoshka fri, xushbo'y ketchup va bir stakan soflantiruvchi Pepsi (0,3 l). Klassikani haqiqiy qadrlovchilari uchun!"
        ),FoodItem(
            name = "Lavash Time Combo 2",
            price = "25 000 so'm",
            imageResId = R.drawable.img4,
            category = "Combo",
            description = "Tovuqli ishtaha ochar mini lavash, qarsildoq mini kartoshka fri, xushbo'y ketchup va bir stakan soflantiruvchi Pepsi (0,3 l). Klassikani haqiqiy qadrlovchilari uchun!"
        ),FoodItem(
            name = "Gap yo‘q Combo 1",
            price = "56 000 so'm",
            imageResId = R.drawable.img5,
            category = "Combo",
            description = "Ishtaha ochar mol go'shti va yangi sabzavotlar qo'shilgan katta shaurma, shirali naggetslar 2 ta, tilla rang mini kartoshka fri, ketchup va bir stakan Pepsi (0,4 l). Ta'miga gap yo'q!"
        ),FoodItem(
            name = "Burger Set",
            price = "25 000 so'm",
            imageResId = R.drawable.img6,
            category = "Combo",
            description = "Ishtaha ochar mol go'shti va yangi sabzavotlar qo'shilgan katta shaurma, shirali naggetslar 2 ta, tilla rang mini kartoshka fri, ketchup va bir stakan Pepsi (0,4 l). Ta'miga gap yo'q!"
        ),FoodItem(
            name = "Lavash Time Combo 3",
            price = "25 000 so'm",
            imageResId = R.drawable.img7,
            category = "Combo",
            description = "Tovuqli ishtaha ochar mini lavash, qarsildoq mini kartoshka fri, xushbo'y ketchup va bir stakan soflantiruvchi Pepsi (0,3 l). Klassikani haqiqiy qadrlovchilari uchun!"
        ),FoodItem(
            name = "Lavash Time Combo 4",
            price = "25 000 so'm",
            imageResId = R.drawable.img8,
            category = "Combo",
            description = "Tovuqli ishtaha ochar mini lavash, qarsildoq mini kartoshka fri, xushbo'y ketchup va bir stakan soflantiruvchi Pepsi (0,3 l). Klassikani haqiqiy qadrlovchilari uchun!"
        ),FoodItem(
            name = "Burger Set",
            price = "35 000 so'm",
            imageResId = R.drawable.img9,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Burger Set",
            price = "35 000 so'm",
            imageResId = R.drawable.img10,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Burger Set",
            price = "35 000 so'm",
            imageResId = R.drawable.img11,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Burger Set",
            price = "35 000 so'm",
            imageResId = R.drawable.img12,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Burger Set",
            price = "35 000 so'm",
            imageResId = R.drawable.img13,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Burger Set",
            price = "35 000 so'm",
            imageResId = R.drawable.img14,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Burger Set",
            price = "35 000 so'm",
            imageResId = R.drawable.img15,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Tovuq go`shtidan katta lavash",
            price = "35 000 so'm",
            imageResId = R.drawable.img16,
            category = "Lavash",
            description = "Mayin tovuq bo‘laklari, yangi pomidorlar, qarsildoq karam va xushbo‘y sarimsoqli sous bilan uyg‘unlashib, har qanday tushlik uchun eng maqbul tanlov hisoblanadi."
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "32 000 so'm",
            imageResId = R.drawable.img17,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "32 000 so'm",
            imageResId = R.drawable.img18,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "35 000 so'm",
            imageResId = R.drawable.img19,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "32 000 so'm",
            imageResId = R.drawable.img20,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "32 000 so'm",
            imageResId = R.drawable.img21,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "32 000 so'm",
            imageResId = R.drawable.img22,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "32 000 so'm",
            imageResId = R.drawable.img23,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Tovuq go'shtidan katta shaurma",
            price = "32 000 so'm",
            imageResId = R.drawable.img24,
            category = "Shaurma",
            description = "Qizarib pishgan tovuq go'shti-grill, yangi bodring va shirali pomidor bo'laklari, kunjut urug'lari sepilgan, yarim doira shaklli, "
        ),FoodItem(
            name = "Dablburger",
            price = "55 000 so'm",
            imageResId = R.drawable.img25,
            category = "Burgerlar",
            description = "Yumshoq va dumaloq bulochkadagi xushboy Grill sousi ostida mol go'shtidan ikkita shirali kotlet, yangi yetilib pishgan pomidor va marinadlangan bodringning dumaloq bo'lakchalari"
        ),FoodItem(
            name = "Dablburger",
            price = "55 000 so'm",
            imageResId = R.drawable.img26,
            category = "Burgerlar",
            description = "Yumshoq va dumaloq bulochkadagi xushboy Grill sousi ostida mol go'shtidan ikkita shirali kotlet, yangi yetilib pishgan pomidor va marinadlangan bodringning dumaloq bo'lakchalari"
        ),FoodItem(
            name = "Dablburger",
            price = "25 000 so'm",
            imageResId = R.drawable.img27,
            category = "Burgerlar",
            description = "Yumshoq va dumaloq bulochkadagi xushboy Grill sousi ostida mol go'shtidan ikkita shirali kotlet, yangi yetilib pishgan pomidor va marinadlangan bodringning dumaloq bo'lakchalari"
        ),FoodItem(
            name = "Dablburger",
            price = "50 000 so'm",
            imageResId = R.drawable.img28,
            category = "Burgerlar",
            description = "Yumshoq va dumaloq bulochkadagi xushboy Grill sousi ostida mol go'shtidan ikkita shirali kotlet, yangi yetilib pishgan pomidor va marinadlangan bodringning dumaloq bo'lakchalari"
        ),FoodItem(
            name = "Dablburger",
            price = "45 000 so'm",
            imageResId = R.drawable.img29,
            category = "Burgerlar",
            description = "Yumshoq va dumaloq bulochkadagi xushboy Grill sousi ostida mol go'shtidan ikkita shirali kotlet, yangi yetilib pishgan pomidor va marinadlangan bodringning dumaloq bo'lakchalari"
        ),FoodItem(
            name = "Dablburger",
            price = "45 000 so'm",
            imageResId = R.drawable.img30,
            category = "Burgerlar",
            description = "Yumshoq va dumaloq bulochkadagi xushboy Grill sousi ostida mol go'shtidan ikkita shirali kotlet, yangi yetilib pishgan pomidor va marinadlangan bodringning dumaloq bo'lakchalari"
        ),FoodItem(
            name = "Dablburger",
            price = "50 000 so'm",
            imageResId = R.drawable.img31,
            category = "Burgerlar",
            description = "Yumshoq va dumaloq bulochkadagi xushboy Grill sousi ostida mol go'shtidan ikkita shirali kotlet, yangi yetilib pishgan pomidor va marinadlangan bodringning dumaloq bo'lakchalari"
        ),FoodItem(
            name = "Donar mol go'shtidan",
            price = "56 000 so'm",
            imageResId = R.drawable.img32,
            category = "Taom",
            description = "Kombinatsiyali idishda shirali gril mol go'shti bo'lakchalari, tillarang kartoshka-fri, miks-guruch, tabiiy, o'zimizda tayyorlangan salat"
        ),FoodItem(
            name = "Donar mol go'shtidan",
            price = "55 000 so'm",
            imageResId = R.drawable.img33,
            category = "Taom",
            description = "Kombinatsiyali idishda shirali gril mol go'shti bo'lakchalari, tillarang kartoshka-fri, miks-guruch, tabiiy, o'zimizda tayyorlangan salat"
        ),FoodItem(
            name = "Donar mol go'shtidan",
            price = "56 000 so'm",
            imageResId = R.drawable.img34,
            category = "Taom",
            description = "Kombinatsiyali idishda shirali gril mol go'shti bo'lakchalari, tillarang kartoshka-fri, miks-guruch, tabiiy, o'zimizda tayyorlangan salat"
        ),FoodItem(
            name = "Donar mol go'shtidan",
            price = "55 000 so'm",
            imageResId = R.drawable.img35,
            category = "Taom",
            description = "Kombinatsiyali idishda shirali gril mol go'shti bo'lakchalari, tillarang kartoshka-fri, miks-guruch, tabiiy, o'zimizda tayyorlangan salat"
        ),FoodItem(
            name = "Hot-Dog Dabl",
            price = "25 000 so'm",
            imageResId = R.drawable.img36,
            category = "Xot Dog",
            description = "Ikkita ishtahaochar sosiska, yangi pomidor va marinadlangan karsildoq bodring bo'lakchalari, \"Aysberg\" salat bargi,"
        ),FoodItem(
            name = "Hot-Dog Dabl",
            price = "25 000 so'm",
            imageResId = R.drawable.img37,
            category = "Xot Dog",
            description = "Ikkita ishtahaochar sosiska, yangi pomidor va marinadlangan karsildoq bodring bo'lakchalari, \"Aysberg\" salat bargi,"
        ),FoodItem(
            name = "Hot-Dog Dabl",
            price = "25 000 so'm",
            imageResId = R.drawable.img38,
            category = "Xot Dog",
            description = "Ikkita ishtahaochar sosiska, yangi pomidor va marinadlangan karsildoq bodring bo'lakchalari, \"Aysberg\" salat bargi,"
        ),FoodItem(
            name = "Hot-Dog Dabl",
            price = "30 000 so'm",
            imageResId = R.drawable.img39,
            category = "Xot Dog",
            description = "Ikkita ishtahaochar sosiska, yangi pomidor va marinadlangan karsildoq bodring bo'lakchalari, \"Aysberg\" salat bargi,"
        ),FoodItem(
            name = "Hot-Dog Dabl",
            price = "26 000 so'm",
            imageResId = R.drawable.img40,
            category = "Xot Dog",
            description = "Ikkita ishtahaochar sosiska, yangi pomidor va marinadlangan karsildoq bodring bo'lakchalari, \"Aysberg\" salat bargi,"
        ),FoodItem(
            name = "Hot-Dog Dabl",
            price = "25 000 so'm",
            imageResId = R.drawable.img41,
            category = "Xot Dog",
            description = "Ikkita ishtahaochar sosiska, yangi pomidor va marinadlangan karsildoq bodring bo'lakchalari, \"Aysberg\" salat bargi,"
        ),FoodItem(
            name = "Hot-Dog Dabl",
            price = "26 000 so'm",
            imageResId = R.drawable.img42,
            category = "Xot Dog",
            description = "Ikkita ishtahaochar sosiska, yangi pomidor va marinadlangan karsildoq bodring bo'lakchalari, \"Aysberg\" salat bargi,"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "15 000 so'm",
            imageResId = R.drawable.img43,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "16 000 so'm",
            imageResId = R.drawable.img44,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "16 000 so'm",
            imageResId = R.drawable.img45,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "15 000 so'm",
            imageResId = R.drawable.img46,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "15 000 so'm",
            imageResId = R.drawable.img47,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "16 000 so'm",
            imageResId = R.drawable.img48,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "16 000 so'm",
            imageResId = R.drawable.img49,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Qulupnayli Moxito",
            price = "15 000 so'm",
            imageResId = R.drawable.img50,
            category = "Ichimliklar",
            description = "Boyitilgan qulupnay ta’mi, laym va yalpizning tetiklantiruvchi xushbo‘yligi bilan uyg‘unlashib, mukammal yozgi ichimlikni yaratadi"
        ),FoodItem(
            name = "Ketchup",
            price = "5 000 so'm",
            imageResId = R.drawable.img51,
            category = "Souslar",
            description = "Tabiiy, yangi pomidorlar va ziravorlardan tayyorlangan ketchup portsiyasi. Sof og'irligi - 25 gr"
        ),FoodItem(
            name = "Ketchup",
            price = "4 000 so'm",
            imageResId = R.drawable.img52,
            category = "Souslar",
            description = "Tabiiy, yangi pomidorlar va ziravorlardan tayyorlangan ketchup portsiyasi. Sof og'irligi - 25 gr"
        ),FoodItem(
            name = "Ketchup",
            price = "4 000 so'm",
            imageResId = R.drawable.img53,
            category = "Souslar",
            description = "Tabiiy, yangi pomidorlar va ziravorlardan tayyorlangan ketchup portsiyasi. Sof og'irligi - 25 gr"
        ),FoodItem(
            name = "Ketchup",
            price = "5 000 so'm",
            imageResId = R.drawable.img54,
            category = "Souslar",
            description = "Tabiiy, yangi pomidorlar va ziravorlardan tayyorlangan ketchup portsiyasi. Sof og'irligi - 25 gr"
        ),
    )


    private val categoryList = listOf(
        "Barchasi",
        "Combo",
        "Burgerlar",
        "Shaurma",
        "Lavash",
        "Souslar",
        "Ichimliklar"

    )

    private var selectedButton: Button? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        view__ = inflater.inflate(R.layout.fragment_menyu, container, false)

        // Sinov uchun bir nechta fake taomlar
        editSearch = view__.findViewById(R.id.editSearch)


        recyclerMenu = view__.findViewById(R.id.menyu_recyclerview)
        categoryContainer = view__.findViewById(R.id.categoryContainer)

        setupCategoryButtons()

        recyclerMenu.layoutManager = GridLayoutManager(requireContext(), 2)
        menuAdapter = MenuAdapter(foodList)
        recyclerMenu.adapter = menuAdapter

        setupSearchListener()

        view__.findViewById<ImageView>(R.id.notification_btn).setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.base_frame, BildirishnomaFragment())
                ?.commit()
        }

        return view__
    }

    private fun setupCategoryButtons() {
        for (category in categoryList) {
            val button = Button(requireContext()).apply {
                text = category
                setPadding(40, 10, 40, 10)
                background = ContextCompat.getDrawable(requireContext(), R.drawable.category_button_selector)
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
                isAllCaps = false
            }

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 16, 0)
            button.layoutParams = params

            button.setOnClickListener {
                selectCategory(button, category)
            }

            categoryContainer.addView(button)

            // Boshlang‘ich tanlov: “Barchasi”
            if (category == "Barchasi") {
                selectCategory(button, category)
            }
        }
    }

    private fun selectCategory(button: Button, category: String) {
        selectedButton?.isSelected = false
        selectedButton?.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

        button.isSelected = true
        button.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
        selectedButton = button

        val filteredList = if (category == "Barchasi") {
            foodList
        } else {
            foodList.filter { it.category == category }
        }

        menuAdapter = MenuAdapter(filteredList)
        recyclerMenu.adapter = menuAdapter
    }

    private fun setupSearchListener() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentQuery = s.toString()
                filterMenu()
            }
        })
    }

    private fun filterMenu() {
        var filteredList = foodList

        // Avval kategoriya bo‘yicha filter
        if (currentCategory != "Barchasi") {
            filteredList = filteredList.filter { it.category == currentCategory }
        }

        // Keyin izlash bo‘yicha filter
        if (currentQuery.isNotEmpty()) {
            filteredList = filteredList.filter {
                it.name.contains(currentQuery, ignoreCase = true)
            }
        }

        menuAdapter = MenuAdapter(filteredList)
        recyclerMenu.adapter = menuAdapter
    }

}