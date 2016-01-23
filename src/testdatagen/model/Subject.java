package testdatagen.model;

import java.io.Serializable;

import testdatagen.utilities.Utilities;

public class Subject implements Serializable
{
	boolean isMainSubject;
	private String subjectschemeidentifier, subjectschemeversion, subjectcode, subjectheadingtext;
	private static final long serialVersionUID = 2L;
	
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

	public boolean isMainSubject()
	{
		return isMainSubject;
	}

	public String getSubjectschemeidentifier()
	{
		return subjectschemeidentifier;
	}

	public String getSubjectschemeversion()
	{
		return subjectschemeversion;
	}

	public String getSubjectcode()
	{
		return subjectcode;
	}

	public String getSubjectheadingtext()
	{
		return subjectheadingtext;
	}

	@Override
	public boolean equals(Object other)
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