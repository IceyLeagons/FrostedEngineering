/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.utils;

import java.util.Locale;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomString {

    /**
     * Generate a random string.
     */
    public String nextString() {
        return RandomStringUtils.random(1, digits);
    }

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static final String lower = upper.toLowerCase(Locale.ROOT);

    public static final String digits = "0123456789$ß¤×÷Łł@&đĐ+!%/=)(~€*¥¢­¬º÷«§»^`ƸƺƵƲƮƬƪƾǁǀǄǆǈǍǑǏǐαβΓγΔδΕεΖζηΘθικΛλμνΞξΟοΠπρΣσςτυΦφΧχΨψΩωÖÜÓŐÚÁŰÉÍöüóőúéáű"
            + "йцукенгшщзхъфывапролджэячсмитьбю"
            + "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЖЭЯЧСМИТЬ"
            + "嗎擺押拇捆摑搵捰押手田水廿口木火土戈人大中心金難女弓竹日火尸豈幽岸崇凱峰豐出。、片北非版數由書裡被";

    public static final String alphanum = upper + lower + digits;

}
