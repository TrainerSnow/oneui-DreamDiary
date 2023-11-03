package com.snow.diary.feature.preferences.screen.backup

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts

object PersistableUriPickerContract : ActivityResultContracts.OpenDocumentTree() {

    override fun createIntent(context: Context, input: Uri?): Intent {
        val intent = super.createIntent(context, input)
        intent.addFlags(flags)

        return intent
    }

    const val flags =
        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION

}