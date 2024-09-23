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
package software.xdev.vaadin.model;

import com.vaadin.flow.function.ValueProvider;
import software.xdev.vaadin.comparators.FilterComparator;

import java.util.List;

/**
 * Extension of the FilterField with enum compatibility.
 *
 * @param <B> The bean.
 * @param <T> The type or field to use.
 */
public class FilterFieldEnumExtension<B, T extends Enum<?>> extends FilterField<B, T>
{
    private final Enum<?>[] enumValues;

    public FilterFieldEnumExtension(
            final ValueProvider<B, T> valueProvider,
            final String description,
            final Class<T> type,
            final List<FilterComparator> availableComparators,
            final Enum<?>[] enumValues)
    {
        super(valueProvider, description, type, availableComparators);
        this.enumValues = enumValues;
    }

    public Enum[] getEnumValues()
    {
        return this.enumValues;
    }

    @Override
    public FilterFieldEnumExtension<B, T> withAvailableComparator(final FilterComparator comparator)
    {
        if (this.getAvailableComparators().stream().noneMatch(c -> c.getClass() == comparator.getClass()))
        {
            this.getAvailableComparators().add(comparator);
        }

        return new FilterFieldEnumExtension<>(
                this.getValueProvider(),
                this.getDescription(),
                this.getType(),
                this.getAvailableComparators(),
                this.enumValues);
    }
}
