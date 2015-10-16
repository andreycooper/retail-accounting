package by.cooper.android.retailaccounting.fragment;

import android.util.Log;
import android.view.MenuItem;

import by.cooper.android.retailaccounting.R;


public class AddPhoneFragment extends BasePhoneFragment {

    public AddPhoneFragment() {

    }

    public static AddPhoneFragment newInstance() {
        return new AddPhoneFragment();
    }

    @Override
    protected int getTitle() {
        return R.string.title_add_phone;
    }

    @Override
    protected int getMenu() {
        return R.menu.menu_edit_phone_fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                // TODO: implement DONE button
                Log.d("AddPhoneFragment", "DONE clicked!");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
