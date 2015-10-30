package by.cooper.android.retailaccounting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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

        mRepo.requestItems(new ResultReceiver<Phone>() {
            @Override
            public void onReceive(List<Phone> itemList) {
                adapter.setPhones(itemList);
            }

            @Override
            public void onError(FirebaseError error) {
                Toast.makeText(HomeActivity.this, "error: " + error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });

        if (mAuthManager.isLoggedIn()) {
            Toast.makeText(this, "Login is Ok!", Toast.LENGTH_SHORT).show();
        }
    }

}
