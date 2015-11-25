package by.cooper.android.retailaccounting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.cooper.android.retailaccounting.App;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.firebase.PhonesRepository;
import by.cooper.android.retailaccounting.firebase.ResultReceiver;
import by.cooper.android.retailaccounting.firebase.auth.AuthManager;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.view.PhonesRecyclerAdapter;

public class HomeActivity extends AppCompatActivity {

    @Inject
    AuthManager mAuthManager;
    @Inject
    PhonesRepository mRepo;


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.phones_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.get(this).getPhonesComponent().inject(this);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> startActivity(new Intent(HomeActivity.this, PhoneActivity.class)));

        PhonesRecyclerAdapter adapter = new PhonesRecyclerAdapter(new ArrayList<>());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (mAuthManager.isLoggedIn()) {
            Log.d("HomeActivity", "Login is Ok, request items");
            mRepo.requestItems(new ResultReceiver<Phone>() {
                @Override
                public void onReceive(List<Phone> itemList) {
                    adapter.setPhones(itemList);
                }

                @Override
                public void onError(FirebaseError error) {
                    Snackbar.make(HomeActivity.this.findViewById(R.id.home_layout),
                            HomeActivity.this.getString(R.string.error_loading, error.getDetails()),
                            Snackbar.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.d("HomeActivity", "Login is failed!");
            // TODO: notify user and start login screen
        }
    }

}
