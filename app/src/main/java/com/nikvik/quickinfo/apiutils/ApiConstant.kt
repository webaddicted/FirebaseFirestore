package com.prodege.shopathome.model.networkCall

/**
 * Created by Deepak Sharma on 01/07/19.
 */
class ApiConstant {

    companion object {
        const val FCM_API = "https://fcm.googleapis.com/fcm/send"
        const val serverKey =
            "key=" + "AAAA9tEus3M:APA91bE7lgT9T0D8zTOzqWRqbrxeBaWMLDce0agnxbIEjwr-oyTYAHr6y5siM_PuyLBoTPZMrV-JKuZECn1YJgkmbY_o_MM9L52CLnF0ZHGAqas0Kj6XkGTqfwux944zP0hMwNaoltj1"
        const val contentType = "application/json"

        /*********API BASE URL************/
//        START COLLECTION
        const val COLLECTION_USERS =  "user"
        const val COLLECTION_MESSAGE =  "message"
//        END COLLECTION
//        START DOCUMENT
        const val DOCUMENT_COMPOSE_MSG =  "compose_msg"
        const val DOCUMENT_DRAFT_MSG =  "draft_msg"
        const val DOCUMENT_MESSAGE_LOG =  "message_log"
//        END DOCUMENT
//        START FIELD
        const val FIELD_TAG_ARRAY =  "tag_array"
        const val FIELD_DRAFT_ARRAY =  "draft_array"
        const val FIELD_MESSAGE_ARRAY =  "message_array"
//        END FIELD

    }
}