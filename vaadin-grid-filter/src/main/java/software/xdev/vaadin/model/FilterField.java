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

import java.util.List;

import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.comparators.ContainsComparator;
import software.xdev.vaadin.comparators.EqualComparator;
import software.xdev.vaadin.comparators.FilterComparator;
import software.xdev.vaadin.comparators.GreaterThanComparator;
import software.xdev.vaadin.comparators.GreaterThanOrEqualsComparator;
import software.xdev.vaadin.comparators.IsAfterComparator;
import software.xdev.vaadin.comparators.IsAfterOrEqualsComparator;
import software.xdev.vaadin.comparators.IsBeforeComparator;
import software.xdev.vaadin.comparators.IsBeforeOrEqualsComparator;
import software.xdev.vaadin.comparators.IsBetweenComparator;
import software.xdev.vaadin.comparators.LessThanComparator;
import software.xdev.vaadin.comparators.LessThanOrEqualsComparator;
import software.xdev.vaadin.comparators.NotContainsComparator;
import software.xdev.vaadin.comparators.NotEqualComparator;


/**
 * @param <B> The bean.
 * @param <T> The type or field to use.
 */
public class FilterField<B, T>
{
	private final ValueProvider<B, T> valueProvider;
	private final String description;
	private final Class<T> type;
	private final List<FilterComparator> availableComparators;
	
	public FilterField(
		final ValueProvider<B, T> valueProvider,
		final String description,
		final Class<T> type,
		final List<FilterComparator> availableComparators)
	{
		this.valueProvider = valueProvider;
		this.description = description;
		this.type = type;
		this.availableComparators = availableComparators;
	}
	
	public ValueProvider<B, T> getValueProvider()
	{
		return this.valueProvider;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public Class<T> getType()
	{
		return this.type;
	}
	
	public List<FilterComparator> getAvailableComparators()
	{
		return this.availableComparators;
	}
	
	/**
	 * Used to make a custom comparator available for selection.
	 *
	 * @param comparator The custom comparator.
	 * @return Returns a new filter field.
	 */
	public FilterField<B, T> withAvailableComparator(final FilterComparator comparator)
	{
		if(this.availableComparators.stream().noneMatch(c -> c.getClass() == comparator.getClass()))
		{
			this.availableComparators.add(comparator);
		}
		
		return new FilterField<>(this.valueProvider, this.description, this.type, this.availableComparators);
	}
	
	public FilterField<B, T> withEqualComparator()
	{
		return this.withAvailableComparator(EqualComparator.getInstance());
	}
	
	public FilterField<B, T> withNotEqualComparator()
	{
		return this.withAvailableComparator(NotEqualComparator.getInstance());
	}
	
	public FilterField<B, T> withContainsComparator()
	{
		return this.withAvailableComparator(ContainsComparator.getInstance());
	}
	
	public FilterField<B, T> withGreaterThanComparator()
	{
		return this.withAvailableComparator(GreaterThanComparator.getInstance());
	}
	
	public FilterField<B, T> withIsAfterComparator()
	{
		return this.withAvailableComparator(IsAfterComparator.getInstance());
	}
	
	public FilterField<B, T> withIsBeforeComparator()
	{
		return this.withAvailableComparator(IsBeforeComparator.getInstance());
	}
	
	public FilterField<B, T> withLessThanComparator()
	{
		return this.withAvailableComparator(LessThanComparator.getInstance());
	}
	
	public FilterField<B, T> withNotContainsComparator()
	{
		return this.withAvailableComparator(NotContainsComparator.getInstance());
	}
	
	public FilterField<B, T> withIsBetweenComparator()
	{
		return this.withAvailableComparator(IsBetweenComparator.getInstance());
	}
	
	public FilterField<B, T> withLessThanOrEqualsComparator()
	{
		return this.withAvailableComparator(LessThanOrEqualsComparator.getInstance());
	}
	
	public FilterField<B, T> withGreaterThanOrEqualsComparator()
	{
		return this.withAvailableComparator(GreaterThanOrEqualsComparator.getInstance());
	}
	
	public FilterField<B, T> withIsAfterOrEqualsComparator()
	{
		return this.withAvailableComparator(IsAfterOrEqualsComparator.getInstance());
	}
	
	public FilterField<B, T> withIsBeforeOrEqualsComparator()
	{
		return this.withAvailableComparator(IsBeforeOrEqualsComparator.getInstance());
	}
}
