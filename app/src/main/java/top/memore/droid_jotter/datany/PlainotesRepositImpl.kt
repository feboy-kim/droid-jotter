package top.memore.droid_jotter.datany

import top.memore.droid_jotter.cloudy.CloudAccessible
import top.memore.droid_jotter.cloudy.CloudSharable
import top.memore.droid_jotter.locally.LocalAccessible
import top.memore.droid_jotter.models.Plainote
import javax.inject.Inject

class PlainotesRepositImpl @Inject constructor(
    private val localine: LocalAccessible,
    private val cloudata: CloudAccessible,
    private val parental: NotesParental,
    private val cloudeal: CloudSharable
) : Repository<Plainote> {

    override fun loadCloudEntities(): List<Plainote> {
        return parental.currentParentId?.let {
            cloudata.getPlainotes(cloudeal.plainoteUri, cloudeal.accessToken, it)
        } ?: cloudata.getPlainotes(cloudeal.plainoteUri, cloudeal.accessToken)
    }

    override fun loadLocalEntities(): List<Plainote> {
        return parental.currentParentId?.let {
            localine.getCategorizedNotes(it).map { n ->
                Plainote.fromNotentry(n)
            }
        } ?: localine.getOrphanNotentries().map {
            Plainote.fromNotentry(it)
        }
    }

    override fun insertLocalEntity(d: Plainote): Boolean {
        return if (d.isValid) {
            localine.insertNotentry(d.toNotentry(cId = parental.currentParentId))
            true
        } else false
    }

    override fun updateLocalEntity(d: Plainote): Boolean {
        return if (d.isValid) {
            localine.updateNotentry(d.toNotentry(cId = parental.currentParentId)) > 0
        } else false
    }

    override fun deleteLocalEntity(d: Plainote): Boolean {
        return localine.deleteNotentry(d.nId) > 0
    }

    override fun upsertLocalEntities(vararg ds: Plainote) {
        val ns = ds.filter {
            it.isValid
        }.map {
            it.toNotentry(parental.currentParentId)
        }.toTypedArray()
        localine.upsertNotentries(*ns)
    }

    override fun deleteLocalEntities(vararg ds: Plainote) {
        val ns = ds.filter {
            it.isValid
        }.map {
            it.toNotentry(parental.currentParentId)
        }.toTypedArray()
        localine.deleteNotentries(*ns)
    }

    override fun upsertCloudEntities(vararg ds: Plainote) {
//        ("Not yet implemented")
    }

    override fun deleteCloudEntities(vararg ds: Plainote) {
//        ("Not yet implemented")
    }

}