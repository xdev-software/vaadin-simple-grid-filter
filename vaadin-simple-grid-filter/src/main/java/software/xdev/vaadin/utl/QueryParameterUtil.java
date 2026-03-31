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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.router.QueryParameters;

import software.xdev.vaadin.model.FilterCondition;


public final class QueryParameterUtil
{
	private static final int BIGGEST_PARAMETER_COUNT = 7;
	
	public static final String QUERY_COMPONENT_ID_STRING = "id";
	public static final String QUERY_FIELD_STRING = "field";
	public static final String QUERY_CONDITION_STRING = "condition";
	public static final String QUERY_INPUT_STRING = "input";
	public static final String QUERY_BADGE_ID_STRING = "badgeId";
	public static final String QUERY_BADGE_EDITABLE_STRING = "editable";
	public static final String QUERY_BADGE_DELETABLE_STRING = "deletable";
	public static final String NO_BADGE_ID_STRING = "noBadgeId";
	
	private QueryParameterUtil()
	{
	}
	
	/**
	 * Used to check if the query parameters are not null, blank and have the correct number of parameters.
	 *
	 * @param parameterValues The query parameters which should be validated.
	 * @return Returns if parameters are valid.
	 */
	public static boolean parametersAreValid(final Map<String, List<String>> parameterValues)
	{
		boolean rightParameters = false;
		
		final List<String> ids = parameterValues.get(QUERY_COMPONENT_ID_STRING);
		final List<String> fields = parameterValues.get(QUERY_FIELD_STRING);
		final List<String> conditions = parameterValues.get(QUERY_CONDITION_STRING);
		final List<String> inputs = parameterValues.get(QUERY_INPUT_STRING);
		
		if((parameterValues.size() == 4 || parameterValues.size() == 5
			|| parameterValues.size() == BIGGEST_PARAMETER_COUNT)
			&& isNotNullOrEmpty(ids)
			&& isNotNullOrEmpty(fields)
			&& isNotNullOrEmpty(conditions)
			&& isNotNullOrEmpty(inputs)
			&& ids.size() == fields.size()
			&& ids.size() == conditions.size()
			&& ids.size() == inputs.size())
		{
			final Set<String> parametersToCheck = Set.of(
				QUERY_INPUT_STRING,
				QUERY_COMPONENT_ID_STRING,
				QUERY_FIELD_STRING,
				QUERY_CONDITION_STRING,
				QUERY_BADGE_ID_STRING,
				QUERY_BADGE_EDITABLE_STRING,
				QUERY_BADGE_DELETABLE_STRING);
			for(final Map.Entry<String, List<String>> entry : parameterValues.entrySet())
			{
				// Conditions and fields cannot be null or empty
				if(parametersToCheck.contains(entry.getKey())
					&& entry.getValue().stream().noneMatch(String::isBlank))
				{
					rightParameters = true;
				}
				else
				{
					return false;
				}
			}
		}
		
		return rightParameters;
	}
	
	/**
	 * Used to a create query parameter.
	 *
	 * @param <T>                 Bean type of the condition.
	 * @param componentIdentifier The identifier of the filter component.
	 * @param filterCondition     The condition which should be converted to a query parameter.
	 * @param badgeId The id of the initial filter badge.
	 * @return Returns new query parameters.
	 */
	public static <T> String createQueryParameterString(
		final String componentIdentifier,
		final FilterCondition<T, ?> filterCondition,
		final String badgeId,
		final Boolean badgeDeletable,
		final Boolean badgeEditable)
	{
		final Map<String, List<String>> query = new HashMap<>();
		query.put(
			QUERY_COMPONENT_ID_STRING,
			Collections.singletonList(componentIdentifier));
		query.put(
			QUERY_FIELD_STRING,
			Collections.singletonList(filterCondition.getItem().getDescription()));
		query.put(
			QUERY_CONDITION_STRING,
			Collections.singletonList(filterCondition.getSelectedCondition().getDescription()));
		query.put(
			QUERY_INPUT_STRING,
			Collections.singletonList(filterCondition.getInputValue()));
		
		// Set the badge id for the condition
		String finalBadgeId = badgeId;
		
		if(finalBadgeId == null || finalBadgeId.isBlank())
		{
			// Just the initial filter have a badgeId
			finalBadgeId = NO_BADGE_ID_STRING;
		}
		
		query.put(QUERY_BADGE_ID_STRING, Collections.singletonList(finalBadgeId));
		query.put(QUERY_BADGE_DELETABLE_STRING, Collections.singletonList(badgeDeletable.toString()));
		query.put(QUERY_BADGE_EDITABLE_STRING, Collections.singletonList(badgeEditable.toString()));
		
		return new QueryParameters(query).getQueryString();
	}
	
	public static boolean isNotNullOrEmpty(final Collection<?> c)
	{
		return c != null && !c.isEmpty();
	}
}
