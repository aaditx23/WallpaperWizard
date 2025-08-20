package com.aaditx23.wallpaperwizard.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class ScheduleModel: RealmObject{
    @PrimaryKey var _id: ObjectId = BsonObjectId()
    var prevHome: String = ""
    var prevLock: String = ""
    var scheduledHome: String = ""
    var scheduledLock: String = ""
    var startTime: String = "00:00"
    var endTime: String = "00:00"
    var repeat: String = "0000000"
    var running: String = ""

}
