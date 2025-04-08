package com.harissabil.damome.core.object_box

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.objectbox.BoxStore
import org.apache.commons.io.comparator.LastModifiedFileComparator
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

class ObjectBoxBackup(private val context: Context) {

    companion object {
        private const val TAG = "ObjectBoxBackup"
        private lateinit var INTERNAL_BACKUP_PATH: File
        private lateinit var DATABASE_PATH: File
        private const val DATABASE_NAME = "data.mdb"
        private const val LOCK_FILE_NAME = "data.mdb.lock"

        private var currentProcess: Int? = null
        private const val PROCESS_BACKUP = 1
        private const val PROCESS_RESTORE = 2
        private var backupFilename: String? = null

        const val BACKUP_LOCATION_INTERNAL = 1
        const val BACKUP_LOCATION_CUSTOM_DIALOG = 2
    }

    private var boxStore: BoxStore? = null
    private var enableLogDebug: Boolean = false
    private var restartIntent: Intent? = null
    private var onCompleteListener: OnCompleteListener? = null
    private var customBackupFileName: String? = null
    private var maxFileCount: Int? = null
    private var backupLocation: Int = BACKUP_LOCATION_INTERNAL
    private var customRestoreDialogTitle: String = "Choose backup file to restore"

    fun database(boxStore: BoxStore): ObjectBoxBackup {
        this.boxStore = boxStore
        return this
    }

    fun enableLogDebug(enableLogDebug: Boolean): ObjectBoxBackup {
        this.enableLogDebug = enableLogDebug
        return this
    }

    fun restartApp(restartIntent: Intent): ObjectBoxBackup {
        this.restartIntent = restartIntent
        restartApp()
        return this
    }

    fun onCompleteListener(onCompleteListener: OnCompleteListener): ObjectBoxBackup {
        this.onCompleteListener = onCompleteListener
        return this
    }

    fun onCompleteListener(listener: (success: Boolean, message: String) -> Unit): ObjectBoxBackup {
        this.onCompleteListener = object : OnCompleteListener {
            override fun onComplete(success: Boolean, message: String) {
                listener(success, message)
            }
        }
        return this
    }

    fun customBackupFileName(customBackupFileName: String): ObjectBoxBackup {
        this.customBackupFileName = customBackupFileName
        return this
    }

    fun backupLocation(backupLocation: Int): ObjectBoxBackup {
        this.backupLocation = backupLocation
        return this
    }

    fun maxFileCount(maxFileCount: Int): ObjectBoxBackup {
        this.maxFileCount = maxFileCount
        return this
    }

    fun customRestoreDialogTitle(title: String): ObjectBoxBackup {
        this.customRestoreDialogTitle = title
        return this
    }

    private fun initBackup(): Boolean {
        if (boxStore == null) {
            if (enableLogDebug) Log.d(TAG, "BoxStore is missing")
            onCompleteListener?.onComplete(false, "BoxStore is missing")
            return false
        }

        if (backupLocation !in listOf(BACKUP_LOCATION_INTERNAL, BACKUP_LOCATION_CUSTOM_DIALOG)) {
            if (enableLogDebug) Log.d(TAG, "Invalid backup location")
            onCompleteListener?.onComplete(false, "Invalid backup location")
            return false
        }

        INTERNAL_BACKUP_PATH = File("${context.filesDir}/objectboxbackup/")
        DATABASE_PATH = File(File(context.filesDir, "objectbox/damome_db"), DATABASE_NAME)

        try {
            INTERNAL_BACKUP_PATH.mkdirs()
        } catch (e: Exception) {
            if (enableLogDebug) Log.d(TAG, "Failed to create backup directory: ${e.message}")
            onCompleteListener?.onComplete(false, "Failed to create backup directory")
            return false
        }

        if (enableLogDebug) {
            Log.d(TAG, "Database Location: $DATABASE_PATH")
            Log.d(TAG, "INTERNAL_BACKUP_PATH: $INTERNAL_BACKUP_PATH")
        }

        return true
    }

    private fun restartApp() {
        restartIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(restartIntent)
        if (context is Activity) {
            context.finish()
        }
        Runtime.getRuntime().exit(0)
    }

    fun backup() {
        if (enableLogDebug) Log.d(TAG, "Starting Backup...")
        val success = initBackup()
        if (!success) return

        currentProcess = PROCESS_BACKUP

        val filename = if (customBackupFileName == null) {
            "objectbox-${getTime()}.mdb"
        } else {
            customBackupFileName as String
        }

        when (backupLocation) {
            BACKUP_LOCATION_INTERNAL -> {
                val backupFile = File("$INTERNAL_BACKUP_PATH/$filename")
                doBackup(backupFile)
            }

            BACKUP_LOCATION_CUSTOM_DIALOG -> {
                backupFilename = filename
                permissionRequestLauncher.launch(arrayOf())
            }
        }
    }

    private fun doBackup(destination: File) {
        try {
            boxStore?.closeThreadResources()
            boxStore?.close()

            FileInputStream(DATABASE_PATH).use { input ->
                FileOutputStream(destination).use { output ->
                    input.copyTo(output)
                }
            }

            if (maxFileCount != null) {
                deleteOldBackups()
            }

            if (enableLogDebug) Log.d(TAG, "Backup saved to $destination")
            onCompleteListener?.onComplete(true, "Backup completed successfully")

        } catch (e: Exception) {
            if (enableLogDebug) Log.d(TAG, "Backup failed: ${e.message}")
            onCompleteListener?.onComplete(false, "Backup failed: ${e.message}")
        }
    }

    fun restore() {
        if (enableLogDebug) Log.d(TAG, "Starting Restore...")
        val success = initBackup()
        if (!success) return

        currentProcess = PROCESS_RESTORE

        when (backupLocation) {
            BACKUP_LOCATION_INTERNAL -> {
                showRestoreDialog()
            }

            BACKUP_LOCATION_CUSTOM_DIALOG -> {
                permissionRequestLauncher.launch(arrayOf())
            }
        }
    }

    private fun showRestoreDialog() {
        val backupFiles = INTERNAL_BACKUP_PATH.listFiles()

        if (backupFiles.isNullOrEmpty()) {
            if (enableLogDebug) Log.d(TAG, "No backups available")
            onCompleteListener?.onComplete(false, "No backups available")
            Toast.makeText(context, "No backups available to restore", Toast.LENGTH_SHORT).show()
            return
        }

        Arrays.sort(backupFiles, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)
        val fileNames = backupFiles.map { it.name }.toTypedArray()

        MaterialAlertDialogBuilder(context)
            .setTitle(customRestoreDialogTitle)
            .setItems(fileNames) { _, which ->
                restoreBackup(File("$INTERNAL_BACKUP_PATH/${fileNames[which]}"))
            }
            .setOnCancelListener {
                if (enableLogDebug) Log.d(TAG, "Restore canceled by user")
                onCompleteListener?.onComplete(false, "Restore canceled by user")
            }
            .show()
    }

    private fun restoreBackup(backupFile: File) {
        try {
            boxStore?.closeThreadResources()
            boxStore?.close()

            // Delete existing database files
            File(File(context.filesDir, "objectbox/damome_db"), DATABASE_NAME).delete()
            File(File(context.filesDir, "objectbox/damome_db"), LOCK_FILE_NAME).delete()

            // Copy backup file to database location
            FileInputStream(backupFile).use { input ->
                FileOutputStream(DATABASE_PATH).use { output ->
                    input.copyTo(output)
                }
            }

            if (enableLogDebug) Log.d(TAG, "Database restored from $backupFile")
            onCompleteListener?.onComplete(true, "Restore completed successfully")

        } catch (e: Exception) {
            if (enableLogDebug) Log.d(TAG, "Restore failed: ${e.message}")
            onCompleteListener?.onComplete(false, "Restore failed: ${e.message}")
        }
    }

    private fun deleteOldBackups() {
        val backupFiles = INTERNAL_BACKUP_PATH.listFiles() ?: return

        if (backupFiles.size > maxFileCount!!) {
            Arrays.sort(backupFiles, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR)
            val filesToDelete = backupFiles.size - maxFileCount!!

            for (i in 0 until filesToDelete) {
                backupFiles[i].delete()
                if (enableLogDebug) Log.d(TAG, "Deleted old backup: ${backupFiles[i]}")
            }
        }
    }

    private fun getTime(): String {
        val currentTime = Calendar.getInstance().time
        val sdf = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
        return sdf.format(currentTime)
    }

    private val backupFileChooser = (context as ComponentActivity)
        .registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                try {
                    val type = context.contentResolver.getType(uri)
                    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
                    val nameIndex: Int? = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor?.moveToFirst()
                    val name: String? = nameIndex?.let { cursor.getString(it) }
                    cursor?.close()
                    val lastDotPosition = name?.lastIndexOf(".")
                    Log.i("backupFileChooser", "File type: $type, File name: $name")
                    if (lastDotPosition != null && lastDotPosition != -1) {
                        val extension = name.substring(lastDotPosition + 1)
                        Log.i("backupFileChooser", "File extension: $extension")
                        if (extension != "mdb") {
                            onCompleteListener?.onComplete(false, "Invalid file type")
                            return@registerForActivityResult
                        }
                    }
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        boxStore?.closeThreadResources()
                        boxStore?.close()

                        // Delete existing database files
                        File(File(context.filesDir, "objectbox/damome_db"), DATABASE_NAME).delete()
                        File(File(context.filesDir, "objectbox/damome_db"), LOCK_FILE_NAME).delete()

                        // Copy backup to database location
                        FileOutputStream(DATABASE_PATH).use { output ->
                            input.copyTo(output)
                        }

                        if (enableLogDebug) Log.d(TAG, "Database restored from selected file")
                        onCompleteListener?.onComplete(true, "Restore completed successfully")
                    }
                } catch (e: Exception) {
                    if (enableLogDebug) Log.d(TAG, "Restore failed: ${e.message}")
                    onCompleteListener?.onComplete(false, "Restore failed: ${e.message}")
                }
            } else {
                onCompleteListener?.onComplete(false, "No file selected")
            }
        }

    private val backupFileCreator = (context as ComponentActivity)
        .registerForActivityResult(ActivityResultContracts.CreateDocument("application/octet-stream")) { uri: Uri? ->
            if (uri != null) {
                try {
                    context.contentResolver.openOutputStream(uri)?.use { output ->
                        boxStore?.closeThreadResources()
                        boxStore?.close()

                        FileInputStream(DATABASE_PATH).use { input ->
                            input.copyTo(output)
                        }

                        if (enableLogDebug) Log.d(TAG, "Backup saved to selected location")
                        onCompleteListener?.onComplete(true, "Backup completed successfully")
                    }
                } catch (e: Exception) {
                    if (enableLogDebug) Log.d(TAG, "Backup failed: ${e.message}")
                    onCompleteListener?.onComplete(false, "Backup failed: ${e.message}")
                }
            } else {
                onCompleteListener?.onComplete(false, "No location selected")
            }
        }

    interface OnCompleteListener {
        fun onComplete(success: Boolean, message: String)
    }

    private val permissionRequestLauncher =
        (context as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                if (!it.value) {
                    onCompleteListener?.onComplete(
                        false,
                        "Storage permissions are required, please allow!",
                    )
                    return@registerForActivityResult
                }
            }
            when (currentProcess) {
                PROCESS_BACKUP -> {
                    backupFileCreator.launch(backupFilename!!)
                }

                PROCESS_RESTORE -> {
                    backupFileChooser.launch(arrayOf("*/*"))
                }
            }
        }
}