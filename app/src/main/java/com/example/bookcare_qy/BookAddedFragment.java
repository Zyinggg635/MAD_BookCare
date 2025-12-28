package com.example.bookcare_qy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//hahah

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookAddedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookAddedFragment extends Fragment {

    public BookAddedFragment() {}

    public static BookAddedFragment newInstance(String param1, String param2) {
        BookAddedFragment fragment = new BookAddedFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static BookAddedFragment newInstance() {
        return new BookAddedFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_book_added, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        Button btnBackHome = view.findViewById(R.id.btnBackToHome);
        btnBackHome.setOnClickListener(v -> 
                navController.navigate(R.id.action_bookAddedFragment_to_navigation_home)
        );
    }
}
