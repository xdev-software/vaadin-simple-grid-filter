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
package software.xdev.vaadin.utl;

import java.util.List;
import java.util.stream.Collectors;

import software.xdev.vaadin.model.ChipBadge;
import software.xdev.vaadin.model.ChipBadgeExtension;
import software.xdev.vaadin.model.FilterCondition;


public final class FilterComponentUtl<T>
{
	public FilterComponentUtl()
	{
	}
	
	/**
	 * Check if the lists contains the same chip badges objects
	 *
	 * @param one List one
	 * @param two List two
	 * @return True if the lists contains the same objects
	 */
	public boolean equalLists(
		final List<ChipBadgeExtension<FilterCondition<T, ?>>> one,
		final List<ChipBadgeExtension<FilterCondition<T, ?>>> two)
	{
		return one.stream()
			.map(ChipBadge::getBadgeId)
			.collect(Collectors.toSet())
			.equals(two.stream()
				.map(ChipBadge::getBadgeId)
				.collect(Collectors.toSet()));
	}
}
