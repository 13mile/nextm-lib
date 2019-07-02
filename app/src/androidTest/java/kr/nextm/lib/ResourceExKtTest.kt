package kr.nextm.lib

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ResourceExKtTest {

    @Test
    fun getDimension() {
        R.dimen.abc_action_bar_content_inset_material.getDimension()
    }

    @Test
    fun getString() {
        R.string.abc_action_bar_home_description.getString()
    }

    @Test
    fun format_string() {
        assertEquals("int 1 / str Str", kr.nextm.ex.next_m_lib.R.string.format_1.getString(1, "Str"))
    }

    @Test
    fun getSample() {
        assertEquals(1, listOf(0,1,2,3,4).getSample(6))
    }

    @Test
    fun getSampleFromArray() {
        assertEquals(1, arrayOf(0,1,2,3,4).getSample(6))
    }

    @Test
    fun getDrawable() {
        R.drawable.abc_ab_share_pack_mtrl_alpha.getDrawable()
    }

    @Test
    fun getColor() {
        R.color.abc_background_cache_hint_selector_material_dark.getColor()
    }

    @Test
    fun `toHtml-개행태그가-없을-경우-개행문자를-br-태그로-변경`() {
        assertEquals("<br>".toHtml(false), "\n".toHtml())
        assertEquals("<br/>".toHtml(false), "\n".toHtml())
    }

    @Test
    fun `toHtml-개행태그가-있을-경우-개행문자를-br-태그로-변경하지-않음`() {
        assertEquals("<br>".toHtml(false), "<br>\n".toHtml())
        assertEquals("<br>".toHtml(false), "<br/>\n".toHtml())
        assertEquals("<p>".toHtml(false), "<p>\n".toHtml())
        assertEquals("<p>".toHtml(false), "<p/>\n".toHtml())
    }

    @Test
    fun containsIgnoreCase() {
        assertEquals(true, "abcdefg".containsIgnoreCase("BCDEF"))
        assertEquals(false, "abcdefg".contains("BCDEF"))
        assertEquals(false, "abcdefg".containsIgnoreCase("fgabc"))
    }

}