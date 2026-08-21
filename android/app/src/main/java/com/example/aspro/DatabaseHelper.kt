package com.example.aspro

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aspro.ui.dashboard.seed.Seed
import com.example.aspro.ui.dashboard.fertilizer.Fertilizer
import com.example.aspro.ui.dashboard.pesticide.Pesticide
import com.example.aspro.ui.home.Crop

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AgriSmartPro.db"
        private const val DATABASE_VERSION = 1

        // Seed Table
        private const val TABLE_SEED = "Seed"
        private const val COLUMN_SEED_ID = "id"
        private const val COLUMN_SEED_NAME = "name"
        private const val COLUMN_SEED_VARIETY = "variety"
        private const val COLUMN_SEED_PACKING_WEIGHT = "packingWeight"
        private const val COLUMN_SEED_NUMBER_OF_PACKETS = "numberOfPackets"
        private const val COLUMN_SEED_PRICE_PER_PACKING = "pricePerPacking"

        // Fertilizer Table
        private const val TABLE_FERTILIZER = "Fertilizer"
        private const val COLUMN_FERTILIZER_ID = "id"
        private const val COLUMN_FERTILIZER_NAME = "name"
        private const val COLUMN_FERTILIZER_COMPANY = "company"
        private const val COLUMN_FERTILIZER_PACKING_WEIGHT = "packingWeight"
        private const val COLUMN_FERTILIZER_NUMBER_OF_PACKETS = "numberOfPackets"
        private const val COLUMN_FERTILIZER_PRICE_PER_PACKING = "pricePerPacking"

        // Pesticide Table
        private const val TABLE_PESTICIDE = "Pesticide"
        private const val COLUMN_PESTICIDE_ID = "id"
        private const val COLUMN_PESTICIDE_NAME = "name"
        private const val COLUMN_PESTICIDE_COMPANY = "company"
        private const val COLUMN_PESTICIDE_LITER_KILOGRAM = "literKilogram"
        private const val COLUMN_PESTICIDE_NUMBER_OF_PACKETS = "numberOfPackets"
        private const val COLUMN_PESTICIDE_PRICE_PER_PACKING = "pricePerPacking"

        // Crop Table
        private const val TABLE_CROP = "Crop"
        private const val COLUMN_CROP_ID = "id"
        private const val COLUMN_PLOT_NUMBER = "plotNumber"
        private const val COLUMN_LAND_IN_PLOT = "landInPlot"
        private const val COLUMN_LAND_UNIT = "landUnit"
        private const val COLUMN_SOWING_DATE = "sowingDate"
        private const val COLUMN_SEED = "seed"
        private const val COLUMN_SEED_QUANTITY = "seedQuantity"
        private const val COLUMN_LAND_PREPARATION_EXPENSES = "landPreparationExpenses"

    }
    override fun onCreate(db: SQLiteDatabase) {
        val createSeedTable = ("CREATE TABLE " + TABLE_SEED + "("
                + COLUMN_SEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SEED_NAME + " TEXT,"
                + COLUMN_SEED_VARIETY + " TEXT,"
                + COLUMN_SEED_PACKING_WEIGHT + " REAL,"
                + COLUMN_SEED_NUMBER_OF_PACKETS + " INTEGER,"
                + COLUMN_SEED_PRICE_PER_PACKING + " REAL" + ")")

        val createFertilizerTable = ("CREATE TABLE " + TABLE_FERTILIZER + "("
                + COLUMN_FERTILIZER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FERTILIZER_NAME + " TEXT,"
                + COLUMN_FERTILIZER_COMPANY + " TEXT,"
                + COLUMN_FERTILIZER_PACKING_WEIGHT + " REAL,"
                + COLUMN_FERTILIZER_NUMBER_OF_PACKETS + " INTEGER,"
                + COLUMN_FERTILIZER_PRICE_PER_PACKING + " REAL" + ")")

        val createPesticideTable = ("CREATE TABLE " + TABLE_PESTICIDE + "("
                + COLUMN_PESTICIDE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PESTICIDE_NAME + " TEXT,"
                + COLUMN_PESTICIDE_COMPANY + " TEXT,"
                + COLUMN_PESTICIDE_LITER_KILOGRAM + " REAL,"
                + COLUMN_PESTICIDE_NUMBER_OF_PACKETS + " INTEGER,"
                + COLUMN_PESTICIDE_PRICE_PER_PACKING + " REAL" + ")")

        val createCropTable = ("CREATE TABLE " + TABLE_CROP + "("
                + COLUMN_CROP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PLOT_NUMBER + " TEXT,"
                + COLUMN_LAND_IN_PLOT + " TEXT,"
                + COLUMN_LAND_UNIT + " TEXT,"
                + COLUMN_SOWING_DATE + " TEXT,"
                + COLUMN_SEED + " TEXT,"
                + COLUMN_SEED_QUANTITY + " REAL,"
                + COLUMN_LAND_PREPARATION_EXPENSES + " REAL" + ")")

        db.execSQL(createCropTable)
        db.execSQL(createSeedTable)
        db.execSQL(createFertilizerTable)
        db.execSQL(createPesticideTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SEED")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FERTILIZER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PESTICIDE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CROP")

        onCreate(db)
    }

    ///////////////// seed
    fun insertSeed(name: String, variety: String, packingWeight: Double, numberOfPackets: Int, pricePerPacking: Double): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_SEED_NAME, name)
            put(COLUMN_SEED_VARIETY, variety)
            put(COLUMN_SEED_PACKING_WEIGHT, packingWeight)
            put(COLUMN_SEED_NUMBER_OF_PACKETS, numberOfPackets)
            put(COLUMN_SEED_PRICE_PER_PACKING, pricePerPacking)
        }
        return db.insert(TABLE_SEED, null, contentValues)
    }

    fun deleteSeed(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_SEED, "$COLUMN_SEED_ID = ?", arrayOf(id.toString()))
    }

    fun updateSeed(id: Int, name: String, variety: String, packingWeight: Double, numberOfPackets: Int, pricePerPacking: Double): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_SEED_NAME, name)
            put(COLUMN_SEED_VARIETY, variety)
            put(COLUMN_SEED_PACKING_WEIGHT, packingWeight)
            put(COLUMN_SEED_NUMBER_OF_PACKETS, numberOfPackets)
            put(COLUMN_SEED_PRICE_PER_PACKING, pricePerPacking)
        }
        return db.update(TABLE_SEED, contentValues, "$COLUMN_SEED_ID = ?", arrayOf(id.toString()))
    }


    fun getSeedById(id: Int): Seed {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_SEED, null, "$COLUMN_SEED_ID = ?",
            arrayOf(id.toString()), null, null, null
        )
        cursor?.moveToFirst()
        val seed = Seed(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEED_ID)), // Corrected
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED_VARIETY)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_PACKING_WEIGHT)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEED_NUMBER_OF_PACKETS)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_PRICE_PER_PACKING))
        )
        cursor.close()
        return seed
    }

    fun getAllSeeds(): List<Seed> {
        val seedList = ArrayList<Seed>()
        val selectQuery = "SELECT * FROM $TABLE_SEED"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val seed = Seed(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEED_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED_VARIETY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_PACKING_WEIGHT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEED_NUMBER_OF_PACKETS)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_PRICE_PER_PACKING))
                )
                seedList.add(seed)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return seedList
    }

    fun getSeedByName(name: String): Seed? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_SEED WHERE $COLUMN_SEED_NAME = ?", arrayOf(name))
        return if (cursor.moveToFirst()) {
            val seed = Seed(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEED_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED_VARIETY)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_PACKING_WEIGHT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SEED_NUMBER_OF_PACKETS)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_PRICE_PER_PACKING))
            )
            cursor.close()
            seed
        } else {
            cursor.close()
            null
        }
    }

    // Function to update Seed Quantity
    fun updateSeedQuantity(id: Int, newQuantity: Double) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_SEED_PACKING_WEIGHT, newQuantity)
        }
        db.update(TABLE_SEED, contentValues, "$COLUMN_SEED_ID = ?", arrayOf(id.toString()))
    }



    ///////////////// fertilizer
    fun insertFertilizer(name: String, company: String, packingWeight: Double, numberOfPackets: Int, pricePerPacking: Double): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FERTILIZER_NAME, name)
            put(COLUMN_FERTILIZER_COMPANY, company)
            put(COLUMN_FERTILIZER_PACKING_WEIGHT, packingWeight)
            put(COLUMN_FERTILIZER_NUMBER_OF_PACKETS, numberOfPackets)
            put(COLUMN_FERTILIZER_PRICE_PER_PACKING, pricePerPacking)
        }
        return db.insert(TABLE_FERTILIZER, null, contentValues)
    }

    fun deleteFertilizer(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_FERTILIZER, "$COLUMN_FERTILIZER_ID = ?", arrayOf(id.toString()))
    }

    fun updateFertilizer(id: Int, name: String, company: String, packingWeight: Double, numberOfPackets: Int, pricePerPacking: Double): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_FERTILIZER_NAME, name)
            put(COLUMN_FERTILIZER_COMPANY, company)
            put(COLUMN_FERTILIZER_PACKING_WEIGHT, packingWeight)
            put(COLUMN_FERTILIZER_NUMBER_OF_PACKETS, numberOfPackets)
            put(COLUMN_FERTILIZER_PRICE_PER_PACKING, pricePerPacking)
        }
        return db.update(TABLE_FERTILIZER, contentValues, "$COLUMN_FERTILIZER_ID = ?", arrayOf(id.toString()))
    }

    fun getFertilizerById(id: Int): Fertilizer {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_FERTILIZER, null, "$COLUMN_FERTILIZER_ID = ?",
            arrayOf(id.toString()), null, null, null
        )
        cursor?.moveToFirst()
        val fertilizer = Fertilizer(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_COMPANY)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_PACKING_WEIGHT)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_NUMBER_OF_PACKETS)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_PRICE_PER_PACKING))
        )
        cursor.close()
        return fertilizer
    }

    fun getAllFertilizers(): List<Fertilizer> {
        val fertilizerList = ArrayList<Fertilizer>()
        val selectQuery = "SELECT * FROM $TABLE_FERTILIZER"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val fertilizer = Fertilizer(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_COMPANY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_PACKING_WEIGHT)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_NUMBER_OF_PACKETS)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FERTILIZER_PRICE_PER_PACKING))
                )
                fertilizerList.add(fertilizer)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return fertilizerList
    }

    // pesticide

    fun insertPesticide(
        name: String,
        company: String,
        literKilogram: Double,
        numberOfPackets: Int,
        pricePerPacking: Double
        ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PESTICIDE_NAME, name)
            put(COLUMN_PESTICIDE_COMPANY, company)
            put(COLUMN_PESTICIDE_LITER_KILOGRAM, literKilogram)
            put(COLUMN_PESTICIDE_NUMBER_OF_PACKETS, numberOfPackets)
            put(COLUMN_PESTICIDE_PRICE_PER_PACKING, pricePerPacking)
        }
        return db.insert(TABLE_PESTICIDE, null, contentValues)
    }

    fun deletePesticide(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_PESTICIDE, "$COLUMN_PESTICIDE_ID = ?", arrayOf(id.toString()))
    }

    fun updatePesticide(
        id: Int,
        name: String,
        company: String,
        literKilogram: Double,
        numberOfPackets: Int,
        pricePerPacking: Double
                ): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PESTICIDE_NAME, name)
            put(COLUMN_PESTICIDE_COMPANY, company)
            put(COLUMN_PESTICIDE_LITER_KILOGRAM, literKilogram)
            put(COLUMN_PESTICIDE_NUMBER_OF_PACKETS, numberOfPackets)
            put(COLUMN_PESTICIDE_PRICE_PER_PACKING, pricePerPacking)
        }
        return db.update(TABLE_PESTICIDE, contentValues, "$COLUMN_PESTICIDE_ID = ?", arrayOf(id.toString()))
    }

    fun getPesticideById(id: Int): Pesticide {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PESTICIDE, null, "$COLUMN_PESTICIDE_ID = ?",
            arrayOf(id.toString()), null, null, null
        )
        cursor?.moveToFirst()
        val pesticide = Pesticide(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_COMPANY)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_LITER_KILOGRAM)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_NUMBER_OF_PACKETS)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_PRICE_PER_PACKING))
        )
        cursor.close()
        return pesticide
    }

    fun getAllPesticides(): List<Pesticide> {
        val pesticideList = ArrayList<Pesticide>()
        val selectQuery = "SELECT * FROM $TABLE_PESTICIDE"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val pesticide = Pesticide(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_COMPANY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_LITER_KILOGRAM)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_NUMBER_OF_PACKETS)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PESTICIDE_PRICE_PER_PACKING))
                )
                pesticideList.add(pesticide)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return pesticideList
    }

    // Insert Crop
    fun insertCrop(
        plotNumber: String,
        landInPlot: String,
        landUnit: String,
        sowingDate: String,
        seed: String,
        seedQuantity: Double,
        landPreparationExpenses: Double
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PLOT_NUMBER, plotNumber)
            put(COLUMN_LAND_IN_PLOT, landInPlot)
            put(COLUMN_LAND_UNIT, landUnit)
            put(COLUMN_SOWING_DATE, sowingDate)
            put(COLUMN_SEED, seed)
            put(COLUMN_SEED_QUANTITY, seedQuantity)
            put(COLUMN_LAND_PREPARATION_EXPENSES, landPreparationExpenses)
        }
        return db.insert(TABLE_CROP, null, contentValues)
    }

    // Delete Crop
    fun deleteCrop(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CROP, "$COLUMN_CROP_ID = ?", arrayOf(id.toString()))
    }

    // Update Crop
    fun updateCrop(
        id: Int,
        plotNumber: String,
        landInPlot: String,
        landUnit: String,
        sowingDate: String,
        seed: String,
        seedQuantity: Double,
        landPreparationExpenses: Double
    ): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_PLOT_NUMBER, plotNumber)
            put(COLUMN_LAND_IN_PLOT, landInPlot)
            put(COLUMN_LAND_UNIT, landUnit)
            put(COLUMN_SOWING_DATE, sowingDate)
            put(COLUMN_SEED, seed)
            put(COLUMN_SEED_QUANTITY, seedQuantity)
            put(COLUMN_LAND_PREPARATION_EXPENSES, landPreparationExpenses)
        }
        return db.update(TABLE_CROP, contentValues, "$COLUMN_CROP_ID = ?", arrayOf(id.toString()))
    }

    // Get Crop by ID
    fun getCropById(id: Int): Crop {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CROP, null, "$COLUMN_CROP_ID = ?",
            arrayOf(id.toString()), null, null, null
        )
        cursor?.moveToFirst()
        val crop = Crop(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CROP_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLOT_NUMBER)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAND_IN_PLOT)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAND_UNIT)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOWING_DATE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_QUANTITY)),
            cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LAND_PREPARATION_EXPENSES))
        )
        cursor.close()
        return crop
    }

    // Get All Crops
    fun getAllCrops(): List<Crop> {
        val cropList = ArrayList<Crop>()
        val selectQuery = "SELECT * FROM $TABLE_CROP"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val crop = Crop(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CROP_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLOT_NUMBER)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAND_IN_PLOT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAND_UNIT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOWING_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SEED)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SEED_QUANTITY)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LAND_PREPARATION_EXPENSES))
                )
                cropList.add(crop)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return cropList
    }


}
