package sourcewc.parserapp;

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBar = findViewById<EditText>(R.id.search_bar)
        val searchButton = findViewById<Button>(R.id.search_button)


        val clickListener = View.OnClickListener { view ->
            val inputText = searchBar.text.toString()

            if (inputText.equals("")) {
                Toast.makeText(this, "input text", Toast.LENGTH_SHORT).show()
            }

            when (view.getId()) {
                R.id.search_button -> htmlParser(inputText)

            }



        }

        searchButton.setOnClickListener(clickListener)
    }

    private fun htmlParser(inputText: String) {
        val list = findViewById<ListView>(R.id.list_view)
        //쓰레드에서 작업
//        val stringBuffer = StringBuffer()
//        val recipe = "recipe_"
//        val recipeImage = "recipe_image_"
//        val recipeTitle = "recipe_title"
//        val recipeDescription = "recipe_discription_"
//        val meterial = "meterial ingredients"
        val cardList = arrayListOf<Recipe>()
        val cardAdapter = CardAdapter(this, cardList)
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.Default) {try {
                val doc = Jsoup.connect("https://cookpad.com/search/" + inputText).get()
                val contents : Elements = doc.select(".recipe-preview a img")
                for(content in contents) {
                    cardList.add(Recipe(content.attr("src"),"심효근","sdfsf","아령하세요잇!"))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } }.await().let {

                //값 출력하기
               list.adapter = cardAdapter
            }

        }


    }
}
