/*
 * Copyright © 2024 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.vaadin.comparators.utl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import software.xdev.vaadin.comparators.FilterComparator;

/**
 * Used for checking if the string can be parsed to another datatype.
 */
public final class TypeHelper
{
    private TypeHelper()
    {
    }

    public static boolean isDouble(final String str)
    {
        Objects.requireNonNull(str);
        
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch (final NumberFormatException e)
        {
            return false;
        }
    }
    
    public static boolean isLocalDate(final String str)
    {
        Objects.requireNonNull(str);
        
        try
        {
            LocalDate.parse(str);
            return true;
        }
        catch (final DateTimeParseException e)
        {
            return false;
        }
    }
    
    public static boolean isLocalDateTime(final String str)
    {
        Objects.requireNonNull(str);
        
        try
        {
            LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
            return true;
        }
        catch (final DateTimeParseException e)
        {
            return false;
        }
    }

    public static void checkIfTypeIsApplicable(final FilterComparator filterComparator, final Class<?> otherType)
    {
        Objects.requireNonNull(filterComparator);
        
        if (!filterComparator.isApplicable(otherType))
        {
            throw new MismatchingTypeException("Type " + otherType + " is not applicable.");
        }
    }
}
