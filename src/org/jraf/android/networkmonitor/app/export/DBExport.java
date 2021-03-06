/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2013 Carmen Alvarez (c@rmen.ca)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jraf.android.networkmonitor.app.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

import org.jraf.android.networkmonitor.Constants;
import org.jraf.android.networkmonitor.provider.NetMonDatabase;

/**
 * Export the Network Monitor DB file.
 */
public class DBExport extends FileExport {
    private static final String TAG = Constants.TAG + DBExport.class.getSimpleName();

    public DBExport(Context context, ExportProgressListener listener) throws FileNotFoundException {
        super(context, new File(context.getExternalFilesDir(null), NetMonDatabase.DATABASE_NAME), listener);
    }

    @Override
    public File export() {
        File db = mContext.getDatabasePath(NetMonDatabase.DATABASE_NAME);
        try {
            InputStream is = new FileInputStream(db);
            OutputStream os = new FileOutputStream(mFile);
            int fileSize = (int) db.length();
            byte[] buffer = new byte[1024];
            int len;
            int bytesWritten = 0;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
                bytesWritten += len;
                // Notify the listener about the number of kb written.
                if (mListener != null) mListener.onExportProgress(bytesWritten / 1000, fileSize / 1000);
            }
            is.close();
            os.close();
            return mFile;
        } catch (IOException e) {
            Log.v(TAG, "Could not copy DB file: " + e.getMessage(), e);
            return null;
        }
    }

}