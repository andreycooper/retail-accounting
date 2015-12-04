package by.cooper.android.retailaccounting.fragment;

import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;


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
        return R.menu.menu_add_phone_fragment;
    }

    @Override
    protected PhoneViewModel getViewModel() {
        return new PhoneViewModel(this);
    }

}
