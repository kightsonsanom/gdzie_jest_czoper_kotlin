package com.example.czoperkotlin.db.ssot

import com.example.czoperkotlin.db.AppDatabase

interface BaseResponse {
    fun saveResponseToDb(appDatabase: AppDatabase)
}