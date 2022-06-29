package top.memore.droid_jotter.locally

import androidx.room.*

@Dao
interface LocalAccessible {

    /*
    * Catentry access
    * */
    @Query("SELECT * FROM catentries")
    fun getCatentries(): List<Catentry>

    @Insert
    fun insertCatentry(c: Catentry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCatentries(vararg cs: Catentry)

    @Update
    fun updateCatentry(c: Catentry): Int

    @Delete
    fun deleteCatentries(vararg cs: Catentry)

    @Query("DELETE FROM catentries WHERE cId = :id")
    fun deleteCatentry(id: Long): Int

    /*
    * Notentry access
    * */
    @Query("SELECT * FROM notentries")
    fun getNotentries(): List<Notentry>

    @Insert
    fun insertNotentry(n: Notentry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertNotentries(vararg ns: Notentry)

    @Update
    fun updateNotentry(n: Notentry): Int

    @Delete
    fun deleteNotentries(vararg ns: Notentry)

    @Query("DELETE FROM notentries WHERE nId = :id")
    fun deleteNotentry(id: Long): Int

    @Query("SELECT * FROM notentries WHERE categoId = :pId")
    fun getCategorizedNotes(pId: Long): List<Notentry>

    @Query("SELECT * FROM notentries " +
            "WHERE categoId IS NULL OR categoId NOT IN (SELECT cId FROM catentries)")
    fun getOrphanNotentries(): List<Notentry>

}