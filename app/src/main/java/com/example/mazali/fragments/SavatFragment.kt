package com.example.mazali.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mazali.R
import com.example.mazali.adapters.CartAdapter
import com.example.mazali.data.FoodItem
import com.example.mazali.data.model.BuyurtmaRequest
import com.example.mazali.data.model.ProductRequest
import com.example.mazali.data.repository.BuyurtmaRepository
import com.example.mazali.ui.auth.viewmodel.BuyurtmaViewModel
import com.example.mazali.ui.auth.viewmodel.BuyurtmaViewModelFactory
import com.example.mazali.utils.PrefsManager
import com.google.android.gms.location.*
import java.util.Locale

class SavatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotal: TextView
    private lateinit var buttonOrder: Button
    private lateinit var emptyText: TextView
    private lateinit var cartList: MutableList<FoodItem>
    private lateinit var cartAdapter: CartAdapter

    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // ⭐ ViewModel integratsiya qilindi
    private lateinit var viewModel: BuyurtmaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_savat, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewCart)
        textTotal = view.findViewById(R.id.textTotalPrice)
        buttonOrder = view.findViewById(R.id.buttonOrder)
        emptyText = view.findViewById(R.id.textEmptyCart)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // ⭐ ViewModel yaratildi
        val repository = BuyurtmaRepository()
        val factory = BuyurtmaViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BuyurtmaViewModel::class.java]

        context?.let { cartList = PrefsManager.getCart(it) }

        cartAdapter = CartAdapter(cartList, object : CartAdapter.CartListener {
            override fun onQuantityChanged() {
                PrefsManager.saveCart(requireContext(), cartList)
                updateTotal()
                toggleEmptyView()
            }

            override fun onItemDeleted(position: Int) {
                PrefsManager.saveCart(requireContext(), cartList)
                updateTotal()
                toggleEmptyView()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = cartAdapter

        // =========================================
        // ⭐ BUYURTMA YARATISH DIALOGLARI VA VM OBSERVERLARI
        // =========================================

        viewModel.createOrderResponse.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                Toast.makeText(requireContext(), "Buyurtma yaratildi: #${response.id}", Toast.LENGTH_SHORT).show()

                // Savatni tozalash
                PrefsManager.clearCart(requireContext())
                cartList.clear()
                cartAdapter.notifyDataSetChanged()
                updateTotal()
                toggleEmptyView()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { err ->
            if (!err.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Xatolik: $err", Toast.LENGTH_LONG).show()
            }
        }

        buttonOrder.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(requireContext(), "Savat bo‘sh!", Toast.LENGTH_SHORT).show()
            } else {
                showCheckoutBottomSheet()
            }
        }

        updateTotal()
        toggleEmptyView()
        return view
    }

    // ======================
    // ⭐ ASOSIY BUYURTMA DIALOGI
    // ======================
    private fun showCheckoutBottomSheet() {
        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottomsheet_checkout, null)

        val buttonGetLocation = bottomSheetView.findViewById<Button>(R.id.buttonGetCurrentLocation)
        val radioGroup = bottomSheetView.findViewById<RadioGroup>(R.id.radioGroupPayment)
        val buttonCheckout = bottomSheetView.findViewById<Button>(R.id.buttonCheckout)

        buttonGetLocation.setOnClickListener {
            checkLocationPermission(buttonGetLocation)
        }

        // ⭐ SERVERGA BUYURTMA YUBORISH
        buttonCheckout.setOnClickListener {

            if (currentLatitude == null || currentLongitude == null) {
                Toast.makeText(requireContext(), "Lokatsiya olinmagan!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(requireContext(), "To‘lov usulini tanlang!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ⭐ ProductRequest listini shakllantiramiz
            val products = cartList.map {
                ProductRequest(
                    product_id = it.id,
                    count = it.quantity
                )
            }

            val lat = String.format(Locale.US, "%.6f", currentLatitude!!).toDouble()
            val long = String.format(Locale.US, "%.6f", currentLongitude!!).toDouble()
            // ⭐ BuyurtmaRequest
            val request = BuyurtmaRequest(
                lat = lat,
                long = long,
                products = products
            )
            val token = PrefsManager.getAuthToken(requireContext()).toString()
            // ⭐ ViewModel orqali backendga yuborish
            viewModel.createOrder(token, request)

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    // ===============================================
    // ⭐ LOKATSIYA KODI (BuyurtmaFragment bilan bir xil)
    // ===============================================
    private fun checkLocationPermission(button: Button) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            checkGpsEnabled(button)
        }
    }

    private fun checkGpsEnabled(button: Button) {
        val lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isEnabled) {
            AlertDialog.Builder(requireContext())
                .setTitle("Lokatsiya o‘chiq")
                .setMessage("Iltimos, lokatsiyani yoqing!")
                .setCancelable(false)
                .setPositiveButton("Ha") { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton("Yo‘q") { _, _ ->
                    Toast.makeText(requireContext(), "Lokatsiya yoqilmaguncha davom eta olmaysiz!", Toast.LENGTH_LONG).show()
                }
                .show()
        } else {
            getCurrentLocation(button)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(button: Button) {
        button.text = "Lokatsiya olinmoqda ..."

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation
                if (loc != null) {
                    currentLatitude = loc.latitude
                    currentLongitude = loc.longitude
                    button.text = "Lat: %.6f, Long: %.6f".format(currentLatitude, currentLongitude)
                    Toast.makeText(requireContext(), "Lokatsiya olindi", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Lokatsiyani aniqlab bo‘lmadi", Toast.LENGTH_SHORT).show()
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }, null)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Ruxsat berildi. Qayta urinib ko‘ring.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Lokatsiya ruxsati kerak!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotal() {
        val total = cartList.sumOf { it.price.filter { c -> c.isDigit() }.toInt() * it.quantity }
        textTotal.text = "Jami: $total so'm"
    }

    private fun toggleEmptyView() {
        if (cartList.isEmpty()) {
            emptyText.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            buttonOrder.isEnabled = false
        } else {
            emptyText.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            buttonOrder.isEnabled = true
        }
    }
}
