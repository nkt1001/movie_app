package com.dgtprm.nkt10.movieapp.sql;

import android.content.SearchRecentSuggestionsProvider;

public class SuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.dgtprm.nkt10.movieapp.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
