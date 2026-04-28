package software.xdev.security;

import java.util.Map;
import java.util.stream.Collectors;


public final class CSP
{
	public static final String POLICY_SELF = "'self'";
	public static final String POLICY_NONE = "'none'";
	
	public static String build(final Map<String, String> attr)
	{
		return attr.entrySet().stream().map(e ->
		{
			if(e.getValue() == null)
			{
				return e.getKey();
			}
			return e.getKey() + " " + e.getValue();
		}).collect(Collectors.joining("; "));
	}
	
	private CSP()
	{
	}
}
