package software.xdev.vaadin.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a person with attributes such as first name, last name, birthday, subscription status, and an identifier.
 */
public class Person
{
	private Integer id;
	private String firstName;
	private String lastName;
	private LocalDate birthday;
	private boolean married;
	private double salary;
	private Department department;
	
	/**
	 * Constructs a new instance of the Person class with the provided attributes.
	 *
	 * @param id         The unique identifier of the person.
	 * @param firstName  The first name of the person.
	 * @param lastName   The last name of the person.
	 * @param birthday   The birthdate of the person.
	 * @param married    Whether the person is a married (true) or not (false).
	 * @param salary     The paid salary
	 * @param department The associated department
	 */
	public Person(
		final Integer id,
		final String firstName,
		final String lastName,
		final LocalDate birthday,
		final boolean married,
		final double salary,
		final Department department)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthday = birthday;
		this.married = married;
		this.salary = salary;
		this.department = department;
	}
	
	/**
	 * Gets the first name of the person.
	 *
	 * @return The first name of the person.
	 */
	public String getFirstName()
	{
		return this.firstName;
	}
	
	/**
	 * Sets the first name of the person.
	 *
	 * @param firstName The new first name to set.
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}
	
	/**
	 * Gets the last name of the person.
	 *
	 * @return The last name of the person.
	 */
	public String getLastName()
	{
		return this.lastName;
	}
	
	/**
	 * Sets the last name of the person.
	 *
	 * @param lastName The new last name to set.
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}
	
	/**
	 * Gets the birthdate of the person.
	 *
	 * @return The birthdate of the person.
	 */
	public LocalDate getBirthday()
	{
		return this.birthday;
	}
	
	/**
	 * Sets the birthdate of the person.
	 *
	 * @param birthday The new birthdate to set.
	 */
	public void setBirthday(final LocalDate birthday)
	{
		this.birthday = birthday;
	}
	
	/**
	 * Gets the unique identifier of the person.
	 *
	 * @return The unique identifier of the person.
	 */
	public Integer getId()
	{
		return this.id;
	}
	
	/**
	 * Sets the unique identifier of the person.
	 *
	 * @param id The new unique identifier to set.
	 */
	public void setId(final Integer id)
	{
		this.id = id;
	}
	
	/**
	 * Checks if the person is married.
	 *
	 * @return True if the person is married, false otherwise.
	 */
	public boolean isMarried()
	{
		return this.married;
	}
	
	/**
	 * Sets person's married status.
	 *
	 * @param married True to mark the person as married, false to mark the person as unmarried.
	 */
	public void setMarried(final boolean married)
	{
		this.married = married;
	}
	
	/**
	 * Gets the salary of the person.
	 *
	 * @return The salary of the person.
	 */
	
	public double getSalary()
	{
		return this.salary;
	}
	
	/**
	 * Sets the salary of the person.
	 *
	 * @param salary The new salary to set.
	 */
	public void setSalary(final double salary)
	{
		this.salary = salary;
	}
	
	/**
	 * Gets the associated department of the person.
	 *
	 * @return The associated department of the person.
	 */
	public Department getDepartment()
	{
		return this.department;
	}
	
	/**
	 * Sets the associated department of the person.
	 *
	 * @param department The associated department to set.
	 */
	public void setDepartment(final Department department)
	{
		this.department = department;
	}
	
	/**
	 * Retrieves a list of sample Person objects.
	 *
	 * @return A List containing sample Person objects.
	 */
	@SuppressWarnings("checkstyle:MagicNumber")
	public static List<Person> getPersons()
	{
		final List<Person> lst = new ArrayList<>();
		
		lst.add(new Person(0, "Siegbert", "Schmidt", LocalDate.of(1990, 12, 17), false, 1000, Department.HR));
		lst.add(new Person(1, "Herbert", "Maier", LocalDate.of(1967, 10, 13), false, 1000, Department.HR));
		lst.add(new Person(2, "Hans", "Lang", LocalDate.of(2002, 5, 9), true, 9050.60, Department.HR));
		lst.add(new Person(3, "Anton", "Meier", LocalDate.of(1985, 1, 24), true, 8000.75, Department.HR));
		lst.add(new Person(4, "Sarah", "Smith", LocalDate.of(1999, 6, 1), false, 5000, Department.IT));
		lst.add(new Person(5, "Niklas", "Sommer", LocalDate.of(1994, 11, 8), true, 4000.33, Department.HR));
		lst.add(new Person(6, "Hanna", "Neubaum", LocalDate.of(1986, 8, 15), true, 3000, Department.HR));
		lst.add(new Person(8, "Laura", "Fels", LocalDate.of(1996, 3, 20), true, 1000.50, Department.HR));
		lst.add(new Person(7, "Sofia", "Sommer", LocalDate.of(1972, 4, 14), false, 2000, Department.ACCOUNTING));
		
		return lst;
	}
}
