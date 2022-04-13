package com.appdroid.ssbtproject.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.appdroid.ssbtproject.Activity.Dashboard;
import com.appdroid.ssbtproject.Activity.ListingProducts;
import com.appdroid.ssbtproject.Adapter.SearchListAdepter;
import com.appdroid.ssbtproject.Database.UserPostsRoomDatabase;
import com.appdroid.ssbtproject.R;
import com.appdroid.ssbtproject.Room.SearchQueryRoomHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment {

    View view;
    EditText searchBar;
    SearchListAdepter searchListAdepter;
    List<SearchQueryRoomHolder> searchQueryRoomHolders;
    TextView clearHistory;
    LinearLayout searchTxtLayout;
    RecyclerView recyclerSearch;
    LottieAnimationView anim;
    ImageView search_voice_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        searchBar = view.findViewById(R.id.searchBar);
        search_voice_btn = view.findViewById(R.id.search_voice_btn);

        searchQueryRoomHolders = new ArrayList<>();
        searchListAdepter = new SearchListAdepter(getContext(),searchQueryRoomHolders);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        recyclerSearch.hasFixedSize();
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));

        clearHistory = view.findViewById(R.id.clearHistory);
        searchTxtLayout  = view.findViewById(R.id.searchTxtLayout);
        anim = view.findViewById(R.id.anim);

        getSearchHistory(recyclerSearch);

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //    Toast.makeText(MainActivity.this, ""+searchBar.getText().toString(), Toast.LENGTH_SHORT).show();

                    uploadSearchQuery(searchBar.getText().toString());

                    performActionForSearch(searchBar.getText().toString());
                    searchBar.setText("");
                    return true;
                }
                return false;
            }
        });

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserPostsRoomDatabase.getInstance(getContext()).postsDao().clearAllHistory();
                         getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchListAdepter.notifyItemRangeRemoved(0,searchQueryRoomHolders.size());
                                searchQueryRoomHolders.clear();
                                SearchListAdepter.searchListAll.clear();
                                searchTxtLayout.setVisibility(View.GONE);
                                anim.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                }).start();
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchListAdepter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search_voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSpeechInput();
            }
        });

        return view;
    }

    private void uploadSearchQuery(String toString) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            HashMap<String ,Object> hashMap = new HashMap<>();
            if (firebaseUser != null){
                hashMap.put("userID",FirebaseAuth.getInstance().getCurrentUser().getUid());
            }else {
                hashMap.put("userID","notLogin");
            }
            hashMap.put("date",new Timestamp(new Date()));
            hashMap.put("tag",toString);
            CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("SerchQuerys");
            firebaseFirestore.add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Log.d("DDDDDDDASDGADF", "onComplete: save "+toString);
                    }
                }
            });

    }

    public void getSpeechInput() {

        Locale locale = new Locale("mar_IN");


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,locale);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(getContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                   // searchBar.setText(result.get(0));
                    uploadSearchQuery(result.get(0));

                    performActionForSearch(result.get(0));
                    searchBar.setText("");
                }
                break;
        }
    }


    private void getSearchHistory(RecyclerView recyclerView) {


        Thread  thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                List<SearchQueryRoomHolder> RoomHolders = UserPostsRoomDatabase.getInstance(getActivity())
                        .postsDao()
                        .getAllSearchHistory();
                Collections.reverse(RoomHolders);

                searchQueryRoomHolders.clear();

                searchQueryRoomHolders.addAll(RoomHolders);




                Log.d("DDDDDDDDD", "run inside run : "+RoomHolders.size());


                /*if (i == 0){
                    savedPostAdapterWithRoomDatabase = new SavedPostAdapterWithRoomDatabase(localRoomSavePostsList,SavedPostsActivity.this);
                    recyclerView.setAdapter(savedPostAdapterWithRoomDatabase);
                    savedPostAdapterWithRoomDatabase.notifyDataSetChanged();
                }else if (i==1){
                    savedPostAdapterWithRoomDatabase.update(localRoomSavePostsList);
                    //  recyclerView.invalidate();
                }*/


                ((Dashboard) getActivity()).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        recyclerView.setAdapter(searchListAdepter);
                        // Stuff that updates the UI
                        if (RoomHolders.size()>0){
                            searchTxtLayout.setVisibility(View.VISIBLE);
                            anim.setVisibility(View.GONE);
                        }else {
                            searchTxtLayout.setVisibility(View.GONE);
                            anim.setVisibility(View.VISIBLE);
                        }
                        if (searchQueryRoomHolders.size()==0){
                            //noSearchHistory.setVisibility(View.VISIBLE);
                        }else {
                            //noSearchHistory.setVisibility(View.GONE);
                        }
                    }
                });

                searchListAdepter.notifyDataSetChanged();
            }
        });
        thread.start();

    /*    if (i == 1){
            savedPostAdapterWithRoomDatabase.notifyItemRangeChanged(0, localRoomSavePostsList.size());
        }*/
    }

    private void performActionForSearch(String query) {
        SearchQueryRoomHolder searchHolder = new SearchQueryRoomHolder();

        searchHolder.setQuery(query);

        SearchFragment.InsertAsnkTask insertAsnkTask = new InsertAsnkTask();
        insertAsnkTask.execute(searchHolder);

        Intent intent = new Intent(getContext(), ListingProducts.class);
        intent.setAction("fromSearch");
        intent.putExtra("flag",query);
        startActivity(intent);

        //  saveVideoEnter(videoHolder);

    }

    public class InsertAsnkTask extends AsyncTask<SearchQueryRoomHolder,Void,Void> {
        @Override
        protected Void doInBackground(SearchQueryRoomHolder... search) {
            UserPostsRoomDatabase.getInstance(getContext())
                    .postsDao()
                    .insertSearchItem(search[0]);

            return null;
        }
    }

}