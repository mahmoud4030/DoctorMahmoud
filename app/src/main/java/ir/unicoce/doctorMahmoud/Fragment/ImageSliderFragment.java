package ir.unicoce.doctorMahmoud.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.unicoce.doctorMahmoud.Adapter.ViewPagerAdapter_ImageSlider;
import ir.unicoce.doctorMahmoud.AsyncTasks.GetSlider;
import ir.unicoce.doctorMahmoud.Classes.Internet;
import ir.unicoce.doctorMahmoud.Classes.Variables;
import ir.unicoce.doctorMahmoud.Database.db_details;
import ir.unicoce.doctorMahmoud.Interface.IWebservice;
import ir.unicoce.doctorMahmoud.Interface.OnFragmentInteractionListener;
import ir.unicoce.doctorMahmoud.Objects.Object_Data;
import ir.unicoce.doctorMahmoud.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ImageSliderFragment extends Fragment implements IWebservice {

    private ViewGroup layout;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private int page = 1;
    private Timer timer;
    private ViewPager vp;
    private List<String> myList = new ArrayList<>();
    private Typeface San;

    private OnFragmentInteractionListener mListener;

    public ImageSliderFragment() {}

    public static ImageSliderFragment newInstance(String param1, String param2) {
        ImageSliderFragment fragment = new ImageSliderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        San = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SansLight.ttf");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (ViewGroup) inflater.inflate(R.layout.fragment_image_slider, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vp = (ViewPager) layout.findViewById(R.id.viewPager);
        // agar net dasht az net begire
        if(Internet.isNetworkAvailable(getActivity())){
            // dar get result adapter set shavad
            GetSlider getdata = new GetSlider(getActivity(),this,Variables.getSliders);
            getdata.execute();
        }
        else {
            // aval DB ra negah konad , age chizi dasht onaro neshoon bede
            List<db_details> list = Select.from(db_details.class).list();
            if(list.size()!=0){
                String[] tmp = list.get(0).getFiles().split(",");
                int size = Arrays.asList(tmp).size();
                    // parse kardane url ha dar urls
                    for (int i = 0; i < size; i+=2) {
                        myList.add(tmp[i]);
                    }
                    vp.setAdapter(new ViewPagerAdapter_ImageSlider(getActivity(),myList,false));
            }
            else
            vp.setAdapter(new ViewPagerAdapter_ImageSlider(getActivity(),myList,true));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
    }

    public void onButtonPressed(int tagNumber) {
        if (mListener != null) {
            mListener.onFragmentInteraction(tagNumber);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pageSwitcher(4);
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        timer.purge();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void pageSwitcher(int seconds) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000);
    }

    @Override
    public void getResult(Object result) throws Exception {
        vp = (ViewPager) layout.findViewById(R.id.viewPager);
        ArrayList<Object_Data> objectDataArrayList = (ArrayList<Object_Data>) result;

        String[] tmp = objectDataArrayList.get(0).getFiles().split(",");
        int size = Arrays.asList(tmp).size();
        if(size<2){
            // neshan dadan e offline e slide ha
            vp.setAdapter(new ViewPagerAdapter_ImageSlider(getActivity(),myList,true));
        }
        else {
            // parse kardane url ha dar urls

            for (int i = 0; i < size; i+=2) {
                myList.add(tmp[i]);
            }
            vp.setAdapter(new ViewPagerAdapter_ImageSlider(getActivity(),myList,false));
         }
    }

    @Override
    public void getError(String ErrorCodeTitle) throws Exception {

    }

    class RemindTask extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (page > 4) {
                        page = 0;
                        vp.setCurrentItem(page++);
                    } else {
                        Log.i(Variables.Tag,"in run");
                        vp.setCurrentItem(page++);
                    }
                }
            });
        }// end run()
    }// end RemindTask{}

}// end class