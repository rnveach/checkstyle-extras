///////////////////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code and other text files for adherence to a set of rules.
// Copyright (C) 2001-2024 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
///////////////////////////////////////////////////////////////////////////////////////////////

package com.rnveach.tools.checkstyle.extras.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.rnveach.tools.checkstyle.extras.tokens.XmlTokenTypes;

/** Contains utility methods for XML ASTs. */
public final class XmlAstUtil {

    /** Maps from a token name to value. */
    private static final Map<String, Integer> TOKEN_NAME_TO_VALUE;
    /** Maps from a token value to name. */
    private static final Map<Integer, String> TOKEN_VALUE_TO_NAME;

    /** Array of all token IDs. */
    private static final int[] TOKEN_IDS;

    /** Format for exception message when getting token by given id. */
    private static final String TOKEN_ID_EXCEPTION_FORMAT = "unknown TokenTypes id '%s'";

    /** Format for exception message when getting token by given name. */
    private static final String TOKEN_NAME_EXCEPTION_FORMAT = "unknown TokenTypes value '%s'";

    // initialise the constants
    static {
        TOKEN_NAME_TO_VALUE = nameToValueMapFromPublicIntFields(XmlTokenTypes.class);
        TOKEN_VALUE_TO_NAME = invertMap(TOKEN_NAME_TO_VALUE);

        TOKEN_IDS = TOKEN_NAME_TO_VALUE.values().stream().mapToInt(Integer::intValue).toArray();
    }

    /** Stop instances being created. **/
    private XmlAstUtil() {
    }

    /**
     * Get all token IDs that are available in TokenTypes.
     *
     * @return array of token IDs
     */
    public static int[] getAllTokenIds() {
        final int[] safeCopy = new int[TOKEN_IDS.length];
        System.arraycopy(TOKEN_IDS, 0, safeCopy, 0, TOKEN_IDS.length);
        return safeCopy;
    }

    /**
     * Returns the name of a token for a given ID.
     *
     * @param id the ID of the token name to get
     * @return a token name
     * @throws IllegalArgumentException when id is not valid
     */
    public static String getTokenName(int id) {
        final String name = TOKEN_VALUE_TO_NAME.get(id);
        if (name == null) {
            throw new IllegalArgumentException(
                    String.format(Locale.ROOT, TOKEN_ID_EXCEPTION_FORMAT, id));
        }
        return name;
    }

    /**
     * Returns the ID of a token for a given name.
     *
     * @param name the name of the token ID to get
     * @return a token ID
     * @throws IllegalArgumentException when id is null
     */
    public static int getTokenId(String name) {
        final Integer id = TOKEN_NAME_TO_VALUE.get(name);
        if (id == null) {
            throw new IllegalArgumentException(
                    String.format(Locale.ROOT, TOKEN_NAME_EXCEPTION_FORMAT, name));
        }
        return id;
    }

    /**
     * Is argument comment-related type.
     *
     * @param type AST type.
     * @return true if type is comment-related type.
     */
    public static boolean isCommentType(int type) {
        return type == XmlTokenTypes.COMMENT;
    }

    /**
     * Creates a map of 'field name' to 'field value' from all {@code public}
     * {@code int} fields of a class.
     *
     * @param cls source class
     * @return unmodifiable name to value map
     */
    private static Map<String, Integer> nameToValueMapFromPublicIntFields(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields()).filter(
                fld -> Modifier.isPublic(fld.getModifiers()) && fld.getType() == Integer.TYPE)
                .collect(Collectors.toUnmodifiableMap(Field::getName,
                        fld -> getIntFromField(fld, fld.getName())));
    }

    /**
     * Inverts a given map by exchanging each entry's key and value.
     *
     * @param map source map
     * @return inverted map
     */
    private static Map<Integer, String> invertMap(Map<String, Integer> map) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    /**
     * Gets the value of a static or instance field of type int or of another
     * primitive type convertible to type int via a widening conversion. Does
     * not throw any checked exceptions.
     *
     * @param field from which the int should be extracted
     * @param object to extract the int value from
     * @return the value of the field converted to type int
     * @throws IllegalStateException if this Field object is enforcing Java
     *         language access control and the underlying field is inaccessible
     * @see Field#getInt(Object)
     */
    private static int getIntFromField(Field field, Object object) {
        try {
            return field.getInt(object);
        }
        catch (final IllegalAccessException exception) {
            throw new IllegalStateException(exception);
        }
    }

}
