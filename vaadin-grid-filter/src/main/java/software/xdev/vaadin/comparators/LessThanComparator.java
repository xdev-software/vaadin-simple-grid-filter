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


/**
 * Used for comparison with less than.
 */
public final class LessThanComparator implements FilterComparator
{
	private static LessThanComparator instance;
	
	private LessThanComparator()
	{
	}
	
	public static LessThanComparator getInstance()
	{
		if(instance == null)
		{
			instance = new LessThanComparator();
		}
		
		return instance;
	}
	
	@Override
	public String getDescription()
	{
		return "is less than";
	}
	
	@Override
	public boolean isApplicable(final Class<?> clazz)
	{
		return Number.class.isAssignableFrom(clazz);
	}
	
	@Override
	public <B, T> Predicate<B> compare(final ValueProvider<B, T> provider, final String searchQuery)
	{
		return item ->
		{
			final T apply = provider.apply(item);
			
			TypeHelper.checkIfTypeIsApplicable(this, apply.getClass());
			
			if(apply instanceof final Number numb && TypeHelper.isDouble(searchQuery))
			{
				return numb.doubleValue() < (Double.parseDouble(searchQuery));
			}
			
			return false;
		};
	}
}
