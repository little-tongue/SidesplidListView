package jijiehao.minhua.com.sidesliplistviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var adapter: BaseCommonAdapter<String>? = null
    private val data = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = object : BaseCommonAdapter<String>(this, R.layout.item_main, data) {
            override fun convert(holder: ViewHolder?, t: String?) {
                holder?.setText(R.id.tvItemName, data[holder?.getmPosition()])
            }
        }

        slvContent.adapter = adapter

        refresh.setOnRefreshListener {
            data.clear()
            (0..20).mapTo(data) { "item" + it }
            adapter?.notifyDataSetChanged()
            refresh.isRefreshing = false
        }

    }
}
