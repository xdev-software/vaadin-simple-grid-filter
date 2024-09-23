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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import software.xdev.vaadin.comparators.FilterComparator;

/**
 * Used for checking if the string can be parsed to another datatype.
 *
 * @author tboehm
 */
public final class TypeHelper
{
    private static final Logger LOGGER = LogManager.getLogger(TypeHelper.class);

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
            LOGGER.debug("This string could not be parsed to Double: {}", str, e);
            return false;
        }
    }

    public static boolean isLocalDate(final String str)
    {
        Objects.requireNonNull(str);
        
        try
        {
            LOGGER.debug("This string has to be parsed to LocalDate: {}", str);
            LocalDate.parse(str);
            LOGGER.debug("String can be parsed into LocalDate.");
            return true;
        }
        catch (final DateTimeParseException e)
        {
            LOGGER.debug("String could not be parsed into LocalDate.", e);
            return false;
        }
    }

    public static boolean isLocalDateTime(final String str)
    {
        Objects.requireNonNull(str);
        
        try
        {
            
            LOGGER.debug("This string has to be parsed to LocalDateTime: {}", str);
            LocalDateTime.parse(str, DateTimeFormatter.ISO_DATE_TIME);
            LOGGER.debug("String can be parsed into LocalDateTime.");
            return true;
        }
        catch (final DateTimeParseException e)
        {
            LOGGER.debug("String could not be parsed into LocalDateTime.", e);
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
