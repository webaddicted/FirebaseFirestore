package com.nikvik.quickinfo.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */

@IgnoreExtraProperties
class MailRespo {
    @SerializedName("tag_array")
    var tag_array: List<TagArray>? = null
    @SerializedName("draft_array")
    var draft_array: ArrayList<DraftArray>? = null
    @SerializedName("message_array")
    var message_array: ArrayList<DraftArray>? = null

    class TagArray {
        @SerializedName("id")
        var id: Long = 0
        @SerializedName("name")
        var name: String = ""
        override fun toString(): String {
            return name.toString()
        }
    }

    class DraftArray:Serializable {
        @SerializedName("action_token")
        var action_token: String = ""

        @SerializedName("designation")
        var designation: String = ""

        @SerializedName("district_name")
        var district_name: String = ""

        @SerializedName("incident")
        var incident: String = ""

        @SerializedName("incident_date")
        var incident_date: String = ""

        @SerializedName("other_info")
        var other_info: String = ""

        @SerializedName("tag1")
        var tag1: String = ""

        @SerializedName("tag2")
        var tag2: String = ""

        @SerializedName("tag3")
        var tag3: String = ""

        @SerializedName("tag4")
        var tag4: String = ""

        @SerializedName("tag5")
        var tag5: String = ""

        @SerializedName("unit_name")
        var unit_name: String = ""
    }
}