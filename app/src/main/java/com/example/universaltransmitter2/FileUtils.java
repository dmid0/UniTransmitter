package com.example.universaltransmitter2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

public class FileUtils {

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        String selection = null;
        String[] selectionArgs;
        //download file
        if (isDownloadsDocument(uri)) {
            {
                Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        String fileName = cursor.getString(0);
                        String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                        if (!TextUtils.isEmpty(path)) {
                            return path;
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
            //media file
        } else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            } else if ("document".equals(type)) {
                contentUri = MediaStore.Files.getContentUri("external");
            }
            selection = "_id=?";
            selectionArgs = new String[]{split[1]};

            return getDataColumn(context, contentUri, selection,selectionArgs);
        }
        return selection;
    }

    private static String getDataColumn(Context context, Uri uri,
                                        String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}