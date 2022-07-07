package com.yellowclass.plugin_native.yc_native.bottom_sheets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yellowclass.plugin_native.yc_native.R
import com.yellowclass.plugin_native.yc_native.adapters.YCPkgAdapter
import com.yellowclass.plugin_native.yc_native.fontFamily.TextViewBold
import com.yellowclass.plugin_native.yc_native.models.ShareablePackage
import com.yellowclass.plugin_native.yc_native.yc_utils.ScreenUtils
import io.flutter.embedding.android.FlutterFragmentActivity

class ShareSheetV2 private constructor(
    itemClickListener: OnBottomSheetItemClickListener,
    items: List<ShareablePackage>,
    sheetTitle : String
) : BottomSheetDialogFragment() {

    private val mBS_BehaviorCallback: BottomSheetCallback = SheetCallBack()
    private val mOnItemSelected: OnBottomSheetItemClickListener?

    private var pkgItems: List<ShareablePackage>
    private lateinit var title: String

    private var parentDialog: Dialog? = null

    private lateinit var ycPkgAdapter: YCPkgAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var sheetTitleTv : TextViewBold

    init {
        mOnItemSelected = itemClickListener
        pkgItems = items
        title = sheetTitle
    }



    companion object {
        fun openBottomSheet(
            mContext: Context,
            items: List<ShareablePackage>,
            itemClickListener: OnBottomSheetItemClickListener,
            title: String
        ) {
            val tfbSheet = ShareSheetV2(itemClickListener, items, title)
            tfbSheet.show(
                (mContext as FlutterFragmentActivity).supportFragmentManager,
                tfbSheet.tag
            )
        }
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        //layout
        val contentView = View.inflate(context, R.layout.fragment_share_sheet, null)
        contentView.setBackgroundColor(context!!.resources.getColor(android.R.color.transparent))
        dialog.setContentView(contentView)
        recyclerView = contentView.findViewById(R.id.pkList)
        sheetTitleTv = contentView.findViewById(R.id.sheetTitle)
        sheetTitleTv.text = title;
        parentDialog = dialog

        inflateData()

        val behavior =
            ((contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams).behavior
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
            behavior.addBottomSheetCallback(mBS_BehaviorCallback)
        }

    }


    private fun inflateData() {

        ycPkgAdapter = YCPkgAdapter(context = context!!,
            list = pkgItems,
            clickListener = object : OnBottomSheetItemClickListener {
                override fun onClicked(selectedPackage: ShareablePackage) {
                    mOnItemSelected!!.onClicked(selectedPackage = selectedPackage)
                    dismiss()
                }

                override fun onCancelled() {
                    //use if needed
                }

            })

        recyclerView.layoutManager = GridLayoutManager(context, columnCount())
        recyclerView.adapter = ycPkgAdapter

    }

    private fun columnCount(): Int {
        val deviceWidth = ScreenUtils.size().width
        val spanCount: Int = when {
            deviceWidth >= 600f -> 4
            //on some android api's device width may not be calculated.
            deviceWidth == 0f -> 2
            else -> 3
        }
        return spanCount
    }

    interface OnBottomSheetItemClickListener {
        fun onClicked(selectedPackage: ShareablePackage)
        fun onCancelled()
    }


    internal inner class SheetCallBack : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {

        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            mOnItemSelected!!.onCancelled();
        }
    }


}


