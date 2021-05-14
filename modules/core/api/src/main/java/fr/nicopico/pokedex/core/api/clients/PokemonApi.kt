package fr.nicopico.pokedex.core.api.clients

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import kotlin.reflect.KClass

const val POKEMON_API_ENDPOINT_V2 = "https://pokeapi.co/api/v2/"

val defaultOkHttpClient: OkHttpClient by lazy { OkHttpClient.Builder()
    .retryOnConnectionFailure(false)
    .build()
}

fun <T : Any> createApiClient(
    clazz: KClass<T>,
    baseUrl: String = POKEMON_API_ENDPOINT_V2,
    okHttpClient: OkHttpClient = defaultOkHttpClient
): T {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(clazz.java)
}
