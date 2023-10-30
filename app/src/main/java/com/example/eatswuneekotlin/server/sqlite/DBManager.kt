package com.example.eatswuneekotlin.server.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBManager /** DB 생성  */(
    // 부가적인 객체들
    private val context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // 생성된 DB가 없을 경우에 한 번만 호출됨
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val createSql = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " LONG PRIMARY KEY, " + COLUMN_NAME + " TEXT, "
                + COLUMN_IMAGE_URL + " TEXT, " + COLUMN_PRICE + " INTEGER, "
                + COLUMN_RES_NAME + " TEXT, " + COLUMN_CNT + " INTEGER)")
        sqLiteDatabase.execSQL(createSql)
        Log.d("sqlite", "DB is opened")
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(sqLiteDatabase)
    }

    // 데이터 전체 검색
    fun readAllData(): Cursor? {
        val sql = ("SELECT * FROM $TABLE_NAME")
        val db = this.readableDatabase
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(sql, null)
        }
        return cursor
    }

    /**
     * 장바구니 등록
     * @param menu_id 이름
     * @param menu_name 메뉴 이름
     * @param menu_image 메뉴 사진
     * @param menu_price 메뉴 가격
     * @param restaurant_name 식당 이름
     * @param menu_cnt 메뉴 수량
     */
    fun addBag(
        menu_id: Long,
        menu_name: String?,
        menu_image: String?,
        menu_price: Int,
        restaurant_name: String?,
        menu_cnt: Int
    ) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COLUMN_ID, menu_id)
        cv.put(COLUMN_NAME, menu_name)
        cv.put(COLUMN_IMAGE_URL, menu_image)
        cv.put(COLUMN_PRICE, menu_price)
        cv.put(COLUMN_RES_NAME, restaurant_name)
        cv.put(COLUMN_CNT, menu_cnt)
        val result = db.insert(TABLE_NAME, null, cv)
        if (result == -1L) {
            Log.d("sqlite", "Insert Failed")
        } else {
            Log.d("sqlite", "Insert Successfully")
        }
    }

    /**
     * 연락처 수정
     * @param menu_name 메뉴 이름
     * @param menu_cnt 메뉴 수량
     */
    fun updateData(menu_name: String, menu_cnt: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()

        // 수정할 값
        cv.put(COLUMN_CNT, menu_cnt)
        val result = db.update(TABLE_NAME, cv, "menu_name=?", arrayOf(menu_name)).toLong()
        if (result == -1L) {
            Log.d("sqlite", "Failed")
        } else {
            Log.d("sqlite", "update successfully")
        }
    }

    /**
     * 연락처 삭제
     * @param menu_name 메뉴 이름
     */
    fun deleteData(menu_name: String) {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "menu_name=?", arrayOf(menu_name)).toLong()
        if (result == -1L) {
            Log.d("sqlite", "Failed")
        } else {
            Log.d("sqlite", "delete successfully")
        }
    }

    /**
     * 데이터 전체 삭제
     */
    fun deleteAllData() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
    }

    companion object {
        // DB 관련 상수 선언
        const val DATABASE_NAME = "shopping_bag.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "shopping_bag"
        const val COLUMN_ID = "menu_id"
        const val COLUMN_NAME = "menu_name"
        const val COLUMN_IMAGE_URL = "menu_image"
        const val COLUMN_PRICE = "menu_price"
        const val COLUMN_RES_NAME = "restaurant_name"
        const val COLUMN_CNT = "menu_cnt"
    }
}