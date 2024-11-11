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
package software.xdev.vaadin;

import static software.xdev.vaadin.comparators.IsBetweenComparator.IS_BETWEEN_COMPARATOR_DESCRIPTION;
import static software.xdev.vaadin.comparators.IsBetweenComparator.IS_BETWEEN_COMPARATOR_SEPARATOR;
import static software.xdev.vaadin.utl.QueryParameterUtil.NO_BADGE_ID_STRING;
import static software.xdev.vaadin.utl.QueryParameterUtil.QUERY_BADGE_DELETABLE_STRING;
import static software.xdev.vaadin.utl.QueryParameterUtil.QUERY_BADGE_EDITABLE_STRING;
import static software.xdev.vaadin.utl.QueryParameterUtil.QUERY_BADGE_ID_STRING;
import static software.xdev.vaadin.utl.QueryParameterUtil.QUERY_COMPONENT_ID_STRING;
import static software.xdev.vaadin.utl.QueryParameterUtil.QUERY_CONDITION_STRING;
import static software.xdev.vaadin.utl.QueryParameterUtil.QUERY_FIELD_STRING;
import static software.xdev.vaadin.utl.QueryParameterUtil.QUERY_INPUT_STRING;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;

import software.xdev.vaadin.builder.CustomizableFilterBuilder;
import software.xdev.vaadin.comparators.ContainsComparator;
import software.xdev.vaadin.comparators.FilterComparator;
import software.xdev.vaadin.comparators.utl.DateHelper;
import software.xdev.vaadin.daterange_picker.business.DateRange;
import software.xdev.vaadin.daterange_picker.business.DateRangeModel;
import software.xdev.vaadin.daterange_picker.business.SimpleDateRanges;
import software.xdev.vaadin.daterange_picker.ui.DateRangePicker;
import software.xdev.vaadin.model.ChipBadge;
import software.xdev.vaadin.model.ChipBadgeExtension;
import software.xdev.vaadin.model.CustomizationDegree;
import software.xdev.vaadin.model.FilterCondition;
import software.xdev.vaadin.model.FilterField;
import software.xdev.vaadin.model.FilterFieldEnumExtension;
import software.xdev.vaadin.model.SimpleFilterField;
import software.xdev.vaadin.utl.QueryParameterUtil;


/**
 * Component for filtering grids.
 *
 * @param <T> Grid type
 */
@SuppressWarnings("PMD.GodClass") // Fixed in v2
public class FilterComponent<T> extends Composite<VerticalLayout> implements BeforeEnterObserver
{
	public static final String CHIP_BADGE_FILTER_COMPONENT = "chipFilterComponent";
	public static final String BTN_ACCEPT_FILTER_FILTER_COMPONENT = "btnAcceptFilterFilterComponent";
	public static final String TXT_SEARCH_QUERY_FILTER_COMPONENT = "txtSearchQueryFilterComponent";
	public static final String NMB_SEARCH_QUERY_FILTER_COMPONENT = "nmbSearchQueryFilterComponent";
	public static final String DATE_TIME_SEARCH_QUERY_FILTER_COMPONENT = "dateTimeSearchQueryFilterComponent";
	public static final String DATE_SEARCH_QUERY_FILTER_COMPONENT = "dateSearchQueryFilterComponent";
	public static final String SEL_SEARCH_QUERY_FILTER_COMPONENT = "selSearchQueryFilterComponent";
	public static final String SEL_OPERATIONS_FILTER_COMPONENT = "selOperationsFilterComponent";
	public static final String SEL_FIELDS_FILTER_COMPONENT = "selFieldsFilterComponent";
	public static final String BTN_ADD_NEW_FILTER_FILTER_COMPONENT = "btnAddNewFilterFilterComponent";
	public static final String BTN_CANCEL_FILTER_FILTER_COMPONENT = "btnCancelFilterFilterComponent";
	public static final String DATE_RANGE_PICKER_QUERY_FILTER_COMPONENT = "dateRangePickerQueryFilterComponent";
	public static final String DELETED_INITIAL_CONDITION_STRING = "deletedInitialCondition";
	public static final String BTN_RESET_FILTER_FILTER_COMPONENT = "btnResetFilterFilterComponent";
	
	private final UI ui;
	private final Button btnAddNewFilter = new Button("Add filter");
	private final Button btnAcceptFilter = new Button(VaadinIcon.CHECK.create());
	private final Button btnCancelFilter = new Button(VaadinIcon.CLOSE.create());
	private final Button btnResetFilter = new Button(VaadinIcon.ROTATE_RIGHT.create());
	private final Select<FilterField<T, ?>> selFields = new Select<>();
	private final Select<FilterComparator> selOperations = new Select<>();
	private final TextField txtSearchQuery = new TextField();
	private final BigDecimalField nmbSearchQuery = new BigDecimalField();
	private final DatePicker dateSearchQuery = new DatePicker();
	private final DateTimePicker dateTimeSearchQuery = new DateTimePicker();
	private final Select<String> selSearchQuery = new Select<>();
	private final HorizontalLayout hlChipBadges = new HorizontalLayout();
	private DateRangePicker<DateRange> dateRangePickerQuery = createDateRangePicker();
	
	// Settings
	private static DateRangePicker<DateRange> createDateRangePicker()
	{
		return new DateRangePicker<>(
			() -> new DateRangeModel<>(LocalDate.now(), LocalDate.now(), SimpleDateRanges.TODAY),
			SimpleDateRanges.allValues());
	}
	
	// Container
	private final HorizontalLayout hlFilter = new HorizontalLayout();
	
	// Data
	private final List<ChipBadgeExtension<FilterCondition<T, ?>>> chipBadges = new ArrayList<>();
	private final List<ChipBadgeExtension<FilterCondition<T, ?>>> initialChipBadges = new ArrayList<>();
	
	private int initialConditionIdCounter = 1;
	private String editingBadgeId;
	private Boolean editingBadgeEnabled;
	private Boolean deletingBadgeEnabled;
	
	private final List<FilterField<T, ?>> filterFieldList = new ArrayList<>();
	private final Grid<T> dataGrid;
	
	private String identifier = "";
	
	// Query
	private List<String> queryComponentIdList = new LinkedList<>();
	private List<String> queryFieldList = new LinkedList<>();
	private List<String> queryConditionFieldList = new LinkedList<>();
	private List<String> queryInputFieldList = new LinkedList<>();
	private List<String> queryBadgeIdList = new LinkedList<>();
	private List<String> queryBadgeEditableList = new LinkedList<>();
	private List<String> queryBadgeDeletableList = new LinkedList<>();
	
	public FilterComponent(final Grid<T> dataGrid)
	{
		this.dataGrid = Objects.requireNonNull(dataGrid);
		this.ui = UI.getCurrent();
		this.initUI();
	}
	
	private void initUI()
	{
		final VerticalLayout vlRoot = this.getContent();
		vlRoot.add(new HorizontalLayout(this.btnAddNewFilter, this.btnResetFilter), this.hlFilter, this.hlChipBadges);
		
		// click listener
		this.btnAddNewFilter.addClickListener(e -> this.onShowFilterInput());
		this.btnCancelFilter.addClickListener(e -> this.hlFilter.removeAll());
		this.btnAcceptFilter.addClickListener(e -> this.onAcceptFilter());
		this.btnResetFilter.addClickListener(e -> this.onResetFilter());
		
		// value change listener
		this.selFields.addValueChangeListener(e -> this.onFieldChange(this.selFields.getValue()));
		this.selOperations.addValueChangeListener(e -> this.onOperatorChanged());
		
		// if input values from user and operations are null, do not activate 'btnAcceptFilter'
		this.nmbSearchQuery.addValueChangeListener(e ->
			this.btnAcceptFilter.setEnabled(e.getValue() != null && this.selOperations.getValue() != null));
		this.dateSearchQuery.addValueChangeListener(e ->
			this.btnAcceptFilter.setEnabled(e.getValue() != null && this.selOperations.getValue() != null));
		this.dateTimeSearchQuery.addValueChangeListener(e ->
			this.btnAcceptFilter.setEnabled(e.getValue() != null && this.selOperations.getValue() != null));
		this.dateRangePickerQuery.addValueChangeListener(e ->
			this.btnAcceptFilter.setEnabled(e.getValue() != null && this.selOperations.getValue() != null));
		this.selSearchQuery.addValueChangeListener(e ->
			this.btnAcceptFilter.setEnabled(e.getValue() != null && this.selOperations.getValue() != null));
		this.txtSearchQuery.addValueChangeListener(e ->
			this.btnAcceptFilter.setEnabled(!e.getValue().isBlank() && this.selOperations.getValue() != null));
		
		this.nmbSearchQuery.setValueChangeMode(ValueChangeMode.EAGER);
		this.txtSearchQuery.setValueChangeMode(ValueChangeMode.EAGER);
		
		// renderer
		this.selFields.setTextRenderer(FilterField::getDescription);
		this.selOperations.setTextRenderer(FilterComparator::getDescription);
		
		this.selFields.setEmptySelectionAllowed(false);
		this.selOperations.setEmptySelectionAllowed(false);
		
		this.btnAcceptFilter.addClickShortcut(Key.ENTER);
		this.btnCancelFilter.addClickShortcut(Key.ESCAPE);
		
		this.btnResetFilter.setEnabled(false);
		
		// ids
		this.btnAcceptFilter.setId(BTN_ACCEPT_FILTER_FILTER_COMPONENT);
		this.btnCancelFilter.setId(BTN_CANCEL_FILTER_FILTER_COMPONENT);
		this.btnResetFilter.setId(BTN_RESET_FILTER_FILTER_COMPONENT);
		this.selFields.setId(SEL_FIELDS_FILTER_COMPONENT);
		this.selOperations.setId(SEL_OPERATIONS_FILTER_COMPONENT);
		this.txtSearchQuery.setId(TXT_SEARCH_QUERY_FILTER_COMPONENT);
		this.nmbSearchQuery.setId(NMB_SEARCH_QUERY_FILTER_COMPONENT);
		this.dateSearchQuery.setId(DATE_SEARCH_QUERY_FILTER_COMPONENT);
		this.dateTimeSearchQuery.setId(DATE_TIME_SEARCH_QUERY_FILTER_COMPONENT);
		this.selSearchQuery.setId(SEL_SEARCH_QUERY_FILTER_COMPONENT);
		this.btnAddNewFilter.setId(BTN_ADD_NEW_FILTER_FILTER_COMPONENT);
		this.dateRangePickerQuery.setId(DATE_RANGE_PICKER_QUERY_FILTER_COMPONENT);
	}
	
	private void onResetFilter()
	{
		final List<ChipBadgeExtension<FilterCondition<T, ?>>> copyChipBadges = new ArrayList<>(this.chipBadges);
		for(final ChipBadgeExtension<FilterCondition<T, ?>> chipBadge : copyChipBadges)
		{
			this.removeChipBadgeCondition(chipBadge);
		}
		
		// Creating the initial filter again
		this.chipBadges.addAll(this.initialChipBadges);
		this.initialChipBadges.forEach(this.hlChipBadges::add);
		this.updateGridFilter();
		
		// Remove query parameter
		this.ui.getPage().fetchCurrentURL(currentUrl ->
			this.ui
				.getPage()
				.getHistory()
				.replaceState(null, currentUrl.getPath()));
		
		// Disable reset button again
		this.btnResetFilter.setEnabled(false);
	}
	
	private void onOperatorChanged()
	{
		this.btnAcceptFilter.setEnabled(this.shouldTheAcceptButtonBeEnabled());
		
		if(this.selOperations.getValue() != null)
		{
			if(this.selFields.getValue() instanceof FilterFieldEnumExtension)
			{
				final boolean isSelSearchQueryVisible = this.selSearchQuery.isVisible();
				
				// Change to a text field if the field is of type enum with condition 'contains'
				if(isSelSearchQueryVisible
					&& this.selOperations.getValue()
					.getDescription()
					.equals(ContainsComparator.CONTAINS_COMPARATOR_DESCRIPTION))
				{
					this.selSearchQuery.setVisible(false);
					this.txtSearchQuery.setVisible(true);
					
					this.btnAcceptFilter.setEnabled(this.shouldTheAcceptButtonBeEnabled());
				}
				else if(!isSelSearchQueryVisible)
				{
					this.selSearchQuery.setVisible(true);
					this.txtSearchQuery.setVisible(false);
					
					this.setEnumSelectValues(this.selFields.getValue());
				}
			}
			else if(this.selFields.getValue() != null && this.selFields.getValue().getType() == LocalDate.class)
			{
				if(this.selOperations.getValue().getDescription().contains(IS_BETWEEN_COMPARATOR_DESCRIPTION))
				{
					this.dateRangePickerQuery.setVisible(true);
					this.dateSearchQuery.setVisible(false);
					
					// Validating if the accept filter button should be activated because the date range picker
					// has already a value by default
					this.btnAcceptFilter.setEnabled(this.shouldTheAcceptButtonBeEnabled());
				}
				else if(this.selOperations.getValue() != null)
				{
					this.dateRangePickerQuery.setVisible(false);
					this.dateSearchQuery.setVisible(true);
				}
			}
		}
	}
	
	private void onShowFilterInput()
	{
		
		if(this.hlFilter.getChildren().findAny().isEmpty())
		{
			// Needed if the previous condition was an editable initial condition
			// The editable initial condition makes the cancel button invisible
			this.btnCancelFilter.setVisible(true);
			
			this.selFields.setValue(null);
			this.selFields.setReadOnly(false);
			this.selOperations.setItems(Collections.emptyList());
			this.selOperations.setEnabled(false);
			this.selOperations.setReadOnly(false);
			this.btnAcceptFilter.setEnabled(false);
			this.txtSearchQuery.clear();
			this.nmbSearchQuery.clear();
			this.dateSearchQuery.clear();
			this.dateTimeSearchQuery.clear();
			this.selSearchQuery.clear();
			this.setInputComponentVisibility(String.class);
			
			this.hlFilter.add(
				this.selFields,
				this.selOperations,
				this.txtSearchQuery,
				this.nmbSearchQuery,
				this.dateSearchQuery,
				this.dateTimeSearchQuery,
				this.dateRangePickerQuery,
				this.selSearchQuery,
				this.btnCancelFilter,
				this.btnAcceptFilter
			);
		}
	}
	
	/**
	 * Method used to make the select field and select condition dependent on the customization degree readonly.
	 *
	 * @param usedCustomizationDegree Used to set which input field is readonly.
	 */
	private void setUsedCustomizationDegreeForComponents(final CustomizationDegree usedCustomizationDegree)
	{
		if(usedCustomizationDegree.equals(CustomizationDegree.INPUT_VALUE))
		{
			this.selFields.setReadOnly(true);
			this.selOperations.setReadOnly(true);
		}
		else if(usedCustomizationDegree.equals(CustomizationDegree.CONDITION_AND_INPUT_VALUE))
		{
			this.selFields.setReadOnly(true);
			this.selOperations.setReadOnly(false);
		}
		else
		{
			this.selFields.setReadOnly(false);
			this.selOperations.setReadOnly(false);
		}
	}
	
	/**
	 * Clicking on the accept filter button.
	 */
	private void onAcceptFilter()
	{
		final String userInput = this.getValueFromVisibleComponent();
		
		final ChipBadgeExtension<FilterCondition<T, ?>> badge;
		CustomizationDegree customizationDegree = CustomizationDegree.EVERYTHING;
		final boolean deletable;
		final boolean editable;
		
		if(this.deletingBadgeEnabled != null && this.editingBadgeEnabled != null)
		{
			deletable = this.deletingBadgeEnabled;
			editable = this.editingBadgeEnabled;
			
			// Get customization rating from initial condition
			customizationDegree = this.initialChipBadges
				.stream()
				.filter(e -> e.getBadgeId().equals(this.editingBadgeId))
				.toList()
				.get(0)
				.getCustomizationRating();
		}
		else
		{
			deletable = true;
			editable = false;
		}
		
		badge = this.createBadgeConditionAndApplyFilter(
			this.selFields.getValue(),
			this.selOperations.getValue(),
			userInput,
			deletable,
			editable,
			customizationDegree);
		
		if(!this.identifier.isBlank())
		{
			if(this.editingBadgeId != null && !this.editingBadgeId.equals(NO_BADGE_ID_STRING))
			{
				badge.setBadgeId(this.editingBadgeId);
				this.editingBadgeId = null;
			}
			else
			{
				badge.setBadgeId(NO_BADGE_ID_STRING);
			}
			
			this.addQueryParameter(badge);
		}
		
		// Removing all filter components after accepting
		this.hlFilter.removeAll();
		
		// When something changes from the initial start, enable the reset button
		this.btnResetFilter.setEnabled(true);
	}
	
	private ChipBadgeExtension<FilterCondition<T, ?>> createBadgeConditionAndApplyFilter(
		final FilterField<T, ?> selField,
		final FilterComparator selOperation,
		final String userInput,
		final boolean deletableCondition,
		final boolean editableCondition,
		final CustomizationDegree customizationDegree)
	{
		final ChipBadgeExtension<FilterCondition<T, ?>> badge = new ChipBadgeExtension<>(
			new FilterCondition<>(
				selField,
				selOperation,
				userInput
			));
		
		badge.setId(CHIP_BADGE_FILTER_COMPONENT);
		
		if(editableCondition)
		{
			badge.setBtnEditEnabled(true);
			badge.addBtnEditClickListener(event ->
			{
				if((this.hlFilter.getChildren().findAny().isEmpty()))
				{
					// Add all components to the hlFilter layout
					this.onShowFilterInput();
					
					// Set the available values
					this.selFields.setValue(selField);
					
					// Will set the visibility of the components and the items for the operation select
					this.onFieldChange(selField);
					
					// Set selected operation
					this.selOperations.setValue(selOperation);
					
					// Set the query value
					this.setQueryValue(userInput);
					
					// Make the cancel button invisible
					this.btnCancelFilter.setVisible(false);
					
					// Needed to save state of the condition if it was editable/deletable before editing
					this.editingBadgeEnabled = badge.isBtnEditEnabled();
					this.deletingBadgeEnabled = badge.isBtnDeleteEnabled();
					
					// Just activated when the url parameters are enabled
					// Set the customization rating for the filter select and condition select
					this.setUsedCustomizationDegreeForComponents(customizationDegree);
					
					// Just activated when the url parameters are activated
					if(!this.identifier.isBlank())
					{
						this.editingBadgeId = badge.getBadgeId();
					}
					
					// Remove filter, update grid
					this.removeChipBadgeCondition(badge);
				}
			});
		}
		
		this.deactivateDeleteButtonFromChipComponents(deletableCondition, badge);
		
		// Format chip badge text if it contains LocalDate/LocalDateTime
		if(TemporalAccessor.class.isAssignableFrom(badge.getItem().getItem().getType())
			&& badge.getItem().getSelectedCondition().isApplicable(badge.getItem().getItem().getType()))
		{
			this.formatLocalDateChipBadgeText(badge);
		}
		
		this.chipBadges.add(badge);
		this.hlChipBadges.add(badge);
		
		this.updateGridFilter();
		
		return badge;
	}
	
	private void deactivateDeleteButtonFromChipComponents(
		final boolean conditionDeletable,
		final ChipBadgeExtension<FilterCondition<T, ?>> badge)
	{
		badge.setBtnDeleteEnabled(conditionDeletable);
		
		if(conditionDeletable)
		{
			badge.addBtnDeleteClickListener(e ->
			{
				this.removeChipBadgeCondition(badge);
				
				if(!this.identifier.isBlank()
					&& badge.getBadgeId() != null
					&& !badge.getBadgeId().equals(NO_BADGE_ID_STRING))
				{
					badge.setBadgeId(DELETED_INITIAL_CONDITION_STRING);
					this.addQueryParameter(badge);
				}
				
				// Activate the reset button
				this.btnResetFilter.setEnabled(true);
			});
		}
	}
	
	/**
	 * Format chip badge input text for LocalDate and LocalDateTime.
	 */
	@SuppressWarnings("PMD.CognitiveComplexity") // Fixed in v2
	private void formatLocalDateChipBadgeText(final ChipBadge<FilterCondition<T, ?>> chipBadge)
	{
		final FilterCondition<T, ?> filterField = chipBadge.getItem();
		
		if(this.dateRangePickerQuery.isVisible()
			&& filterField.getSelectedCondition().getDescription().equals(IS_BETWEEN_COMPARATOR_DESCRIPTION))
		{
			chipBadge.setItemLabelGenerator((ItemLabelGenerator<FilterCondition<T, ?>>)tFilterCondition ->
			{
				LocalDate startDate;
				LocalDate endDate;
				
				try
				{
					final String[] dates = tFilterCondition.getInputValue().split(IS_BETWEEN_COMPARATOR_SEPARATOR);
					
					startDate = LocalDate.parse(dates[0]);
					endDate = LocalDate.parse(dates[1]);
				}
				catch(final DateTimeParseException e)
				{
					startDate = LocalDate.MIN;
					endDate = LocalDate.MAX;
				}
				
				String dateString;
				
				if(this.dateRangePickerQuery.getDatePickerI18n().isPresent()
					&& this.dateRangePickerQuery.getDatePickerI18n().get().getDateFormats() != null)
				{
					final DatePicker.DatePickerI18n datePickerI18n =
						this.dateRangePickerQuery.getDatePickerI18n().get();
					
					dateString = startDate.format(DateHelper.getDatePattern(datePickerI18n));
					dateString += " and " + endDate.format(DateHelper.getDatePattern(datePickerI18n));
				}
				else
				{
					dateString = startDate + " and " + endDate;
				}
				
				return createChipComponentString(tFilterCondition, dateString);
			});
		}
		else if(this.dateSearchQuery.isVisible() && filterField.getItem().getType().equals(LocalDate.class))
		{
			chipBadge.setItemLabelGenerator((ItemLabelGenerator<FilterCondition<T, ?>>)tFilterCondition ->
			{
				LocalDate localDate;
				
				try
				{
					localDate = LocalDate.parse(tFilterCondition.getInputValue());
				}
				catch(final DateTimeParseException e)
				{
					localDate = LocalDate.MIN;
				}
				
				final String dateString;
				
				if(this.dateSearchQuery.getI18n() != null
					&& this.dateSearchQuery.getI18n().getDateFormats() != null)
				{
					dateString = localDate.format(DateHelper.getDatePattern(this.dateSearchQuery.getI18n()));
				}
				else
				{
					dateString = localDate.toString();
				}
				
				return createChipComponentString(tFilterCondition, dateString);
			});
		}
		else if(this.dateTimeSearchQuery.isVisible() && filterField.getItem().getType().equals(LocalDateTime.class))
		{
			chipBadge.setItemLabelGenerator((ItemLabelGenerator<FilterCondition<T, ?>>)tFilterCondition ->
				{
					LocalDateTime localDateTime;
					LocalDate localDate;
					
					try
					{
						localDateTime = LocalDateTime.parse(tFilterCondition.getInputValue());
						localDate = LocalDateTime.parse(tFilterCondition.getInputValue()).toLocalDate();
					}
					catch(final DateTimeParseException e)
					{
						localDateTime = LocalDateTime.MIN;
						localDate = LocalDate.MIN;
					}
					
					String dateString;
					
					if(this.dateTimeSearchQuery.getDatePickerI18n() != null
						&& this.dateTimeSearchQuery.getDatePickerI18n().getDateFormats() != null)
					{
						dateString =
							localDate.format(DateHelper.getDatePattern(
								this.dateTimeSearchQuery.getDatePickerI18n()).withLocale(
								this.dateSearchQuery.getLocale()));
					}
					else
					{
						dateString =
							localDate
								.format(DateTimeFormatter.ISO_LOCAL_DATE
									.withLocale(this.dateSearchQuery.getLocale()));
					}
					
					dateString += " " + localDateTime.toLocalTime();
					
					return createChipComponentString(tFilterCondition, dateString);
				}
			);
		}
	}
	
	private static <T> String createChipComponentString(
		final FilterCondition<T, ?> tFilterCondition,
		final String dateString)
	{
		return tFilterCondition.getItem().getDescription() + " "
			+ tFilterCondition.getSelectedCondition().getDescription() + " "
			+ dateString;
	}
	
	/**
	 * Get the value from the component which is visible at the moment.
	 *
	 * @return Value of visible component.
	 */
	private String getValueFromVisibleComponent()
	{
		if(this.nmbSearchQuery.isVisible())
		{
			return this.nmbSearchQuery.getValue().toString();
		}
		else if(this.dateSearchQuery.isVisible())
		{
			return this.dateSearchQuery.getValue().toString();
		}
		else if(this.dateTimeSearchQuery.isVisible())
		{
			return this.dateTimeSearchQuery.getValue().format(DateTimeFormatter.ISO_DATE_TIME);
		}
		else if(this.dateRangePickerQuery.isVisible())
		{
			// '#' is the separator for the between two dates search query
			return this.dateRangePickerQuery.getValue().getStart() + IS_BETWEEN_COMPARATOR_SEPARATOR
				+ this.dateRangePickerQuery.getValue()
				.getEnd();
		}
		else if(this.selSearchQuery.isVisible())
		{
			return this.selSearchQuery.getValue();
		}
		else
		{
			return this.txtSearchQuery.getValue();
		}
	}
	
	/**
	 * Method for setting the query value for the visible input field.
	 *
	 * @param searchQuery The value which should be set.
	 */
	private void setQueryValue(final String searchQuery)
	{
		if(this.nmbSearchQuery.isVisible())
		{
			this.nmbSearchQuery.setValue(new BigDecimal(searchQuery));
		}
		else if(this.dateSearchQuery.isVisible())
		{
			this.dateSearchQuery.setValue(LocalDate.parse(searchQuery));
		}
		else if(this.dateTimeSearchQuery.isVisible())
		{
			this.dateTimeSearchQuery.setValue(LocalDateTime.parse(searchQuery));
		}
		else if(this.selSearchQuery.isVisible())
		{
			this.selSearchQuery.setValue(searchQuery);
		}
		else
		{
			this.txtSearchQuery.setValue(searchQuery);
		}
	}
	
	/**
	 * Is used to check if the values in the input fields are null and an operation is selected.
	 *
	 * @return Returns true when the value of the input fields are not null and an operation is selected.
	 */
	private boolean shouldTheAcceptButtonBeEnabled()
	{
		final boolean valueNotNull;
		
		if(this.nmbSearchQuery.isVisible())
		{
			valueNotNull = this.nmbSearchQuery.getValue() != null;
		}
		else if(this.dateSearchQuery.isVisible())
		{
			valueNotNull = this.dateSearchQuery.getValue() != null;
		}
		else if(this.dateTimeSearchQuery.isVisible())
		{
			valueNotNull = this.dateTimeSearchQuery.getValue() != null;
		}
		else if(this.dateRangePickerQuery.isVisible())
		{
			valueNotNull = this.dateRangePickerQuery.getValue().getStart() != null
				&& this.dateRangePickerQuery.getValue().getEnd() != null;
		}
		else if(this.selSearchQuery.isVisible())
		{
			valueNotNull = this.selSearchQuery.getValue() != null;
		}
		else
		{
			// For txtSearchQuery
			valueNotNull = !this.txtSearchQuery.getValue().isBlank();
		}
		
		return valueNotNull && this.selOperations.getValue() != null;
	}
	
	/**
	 * This method will be called when the selected column in the filter component changes.
	 */
	private void onFieldChange(final FilterField<T, ?> filterField)
	{
		if(filterField != null)
		{
			this.setInputComponentVisibility(filterField.getType());
			final List<FilterComparator> comparatorList = filterField.getAvailableComparators();
			
			this.selOperations.setItems(comparatorList);
			this.selOperations.setEnabled(!comparatorList.isEmpty());
			
			// Check if a filterField is an enum
			if(filterField instanceof FilterFieldEnumExtension)
			{
				this.setEnumSelectValues(this.selFields.getValue());
			}
			else if(filterField.getType() == Boolean.class)
			{
				this.selSearchQuery.setItems(new ArrayList<>(Arrays.asList("true", "false")));
			}
		}
	}
	
	private void setInputComponentVisibility(final Class<?> type)
	{
		this.dateTimeSearchQuery.setVisible(false);
		this.dateSearchQuery.setVisible(false);
		this.dateRangePickerQuery.setVisible(false);
		this.nmbSearchQuery.setVisible(false);
		this.selSearchQuery.setVisible(false);
		this.txtSearchQuery.setVisible(false);
		
		if(type.isAssignableFrom(LocalDateTime.class))
		{
			this.dateTimeSearchQuery.setVisible(true);
		}
		else if(type.isAssignableFrom(LocalDate.class))
		{
			this.dateSearchQuery.setVisible(true);
		}
		else if(type.isAssignableFrom(Number.class))
		{
			this.nmbSearchQuery.setVisible(true);
		}
		else if(type.isAssignableFrom(Enum.class) || type.isAssignableFrom(Boolean.class))
		{
			this.selSearchQuery.setVisible(true);
		}
		else
		{
			this.txtSearchQuery.setVisible(true);
		}
	}
	
	private void updateGridFilter()
	{
		if(this.chipBadges.isEmpty())
		{
			this.dataGrid.getListDataView().removeFilters();
			return;
		}
		
		this.dataGrid.getListDataView().setFilter(item ->
		{
			final List<Predicate<T>> predicates = new ArrayList<>();
			
			for(final ChipBadge<FilterCondition<T, ?>> chipBadge : this.chipBadges)
			{
				final FilterCondition<T, ?> item1 = chipBadge.getItem();
				final String inputValue = item1.getInputValue();
				
				predicates.add(
					
					item1.getSelectedCondition().compare(
						item1.getItem().getValueProvider(),
						inputValue)
				);
			}
			
			return predicates.stream().map(p -> p.test(item)).reduce(Boolean::logicalAnd).orElseThrow();
		});
	}
	
	/**
	 * Used to set the enum values in the select search query.
	 *
	 * @param filterField Used filterField.
	 */
	private void setEnumSelectValues(final FilterField<T, ?> filterField)
	{
		final FilterFieldEnumExtension<T, ?> fieldEnumExtension = (FilterFieldEnumExtension<T, ?>)filterField;
		this.selSearchQuery.setItems(Stream.of(fieldEnumExtension.getEnumValues())
			.map(Enum::name)
			.toList());
	}
	
	@SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CollapsibleIfStatements"}) // Fixed in v2
	private void createConditionsFromQueryParameters()
	{
		for(int i = 0; i < this.queryFieldList.size(); i++)
		{
			final String conditionDescription = this.queryConditionFieldList.get(i);
			
			for(final FilterField<T, ?> filterField : this.selFields.getListDataView().getItems().toList())
			{
				final Optional<FilterComparator> comparatorOptional = filterField
					.getAvailableComparators()
					.stream()
					.filter(x -> x.getDescription().equals(conditionDescription))
					.findAny();
				
				// Check if the comparator is available for this field
				if(filterField.getDescription().equals(this.queryFieldList.get(i)) && comparatorOptional.isPresent())
				{
					if(i < this.queryBadgeIdList.size()
						&& i < this.queryBadgeDeletableList.size()
						&& i < this.queryBadgeEditableList.size())
					{
						if(!this.queryBadgeIdList.get(i).equals(DELETED_INITIAL_CONDITION_STRING))
						{
							final String badgeId = this.queryBadgeIdList.get(i);
							CustomizationDegree customizationDegree = CustomizationDegree.EVERYTHING;
							
							// Check if it's an initial condition
							final List<ChipBadgeExtension<FilterCondition<T, ?>>> cD = this.initialChipBadges
								.stream()
								.filter(e -> e.getBadgeId().equals(badgeId))
								.toList();
							
							if(!cD.isEmpty())
							{
								customizationDegree = cD.get(0).getCustomizationRating();
							}
							
							final ChipBadgeExtension<FilterCondition<T, ?>> chipBadgeExtension =
								this.createBadgeConditionAndApplyFilter(
									filterField,
									comparatorOptional.get(),
									this.queryInputFieldList.get(i),
									Boolean.parseBoolean(this.queryBadgeDeletableList.get(i)),
									Boolean.parseBoolean(this.queryBadgeEditableList.get(i)),
									customizationDegree);
							
							chipBadgeExtension.setBadgeId(badgeId);
						}
						else
						{
							final List<ChipBadgeExtension<FilterCondition<T, ?>>> tmpChipBadges =
								new ArrayList<>(this.chipBadges);
							
							// Remove badge if it has the badge id 'deletedInitialCondition'
							for(final ChipBadgeExtension<FilterCondition<T, ?>> chipBadge : tmpChipBadges)
							{
								// Check for same condition
								final FilterCondition<T, ?> item = chipBadge.getItem();
								if(!chipBadge.getBadgeId().equals(NO_BADGE_ID_STRING)
									&& this.queryInputFieldList.get(i).equals(item.getInputValue())
									&& this.queryConditionFieldList.get(i)
									.equals(item.getSelectedCondition().getDescription())
									&& this.queryFieldList.get(i).equals(item.getItem().getDescription()))
								{
									this.removeChipBadgeCondition(chipBadge);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Removes the chip component, the associated condition and query parameter. It also updates the grid.
	 *
	 * @param chip The condition component which should be removed.
	 */
	private void removeChipBadgeCondition(final ChipBadgeExtension<FilterCondition<T, ?>> chip)
	{
		this.chipBadges.remove(chip);
		this.hlChipBadges.remove(chip);
		this.updateGridFilter();
		this.removeQueryParameter(chip);
	}
	
	/**
	 * Sets the internationalization properties for the datePicker.
	 *
	 * @param datePickerI18n The internationalised properties that should be set.
	 * @return Returns this filter-component.
	 */
	public FilterComponent<T> withDatePickerI18n(final DatePicker.DatePickerI18n datePickerI18n)
	{
		Objects.requireNonNull(datePickerI18n);
		this.dateSearchQuery.setI18n(datePickerI18n);
		
		return this;
	}
	
	/**
	 * Sets the internationalization properties for the dateTimePicker.
	 *
	 * @param dateTimePickerI18n The internationalised properties that should be set.
	 * @return Returns this filter-component.
	 */
	public FilterComponent<T> withDateTimePickerI18n(final DatePicker.DatePickerI18n dateTimePickerI18n)
	{
		Objects.requireNonNull(dateTimePickerI18n);
		this.dateTimeSearchQuery.setDatePickerI18n(dateTimePickerI18n);
		
		return this;
	}
	
	/**
	 * Sets the internationalization properties for the dateRangePicker.
	 *
	 * @param datePickerI18n The internationalised properties that should be set.
	 * @return Returns this filter-component.
	 */
	public FilterComponent<T> withDateRangePickerI18n(final DatePicker.DatePickerI18n datePickerI18n)
	{
		Objects.requireNonNull(datePickerI18n);
		this.dateRangePickerQuery.withDatePickerI18n(datePickerI18n);
		
		return this;
	}
	
	/**
	 * Sets the locale for the dateTimePicker. Has no effect when setting a custom date format through
	 * {@link  software.xdev.vaadin.FilterComponent#withDatePickerI18n(DatePicker.DatePickerI18n)}.
	 *
	 * @param locale The locale which should be set.
	 * @return Returns this filter-component.
	 * @see software.xdev.vaadin.FilterComponent#withDatePickerI18n(DatePicker.DatePickerI18n)
	 */
	public FilterComponent<T> withDatePickerLocale(final Locale locale)
	{
		Objects.requireNonNull(locale);
		this.dateSearchQuery.setLocale(locale);
		
		return this;
	}
	
	/**
	 * Sets the locale for the dateTimePicker. Has no effect when setting a custom date format through
	 * {@link  software.xdev.vaadin.FilterComponent#withDateTimePickerI18n(DatePicker.DatePickerI18n)}.
	 *
	 * @param locale The locale which should be set.
	 * @return Returns this filter-component.
	 * @see software.xdev.vaadin.FilterComponent#withDateTimePickerI18n(DatePicker.DatePickerI18n)
	 */
	public FilterComponent<T> withDateTimePickerLocale(final Locale locale)
	{
		Objects.requireNonNull(locale);
		this.dateTimeSearchQuery.setLocale(locale);
		
		return this;
	}
	
	/**
	 * Sets a custom date range model for the DateRangePicker.
	 *
	 * @param dateRangeModel The custom date range model which should be set for the DateRangePicker search query.
	 * @return Returns this filter-component.
	 */
	public FilterComponent<T> withCustomDateRangeModel(
		final DateRangeModel<DateRange> dateRangeModel,
		final Collection<DateRange> items)
	{
		Objects.requireNonNull(dateRangeModel);
		this.dateRangePickerQuery = new DateRangePicker<>(dateRangeModel, items);
		this.dateRangePickerQuery.setId(DATE_RANGE_PICKER_QUERY_FILTER_COMPONENT);
		
		return this;
	}
	
	/**
	 * Sets the date range picker localization.
	 *
	 * @param dateRangeLocalizerFunction Localization function for the date range. See <a href="https://github
	 *                                   .com/xdev-software/vaadin-date-range-picker/blob
	 *                                   /75d496fb2e1a0ece4d8d3f4f1071fd605167a9c8/vaadin-date-range-picker-demo
	 *                                   /src/main/java/software/xdev/vaadin/daterange_picker/example
	 *                                   /DateRangePickerLocalizedDemo.java#L39">following link</a> for an example.
	 * @param startLabel                 Localization for the start label.
	 * @param endLabel                   Localization for the end label.
	 * @param dateRangeOptionsLabel      Localization for the date range options label.
	 * @return Returns this filter-component.
	 */
	public FilterComponent<T> withDateRangeLocalization(
		final ItemLabelGenerator<DateRange> dateRangeLocalizerFunction,
		final String startLabel,
		final String endLabel,
		final String dateRangeOptionsLabel)
	{
		Objects.requireNonNull(dateRangeLocalizerFunction);
		Objects.requireNonNull(startLabel);
		Objects.requireNonNull(endLabel);
		Objects.requireNonNull(dateRangeOptionsLabel);
		
		this.dateRangePickerQuery.withDateRangeLocalizerFunction(dateRangeLocalizerFunction);
		this.dateRangePickerQuery.setStartLabel(startLabel);
		this.dateRangePickerQuery.setEndLabel(endLabel);
		this.dateRangePickerQuery.setDateRangeOptionsLabel(dateRangeOptionsLabel);
		
		return this;
	}
	
	/**
	 * Sets the text of the 'AddNewFilter'-Button.
	 *
	 * @param buttonText The text displayed on the button.
	 * @return Returns this filter-component.
	 */
	public FilterComponent<T> withFilterButtonText(final String buttonText)
	{
		Objects.requireNonNull(buttonText);
		this.btnAddNewFilter.setText(buttonText);
		
		return this;
	}
	
	/**
	 * Used to create a new filter.
	 *
	 * @param filterField The new filterField that should be created. The CustomizableFilterBuilder cane be used here.
	 * @param <X>         The type of the field.
	 * @return Returns this filter-component.
	 * @see CustomizableFilterBuilder
	 */
	public <X> FilterComponent<T> withFilter(final FilterField<T, X> filterField)
	{
		Objects.requireNonNull(filterField);
		
		final List<FilterField<T, ?>> filterFields = this.filterFieldList.stream()
			.filter(f -> f.getDescription().equals(filterField.getDescription()))
			.toList();
		
		// Check if filter field is already existing
		// If not add the filter field to the filterFieldList
		if(filterFields.isEmpty())
		{
			
			this.filterFieldList.add(filterField);
			this.selFields.setItems(this.filterFieldList);
		}
		else
		{
			this.checkForNotDuplicatedComparatorsAndAddThemToTheFilterField(filterField, filterFields.get(0));
		}
		
		return this;
	}
	
	private <X> FilterField<T, ?> checkForNotDuplicatedComparatorsAndAddThemToTheFilterField(
		final FilterField<T, X> newFilterField,
		final FilterField<T, ?> oldFilterField)
	{
		FilterField<T, ?> newlyBuildFilterField = oldFilterField;
		
		for(final FilterComparator comparator : newFilterField.getAvailableComparators())
		{
			if(!oldFilterField.getAvailableComparators().contains(comparator))
			{
				// Add filter field with new comparator
				this.filterFieldList.remove(oldFilterField);
				newlyBuildFilterField = oldFilterField.withAvailableComparator(comparator);
				this.filterFieldList.add(oldFilterField);
			}
		}
		
		return newlyBuildFilterField;
	}
	
	/**
	 * Used to create a new filter with preconfigured comparators.
	 *
	 * @param simpleFilterField The new filterField that should be created. The SimpleFilterField can be used here.
	 * @return Returns this filter-component.
	 * @see SimpleFilterField
	 */
	public FilterComponent<T> withFilter(final SimpleFilterField<T> simpleFilterField)
	{
		Objects.requireNonNull(simpleFilterField);
		
		return this.withFilter(simpleFilterField.getFilterField());
	}
	
	/**
	 * Used to activate query parameters for the filter component.
	 *
	 * @param identifier Used to identify the filter component within the query parameters.
	 * @return Returns this filter component.
	 */
	public FilterComponent<T> withUrlParameters(final String identifier)
	{
		Objects.requireNonNull(identifier);
		
		if(!identifier.isBlank())
		{
			this.identifier = identifier;
		}
		
		return this;
	}
	
	/**
	 * 'setParameter' wouldn't be called in this class that's why we are using 'beforeEnter' here. Get the query
	 * parameter and create the filters. Duplicated query parameters will create just one condition.
	 *
	 * @param beforeEnterEvent Before navigation event with event details.
	 */
	@Override
	public void beforeEnter(final BeforeEnterEvent beforeEnterEvent)
	{
		// Get conditions with same identifier as this component
		if(!this.identifier.isBlank())
		{
			final Map<String, List<String>> parametersMap =
				beforeEnterEvent.getLocation().getQueryParameters().getParameters();
			
			if(QueryParameterUtil.parametersAreValid(parametersMap))
			{
				this.queryComponentIdList.clear();
				this.queryFieldList.clear();
				this.queryConditionFieldList.clear();
				this.queryInputFieldList.clear();
				
				this.queryBadgeIdList.clear();
				this.queryBadgeEditableList.clear();
				this.queryBadgeDeletableList.clear();
				
				final List<String> idList = parametersMap.get(QUERY_COMPONENT_ID_STRING);
				
				// Get all indices which component ids are matching this one
				final int[] matchingIndices = IntStream.range(0, idList.size())
					.filter(i -> this.identifier.equals(idList.get(i)))
					.toArray();
				
				String componentId, field, condition, input, badgeId, editable, deletable;
				
				for(final int i : matchingIndices)
				{
					componentId = parametersMap.get(QUERY_COMPONENT_ID_STRING).get(i);
					field = parametersMap.get(QUERY_FIELD_STRING).get(i);
					condition = parametersMap.get(QUERY_CONDITION_STRING).get(i);
					input = parametersMap.get(QUERY_INPUT_STRING).get(i);
					
					badgeId = parametersMap.get(QUERY_BADGE_ID_STRING).get(i);
					editable = parametersMap.get(QUERY_BADGE_EDITABLE_STRING).get(i);
					deletable = parametersMap.get(QUERY_BADGE_DELETABLE_STRING).get(i);
					
					if(this.queryParameterIsNotAlreadyExisting(componentId, field, condition, input))
					{
						this.queryComponentIdList.add(componentId);
						this.queryFieldList.add(field);
						this.queryConditionFieldList.add(condition);
						this.queryInputFieldList.add(input);
						
						this.queryBadgeIdList.add(badgeId);
						this.queryBadgeEditableList.add(editable);
						this.queryBadgeDeletableList.add(deletable);
					}
				}
				
				this.removeInitialConditionIfBadgeIdAlreadyExists(this.queryBadgeIdList);
				this.createConditionsFromQueryParameters();
				
				this.btnResetFilter.setEnabled(true);
			}
		}
	}
	
	/**
	 * This method is used to remove the initial filters after they have been added before the query parameters are
	 * read.
	 *
	 * @param queryBadgeIdList The list with all badge ids.
	 */
	private void removeInitialConditionIfBadgeIdAlreadyExists(final List<String> queryBadgeIdList)
	{
		final List<ChipBadgeExtension<FilterCondition<T, ?>>> chipBadges = new ArrayList<>(this.chipBadges);
		
		for(final String badgeId : queryBadgeIdList)
		{
			for(final ChipBadgeExtension<FilterCondition<T, ?>> chipBadge : chipBadges)
			{
				if(chipBadge.getBadgeId().equals(badgeId))
				{
					this.removeChipBadgeCondition(chipBadge);
				}
			}
		}
	}
	
	private boolean queryParameterIsNotAlreadyExisting(
		final String componentId,
		final String field,
		final String condition,
		final String input)
	{
		for(int i = 0; i < this.queryComponentIdList.size(); i++)
		{
			if(this.queryComponentIdList.get(i).equals(componentId)
				&& field.equals(this.queryFieldList.get(i))
				&& condition.equals(this.queryConditionFieldList.get(i))
				&& input.equals(this.queryInputFieldList.get(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private String createMultipleQueryParameterString()
	{
		final Map<String, List<String>> query = new HashMap<>();
		query.put(QUERY_COMPONENT_ID_STRING, this.queryComponentIdList);
		query.put(QUERY_FIELD_STRING, this.queryFieldList);
		query.put(QUERY_INPUT_STRING, this.queryInputFieldList);
		query.put(QUERY_CONDITION_STRING, this.queryConditionFieldList);
		query.put(QUERY_BADGE_ID_STRING, this.queryBadgeIdList);
		query.put(QUERY_BADGE_DELETABLE_STRING, this.queryBadgeDeletableList);
		query.put(QUERY_BADGE_EDITABLE_STRING, this.queryBadgeEditableList);
		
		return new QueryParameters(query).getQueryString();
	}
	
	/**
	 * Method for adding a specific filter condition as query parameter.
	 *
	 * @param filterCondition The condition which should be converted to query parameter.
	 */
	private void addQueryParameter(final ChipBadgeExtension<FilterCondition<T, ?>> chipBadge)
	{
		final FilterCondition<T, ?> filterCondition = chipBadge.getItem();
		this.queryFieldList.add(filterCondition.getItem().getDescription());
		this.queryConditionFieldList.add(filterCondition.getSelectedCondition().getDescription());
		this.queryInputFieldList.add(filterCondition.getInputValue());
		this.queryBadgeIdList.add(chipBadge.getBadgeId());
		this.queryBadgeDeletableList.add(String.valueOf(chipBadge.isBtnDeleteEnabled()));
		this.queryBadgeEditableList.add(String.valueOf(chipBadge.isBtnEditEnabled()));
		
		this.ui.getPage().fetchCurrentURL(currentUrl ->
		{
			final String questionMarkCharacter = "?";
			String querySeperator = "";
			String currentQuery = currentUrl.getQuery();
			
			if(currentQuery != null)
			{
				querySeperator = "&";
			}
			else
			{
				currentQuery = "";
			}
			
			this.ui
				.getPage()
				.getHistory()
				.replaceState(
					null,
					new Location(
						currentUrl.getPath()
							+ questionMarkCharacter
							+ currentQuery
							+ querySeperator
							+ QueryParameterUtil.createQueryParameterString(
							this.identifier,
							filterCondition,
							chipBadge.getBadgeId(),
							chipBadge.isBtnDeleteEnabled(),
							chipBadge.isBtnEditEnabled())));
		});
	}
	
	/**
	 * Method for removing a query parameter.
	 *
	 * @param chipBadge The badge with the condition which should be removed as a query parameter.
	 */
	private void removeQueryParameter(final ChipBadgeExtension<FilterCondition<T, ?>> chipBadge)
	{
		this.ui.getPage().fetchCurrentURL(currentUrl ->
		{
			if(currentUrl.getQuery() != null)
			{
				final Map<String, List<String>> param =
					QueryParameters.fromString(currentUrl.getQuery()).getParameters();
				final FilterCondition<T, ?> filterCondition = chipBadge.getItem();
				
				this.queryComponentIdList = new LinkedList<>(param.get(QUERY_COMPONENT_ID_STRING));
				this.queryFieldList = new LinkedList<>(param.get(QUERY_FIELD_STRING));
				this.queryConditionFieldList = new LinkedList<>(param.get(QUERY_CONDITION_STRING));
				this.queryInputFieldList = new LinkedList<>(param.get(QUERY_INPUT_STRING));
				this.queryBadgeIdList = new LinkedList<>(param.get(QUERY_BADGE_ID_STRING));
				this.queryBadgeDeletableList = new LinkedList<>(param.get(QUERY_BADGE_DELETABLE_STRING));
				this.queryBadgeEditableList = new LinkedList<>(param.get(QUERY_BADGE_EDITABLE_STRING));
				
				// Checking if condition is in the current url
				if(this.queryComponentIdList
					.stream().anyMatch(x -> x.equals(this.identifier))
					&& this.queryFieldList
					.stream().anyMatch(x -> x.equals(filterCondition.getItem().getDescription()))
					&& this.queryConditionFieldList
					.stream().anyMatch(x -> x.equals(filterCondition.getSelectedCondition().getDescription()))
					&& this.queryInputFieldList
					.stream().anyMatch(x -> x.equals(filterCondition.getInputValue()))
					&& this.queryBadgeIdList
					.stream().anyMatch(x -> x.equals(chipBadge.getBadgeId()))
					&& this.queryBadgeDeletableList
					.stream().anyMatch(x -> x.equals(String.valueOf(chipBadge.isBtnDeleteEnabled())))
					&& this.queryBadgeEditableList
					.stream().anyMatch(x -> x.equals(String.valueOf(chipBadge.isBtnEditEnabled()))))
				{
					
					this.queryComponentIdList.remove(this.identifier);
					this.queryFieldList.remove(filterCondition.getItem().getDescription());
					this.queryConditionFieldList.remove(filterCondition.getSelectedCondition().getDescription());
					this.queryInputFieldList.remove(filterCondition.getInputValue());
					this.queryBadgeIdList.remove(chipBadge.getBadgeId());
					this.queryBadgeDeletableList.remove(String.valueOf(chipBadge.isBtnDeleteEnabled()));
					this.queryBadgeEditableList.remove(String.valueOf(chipBadge.isBtnEditEnabled()));
					
					this.ui
						.getPage()
						.getHistory()
						.replaceState(null, currentUrl.getPath()
							+ "?"
							+ this.createMultipleQueryParameterString());
				}
			}
		});
	}
	
	/**
	 * Method used for adding initial filters.
	 *
	 * @param filterField       The selected field which should be used for the initial condition.
	 * @param selectedCondition The filter comparator which should be used for the condition.
	 * @param searchQuery       The input value which should be used for the condition.
	 * @param badgeId           The badge ID is used to recognise the initial filter, for example after refreshing the
	 *                          page.
	 * @return Returns this filter component.
	 */
	public FilterComponent<T> withInitialFilter(
		final FilterField<T, ?> filterField,
		final FilterComparator selectedCondition,
		final String searchQuery,
		final boolean conditionDeletable,
		final boolean conditionEditable,
		final CustomizationDegree customizationDegree,
		final String badgeId)
	{
		FilterField<T, ?> finalFilterField = filterField;
		
		if(conditionEditable)
		{
			final List<FilterField<T, ?>> filterFields = this.filterFieldList.stream()
				.filter(f -> f.getDescription().equals(filterField.getDescription()))
				.toList();
			
			// Check if filter field is already existing
			// If not add the filter field to the filterFieldList
			if(filterFields.isEmpty())
			{
				finalFilterField = finalFilterField.withAvailableComparator(selectedCondition);
				this.filterFieldList.add(finalFilterField);
			}
			// If yes check if the comparator is already in the filter field
			else
			{
				finalFilterField =
					this.checkForNotDuplicatedComparatorsAndAddThemToTheFilterField(filterField, filterFields.get(0));
			}
			
			this.selFields.setItems(this.filterFieldList);
		}
		
		final ChipBadgeExtension<FilterCondition<T, ?>> chipBadge = this.createBadgeConditionAndApplyFilter(
			finalFilterField,
			selectedCondition,
			searchQuery,
			conditionDeletable,
			conditionEditable,
			customizationDegree);
		
		// Just needed if the url parameters are activated
		chipBadge.setBadgeId(badgeId);
		
		chipBadge.setCustomizationRating(customizationDegree);
		
		// Needed for resetting the conditions
		this.initialChipBadges.add(chipBadge);
		
		return this;
	}
	
	/**
	 * Method used for adding initial filters. The badge id is set automatically.
	 *
	 * @param filterField       The selected field which should be used for the initial condition.
	 * @param selectedCondition The filter comparator which should be used for the condition.
	 * @param searchQuery       The input value which should be used for the condition.
	 * @return Returns this filter component.
	 */
	public FilterComponent<T> withInitialFilter(
		final FilterField<T, ?> filterField,
		final FilterComparator selectedCondition,
		final String searchQuery,
		final boolean conditionDeletable,
		final boolean conditionEditable)
	{
		final FilterComponent<T> filterComponent =
			this.withInitialFilter(
				filterField,
				selectedCondition,
				searchQuery,
				conditionDeletable,
				conditionEditable,
				CustomizationDegree.EVERYTHING,
				String.valueOf(this.initialConditionIdCounter));
		
		this.initialConditionIdCounter++;
		
		return filterComponent;
	}
	
	public FilterComponent<T> withInitialFilter(
		final FilterField<T, ?> filterField,
		final FilterComparator selectedCondition,
		final String searchQuery,
		final boolean conditionDeletable,
		final boolean conditionEditable,
		final CustomizationDegree customizationDegree)
	{
		final FilterComponent<T> filterComponent =
			this.withInitialFilter(
				filterField,
				selectedCondition,
				searchQuery,
				conditionDeletable,
				conditionEditable,
				customizationDegree,
				String.valueOf(this.initialConditionIdCounter));
		
		this.initialConditionIdCounter++;
		
		return filterComponent;
	}
}
