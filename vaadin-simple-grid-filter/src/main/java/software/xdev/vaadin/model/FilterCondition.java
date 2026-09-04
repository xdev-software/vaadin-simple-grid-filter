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

import software.xdev.vaadin.comparators.FilterComparator;


/**
 * @param <B> The bean.
 * @param <T> The type or field to use.
 */
public class FilterCondition<B, T>
{
	private final FilterField<B, T> item;
	private final FilterComparator selectedCondition;
	private final String inputValue;
	
	public FilterCondition(
		final FilterField<B, T> item,
		final FilterComparator selectedCondition,
		final String inputValue)
	{
		this.item = item;
		this.selectedCondition = selectedCondition;
		this.inputValue = inputValue;
	}
	
	public FilterField<B, T> getItem()
	{
		return this.item;
	}
	
	public FilterComparator getSelectedCondition()
	{
		return this.selectedCondition;
	}
	
	public String getInputValue()
	{
		return this.inputValue;
	}
	
	@Override
	public String toString()
	{
		return this.item.getDescription()
			+ " " + this.selectedCondition.getDescription()
			+ " " + this.inputValue;
	}
}
