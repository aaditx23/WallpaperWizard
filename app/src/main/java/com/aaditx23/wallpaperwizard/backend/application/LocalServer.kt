package com.aaditx23.wallpaperwizard.backend.application



import com.aaditx23.wallpaperwizard.backend.models.QuickSetModel
import com.aaditx23.wallpaperwizard.backend.models.ScheduleModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object LocalServer {
    lateinit var realm: Realm
        private set

    fun initializeRealm() {
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(     // register models here
                    QuickSetModel::class,
                    ScheduleModel::class,
                )
            )
        )
    }
}
