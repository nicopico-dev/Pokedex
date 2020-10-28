package fr.nicopico.pokedex.core.api.clients

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class PokemonApiClient(
    rootUrl: String = PokemonApi.endpoint_v2,
    okHttpClient: OkHttpClient? = null
) : PokemonApi by Retrofit.Builder()
    .baseUrl(rootUrl)
    .addConverterFactory(MoshiConverterFactory.create())
    .client(okHttpClient ?: defaultOkHttpClient())
    .build()
    .create(PokemonApi::class.java)

private fun defaultOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .retryOnConnectionFailure(false)
        .build()
}
