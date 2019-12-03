package kr.ac.mjc.mjstargram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.text.FieldPosition

class MainActivity : AppCompatActivity() {

    lateinit var viewpager:ViewPager
    lateinit var tab:TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager=findViewById(R.id.viewpager)
        tab=findViewById(R.id.tab)

        var mainAdapter=MainAdapter(supportFragmentManager)
        viewpager.adapter=mainAdapter
        tab.setupWithViewPager(viewpager)

        tab.getTabAt(0)?.setIcon(resources.getDrawable(R.drawable.baseline_home_black_48))
        tab.getTabAt(1)?.setIcon(resources.getDrawable(R.drawable.baseline_search_black_48))
        tab.getTabAt(2)?.setIcon(resources.getDrawable(R.drawable.baseline_add_circle_outline_black_48))
        tab.getTabAt(3)?.setIcon(resources.getDrawable(R.drawable.baseline_favorite_border_black_48))
        tab.getTabAt(4)?.setIcon(resources.getDrawable(R.drawable.baseline_perm_identity_black_48))
    }

    fun moveTab(position:Int){
        viewpager.currentItem=position
    }
}
