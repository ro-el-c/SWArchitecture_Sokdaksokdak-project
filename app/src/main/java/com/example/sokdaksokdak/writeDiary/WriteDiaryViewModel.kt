package com.example.sokdaksokdak.writeDiary

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.sokdaksokdak.database.AppDatabase

class WriteDiaryViewModel(application: Application): AndroidViewModel(application) {
    var recommendKeyword = RecommendKeyword()
    private var writeDiary = WriteDiary(application)

    public fun insertData() {
        writeDiary.insertDiary()
        println("successfully initialized diary data")
    }

    public fun newDiaryData(keyword:String, content:String) {
        writeDiary.newDiaryData(keyword, content)
    }

    public fun checkDataExists(): Boolean {
        return writeDiary.isDataExists()
    }

    public fun checkDiaryCompleted(): Boolean {
        val content = writeDiary.getDiaryContent()
        return if (content == "일기를 작성하세요."){
            false
        } else{
            println(content)
            true
        }
    }

    public fun showKeyword(): String{
        var keywordDB = writeDiary.getKeyword()

        println("현재 저장 keyword: " + keywordDB)

        return if (keywordDB == "키워드를 선택하세요."){
            println("Get Random Keyword")
            getRandomKeyword()
        } else{
            return keywordDB
        }
        // 만약 DB에 저장된 keyword 가
        // 기본값(키워드를 선택하세요.)이면 getRandomKeyword 호출
        // 기본값이 아니면, getKeyword 호출
    }

    public fun showContent(): String{
        return writeDiary.getDiaryContent()
    }

    private fun getRandomKeyword(): String {
        val keyword = recommendKeyword.randomKeyword()
        writeDiary.updateKeyword(keyword)
        return keyword
    }

    fun deleteData() {
        writeDiary.deleteData()
    }

    /*fun setKeyword(keyword: String) {
        writeDiary.setKeyword(keyword)
    }*/



}