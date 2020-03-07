package sourcewc.parserapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.io.InputStream
import java.net.URL

class CardAdapter (val context: Context, val recipeList: ArrayList<Recipe>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.search_bar, null)
        val imageFood = view.findViewById<ImageView>(R.id.imageicon)
        val title = view.findViewById<TextView>(R.id.title)
        val description = view.findViewById<TextView>(R.id.description)
        val Content = view.findViewById<TextView>(R.id.content)

        val card = recipeList[position]

        val url = URL(card.image)
        val input:InputStream = url.openConnection().getInputStream()
        val bitmap: Bitmap? = BitmapFactory.decodeStream(input)

        imageFood.setImageBitmap(bitmap)
        title.text = card.title
        description.text = card.description
        Content.text = card.content

        return view
    }

    override fun getItem(position: Int): Any {
        return recipeList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return recipeList.size
    }
}
