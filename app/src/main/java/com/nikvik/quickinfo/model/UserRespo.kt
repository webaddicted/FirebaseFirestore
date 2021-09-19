package com.nikvik.quickinfo.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.annotations.SerializedName

/**
 * Created by Deepak Sharma(webaddicted) on 15/01/20.
 */

@IgnoreExtraProperties
class UserRespo {
    @SerializedName("designation_array")
    var designation_array: List<Designation>? = null

    @SerializedName("district_array")
    var district_array: List<District>? = null

    @SerializedName("lang_array")
    var lang_array: List<Language>? = null

    class Designation {
        @SerializedName("id")
        var id: Long = 0

        @SerializedName("name")
        var name: String = ""

        override fun toString(): String {
            return name
        }
    }

    class District {
        @SerializedName("district_id")
        var district_id: Long = 0
        @SerializedName("district_name")
        var district_name: String = ""
        @SerializedName("unit_array")
        var unit_array: List<UnitArray>? = null
        class UnitArray {
            @SerializedName("unit_id")
            var unit_id: Long = 0
            @SerializedName("password")
            var password: String = ""
            @SerializedName("unit_name")
            var unit_name: String = ""

            override fun toString(): String {
                return unit_name
            }
        }

        override fun toString(): String {
            return district_name
        }
    }

        class Language {
                @SerializedName("lang_id")
                var lang_id: Long = 0
                @SerializedName("lang_name")
                var lang_name: String = ""
            override fun toString(): String {
                return lang_name
            }
        }
}