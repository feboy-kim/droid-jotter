package top.memore.droid_jotter.locally

import androidx.room.*

@Dao
interface LocalAccessible {

    /*
    * Category access
    * */
    @Query("SELECT nId, title, millitime FROM categories")
    fun getCategories(): List<Litentry>

    @Query("SELECT nId, title, millitime FROM categories WHERE nId = :id")
    fun getOneCategory(id: Long): Litentry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertCategories(vararg cs: Category)

    @Insert
    fun insertCategories(vararg cs: Category)

    @Update
    fun updateCategories(vararg cs: Category): Int

    @Delete
    fun deleteCategories(vararg cs: Category): Int

    @Query("UPDATE categories SET millitime = 0 - millitime WHERE nId = :id AND millitime > 0")
    fun updateCategoryAsUseless(id: Long): Int

    @Query("UPDATE categories SET millitime = 0 - millitime WHERE nId = :id AND millitime < 0")
    fun updateCategoryAsUsable(id: Long): Int

    /*
    * Notentry access
    * */
    @Query(
        "SELECT nId, title, millitime FROM notentries WHERE cateId IS NULL OR " +
                "cateId NOT IN (SELECT nId FROM categories WHERE categories.millitime > 0)"
    )
    fun getLiteNotes(): List<Litentry>

    @Query(
        "SELECT nId, title, millitime FROM notentries WHERE cateId IS NOT NULL and cateId = :pId " +
                "AND cateId IN (SELECT nId FROM categories WHERE categories.millitime > 0)"
    )
    fun getLiteNotes(pId: Long): List<Litentry>

    @Query("SELECT * FROM notentries WHERE nId = :id")
    fun getOneNotentry(id: Long): Notentry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertNotentries(vararg ns: Notentry)

    @Insert
    fun insertNotentries(vararg ns: Notentry)

    @Update
    fun updateNotentries(vararg ns: Notentry): Int

    @Delete
    fun deleteNotentries(vararg ns: Notentry): Int

    @Query("UPDATE notentries SET millitime = 0 - millitime WHERE nId = :id AND millitime > 0")
    fun updateNotentryAsUseless(id: Long): Int

    @Query("UPDATE notentries SET millitime = 0 - millitime WHERE nId = :id AND millitime < 0")
    fun updateNotentryAsUsable(id: Long): Int

}