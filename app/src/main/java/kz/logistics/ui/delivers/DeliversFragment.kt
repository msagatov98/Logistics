package kz.logistics.ui.delivers


import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kz.logistics.R
import kz.logistics.databinding.DeliversFragmentBinding
import kz.logistics.model.Deliver

class DeliversFragment : BottomSheetDialogFragment() {

    private val binding by viewBinding(DeliversFragmentBinding::bind)

    companion object {
        const val RESULT_DELIVER = "result_deliver"
        const val BUNDLE_DELIVER = "bundle_deliver"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = layoutInflater.inflate(R.layout.delivers_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
    }

    private fun setupListener() = with(binding) {
        fedexLayout.setOnClickListener {
            selectDeliver("FeDex", R.drawable.ic_fedex_express_6_small, 800)
        }
        dhlLayout.setOnClickListener {
            selectDeliver("DHL", R.drawable.ic_dhl_express_small, 1000)
        }
    }

    private fun selectDeliver(name: String, imageResId: Int, price: Int) {
        val deliver = Deliver(name = name, price = price, imageResId = imageResId)
        setFragmentResult(RESULT_DELIVER, bundleOf(BUNDLE_DELIVER to deliver))
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also {
            it.setOnShowListener { dialogInterface ->
                val bottomSheetDialog = dialogInterface as BottomSheetDialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { view ->
                    val behaviour = BottomSheetBehavior.from(view)
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                    view.setBackgroundColor(Color.TRANSPARENT)
                }
            }
        }
    }
}