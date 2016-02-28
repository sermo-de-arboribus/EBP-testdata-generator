package testdatagen.model;

import java.io.Serializable;

import testdatagen.utilities.Utilities;

/**
 * Model class for Onix style subject information
 */
public class Subject implements Serializable
{
	boolean isMainSubject;
	private String subjectschemeidentifier, subjectschemeversion, subjectcode, subjectheadingtext;
	private static final long serialVersionUID = 2L;

	/**
	 * Constructor
	 * @param subjectschemeidentifier String representing the Onix subjet scheme identifier
	 * @param subjectschemeversion String representing the Onix subject scheme version
	 * @param subjectcode String representing the Onix subject code
	 * @param subjectheadingtext String representing the subject heading text
	 * @param isMainSubject boolean to indicate whether the subject is an Onix style main subject
	 */
	public Subject(final String subjectschemeidentifier, final String subjectschemeversion, final String subjectcode, 
			final String subjectheadingtext, final boolean isMainSubject)
	{
		if(subjectschemeidentifier == null || subjectschemeidentifier == "")
		{
			throw new IllegalArgumentException("Subject parameter 'subjectschemeidentifier' must not be null or empty!");
		}
		this.subjectschemeidentifier = subjectschemeidentifier;
		this.subjectschemeversion = subjectschemeversion;
		this.subjectcode = subjectcode;
		this.subjectheadingtext = subjectheadingtext;
		this.isMainSubject = isMainSubject;
	}

	/**
	 * Return a clone of this subject
	 */
	public Subject clone()
	{
		return new Subject(this.subjectschemeidentifier, this.subjectschemeversion, this.subjectcode,
				this.subjectheadingtext, this.isMainSubject);
	}
	
	/**
	 * Check if this subject is a main subject in the context of an Onix subject
	 * @return
	 */
	public boolean isMainSubject()
	{
		return isMainSubject;
	}

	/**
	 * Get the Onix subject scheme identifier
	 * @return String representing the subject scheme identifier
	 */
	public String getSubjectschemeidentifier()
	{
		return subjectschemeidentifier;
	}

	/**
	 * Get the Onix subject scheme version
	 * @return String representing the subject scheme version
	 */
	public String getSubjectschemeversion()
	{
		return subjectschemeversion;
	}

	/**
	 * Get the Onix subject code
	 * @return String representing the subject code
	 */
	public String getSubjectcode()
	{
		return subjectcode;
	}

	/**
	 * Get the Onix subject heading text
	 * @return String representing the subject heading text
	 */
	public String getSubjectheadingtext()
	{
		return subjectheadingtext;
	}

	@Override
	public boolean equals(final Object other)
	{
		if(other == null)
			return false;
		if(this == other)
			return true;
		if(this.getClass() != other.getClass())
			return false;
		
		final Subject otherSubject = (Subject) other;
		return Utilities.nullSafeEquals(subjectschemeidentifier, otherSubject.subjectschemeidentifier) &&
				Utilities.nullSafeEquals(subjectschemeversion, otherSubject.subjectschemeversion) &&
				Utilities.nullSafeEquals(subjectcode, otherSubject.subjectcode) &&
				Utilities.nullSafeEquals(subjectheadingtext, otherSubject.subjectheadingtext) &&
				this.isMainSubject == otherSubject.isMainSubject;
	}
	
	@Override
	public int hashCode()
	{
		return subjectschemeidentifier.hashCode() + subjectschemeversion.hashCode() +
				subjectcode.hashCode() + subjectheadingtext.hashCode() * (isMainSubject ? 37 : 53);
	}
	
	@Override
	public String toString()
	{
		return subjectschemeidentifier + "-" + subjectcode + "-" + subjectheadingtext + (isMainSubject ? "[M]" : "");
	}
}