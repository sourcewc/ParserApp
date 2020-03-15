package sourcewc.parserapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import kotlinx.coroutines.*
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val urlList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBar = findViewById<EditText>(R.id.search_bar)
        val searchButton = findViewById<Button>(R.id.search_button)
        val itemListView = findViewById<ListView>(R.id.list_view)

        searchButton.setOnClickListener { view ->
            val inputText = searchBar.text.toString()

            if (inputText.equals("")) {
                Toast.makeText(this, "input text", Toast.LENGTH_SHORT).show()
            }

            when (view.getId()) {
                R.id.search_button -> htmlParser(inputText)
            }
         }


        itemListView.setOnItemClickListener { parent, view, position, id ->
            val nextIntent = Intent(this, CookPadWeb::class.java)
            val recipeUrl : String = "https://cookpad.com/" + urlList.get(position)
            nextIntent.putExtra("URL_KEY",recipeUrl)
            startActivity(nextIntent)
        }
    }

    private fun htmlParser(inputText: String) {

        val list = findViewById<ListView>(R.id.list_view)

        val cardList = arrayListOf<Recipe>()
        val cardAdapter = CardAdapter(this, cardList)

        val imageUrlList= mutableListOf<String>()
        val contentsList = mutableListOf<String>()
        val titleList = mutableListOf<String>()
        val materialList = mutableListOf<String>()

        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.Default) {try {
                val doc = Jsoup.connect("https://cookpad.com/search/" + inputText).get()

                val urlContents : Elements = doc.select(".recipe-preview a")
                val imageContents : Elements = doc.select(".recipe-preview a img")
                val titleContents : Elements = doc.select(".recipe-preview a img")
                val contents : Elements = doc.select(".recipe_description")
                val materials : Elements = doc.select(".material")

                for(content in urlContents) {
                    urlList.add(content.attr("href"))
                }
                for(content in imageContents) {
                    imageUrlList.add(content.attr("src"))
                }
                for(content in contents) {
                    contentsList.add(content.text())
                }
                for(content in titleContents) {
                    titleList.add(content.attr("alt"))
                }
                for(content in materials) {
                    materialList.add(content.text())
                }
                for( i in 0 until imageUrlList.size ) {
                    cardList.add(Recipe(imageUrlList.get(i),titleList.get(i),contentsList.get(i),materialList.get(i)))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            } }.await().let {
                //값 출력하기
               list.adapter = cardAdapter
            }
        }
        imageUrlList.clear()
        contentsList.clear()
        titleList.clear()
        materialList.clear()
    }
}
