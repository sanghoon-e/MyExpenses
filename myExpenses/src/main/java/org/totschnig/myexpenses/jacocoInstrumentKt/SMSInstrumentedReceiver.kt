package org.totschnig.myexpenses.jacocoInstrumentKt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import java.io.File


class SMSInstrumentedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // write the coverage file to the internal storage, which will not require any permissions
        // see: https://developer.android.com/training/data-storage/files
        // the output dir usually locates at: /data/data/#{app_package_name}/files/coverage.ec

        // This api has been deprecated since Q os(Android 10)
        /*
        File coverageFile = new File(context.getFilesDir(), "coverage.ec");
        String coverageFilePath = coverageFile.getAbsolutePath();
        */
        val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadDir.exists()) {
            downloadDir.mkdirs()
        }

        val coverageFileName = "coverage.ec"
        val coverageFilePath = File(downloadDir.toString() + File.separator + coverageFileName)
        Log.i("COV_KT", "checking coverage path : $coverageFilePath")

        val mListener: FinishListener = JacocoInstrumentation(coverageFilePath.absolutePath)
        mListener.dumpIntermediateCoverage(coverageFilePath.absolutePath)
    }
}

