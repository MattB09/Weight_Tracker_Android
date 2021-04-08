package com.example.weighttracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weighttracker.R
import com.example.weighttracker.database.WeightDatabase
import com.example.weighttracker.databinding.FragmentDashboardBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.Series


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentDashboardBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dashboard, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = WeightDatabase.getInstance(application).weightDatabaseDao
        val viewModelFactory = DashboardViewModelFactory(dataSource, application)
        val dashboardViewModel =
                ViewModelProvider(this, viewModelFactory).get(DashboardViewModel::class.java)

        binding.dashboardViewModel = dashboardViewModel
        binding.setLifecycleOwner(this)

        val textView: TextView =  binding.root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        val graph: GraphView =  binding.root.findViewById(R.id.graph)
        var series: Series<DataPoint>
        dashboardViewModel.series.observe(viewLifecycleOwner, Observer {
            series = it as Series<DataPoint>
            graph.addSeries(series)
        })

        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setXAxisBoundsManual(true)
        graph.getViewport().setMinX(-14.0)
        graph.getViewport().setMaxX(0.0)

//        var series = LineGraphSeries<DataPoint>()
//        var y: Double = 66.5; var x: Double = -12.0;
//        for (i in 0..10) {
//            series.appendData(DataPoint(x,y), true, 5000)
//            x += 1.0
//            y += 0.1
//        }
//
//        graph.addSeries(series)

        return binding.root

//        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


//
//        return root
    }
}