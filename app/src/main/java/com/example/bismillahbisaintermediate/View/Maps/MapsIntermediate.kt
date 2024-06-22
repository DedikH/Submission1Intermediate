package com.example.bismillahbisaintermediate.View.Maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bismillahbisaintermediate.API.APIConfig
import com.example.bismillahbisaintermediate.Auth.UserPreferenceDataStore
import com.example.bismillahbisaintermediate.Auth.UserRepository
import com.example.bismillahbisaintermediate.Database.ListStoryDB
import com.example.bismillahbisaintermediate.R
import com.example.bismillahbisaintermediate.View.ListStory.withmaps.MapsViewModel
import com.example.bismillahbisaintermediate.ViewModelFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.bismillahbisaintermediate.databinding.ActivityMapsIntermediateBinding

class MapsIntermediate : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsIntermediateBinding
    private lateinit var listStoryMapsViewModel: MapsViewModel
    private lateinit var authRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsIntermediateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val apiService = APIConfig.postLogin()
        val userPreferenceDataStore = UserPreferenceDataStore(this)
        val storyDatabase = ListStoryDB.getDatabase(this)
        authRepository = UserRepository(apiService,userPreferenceDataStore, storyDatabase)
        val factory = ViewModelFactory(authRepository)
        listStoryMapsViewModel = ViewModelProvider(this, factory).get(MapsViewModel::class.java)

        listStoryMapsViewModel.MarkerwithLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true

        listStoryMapsViewModel.stories.observe(this, Observer { response ->
            response.listStory.forEach { story ->
                val latLng = LatLng(story.lat as Double, story.lon as Double)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                )
            }
        })
    }
}