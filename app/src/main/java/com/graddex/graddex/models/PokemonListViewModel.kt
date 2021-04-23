package com.graddex.graddex.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.graddex.graddex.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PokemonListViewModel : ViewModel(), CoroutineScope {

    private val coroutineSupervisor = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.IO + coroutineSupervisor

    override fun onCleared() {
        super.onCleared()

        coroutineSupervisor.cancel()
    }

    val pokemonList = MutableLiveData<PokemonResponse>()

    private val tag = "PokeAPI Details"

    private val pokemonService: PokemonService = PokemonServiceImpl()

    fun getPokemonList() {
        launch(coroutineContext) {
            val res = pokemonService.getPokemonList()
            pokemonList.postValue(res)
        }
    }

}