package page.shellcore.tech.android.viewmodellivedatakotlin.bindingAdapter

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("showVisible")
    fun setVisibility(view: View, isShowed: Boolean) {
        view.visibility = if (isShowed) View.VISIBLE else View.INVISIBLE
    }

}