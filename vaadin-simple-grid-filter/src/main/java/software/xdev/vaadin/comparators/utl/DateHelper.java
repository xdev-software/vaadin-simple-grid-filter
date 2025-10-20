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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import com.vaadin.flow.component.datepicker.DatePicker;


/**
 * Used to build the date pattern from I18n.
 */
public final class DateHelper
{
	private static final Map<List<String>, DateTimeFormatter> CACHED_FORMATTERS =
		Collections.synchronizedMap(new WeakHashMap<>());
	
	private DateHelper()
	{
	}
	
	@SuppressWarnings("PMD.AvoidRecreatingDateTimeFormatter")
	public static DateTimeFormatter getDatePattern(final DatePicker.DatePickerI18n datePickerI18n)
	{
		Objects.requireNonNull(datePickerI18n);
		
		return CACHED_FORMATTERS.computeIfAbsent(
			datePickerI18n.getDateFormats(),
			formats -> DateTimeFormatter.ofPattern(formats
				.stream()
				.map(pattern -> "[" + pattern + "]")
				.collect(Collectors.joining())));
	}
}
