package cn.yfengtech.launcheroverlay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yf.launcheroverlay.R
import kotlinx.android.synthetic.main.fragment_demo.*


private const val KEY_POSITION = "position"

/**
 * Created by yf.
 * @date 2019-12-22
 */
class DemoFragment private constructor() : Fragment() {

    private var mPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPosition = arguments?.getInt(KEY_POSITION)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.text = "This is page $mPosition"
    }


    companion object {
        fun newInstance(position: Int): DemoFragment {
            return DemoFragment().apply {
                val bundle = Bundle()
                bundle.putInt(KEY_POSITION, position)
                arguments = bundle
            }
        }
    }
}