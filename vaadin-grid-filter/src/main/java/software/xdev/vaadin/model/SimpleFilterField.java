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

import software.xdev.vaadin.builder.CustomizableFilterBuilder;


/**
 * Class for creating a FilterField with preconfigured conditions.
 *
 * @param <T> The bean.
 */
public class SimpleFilterField<T>
{
	private static final CustomizableFilterBuilder BUILDER = CustomizableFilterBuilder.builder();
	private final FilterField<T, ?> filterField;
	
	public SimpleFilterField(
		final FilterProvider.StringProvider<T> provider,
		final String description)
	{
		this.filterField =
			BUILDER.withValueProvider(provider, description)
				.withNotEqualComparator()
				.withEqualComparator()
				.withContainsComparator()
				.withNotContainsComparator();
	}
	
	public SimpleFilterField(
		final FilterProvider.EnumProvider<T> provider,
		final String description,
		final Enum<?>[] enumValues)
	{
		this.filterField = BUILDER
			.withValueProvider(provider, description, enumValues)
			.withEqualComparator()
			.withNotEqualComparator()
			.withContainsComparator()
			.withNotContainsComparator();
	}
	
	public SimpleFilterField(
		final FilterProvider.NumberProvider<T> provider,
		final String description)
	{
		this.filterField = BUILDER
			.withValueProvider(provider, description)
			.withEqualComparator()
			.withNotEqualComparator()
			.withContainsComparator()
			.withNotContainsComparator()
			.withLessThanComparator()
			.withGreaterThanComparator();
	}
	
	public SimpleFilterField(
		final FilterProvider.LocalDateProvider<T> provider,
		final String description)
	{
		this.filterField = BUILDER
			.withValueProvider(provider, description)
			.withIsBeforeComparator()
			.withIsAfterComparator()
			.withIsBetweenComparator();
	}
	
	public SimpleFilterField(
		final FilterProvider.LocalDateTimeProvider<T> provider,
		final String description)
	{
		this.filterField = BUILDER
			.withValueProvider(provider, description)
			.withIsBeforeComparator()
			.withIsAfterComparator();
	}
	
	public SimpleFilterField(
		final FilterProvider.BooleanProvider<T> provider,
		final String description)
	{
		this.filterField = BUILDER
			.withValueProvider(provider, description)
			.withEqualComparator()
			.withNotEqualComparator();
	}
	
	public FilterField<T, ?> getFilterField()
	{
		return this.filterField;
	}
}
