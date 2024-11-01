package com.aaditx23.wallpaperwizard.backend.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class QuickSetModel: RealmObject{
    @PrimaryKey var _id: ObjectId = BsonObjectId()
}
