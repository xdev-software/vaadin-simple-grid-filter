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
package software.xdev.vaadin.comparators.utl;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.vaadin.flow.component.datepicker.DatePicker;


/**
 * Used to build the date pattern from I18n.
 */
public final class DateHelper
{
	private DateHelper()
	{
	}
	
	public static DateTimeFormatter getDatePattern(final DatePicker.DatePickerI18n datePickerI18n)
	{
		Objects.requireNonNull(datePickerI18n);
		
		final StringBuilder patternString = new StringBuilder();
		
		for(final String pattern : datePickerI18n.getDateFormats())
		{
			patternString.append("[").append(pattern).append("]");
		}
		
		return DateTimeFormatter.ofPattern(patternString.toString());
	}
}
