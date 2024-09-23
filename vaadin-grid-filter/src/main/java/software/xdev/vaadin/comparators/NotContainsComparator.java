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
package software.xdev.vaadin.comparators;


import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.comparators.utl.TypeHelper;

/**
 * Used for comparison with does not contain.
 */
public final class NotContainsComparator implements FilterComparator
{
    private static final Logger LOGGER = LogManager.getLogger(NotContainsComparator.class);
    private static NotContainsComparator instance;

    private NotContainsComparator()
    {
    }

    public static NotContainsComparator getInstance()
    {
        if (instance == null)
        {
            instance = new NotContainsComparator();
        }

        return instance;
    }
    @Override
    public String getDescription()
    {
        return "not contains";
    }

    @Override
    public boolean isApplicable(final Class<?> clazz)
    {
        return String.class.isAssignableFrom(clazz)
               || Number.class.isAssignableFrom(clazz)
               || Enum.class.isAssignableFrom(clazz);
    }

    @Override
    public <B, T> Predicate<B> compare(final ValueProvider<B, T> provider, final String searchQuery)
    {
        LOGGER.debug("Checking if the item does not contain {}", searchQuery);
        
        return item ->
        {
            final T apply = provider.apply(item);

            TypeHelper.checkIfTypeIsApplicable(this, apply.getClass());

            if (apply instanceof final String strValue)
            {
                LOGGER.debug("Item is an instance of String.");
                return !strValue.contains(searchQuery);
            }

            if (apply instanceof final Number numb && TypeHelper.isDouble(searchQuery))
            {
                LOGGER.debug("Item is an instance of Number.");
                return !numb.toString().contains(searchQuery);
            }

            if (apply instanceof final Enum<?> enm)
            {
                LOGGER.debug("Item is an instance of Enum.");
                return !enm.toString().contains(searchQuery);
            }
            
            LOGGER.debug("Condition is false because the type is not applicable.");
            return false;
        };
    }
}
