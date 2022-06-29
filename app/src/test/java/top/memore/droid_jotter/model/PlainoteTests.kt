package top.memore.droid_jotter.model

import org.junit.Assert
import org.junit.Test
import top.memore.droid_jotter.*
import top.memore.droid_jotter.models.*

class PlainoteTests {
    @Test
    fun newInstance_withYearMonthDay_newPlainoteCreated() {
        val n1 = Plainote.newInstance(
            t = "Brief",
            y = 2001,
            m = MONTH_MIN,
            d = DAY_OF_MONTH_MIN,
            b = "remark"
        )
        val n2 = Plainote.newInstance(
            t = "Brief",
            y = 2002,
            m = MONTH_MIN,
            d = DAY_OF_MONTH_MAX,
            b = "remark"
        )
        val n3 = Plainote.newInstance(
            t = "Brief",
            y = 2003,
            m = MONTH_MAX,
            d = DAY_OF_MONTH_MIN,
            b = "remark"
        )
        val n4 = Plainote.newInstance(
            t = "Brief",
            y = 2004,
            m = MONTH_MAX,
            d = DAY_OF_MONTH_MAX,
            b = "remark"
        )
        val d1 = n1?.ymdTriple
        val d2 = n2?.ymdTriple
        val d3 = n3?.ymdTriple
        val d4 = n4?.ymdTriple

        Assert.assertNotNull(d1)
        d1?.let {
            Assert.assertEquals(2001, it.first.toInt())
            Assert.assertEquals(MONTH_MIN, it.second)
            Assert.assertEquals(DAY_OF_MONTH_MIN, it.third)
        }
        Assert.assertNotNull(d2)
        d2?.let {
            Assert.assertEquals(2002, it.first.toInt())
            Assert.assertEquals(MONTH_MIN, it.second)
            Assert.assertEquals(DAY_OF_MONTH_MAX, it.third)
        }
        Assert.assertNotNull(d3)
        d3?.let {
            Assert.assertEquals(2003, it.first.toInt())
            Assert.assertEquals(MONTH_MAX, it.second)
            Assert.assertEquals(DAY_OF_MONTH_MIN, it.third)
        }
        Assert.assertNotNull(d4)
        d4?.let {
            Assert.assertEquals(2004, it.first.toInt())
            Assert.assertEquals(MONTH_MAX, it.second)
            Assert.assertEquals(DAY_OF_MONTH_MAX, it.third)
        }
    }

    @Test
    fun newInstance_withInvalidMonth_noPlainoteCreated() {
        val n1 = Plainote.newInstance(
            t = "Brief",
            y = 2000,
            m = (MONTH_MIN - 1).toByte(),
            d = 1,
            b = "remark"
        )
        val n2 = Plainote.newInstance(
            t = "Brief",
            y = 2000,
            m = (MONTH_MAX + 1).toByte(),
            d = 31,
            b = "remark"
        )

        Assert.assertNull(n1)
        Assert.assertNull(n2)
    }

    @Test
    fun newInstance_withInvalidDay_noPlainoteCreated() {
        val n1 = Plainote.newInstance(
            t = "Brief",
            y = 2000,
            m = 1,
            d = (DAY_OF_MONTH_MIN - 1).toByte(),
            b = "remark"
        )
        val n2 = Plainote.newInstance(
            t = "Brief",
            y = 2000,
            m = 12,
            d = (DAY_OF_MONTH_MAX + 1).toByte(),
            b = "remark"
        )

        Assert.assertNull(n1)
        Assert.assertNull(n2)
    }

    @Test
    fun newInstance_withoutYearMonthDay_newPlainoteCreated() {
        val note = Plainote.newInstance(t = "Brief", b = "remark")
        val date = note?.ymdTriple

        Assert.assertNull(date)
    }

    @Test
    fun newInstance_withEmptyTitle_noPlainoteCreated() {
        val note = Plainote.newInstance(t = "", y = 2000, m = 12, d = 21, b = "remark")

        Assert.assertNull(note)
    }

    @Test
    fun newInstance_withTooLongTitle_noPlainoteCreated() {
        val note = Plainote.newInstance(t = "Title".padEnd(NAME_MAX_LENGTH + 1, 'T'))

        Assert.assertNull(note)
    }

    @Test
    fun newInstance_withTooLongBrief_noPlainoteCreated() {
        val note = Plainote.newInstance(t = "Title", b = "Brief".padEnd(TEXT_MAX_LENGTH + 1, 'B'))

        Assert.assertNull(note)
    }

}