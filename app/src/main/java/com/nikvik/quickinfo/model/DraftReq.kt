package com.nikvik.quickinfo.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */

@IgnoreExtraProperties
class DraftReq {

    @SerializedName("draft_array")
    var draft_array: List<MailRespo.DraftArray>? = null
}