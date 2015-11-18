package by.cooper.android.retailaccounting.fragment;

import android.os.Bundle;

import org.parceler.Parcels;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;


public class EditPhoneFragment extends BasePhoneFragment {

    public EditPhoneFragment() {

    }

    public static EditPhoneFragment newInstance(Phone phone) {
        EditPhoneFragment fragment = new EditPhoneFragment();
        Bundle args = new Bundle();
        args.putParcelable(PHONE, Parcels.wrap(phone));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected PhoneViewModel getViewModel() {
        return new PhoneViewModel(this, mPhone);
    }

    @Override
    protected int getTitle() {
        return R.string.title_edit_phone;
    }

    @Override
    protected int getMenu() {
        return R.menu.menu_edit_phone_fragment;
    }
}
