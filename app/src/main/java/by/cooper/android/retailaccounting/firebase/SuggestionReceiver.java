package by.cooper.android.retailaccounting.firebase;


import java.util.List;

public interface SuggestionReceiver {
    void onBrandsReceived(List<String> brandList);

    void onModelsReceived(List<String> modelList);

    void clearSuggestions();

}
