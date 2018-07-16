package com.example.dell.contactsmodifier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class Invisible extends Fragment{
    private List<product_pojo> proList;
    private ListView lView;
    private ProductAdapter2 ad;
    ProductHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.invisible, container, false);
        lView=(ListView)view.findViewById(R.id.pList1);
        db=new ProductHandler(this.getActivity());
        proList=db.getAllProductsOthers();
        ad=new ProductAdapter2(this.getActivity(),proList);
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
