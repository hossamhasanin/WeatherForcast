package com.example.weatherforcast.Externals

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

// we have to rebuild the project after creating this class so the glide could build an GlideApp and we could use it
// ia am not sure why this class exists but i think it is best practise or something

@GlideModule
class WeatherGlideAppModule : AppGlideModule()