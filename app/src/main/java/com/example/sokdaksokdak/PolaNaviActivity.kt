package com.example.sokdaksokdak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.sokdaksokdak.Factory.DefaultPreferenceManager
import com.example.sokdaksokdak.Login.LoginFragment
import com.example.sokdaksokdak.database.AppDatabase
import com.example.sokdaksokdak.databinding.ActivityPolaNaviBinding
import com.example.sokdaksokdak.writeDiary.DiaryFragment
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient


private const val TAG_DIARY = "diary_fragment"
private const val TAG_LOGIN = "login_fragment"
private const val TAG_MY_PAGE = "mypage_fragment"
private const val TAG_THEME = "theme_change_fragment"

class PolaNaviActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPolaNaviBinding
    var currentTheme = R.style.AppPolaTheme
    private lateinit var pref: DefaultPreferenceManager
    var auth : FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pref = DefaultPreferenceManager(this)

        val theme = pref.getThemeType()
        currentTheme = getAppTheme(theme)
        setTheme(currentTheme)

        binding = ActivityPolaNaviBinding.inflate(layoutInflater)


        //구글 계정 가져옴(없으면 currentUser == null)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth?.currentUser

        //카카오 로그인 확인 및 카카오 구글 둘 다 로그인 안되어있으면 로그인페이지, 아니면 일기작성 페이지로 이동
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d("로그인 화면", "아직 카카오 로그인 안함")
                if(currentUser != null){
                    Log.d("로그인 화면", "이미 구글 로그인되어있음")
                    setFragment(TAG_DIARY,DiaryFragment())
                }else{
                    Log.d("로그인 화면", "아직 구글 로그인 안함")
                    setFragment(TAG_LOGIN, LoginFragment())
                }
            } else if (tokenInfo != null) {
                Log.d("로그인 화면", "이미 카카오 로그인되어있음")
                setFragment(TAG_DIARY,DiaryFragment())
            }
        }

        binding.navigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.diaryFragment->{
                    setFragment(TAG_DIARY, DiaryFragment())
                    true
                }
                R.id.mypageFragment->{
                    setFragment(TAG_MY_PAGE,MypageFragment())

                    true
                }
                else -> false
            }

        }
        setContentView(binding.root)
    }

    override fun onResume() {

        val themeType = pref.getThemeType()
        val settingTheme = getAppTheme(themeType)

        if (currentTheme != settingTheme) {
            recreate()
        }
            super.onResume()
    }

    fun getAppTheme(theme: String?): Int {
        var newTheme: Int
        when (theme) {
            "pola_theme" -> newTheme = R.style.AppPolaTheme
            "clover_theme" -> newTheme = R.style.AppCloverTheme
            else -> newTheme = R.style.AppPolaTheme
        }
        return newTheme

    }


    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }


        val diary = manager.findFragmentByTag(TAG_DIARY)
        val mypage = manager.findFragmentByTag(TAG_MY_PAGE)
        val login = manager.findFragmentByTag(TAG_LOGIN)
        val theme = manager.findFragmentByTag(TAG_THEME)


        if (diary != null) {
            fragTransaction.hide(diary)
        }

        if (mypage != null) {
            fragTransaction.hide(mypage)
        }
        if (theme != null) {
            fragTransaction.hide(theme)
        }

         if (tag == TAG_DIARY) {
            if (diary != null) {
                fragTransaction.show(diary)
            }
        } else if (tag == TAG_MY_PAGE) {
            if (mypage != null) {
                fragTransaction.show(mypage)
            }
        } else if (tag == TAG_LOGIN) {
            if (login != null) {
                fragTransaction.show(login)
            }
        } else if (tag == TAG_THEME) {
            if (theme != null) {
                fragTransaction.show(theme)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }

}

