package software.xdev.vaadin.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.FilterComponent;
import software.xdev.vaadin.builder.CustomizableFilterBuilder;
import software.xdev.vaadin.comparators.EqualComparator;
import software.xdev.vaadin.comparators.IsBetweenComparator;
import software.xdev.vaadin.comparators.NotEqualComparator;
import software.xdev.vaadin.daterange_picker.business.DateRangeModel;
import software.xdev.vaadin.daterange_picker.business.SimpleDateRanges;
import software.xdev.vaadin.model.Department;
import software.xdev.vaadin.model.Person;
import software.xdev.vaadin.model.SimpleFilterField;


/**
 * Primary UI with filter component and grid.
 */
@Route("")
public class MainView extends VerticalLayout implements AfterNavigationObserver
{
	private final Grid<Person> dataGrid = new Grid<>(Person.class, true);
	
	public MainView()
	{
		super();
		this.initUI();
	}
	
	private void initUI()
	{
		// Grid configuration.
		this.dataGrid.setSelectionMode(Grid.SelectionMode.NONE);
		this.dataGrid.setSizeFull();
		
		// Fill grid with dummy data.
		this.dataGrid.setDataProvider(new ListDataProvider<>(Person.getPersons()));
		
		final DatePicker.DatePickerI18n datePickerI18n = new DatePicker.DatePickerI18n()
			.setDateFormat("dd.MM.yyyy");
		
		// Create the FilterComponent.
		final FilterComponent<Person> filterComponent = new FilterComponent<>(this.dataGrid)
			.withFilter(new SimpleFilterField<>(Person::getLastName, "Lastname"))
			.withFilter(new SimpleFilterField<>(Person::getSalary, "Salary"))
			.withFilter(
				CustomizableFilterBuilder.builder()
					.withValueProvider(Person::getId, "Personnel number")
					.withEqualComparator()
					.withGreaterThanComparator()
					.withLessThanComparator()
			)
			.withFilter(
				CustomizableFilterBuilder.builder()
					.withValueProvider(Person::isMarried, "Married")
					.withEqualComparator()
					.withAvailableComparator(NotEqualComparator.getInstance())
			)
			.withFilter(
				CustomizableFilterBuilder.builder()
					.withValueProvider(Person::getDepartment, "Department", Department.values())
					.withContainsComparator()
					.withEqualComparator()
			)
			.withFilter(
				CustomizableFilterBuilder.builder()
					.withValueProvider(Person::getBirthday, "Birthday")
					.withIsAfterComparator()
					.withIsAfterOrEqualsComparator()
					.withIsBeforeComparator()
					.withIsBeforeOrEqualsComparator()
					.withIsBetweenComparator()
			)
			.withDatePickerI18n(datePickerI18n)
			.withDateTimePickerLocale(Locale.GERMANY)
			.withFilterButtonText("Add filter")
			.withUrlParameters("filter1")
			.withCustomDateRangeModel(
				new DateRangeModel<>(LocalDate.now(), LocalDate.now().plusDays(5), SimpleDateRanges.FREE),
				List.of(SimpleDateRanges.allValues()))
			.withInitialFilter(
				CustomizableFilterBuilder.builder().withValueProvider(Person::getBirthday, "Birthday"),
				IsBetweenComparator.getInstance(), "2000-01-01#2002-08-12",
				true,
				true
			)
			.withInitialFilter(
				CustomizableFilterBuilder.builder().withValueProvider(Person::isMarried, "Married"),
				EqualComparator.getInstance(),
				"true",
				false,
				true
			);
		
		this.add(filterComponent, this.dataGrid);
		this.setSizeFull();
	}
	
	@Override
	public void afterNavigation(final AfterNavigationEvent event)
	{
	
	}
}
