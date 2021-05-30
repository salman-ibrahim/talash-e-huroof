

package com.gatow.salman.talashehuroof.presenter.requests


import android.util.Log
import com.gatow.salman.talashehuroof.models.WordAvailable
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

/**
 * Presenter that make the Network requests to the Datamuse API
 * It will be used only if the developer chooses to introduce english levels
 * @author Salman Ibrahim (salmanmaik-pk)
 */
class DataMuseAPI {

    /**
     * Make a OkHttp request to the Datamuse API to get a list of words based on given category
     * @param subject category of the word list to be searched
     * @param size size of the desired list
     */
    fun getRandomWordList(subject: String, size: Int = 6): List<WordAvailable> {
//        suspendCoroutine {

        val client = OkHttpClient()
        val request =
            Request.Builder().url("https://api.datamuse.com/words?ml=$subject").build()
        val jsonArrayString = client.newCall(request).execute().body?.string()
        val jsonArray = JSONArray(jsonArrayString)

        val list = mutableListOf<WordAvailable>()

        while (list.size < size) {
            val json = jsonArray.remove(Random.nextInt(jsonArray.length())) as? JSONObject
            val word = json?.getString("word") ?: "Fail"
            if (word.contains(' ') || word.contains('-') || word.length > 10) continue
            list.add(WordAvailable(word))
            Log.d("DataMuseAPI", "getRandomWordList() $word")
        }
        require(list.size == size)
        return list
    }
}