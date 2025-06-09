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
package software.xdev.vaadin.builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

import com.vaadin.flow.function.ValueProvider;

import software.xdev.vaadin.model.FilterField;
import software.xdev.vaadin.model.FilterFieldEnumExtension;
import software.xdev.vaadin.model.FilterProvider;


/**
 * Used to create a customizable FilterField.
 *
 * @see FilterField
 */
public class CustomizableFilterBuilder
{
	public static CustomizableFilterBuilder builder()
	{
		return new CustomizableFilterBuilder();
	}
	
	public <T> FilterField<T, String> withValueProvider(
		final FilterProvider.StringProvider<T> provider,
		final String description)
	{
		return this.withValueProvider(provider, description, String.class);
	}
	
	public <T> FilterField<T, Number> withValueProvider(
		final FilterProvider.NumberProvider<T> provider,
		final String description)
	{
		return this.withValueProvider(provider, description, Number.class);
	}
	
	public <T> FilterField<T, LocalDate> withValueProvider(
		final FilterProvider.LocalDateProvider<T> provider,
		final String description)
	{
		return this.withValueProvider(provider, description, LocalDate.class);
	}
	
	public <T> FilterField<T, LocalDateTime> withValueProvider(
		final FilterProvider.LocalDateTimeProvider<T> provider,
		final String description)
	{
		return this.withValueProvider(provider, description, LocalDateTime.class);
	}
	
	public <T> FilterField<T, Boolean> withValueProvider(
		final FilterProvider.BooleanProvider<T> provider,
		final String description)
	{
		return this.withValueProvider(provider, description, Boolean.class);
	}
	
	public <T> FilterField<T, Enum> withValueProvider(
		final FilterProvider.EnumProvider<T> provider,
		final String description,
		final Enum<?>[] enumValues)
	{
		return new FilterFieldEnumExtension<>(provider, description, Enum.class, new ArrayList<>(), enumValues);
	}
	
	public <T, X> FilterField<T, X> withValueProvider(
		final ValueProvider<T, X> provider,
		final String description,
		final Class<X> type)
	{
		Objects.requireNonNull(provider);
		Objects.requireNonNull(description);
		
		return new FilterField<>(provider, description, type, new ArrayList<>());
	}
}
