package sourcewc.parserapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler
import android.view.View
import android.widget.*
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
            val inputText = searchBar.text.toString();

            if (inputText.equals("")) {
                Toast.makeText(this, "input text", Toast.LENGTH_SHORT)
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
        Thread(Runnable {

            val stringBuffer = StringBuffer()
            val recipe = "recipe_"
            val recipeImage = "recipe_image_"
            val recipeTitle = "recipe_title"
            val recipeDescription = "recipe_discription_"
            val meterial = "meterial ingredients"

            try {
                val doc = Jsoup.connect("https://cookpad.com/search/" + inputText).get()
                val contents : Elements = doc.select(".recipe-preview a img")

                var cardList = arrayListOf<Recipe>()
                for(content in contents) {
                    cardList.add(Recipe(content.attr("src"),"심효근","sdfsf","아령하세요잇!"))
                }
                val Adapter = CardAdapter(this, cardList)


//                val bufferedReader = BufferedReader(InputStreamReader(doc.inputStream, "utf-8"))
//                while (true) {
//                    //더 이상 읽어올 데이터가 없으면 루프 탈출
//                    val line: String = bufferedReader.readLine() ?: break
//                    //원하는 데이터가 있을때까지 찾기.
//                    if (line.contains(recipe)) {
//                        //원하는 문자열 추출
//                        val getRecipe = line.indexOf(recipe)
//                        val b = line.substring(getRecipe + recipe.length, line.lastIndex)
//                        val c = b.substring(0, b.indexOf('"'))
//
//                        stringBuffer.append(c + "\n")
//                    }
//                }
                //bufferedReader.close()

                //연결 종료
                //connectUrl.disconnect()
                //값 출력하기
                handler.post { list.adapter = Adapter }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start() // 쓰레드 시작
    }
}
