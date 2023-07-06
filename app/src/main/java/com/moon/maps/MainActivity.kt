package com.moon.maps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var mMap: GoogleMap
    lateinit var imageLoc: ImageView
    var isOffed = false

    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.white)
        BitMapHelper.vectorToBitmap(this, R.drawable.baseline_person_24, color)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageLoc = findViewById(R.id.imLoc)
        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_geotype, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.HYBRID -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
            R.id.SATELLITE -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                return true
            }
            R.id.NONE -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NONE
                return true
            }
            R.id.NORMAL -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }
            R.id.TERRAIN -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        var point = LatLng(43.256564, 76.901852)
        mMap.addMarker(MarkerOptions().position(point).title("MapsGo").icon(bicycleIcon))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(point))
        mMap.isBuildingsEnabled = true
        mMap.isIndoorEnabled = true


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
            return
        }
        mMap.isMyLocationEnabled = true

        var trafficIcon: ImageView = findViewById(R.id.trafOff)
        var minus: CardView = findViewById(R.id.min)
        var max: CardView = findViewById(R.id.max)

        trafficIcon.setOnClickListener {
            if (!isOffed) {
                mMap.isTrafficEnabled = true
                trafficIcon.setImageResource(R.drawable.baseline_traffic)
                isOffed = true
            } else {
                mMap.isTrafficEnabled = false
                trafficIcon.setImageResource(R.drawable.baseline_traffic_24)
                isOffed = false
            }
        }
        minus.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }
        max.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }
    }
}