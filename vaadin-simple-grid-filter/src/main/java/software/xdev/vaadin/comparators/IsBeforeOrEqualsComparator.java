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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.function.Predicate;

import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.comparators.utl.TypeHelper;

// CPD-OFF - Fixed in v2
/**
 * Used for comparison with is before.
 */
public final class IsBeforeOrEqualsComparator implements FilterComparator
{
	public static final String IS_BEFORE_OR_EQUALS_COMPARATOR_DESCRIPTION = "is before or equals";
	
	private static IsBeforeOrEqualsComparator instance;
	
	private IsBeforeOrEqualsComparator()
	{
	}
	
	public static IsBeforeOrEqualsComparator getInstance()
	{
		if(instance == null)
		{
			instance = new IsBeforeOrEqualsComparator();
		}
		
		return instance;
	}
	
	@Override
	public String getDescription()
	{
		return IS_BEFORE_OR_EQUALS_COMPARATOR_DESCRIPTION;
	}
	
	@Override
	public boolean isApplicable(final Class<?> clazz)
	{
		return TemporalAccessor.class.isAssignableFrom(clazz);
	}
	
	@Override
	public <B, T> Predicate<B> compare(final ValueProvider<B, T> provider, final String searchQuery)
	{
		return item ->
		{
			final T apply = provider.apply(item);
			
			TypeHelper.checkIfTypeIsApplicable(this, apply.getClass());
			
			if(apply instanceof final LocalDate date && TypeHelper.isLocalDate(searchQuery))
			{
				final LocalDate dateInstance = LocalDate.from(date);
				final LocalDate parsedSearchQuery = LocalDate.parse(searchQuery);
				
				return dateInstance.isBefore(parsedSearchQuery)
					|| dateInstance.isEqual(parsedSearchQuery);
			}
			
			if(apply instanceof final LocalDateTime dateTime && TypeHelper.isLocalDateTime(searchQuery))
			{
				final LocalDateTime dateTimeInstance = LocalDateTime.from(dateTime);
				final LocalDateTime parsedSearchQuery = LocalDateTime.parse(searchQuery);
				
				return dateTimeInstance.isBefore(parsedSearchQuery)
					|| dateTimeInstance.isEqual(parsedSearchQuery);
			}
			
			return apply.equals(searchQuery);
		};
	}
}
