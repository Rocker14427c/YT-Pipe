package org.schabi.newpipe.settings

import android.content.SharedPreferences
import android.util.Log
import org.schabi.newpipe.streams.io.SharpOutputStream
import org.schabi.newpipe.streams.io.StoredFileHelper
import org.schabi.newpipe.util.ZipHelper
import java.io.BufferedOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.zip.ZipOutputStream

class ContentSettingsManager(private val fileLocator: NewPipeFileLocator) {
    companion object {
        const val TAG = "ContentSetManager"
        const val UI_PRESET_ENTRY = "pipepipe.ui-preset"
    }

    /**
     * Exports given [SharedPreferences] to the file in given outputPath.
     * It also creates the file.
     */
    @Throws(Exception::class)
    fun exportDatabase(preferences: SharedPreferences, file: StoredFileHelper) {
        file.create()
        ZipOutputStream(BufferedOutputStream(SharpOutputStream(file.stream)))
            .use { outZip ->
                ZipHelper.addFileToZip(outZip, fileLocator.db.path, "newpipe.db")

                try {
                    ObjectOutputStream(FileOutputStream(fileLocator.settings)).use { output ->
                        output.writeObject(preferences.all)
                        output.flush()
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "Unable to exportDatabase", e)
                }

                ZipHelper.addFileToZip(outZip, fileLocator.settings.path, "newpipe.settings")
            }
    }

    fun deleteSettingsFile() {
        fileLocator.settings.delete()
    }

    /**
     * Tries to create database directory if it does not exist.
     *
     * @return Whether the directory exists afterwards.
     */
    fun ensureDbDirectoryExists(): Boolean {
        return fileLocator.dbDir.exists() || fileLocator.dbDir.mkdir()
    }

    /**
     * Returns true for the safe settings-only preset format. Presets contain a marker and
     * intentionally omit newpipe.db, so a user's subscriptions, history and playlists remain
     * untouched when they import a visual configuration.
     */
    fun isUiPreset(file: StoredFileHelper): Boolean {
        return ZipHelper.containsEntry(file, UI_PRESET_ENTRY)
    }

    fun extractDb(file: StoredFileHelper): Boolean {
        val success = ZipHelper.extractFileFromZip(file, fileLocator.db.path, "newpipe.db")
        if (success) {
            fileLocator.dbJournal.delete()
            fileLocator.dbWal.delete()
            fileLocator.dbShm.delete()
        }

        return success
    }

    fun extractSettings(file: StoredFileHelper): Boolean {
        return ZipHelper.extractFileFromZip(file, fileLocator.settings.path, "newpipe.settings")
    }

    fun loadSharedPreferences(preferences: SharedPreferences) {
        loadSharedPreferences(preferences, false)
    }

    /**
     * Loads settings from an exported map. Full backups replace the preference store as before;
     * a UI preset merges only the keys included in the preset and leaves account, path and
     * service-specific values intact.
     */
    fun loadSharedPreferences(preferences: SharedPreferences, merge: Boolean) {
        try {
            val preferenceEditor = preferences.edit()
            if (!merge) {
                preferenceEditor.clear()
            }

            ObjectInputStream(FileInputStream(fileLocator.settings)).use { input ->
                @Suppress("UNCHECKED_CAST")
                val entries = input.readObject() as Map<String, *>
                for ((key, value) in entries) {
                    when (value) {
                        is Boolean -> {
                            preferenceEditor.putBoolean(key, value)
                        }
                        is Float -> {
                            preferenceEditor.putFloat(key, value)
                        }
                        is Int -> {
                            preferenceEditor.putInt(key, value)
                        }
                        is Long -> {
                            preferenceEditor.putLong(key, value)
                        }
                        is String -> {
                            preferenceEditor.putString(key, value)
                        }
                        is Set<*> -> {
                            // There are currently only Sets with type String possible
                            @Suppress("UNCHECKED_CAST")
                            preferenceEditor.putStringSet(key, value as Set<String>?)
                        }
                    }
                }
                preferenceEditor.commit()
            }
        } catch (e: IOException) {
            Log.e(TAG, "Unable to loadSharedPreferences", e)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "Unable to loadSharedPreferences", e)
        }
    }
}
