package org.totschnig.myexpenses.jacocoInstrumentKt

import android.app.Instrumentation
import android.os.Bundle
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class JacocoInstrumentation(private var coveragePath: String) : Instrumentation(), FinishListener {
    private var mCoverage = true
    private var mCoverageFilePath: String? = null
    private var mDefaultCoverageFilePath: String = ""

    init {
        if (coveragePath.isEmpty()) {
            throw IllegalArgumentException("Error")
        }
        this.mDefaultCoverageFilePath = coveragePath
    }

    override fun onCreate(arguments: Bundle) {
        Log.d(TAG, "onCreate($arguments)")
        super.onCreate(arguments)
        mCoverage = getBooleanArgument(arguments, "coverage")
        mCoverageFilePath = arguments.getString("coverageFile")
        start()
    }

    private fun getBooleanArgument(arguments: Bundle, tag: String): Boolean {
        val tagString = arguments.getString(tag)
        return tagString != null && java.lang.Boolean.parseBoolean(tagString)
    }

    private fun getCoverageFilePath(): String {
        if(mCoverageFilePath == null) {
            return mDefaultCoverageFilePath
        }
        else {
            return mCoverageFilePath as String
        }
    }

    // use java reflection to dump coverage report
    private fun generateCoverageReport() {
        Log.d(TAG, "generateCoverageReport(): " + getCoverageFilePath() )
        var out: OutputStream? = null
        try {
            out = FileOutputStream(getCoverageFilePath(), false)
            val agent = Class.forName("org.jacoco.agent.rt.RT").getMethod("getAgent")
                    .invoke(null)
            //https://www.jacoco.org/jacoco/trunk/doc/api/index.html
            //reset - if true the current execution data is cleared afterwards
            out.write(agent.javaClass.getMethod("getExecutionData", Boolean::class.javaPrimitiveType)
                    .invoke(agent, false) as ByteArray)
        } catch (e: Exception) {
            Log.d(TAG, e.toString(), e)
        } finally {
            if (out != null) {
                try {
                    out.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    // set the coverage file path
    private fun setCoverageFilePath(filePath: String?): Boolean {
        if (filePath != null && filePath.length > 0) {
            mCoverageFilePath = filePath
            return true
        }
        return false
    }

    override fun dumpIntermediateCoverage(filePath: String) {
        if (LOGD) {
            Log.d(TAG, "Intermediate Dump Called with file name :$filePath")
        }
        if (mCoverage) {
            if (!setCoverageFilePath(filePath)) {
                if (LOGD) {
                    Log.d(TAG, "Unable to set the given file path:$filePath as dump target.")
                }
            }
            generateCoverageReport()
            setCoverageFilePath(mDefaultCoverageFilePath)
        }
    }

    companion object {
        var TAG = "JacocoInstrumentation:"
        private const val LOGD = true
    }
}
