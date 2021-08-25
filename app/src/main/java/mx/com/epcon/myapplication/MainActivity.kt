package mx.com.epcon.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var txvInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txvInfo = findViewById(R.id.txvInfo)
    }

    private val games = mutableListOf<Juego>()
    private val shoother =
        arrayOf(
            Shot("Ranbow Six",    6,   "Shoother Firt Person"),
            Shot("Rogue Company", 2,   "Shoother third Person"),
            Shot("Call of Duty",  3,   "Shoother Firt Person" ),
        )
    private val BattleRoyal =
        arrayOf(
            Royal("WarZone", 2, "New Battle Royal"),
            Royal("Apex",    3, "Battle Royal Raro"),
            Royal("Fornite", 4, "Battle Royal Animado"),
        )

    private var counter = -1
    private var royalCounter = 0
    private var shotCounter = 0

    private lateinit var actualGame: Juego

    fun createNewGame(view: View) {
        val evenOrOdd = Random.nextInt() % 2 == 0
        var newGame: Juego? = null

        when {
            evenOrOdd && shotCounter < 3 -> newGame = shoother[shotCounter]
            !evenOrOdd && royalCounter < 3 -> newGame = BattleRoyal[royalCounter]
            else -> makeToast("No hay mas ${if (evenOrOdd) "Shooters" else "Battle Royals"}, Intentao Despues.")
        }

        newGame?.let {
            when (newGame) {
                is Shot -> shotCounter++
                is Royal -> royalCounter++
            }

            setActualGame(it)
            games.add(it)
            counter = games.size - 1
        }
    }

    fun getPreviousGame(view: View) {
        if (counter - 1 != -1 && counter - 1 >= 0) {
            setActualGame(games[counter - 1])
            counter--
        }
    }

    fun getNextGame(view: View) {
        if (counter != -1 && counter + 1 <= games.size - 1) {
            setActualGame(games[counter + 1])
            counter++
        }
    }

    private fun setActualGame(game: Juego) {
        actualGame = game
        setBasicInfo()
    }

    private fun setBasicInfo() {
        txvInfo.text = actualGame.getBasicInfo()
    }

    private fun makeToast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    fun makeSound(view: View) = ::actualGame.isInitialized.let {
        if (it) makeToast(actualGame.makeSound())
    }

    fun play(view: View) = ::actualGame.isInitialized.let {
        if (it) makeToast(actualGame.play())
    }

    fun eat(view: View) = ::actualGame.isInitialized.let {
        if (it) makeToast(actualGame.eat())
    }

}

abstract class Juego(
    protected val   name: String = "",
    private val      age: Int = 0,
    protected val gender: String? = null
) {

    abstract val type: String

    fun getBasicInfo() =
        "Tu juego es un: $type \n El nombre del juego es: $name \n El tiempo que lleva el juego es: $age \n y este es el Genero: ${gender ?: "NA"}"

    abstract fun makeSound(): String
    abstract fun play(): String
    abstract fun eat(): String

}

class Shot(name: String, age: Int, gender: String? = null) : Juego(name, age, gender) {

    override val type = "Shot"

    override fun makeSound() = "PIIU PIIIU"

    override fun play() = "$name Estas Jugando A Un Shooter"

    override fun eat() = gender?.let {
        "$name Necesitas comer algo Hermano $it"
    } ?: "No information"

}

class Royal(name: String, age: Int, gender: String? = null) : Juego(name, age, gender) {

    override val type = "Royal"

    override fun makeSound() = "Construye, Mata y Gna"

    override fun play() = "$name Estas Jugando Un Battle Royal, Se el Mejor Y Se El Unico En Pie"

    override fun eat() = gender?.let {
        "$name Necesitas Comer algo Hermano $it"
    } ?: "No information"

}