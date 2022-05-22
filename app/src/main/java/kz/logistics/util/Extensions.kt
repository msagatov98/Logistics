package kz.logistics.util

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect

fun <T> FragmentActivity.collect(flow: kotlinx.coroutines.flow.Flow<T>, block: (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(block::invoke)
        }
    }
}

fun <T> Fragment.collect(flow: kotlinx.coroutines.flow.Flow<T>, block: (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(block::invoke)
        }
    }
}

fun Spinner.doOnItemSelected(action: (String) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            action.invoke(parent?.getItemAtPosition(position).toString())
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            action(parent?.getItemAtPosition(0).toString())
        }
    }
}