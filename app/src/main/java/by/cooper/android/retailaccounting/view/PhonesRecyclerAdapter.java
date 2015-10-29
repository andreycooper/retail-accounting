package by.cooper.android.retailaccounting.view;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import by.cooper.android.retailaccounting.BR;
import by.cooper.android.retailaccounting.R;
import by.cooper.android.retailaccounting.model.Phone;
import by.cooper.android.retailaccounting.viewmodel.PhoneViewModel;


public class PhonesRecyclerAdapter extends RecyclerView.Adapter<PhonesRecyclerAdapter.PhoneBindingHolder> {

    private List<Phone> mPhones;

    public PhonesRecyclerAdapter(List<Phone> phones) {
        mPhones = phones;
    }

    @Override
    public PhoneBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_item, parent, false);
        return new PhoneBindingHolder(view);
    }

    @Override
    public void onBindViewHolder(PhoneBindingHolder holder, int position) {
        final Phone phone = mPhones.get(position);
        final PhoneViewModel viewModel = new PhoneViewModel(phone);
        holder.getBinding().setVariable(BR.phoneViewModel, viewModel);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mPhones.size();
    }

    public void setPhones(List<Phone> phones) {
        mPhones.clear();
        mPhones.addAll(phones);
        notifyDataSetChanged();
    }

    public static class PhoneBindingHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;

        public PhoneBindingHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
