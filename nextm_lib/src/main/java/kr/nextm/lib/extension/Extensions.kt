package kr.nextm.lib.extension

import kr.nextm.lib.R

fun <T> requireEquals(expected: T, actual: T, lazyMessage: () -> String = { "" }) {
    require(expected == actual) {
        R.string.error_not_expected.getFormatString(
            expected.toString(),
            actual.toString(),
            lazyMessage.invoke())
    }
}
