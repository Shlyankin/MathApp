package com.heads.thinking.mathapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), View.OnClickListener {

    private var listener: OnFragmentListener? = null

    override fun onClick(view: View) {
        when(view.id) {
            R.id.integralsBtn -> {
                listener?.changeFragment(IntegralsFragment.newInstance())
            }
            R.id.diffursBtn -> {
                listener?.changeFragment(DiffursFragment.newInstance())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listener = this.activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_main, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        integralsBtn.setOnClickListener(this)
        diffursBtn.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}
