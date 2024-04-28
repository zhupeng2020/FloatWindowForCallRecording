package com.pri.recordview.search;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.SearchIndexableResource;
import android.provider.SearchIndexablesContract;
import android.provider.SearchIndexablesProvider;

import com.pri.recordview.R;
import com.pri.recordview.activity.MainActivity;

import static android.provider.SearchIndexablesContract.INDEXABLES_RAW_COLUMNS;
import static android.provider.SearchIndexablesContract.INDEXABLES_XML_RES_COLUMNS;
import static android.provider.SearchIndexablesContract.NON_INDEXABLES_KEYS_COLUMNS;

public class RecordViewSearchIndexablesProvider extends SearchIndexablesProvider {
    private static final int IGNORED_RANK = 2112;
    private static final int NO_ICON_ID = 0;
    private static final SearchIndexableResource[] INDEXABLE_RES = new SearchIndexableResource[]{
            new SearchIndexableResource(IGNORED_RANK, R.xml.screen_record_search,
                    MainActivity.class.getName(),
                    NO_ICON_ID)
    };

    @Override
    public Cursor queryXmlResources(String[] projection) {
        MatrixCursor cursor = new MatrixCursor(INDEXABLES_XML_RES_COLUMNS);
        for (SearchIndexableResource res : INDEXABLE_RES) {
            cursor.newRow()
                    .add(SearchIndexablesContract.XmlResource.COLUMN_RANK, res.rank)
                    .add(SearchIndexablesContract.XmlResource.COLUMN_XML_RESID, res.xmlResId)
                    .add(SearchIndexablesContract.XmlResource.COLUMN_CLASS_NAME, null)
                    .add(SearchIndexablesContract.XmlResource.COLUMN_ICON_RESID, res.iconResId)
                    .add(SearchIndexablesContract.XmlResource.COLUMN_INTENT_ACTION, "android.intent.action.MAIN")
                    .add(SearchIndexablesContract.XmlResource.COLUMN_INTENT_TARGET_PACKAGE, "com.pri.recordview")
                    .add(SearchIndexablesContract.XmlResource.COLUMN_INTENT_TARGET_CLASS, res.className);
        }

        return cursor;
    }

    @Override
    public Cursor queryRawData(String[] projection) {
        return new MatrixCursor(INDEXABLES_RAW_COLUMNS);
    }

    @Override
    public Cursor queryNonIndexableKeys(String[] projection) {
        return new MatrixCursor(NON_INDEXABLES_KEYS_COLUMNS);
    }

    @Override
    public boolean onCreate() {
        return true;
    }
}