package page.shellcore.tech.android.mvvmkotlin.ui.search


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import page.shellcore.tech.android.mvvmkotlin.R
import page.shellcore.tech.android.mvvmkotlin.di.Injectable

class SearchFragment : Fragment(), Injectable {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


}
