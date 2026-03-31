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

import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.comparators.utl.TypeHelper;

// CPD-OFF - Fixed in v2
/**
 * Used for comparison with contains.
 */
public final class ContainsComparator implements FilterComparator
{
	public static final String CONTAINS_COMPARATOR_DESCRIPTION = "contains";
	
	private static ContainsComparator instance;
	
	private ContainsComparator()
	{
	}
	
	public static ContainsComparator getInstance()
	{
		if(instance == null)
		{
			instance = new ContainsComparator();
		}
		
		return instance;
	}
	
	@Override
	public String getDescription()
	{
		return CONTAINS_COMPARATOR_DESCRIPTION;
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
		return item ->
		{
			final T apply = provider.apply(item);
			
			TypeHelper.checkIfTypeIsApplicable(this, apply.getClass());
			
			if(apply instanceof final String strValue)
			{
				return strValue.contains(searchQuery);
			}
			
			if(apply instanceof final Number numb && TypeHelper.isDouble(searchQuery))
			{
				return numb.toString().contains(searchQuery);
			}
			
			if(apply instanceof final Enum<?> enm)
			{
				return enm.toString().contains(searchQuery);
			}
			
			return false;
		};
	}
}
