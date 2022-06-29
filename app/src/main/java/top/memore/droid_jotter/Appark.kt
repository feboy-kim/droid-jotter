package top.memore.droid_jotter

const val NAME_MAX_LENGTH = 100
const val TEXT_MAX_LENGTH = 900

const val MONTH_MIN: Byte = 1
const val MONTH_MAX: Byte = 12

const val DAY_OF_MONTH_MIN: Byte = 1
const val DAY_OF_MONTH_MAX: Byte = 31

fun ymd(y: Short, m: Byte, d: Byte) = (y.toInt() shl 16) or (m.toInt() shl 8) or d.toInt()
