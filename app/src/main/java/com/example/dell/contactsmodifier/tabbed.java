package com.example.dell.contactsmodifier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

public class tabbed extends Fragment {

    List<product_pojo> proList;
    ListView lView;

    ProductAdapter ad;
    ProductHandler db;
    int a,b;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tabs, container, false);
        db=new ProductHandler(this.getActivity());
        a=db.getProductsCountWapp();
        b=db.getProductsCountOthers();
        Constants.tab1=a;
        Constants.tab2=b;

        final TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("WAPP("+a+")"));
        tabLayout.addTab(tabLayout.newTab().setText("OTHERS("+b+")"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        displaySelectedScreen(Constants.index);
        tabLayout.getTabAt(Constants.index).select();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Constants.tab1=db.getProductsCountWapp();
                Constants.tab2=db.getProductsCountOthers();
                Constants.index=tab.getPosition();

                tabLayout.getTabAt(0).setText("WAPP("+Constants.tab1+")");
                tabLayout.getTabAt(1).setText("OTHERS("+Constants.tab2+")");

                displaySelectedScreen(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                a=db.getProductsCountWapp();
                b=db.getProductsCountOthers();
                Constants.index=tab.getPosition();

                tabLayout.getTabAt(0).setText("WAPP("+Constants.tab1+")");
                tabLayout.getTabAt(1).setText("OTHERS("+Constants.tab2+")");

                displaySelectedScreen(tab.getPosition());

            }
        });
        return view;
    }

    public void displaySelectedScreen(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case 0:
                fragment = new Visible();
                break;

            case 1:
                fragment = new Invisible();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame1, fragment);
            ft.commit();
        }
    }
}
