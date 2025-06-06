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

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.vaadin.flow.function.ValueProvider;


public final class FilterProvider
{
	private FilterProvider()
	{
	}
	
	public interface StringProvider<T> extends ValueProvider<T, String>
	{
	}
	
	
	public interface NumberProvider<T> extends ValueProvider<T, Number>
	{
	}
	
	
	public interface LocalDateProvider<T> extends ValueProvider<T, LocalDate>
	{
	}
	
	
	public interface LocalDateTimeProvider<T> extends ValueProvider<T, LocalDateTime>
	{
	}
	
	
	public interface BooleanProvider<T> extends ValueProvider<T, Boolean>
	{
	}
	
	
	public interface EnumProvider<T> extends ValueProvider<T, Enum>
	{
	}
}
