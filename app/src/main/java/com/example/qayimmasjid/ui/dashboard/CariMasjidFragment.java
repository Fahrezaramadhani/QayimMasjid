package com.example.qayimmasjid.ui.dashboard;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.qayimmasjid.PaginationScrollListener;
import com.example.qayimmasjid.masjid.MasjidViewModel;
import com.example.qayimmasjid.R;
import com.example.qayimmasjid.adapter.MasjidTerdekatAdapter;
import com.example.qayimmasjid.model.masjid.Masjid;
import com.example.qayimmasjid.model.masjid.MasjidList;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CariMasjidFragment extends Fragment {
    private RecyclerView recyclerView;
    private MasjidTerdekatAdapter mMasjidTerdekatAdapter;
    private ArrayList<Masjid> mMasjidList=new ArrayList<Masjid>();
    private List<Masjid> listMasjid;
    private MasjidViewModel masjidViewModel;
    public View ftView;
    private double currentLatitude, currentLongitude;
    public EditText et_search;
    public String keyword = "";
    public List<Masjid> listTerurut;

    public boolean isLoading;
    public boolean isLastPage;
    private int currentPage = 1;
    private int totalPage;
    private ProgressBar progressBar;

    private AutoCompleteTextView autoCompleteTextView;
    private TextInputLayout textInputLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cari_masjid, container, false);

        et_search = view.findViewById(R.id.editText);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        getCurrentLocation();

        masjidViewModel = new ViewModelProvider(requireActivity()).get(MasjidViewModel.class);
        masjidViewModel.getListKegiatan(getContext());

        ftView = inflater.inflate(R.layout.footer_view, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_masjid);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        mMasjidTerdekatAdapter = new MasjidTerdekatAdapter();

        recyclerView.setAdapter(mMasjidTerdekatAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        getListMasjid(1);
//        searchMasjid(1, keyword);


        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);


        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading = true;
                currentPage += 1;
//                getListMasjid(2);
//                searchMasjid(2,keyword);
                filterMasjid(2, keyword, autoCompleteTextView.getText().toString());
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });


        autoCompleteTextView = view.findViewById(R.id.field_filter_masjid);
        textInputLayout = view.findViewById(R.id.til);

        // Set Value for Spinner
        final String[] filterMasjid = getResources().getStringArray(R.array.filter_masjid);
        final ArrayAdapter arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, filterMasjid);
        autoCompleteTextView.setAdapter(arrayAdapter);

        filterMasjid(1, keyword, autoCompleteTextView.getText().toString());


        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    textInputLayout.setStartIconVisible(false);
                } else {
                    textInputLayout.setStartIconVisible(true);
                }
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filterMasjid(1, keyword, autoCompleteTextView.getText().toString());
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
//                searchMasjid(1, s.toString());
                filterMasjid(1, s.toString(), autoCompleteTextView.getText().toString());
            }
        });
        return view;
    }

    private void filterMasjid(int type, String key, String filter){
        masjidViewModel.searchMasjid(key);
        masjidViewModel.getList().observe(getViewLifecycleOwner(), masjids -> {
            setDistance(masjids.getMmasjid());
            if (filter.matches("Terdekat")){
                sortAscending(masjids);
            }
            else if (filter.matches("Terjauh")){
                sortDescending(masjids);
            }
            if (masjids != null){
                totalPage = masjids.getMmasjid().size()/10 + 1;
                if (type == 1){
                    List<Masjid> list = new ArrayList<>();
                    if (masjids.getMmasjid().size() > 10){
                        double distance;
                        for (int i = 0; i < 10; i++){
//                        Log.d("current from get", "getListMasjid: " + currentLatitude);
//                            distance = getDistance(masjids.getMmasjid().get(i).getMlatidue(), masjids.getMmasjid().get(i).getMlongitude());
//                            Log.d("distance", "getListMasjid: " + distance);
//                            masjids.getMmasjid().get(i).setDistance(distance);
                            list.add(masjids.getMmasjid().get(i));
                        }
                        listMasjid = list;
//                        setDistance(listMasjid);
                        mMasjidTerdekatAdapter.setData(listMasjid);
                        Log.d("List", "setFirstData21321: " + listMasjid);
                        recyclerView.setAdapter(mMasjidTerdekatAdapter);

                        if (currentPage < totalPage) {
                            mMasjidTerdekatAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }else {
                        double distance;
                        for (int i = 0; i < masjids.getMmasjid().size(); i++){
//                            distance = getDistance(masjids.getMmasjid().get(i).getMlatidue(), masjids.getMmasjid().get(i).getMlongitude());
//                            Log.d("distance", "getListMasjid: " + distance);
//                            masjids.getMmasjid().get(i).setDistance(distance);
                            list.add(masjids.getMmasjid().get(i));
                        }
                        listMasjid = list;
                        mMasjidTerdekatAdapter.setData(listMasjid);
                        Log.d("List", "setFirstData21321: " + listMasjid);
                        recyclerView.setAdapter(mMasjidTerdekatAdapter);

                        if (currentPage < totalPage) {
                            mMasjidTerdekatAdapter.addFooterLoading();
                        } else {
                            isLastPage = true;
                        }
                    }
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            double distance;
                            List<Masjid> list1 = masjids.getMmasjid();
                            List<Masjid> list2 = new ArrayList<>();
                            for (int i = 1; i < list1.size(); i++){
                                if (!findMasjid(list1.get(i), listMasjid)){
                                    if (list2.size() != 10){
//                                        distance = getDistance(list1.get(i).getMlatidue(), list1.get(i).getMlongitude());
//                                        Log.d("distance", "getListMasjid: " + distance);
//                                        list1.get(i).setDistance(distance);
                                        list2.add(list1.get(i));
                                    }
                                }
                            }

                            mMasjidTerdekatAdapter.removeFooterLoading();
                            listMasjid.addAll(list2);
                            mMasjidTerdekatAdapter.notifyDataSetChanged();

                            isLoading = false;
                            if (currentPage < totalPage) {
                                mMasjidTerdekatAdapter.addFooterLoading();
                            } else {
                                isLastPage = true;
                            }
                        }
                    }, 2000);
                }
            }
        });
    }

    private boolean findMasjid(Masjid masjid, List<Masjid> list) {
        for (int i = 0; i < list.size(); i++) {
            if (masjid.getMid_masjid() == list.get(i).getMid_masjid()){
                return true;
            }
        }
        return false;
    }

    private double getDistance (double latitude, double longitude){
        double distance;
        LatLng latLngFrom = new LatLng(currentLatitude, currentLongitude);
        Log.d("current from get", "getDistance: " + currentLatitude);
        LatLng latLngGo = new LatLng(latitude, longitude);

        distance = SphericalUtil.computeDistanceBetween(latLngFrom, latLngGo);

        distance = distance/1000;

        BigDecimal bd = new BigDecimal(distance).setScale(2, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    private void setDistance (List<Masjid> listMasjid){
        for (int i = 0; i < listMasjid.size(); i++){
            listMasjid.get(i).setDistance(getDistance(listMasjid.get(i).getMlatidue(), listMasjid.get(i).getMlongitude()));
        }
    }

    private void sortAscending (MasjidList masjidList){
        for (int i = 0; i < masjidList.getMmasjid().size(); i++){
            for (int j = i + 1; j < masjidList.getMmasjid().size(); j++){
                Masjid temp = new Masjid();
                if (masjidList.getMmasjid().get(i).getDistance() > masjidList.getMmasjid().get(j).getDistance()){
                    Log.d("sort", "sortDescending: " + masjidList.getMmasjid().get(i).getDistance());
                    Log.d("sort", "sortDescending: " + masjidList.getMmasjid().get(j).getDistance());
                    temp = masjidList.getMmasjid().get(i);
                    masjidList.getMmasjid().set(i, masjidList.getMmasjid().get(j));
                    masjidList.getMmasjid().set(j, temp);
                }
            }
        }
    }

    private void sortDescending (MasjidList masjidList){
        for (int i = 0; i < masjidList.getMmasjid().size(); i++){
            for (int j = i + 1; j < masjidList.getMmasjid().size(); j++){
                Masjid temp = new Masjid();
                if (masjidList.getMmasjid().get(i).getDistance() < masjidList.getMmasjid().get(j).getDistance()){
                    Log.d("sort", "sortDescending: " + masjidList.getMmasjid().get(i).getDistance());
                    Log.d("sort", "sortDescending: " + masjidList.getMmasjid().get(j).getDistance());
                    temp = masjidList.getMmasjid().get(i);
                    masjidList.getMmasjid().set(i, masjidList.getMmasjid().get(j));
                    masjidList.getMmasjid().set(j, temp);
                }
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                    try {
                        List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        currentLatitude = address.get(0).getLatitude();
                        Log.d("current", "onComplete: " + currentLatitude);
                        currentLongitude = address.get(0).getLongitude();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}