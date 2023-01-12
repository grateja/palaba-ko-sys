//package com.csi.palabakosys.util
//
//import android.widget.RadioGroup
//import androidx.databinding.BindingAdapter
//import androidx.databinding.InverseBindingListener
//import androidx.databinding.InverseBindingMethod
//import androidx.databinding.InverseBindingMethods
//
//
//@InverseBindingMethods(
//    InverseBindingMethod(
//        type = RadioGroup::class,
//        attribute = "android:checkedButton",
//        method = "getCheckedRadioButtonId"
//    )
//)
//object RadioGroupBindingAdapter {
//    @BindingAdapter("android:checkedButton")
//    fun setCheckedButton(view: RadioGroup, id: Int) {
//        if (id != view.checkedRadioButtonId) {
//            view.check(id)
//        }
//    }
//
//    @BindingAdapter(
//        value = ["android:onCheckedChanged", "android:checkedButtonAttrChanged"],
//        requireAll = false
//    )
//    fun setListeners(
//        view: RadioGroup,
//        listener: RadioGroup.OnCheckedChangeListener?,
//        attrChange: InverseBindingListener?
//    ) {
//        if (attrChange == null) {
//            view.setOnCheckedChangeListener(listener)
//        } else {
//            view.setOnCheckedChangeListener { group, checkedId ->
//                listener?.onCheckedChanged(group, checkedId)
//                attrChange.onChange()
//            }
//        }
//    }
//}