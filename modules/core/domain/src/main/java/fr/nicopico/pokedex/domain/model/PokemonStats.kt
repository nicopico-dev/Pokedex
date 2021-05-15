package fr.nicopico.pokedex.domain.model

@Deprecated("do not use")
data class PokemonStats(
    val code: String,
    val label: String,
    val value: Int
) {
    val maxValue: Int?
            get() = MAX_VALUES[code]

    companion object {
        const val HEALTH = "hp"
        const val ATTACK = "atk"
        const val DEFENSE = "def"
        const val SPEED = "spd"
        const val EXPERIENCE = "exp"

        private val MAX_VALUES = mapOf(
            HEALTH to 300,
            ATTACK to 300,
            DEFENSE to 300,
            SPEED to 300,
            EXPERIENCE to 1000
        )
    }
}
