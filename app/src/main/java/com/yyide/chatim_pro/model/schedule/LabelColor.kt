package com.yyide.chatim_pro.model.schedule

/**
 *
 * @author liu tao
 * @date 2021/9/13 16:06
 * @description 描述
 */
data class LabelColor(
    val color: String,
    var checked: Boolean
) {
    companion object {
        const val color1 = "#78AAFF"
        const val color2 = "#FC6360"
        const val color3 = "#E5376A"
        const val color4 = "#FA5170"
        const val color5 = "#FD4A62"
        const val color6 = "#FF72C3"
        const val color7 = "#E16C39"
        const val color8 = "#F58E41"
        const val color9 = "#FD8208"
        const val color10 = "#E5A110"
        const val color11 = "#E4B333"
        const val color12 = "#DCC72E"
        const val color13 = "#AA52CD"
        const val color14 = "#AA7FEA"
        const val color15 = "#7E7CEB"
        const val color16 = "#4B8CF2"
        const val color17 = "#2A98F9"
        const val color18 = "#18B9ED"
        const val color19 = "#1CAFAE"
        const val color20 = "#1CAFAE"
        const val color21 = "#8ABB60"
        const val color22 = "#09BA71"
        const val color23 = "#1BAE5B"
        const val color24 = "#51B754"

        fun getLabelColorList(): List<LabelColor> {
            return listOf(
                LabelColor(color1, false),
                LabelColor(color2, false),
                LabelColor(color3, false),
                LabelColor(color4, false),
                LabelColor(color5, false),
                LabelColor(color6, false),
                LabelColor(color7, false),
                LabelColor(color8, false),
                LabelColor(color9, false),
                LabelColor(color10, false),
                LabelColor(color11, false),
                LabelColor(color12, false),
                LabelColor(color13, false),
                LabelColor(color14, false),
                LabelColor(color15, false),
                LabelColor(color16, false),
                LabelColor(color17, false),
                LabelColor(color18, false),
                LabelColor(color19, false),
                LabelColor(color20, false),
                LabelColor(color21, false),
                LabelColor(color22, false),
                LabelColor(color23, false),
                LabelColor(color24, false)
            )
        }
    }
}