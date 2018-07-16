package com.example.dell.contactsmodifier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;


public class Visible extends Fragment {

    List<product_pojo> proList;
    ListView lView;

    ProductAdapter ad;
    ProductHandler db;
    Button b;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.visible, container, false);


        lView=(ListView)view.findViewById(R.id.pList);
        db=new ProductHandler(this.getActivity());
        proList=db.getAllProductsWapp();
        ad=new ProductAdapter(this.getActivity(),proList);
        lView.setAdapter(ad);

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Contacts Modifier");
    }




}