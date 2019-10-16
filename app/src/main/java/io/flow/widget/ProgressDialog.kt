package io.flow.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.jpardogo.android.googleprogressbar.library.ChromeFloatingCirclesDrawable
import io.flow.R
import kotlinx.android.synthetic.main.progress_dialog.*

class ProgressDialog(context: Context): Dialog(context) {
    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.progress_dialog)
        this.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        google_progress.indeterminateDrawable = ChromeFloatingCirclesDrawable.Builder(context).build()
    }
}