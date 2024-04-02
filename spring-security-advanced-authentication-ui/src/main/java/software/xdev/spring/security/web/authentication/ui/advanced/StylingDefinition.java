package software.xdev.spring.security.web.authentication.ui.advanced;

import java.util.Set;
import java.util.stream.Collectors;


public record StylingDefinition(
	Set<String> classNames,
	String style
)
{
	public String classNameString()
	{
		return this.classNames().stream().map(s -> " " + s).collect(Collectors.joining());
	}
	
	public static StylingDefinition classNames(final String... classNames)
	{
		return new StylingDefinition(Set.of(classNames), null);
	}
	
	public static StylingDefinition style(final String style)
	{
		return new StylingDefinition(Set.of(), style);
	}
}
