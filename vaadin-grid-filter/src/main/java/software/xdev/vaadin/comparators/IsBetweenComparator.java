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
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.comparators.utl.IncorrectSearchQueryFormatException;
import software.xdev.vaadin.comparators.utl.TypeHelper;


/**
 * Used for comparison with is after.
 */
public final class IsBetweenComparator implements FilterComparator
{
	public static final String IS_BETWEEN_COMPARATOR_DESCRIPTION = "is between";
	public static final String IS_BETWEEN_COMPARATOR_SEPARATOR = "#";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IsAfterComparator.class);
	private static IsBetweenComparator instance;
	
	private IsBetweenComparator()
	{
	}
	
	public static IsBetweenComparator getInstance()
	{
		if(instance == null)
		{
			instance = new IsBetweenComparator();
		}
		
		return instance;
	}
	
	@Override
	public String getDescription()
	{
		return IS_BETWEEN_COMPARATOR_DESCRIPTION;
	}
	
	@Override
	public boolean isApplicable(final Class<?> clazz)
	{
		return LocalDate.class.isAssignableFrom(clazz);
	}
	
	@Override
	public <B, T> Predicate<B> compare(final ValueProvider<B, T> provider, final String searchQuery)
	{
		return item ->
		{
			final T apply = provider.apply(item);
			TypeHelper.checkIfTypeIsApplicable(this, apply.getClass());
			
			final String startDate;
			final String endDate;
			
			try
			{
				if(searchQuery == null || searchQuery.isBlank())
				{
					return true;
				}
				
				final String[] dates = searchQuery.split(IS_BETWEEN_COMPARATOR_SEPARATOR);
				startDate = dates[0];
				endDate = dates[1];
				
				LOGGER.debug("Checking if the item is between {} and {}", startDate, endDate);
			}
			catch(final ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException)
			{
				throw new IncorrectSearchQueryFormatException(
					"Format of the following search query is not correct: '" + searchQuery + "'");
			}
			
			if(apply instanceof final LocalDate date
				&& TypeHelper.isLocalDate(startDate)
				&& TypeHelper.isLocalDate(endDate))
			{
				LOGGER.debug("Item is an instance of LocalDate.");
				return (LocalDate.from(date).isAfter(LocalDate.parse(startDate))
					|| LocalDate.from(date).isEqual(LocalDate.parse(startDate)))
					&& (LocalDate.from(date).isBefore(LocalDate.parse(endDate))
					|| LocalDate.from(date).isEqual(LocalDate.parse(endDate)));
			}
			
			return apply.equals(startDate) && apply.equals(endDate);
		};
	}
}
