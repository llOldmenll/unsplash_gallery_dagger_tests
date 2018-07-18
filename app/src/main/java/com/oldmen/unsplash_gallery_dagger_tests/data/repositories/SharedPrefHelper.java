package com.oldmen.unsplash_gallery_dagger_tests.data.repositories;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefHelper {
    private static String CURRENT_POSITION_KEY = "CURRENT_POSITION_KEY";
    private static String CURRENT_PAGE_KEY = "CURRENT_PAGE_KEY";
    private static String PAGES_NUMBER_KEY = "PAGES_NUMBER_KEY";
    private static String CURRENT_SEARCH_QUERY_KEY = "CURRENT_SEARCH_QUERY_KEY";
    private static String FOOTER_STATE_KEY = "FOOTER_STATE_KEY";
    private static String USER_KEY = "UserSession";

    private Context mContext;

    public SharedPrefHelper(Context context) {
        mContext = context;
    }

    public int getCurrentPosition() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CURRENT_POSITION_KEY, 0);
    }

    public void saveCurrentPosition(int currentImgPosition) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CURRENT_POSITION_KEY, currentImgPosition).apply();
    }

    public int getCurrentPage() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CURRENT_PAGE_KEY, 0);
    }

    public void saveCurrentPage(int currentPage) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(CURRENT_PAGE_KEY, currentPage).apply();
    }

    public int getPagesNumber() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(PAGES_NUMBER_KEY, 1);
    }

    public void savePagesNumber(int pagesNumber) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(PAGES_NUMBER_KEY, pagesNumber).apply();
    }

    public String getSearchQuery() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CURRENT_SEARCH_QUERY_KEY, "");
    }

    public void saveSearchQuery(String searchQuery) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(CURRENT_SEARCH_QUERY_KEY, searchQuery).apply();
    }

    public boolean getFooterState() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(FOOTER_STATE_KEY, true);
    }

    public void saveFooterState(boolean footerState) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(FOOTER_STATE_KEY, footerState).apply();
    }

    public void removeAll() {
        mContext.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE).edit().clear().apply();
    }

}
