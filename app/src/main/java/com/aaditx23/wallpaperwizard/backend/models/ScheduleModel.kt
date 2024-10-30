package com.aaditx23.wallpaperwizard.backend.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class ScheduleModel: RealmObject{
    @PrimaryKey var _id: ObjectId = BsonObjectId()
    var id: Int = 0
    var prevHomeScreen: String? = null
    var prevLockScreen: String? = null
    var homeScreen: String? = null
    var lockScreen: String? = null
    var repeatDays: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var repeat: String? = null
}
