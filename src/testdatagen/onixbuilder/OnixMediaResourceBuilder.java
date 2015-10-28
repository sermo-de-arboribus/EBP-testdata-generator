package testdatagen.onixbuilder;

import java.util.HashMap;

import nu.xom.Element;
import nu.xom.Text;

public class OnixMediaResourceBuilder extends OnixPartsBuilder
{
	/* 
	 * For a comment on this string array's table format see the parent class
	 */
	private static final String[][] MEDIA_RESOURCE_ELEMENT_DEFINITIONS = 
		{
			{"supportingsresource", "SupportingResource", "mediafile", "MediaFile", "", ""},
			{"x436", "ResourceContentType", "f114", "MediaFileTypeCode", "resourcecontenttype", "01"},
			{"x427", "ContentAudience", "", "", "contentaudience", "00"},
			{"x437", "ResourceMode", "", "", "resourcemode", "03"},
			{"resourceversion", "ResourceVersion", "", "", "", ""},
			{"x441", "ResourceForm", "", "", "resourceform", "02"},
			{"x435", "ResourceLink", "f117", "MediaFileLink", "resourcelink", "http://www.example.com/"},
			{"contentdate", "ContentDate", "", "", "", ""},
			{"x429", "ContentDateRole", "", "", "contentdaterole", "17"},
			{"b306", "Date", "f373", "MediaFileDate", "mediafiledate", "20151025"},
			{"", "", "f116", "MediaFileLinkTypeCode", "", "01"}
		};
	/*
	 * Map for determining the ONIX 2.1 MediaFileTypeCode out of ONIX 3.0 ResourceMode + ResourceContentType information
	 */
	private static final HashMap<String, String> MEDIA_FILE_TYPE_CODE_MAP = new HashMap<String, String>()
			{{
				put("0115", "51");
				put("0116", "51");
				put("0214", "45");
				put("0215", "44");
				put("0221", "45");
				put("0222", "45");
				put("0224", "45");
				put("0225", "44");
				put("0226", "45");
				put("0228", "01");
				put("0301", "04");
				put("0302", "24");
				put("0315", "23");
				put("0325", "23");
				put("0327", "07");
				put("0328", "01");
				put("0505", "36");
				put("0506", "36");
				put("0507", "36");
				put("0508", "36");
				put("0509", "36");
				put("0510", "36");
				put("0514", "36");
				put("0519", "36");
				put("0520", "36");
				put("0521", "36");
				put("0522", "36");
				put("0523", "36");
				put("0524", "36");
				put("0526", "36");
				put("0528", "01");
				put("0530", "36");
				put("0628", "01");
			}};
	
	public OnixMediaResourceBuilder(String onixVersion, int tagType, HashMap<String, String> args)
	{
		super(onixVersion, tagType, args);
		elementDefinitions = MEDIA_RESOURCE_ELEMENT_DEFINITIONS;
	}
	
	@Override
	public Element build()
	{
		Element mediaResource = new Element(getTagName(0));
		
		// ONIX 2.1 and ONIX 3.0 are structuring media resource information in a very different way.
		// So we need completely different building blocks for both versions.
		if(onixVersion.equals("2.1"))
		{
			buildOnix2mediaFile(mediaResource);
		}
		else
		{
			buildOnix3SupportingResource(mediaResource);
		}
		
		return mediaResource;
	}
	
	private void appendElementsFromTo(Element parent, int from, int to)
	{
		for(int i = from; i <= to; i++)
		{
			Element nextElement = new Element(getTagName(i));
			nextElement.appendChild(new Text(determineElementContent(i)));
			parent.appendChild(nextElement);
		}
	}
	
	private void buildOnix2mediaFile(Element mediaResource)
	{
		// the ONIX 2.1 MediaFileTypeCode is determined by the resourcecontenttype and the resourcemode arguments
		String resourceContentType = determineElementContent(1);
		String resourceMode = determineElementContent(3);
		String mediaFileTypeCodeContent = determineMediaFileTypeCode(resourceContentType, resourceMode);
		Element mediaFileTypeCode = new Element(getTagName(1));
		mediaFileTypeCode.appendChild(new Text(mediaFileTypeCodeContent));
		mediaResource.appendChild(mediaFileTypeCode);
		
		Element mediaFileLink = new Element(getTagName(6));
		String url = determineElementContent(6);
		mediaFileLink.appendChild(new Text(url));
		mediaResource.appendChild(mediaFileLink);
		
		Element mediaFileLinkType = new Element(getTagName(10));
		String mfltCode = determineMediaFileLinkTypeCode(url);
		mediaFileLinkType.appendChild(new Text(mfltCode));
		mediaResource.appendChild(mediaFileLinkType);
		
		Element mediaFileDate = new Element(getTagName(9));
		mediaFileDate.appendChild(new Text(determineElementContent(9)));
		mediaResource.appendChild(mediaFileDate);
	}
	
	private void buildOnix3SupportingResource(Element mediaResource)
	{
		appendElementsFromTo(mediaResource, 1, 3);
		
		Element resourceVersion = new Element(getTagName(4));
		mediaResource.appendChild(resourceVersion);
		
		appendElementsFromTo(resourceVersion, 5, 6);
		
		Element contentDate = new Element(getTagName(7));
		mediaResource.appendChild(contentDate);
		
		appendElementsFromTo(contentDate, 8, 9);
	}

	private String determineMediaFileLinkTypeCode(String url)
	{
		if (url.startsWith("doi:") || url.startsWith("http://dx.doi.org"))
		{
			return "02";
		}
		else if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("www."))
		{
			return "01";
		}
		else if (url.startsWith("urn:"))
		{
			return "04"; 
		}
		else if (url.startsWith("ftp://") || url.startsWith("sftp://") || url.startsWith("ftp.") || url.startsWith("sftp."))
		{
			return "05";
		}
		else
		{
			return "06";
		}
	}
	
	private String determineMediaFileTypeCode(String resourceContentType, String resourceMode)
	{
		return MEDIA_FILE_TYPE_CODE_MAP.get(resourceMode + resourceContentType);
	}
}