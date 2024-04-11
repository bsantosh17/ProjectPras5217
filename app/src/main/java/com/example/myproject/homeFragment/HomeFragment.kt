package com.example.cloudcatch.ui.fragments.homeFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cloudcatch.R
import com.example.cloudcatch.apiServer.ApiInterface
import com.example.cloudcatch.apiServer.RetrofitInstance
import com.example.cloudcatch.ui.dashboard.DashboardActivity
import com.example.cloudcatch.ui.fragments.homeFragment.adapter.AdapterDashboard
import com.example.cloudcatch.ui.fragments.homeFragment.model.DashboardData
import com.example.cloudcatch.ui.fragments.homeFragment.model.ModelDashboard
import com.example.cloudcatch.ui.fragments.homeFragment.model.ResponseDashboard
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

//Fragment for dashboard
class HomeFragment : Fragment() {

    private lateinit var txtSpanDown:TextView
    private lateinit var txtSpanDownWIP1:TextView
    private lateinit var txtSpanDownWIP2:TextView
    private lateinit var txtSpanDownWIP3:TextView
    private lateinit var txtTHREAT:TextView
    private lateinit var txtDown:TextView
    private lateinit var txtRestore:TextView
    private lateinit var txtCleared:TextView
    private lateinit var txtTotalCallExpect:TextView
    private lateinit var txtActualCallMade:TextView
    private lateinit var txtWithinSLA:TextView
    private lateinit var txtWithinSLATime:TextView
    private lateinit var txtOutOfSLA:TextView
    private lateinit var txtOutOfSLATime:TextView
    private lateinit var txtSmartDeskAvgSLA:TextView

    private lateinit var edtStartDate:EditText
    private lateinit var edtEndDate:EditText
    private lateinit var btnSearch: Button
    private lateinit var btnResetfilter: Button



    //Default start and end date is current Date
    private var strStartDate:String = ""
    private var strEndDate:String = ""
    private var currentDate:String = ""

    private lateinit var rvDashboard: RecyclerView
    private lateinit var arrayList: ArrayList<DashboardData>

    //SharePref for Employer and Candidate
    lateinit var  sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private val  sharedPrefName = "login"
    private var BASE_URL:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        sharedPreferences = requireContext().getSharedPreferences(sharedPrefName, AppCompatActivity.MODE_PRIVATE)
        BASE_URL = sharedPreferences.getString("BASE_URL",null).toString()
        //toastMessage(""+BASE_URL)


        edtStartDate = view.findViewById(R.id.edtStartDate)
        edtEndDate = view.findViewById(R.id.edtEndDate)
        btnSearch = view.findViewById(R.id.btnSearch)
        btnResetfilter = view.findViewById(R.id.btnResetfilter)

        rvDashboard = view.findViewById(R.id.rvDashboard)
        rvDashboard.layoutManager = LinearLayoutManager(context)
        rvDashboard.setHasFixedSize(true)
        arrayList = arrayListOf()


        //Current Date
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdf.format(Date())

        getDashboardData(currentDate,currentDate)
        getDateRangeData()

        //Search Button
        btnSearch.setOnClickListener(View.OnClickListener {
            searchIntegration()
        })

        //Reset Filter Button
        btnResetfilter.setOnClickListener(View.OnClickListener {
            startActivity(Intent(context,DashboardActivity::class.java))
        })

        return view
    }

    //get dashboard Default Data
    private fun getDashboardData(startDate:String,endDate:String){

        val progress1 = ProgressDialog(context)
        progress1.setTitle("Wait...")
        progress1.show()
        val retrofitInstance = RetrofitInstance(requireContext(),BASE_URL).getRetrofitInstance().create(ApiInterface::class.java)
        val dashboardModel = ModelDashboard(startDate,endDate)
        retrofitInstance.dashboardData(dashboardModel).enqueue(object : Callback<ResponseDashboard> {
            override fun onFailure(call: Call<ResponseDashboard>, t: Throwable) {
                progress1.dismiss()
                toastMessage("Network Error...")

            }

            override fun onResponse(call: Call<ResponseDashboard>, response: Response<ResponseDashboard>) {
                progress1.dismiss()
                val respCollect = response.body()!!
                //toastMessage(""+respCollect.data)
                    if(respCollect.data.isEmpty()){
                        toastMessage("No Data Found")
                    }else {
                        for (i in respCollect.data) {
                          val strCount = i.count
                          val strTitle = i.title
                          val strBGColour = i.background_color

                            val collectData = DashboardData(strBGColour,strCount,strTitle)
                            arrayList.add(collectData)
                        }
                        rvDashboard.adapter = context?.let { AdapterDashboard(it, arrayList) }
                    }

                }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() : HomeFragment {
                return HomeFragment()
            }
    }

    //Select Date to fetch record
    private + getDateRangeData(){
        edtStartDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd =
                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        edtStartDate.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                        //edtStartDate.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)
                        strStartDate =  ""+year + "-" + (monthOfYear+1)+"-"+dayOfMonth
                    }, year, month, day)
                }
            // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
            dpd?.show()
        })

        edtEndDate.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd =
                context?.let { it1 ->
                    DatePickerDialog(it1, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                       //edtEndDate.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)
                       edtEndDate.setText("" + dayOfMonth + "-" + (monthOfYear+1) + "-" + year)
                        strEndDate =  ""+year + "-" + (monthOfYear+1)+"-"+dayOfMonth
                    }, year, month, day)
                }
            // dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
            dpd?.show()
        })

    }

    //Search Date Range Data
    private fun searchIntegration(){
        if(edtStartDate.text.toString().isEmpty() || edtEndDate.text.toString().isEmpty()){
            getDashboardData(currentDate,currentDate)
        }else{
            arrayList.clear()
            rvDashboard?.adapter?.notifyDataSetChanged()
            getDashboardData(strStartDate,strEndDate)
        }
    }

    //Toast Message
    private fun toastMessage(msg:String){
        Toast.makeText(context,""+msg, Toast.LENGTH_LONG).show()
    }
}