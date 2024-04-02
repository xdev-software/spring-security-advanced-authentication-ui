package software.xdev.spring.security.web.authentication.ui.advanced.filters;

import java.util.List;
import java.util.Map;

import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultPageGeneratingFilter;


public interface AdvancedSharedPageGeneratingFilter<S extends AdvancedSharedPageGeneratingFilter<S>>
	extends ExtendableDefaultPageGeneratingFilter
{
	List<String> DEFAULT_HEADER_ELEMENTS = List.of(
		"<link "
			+ "href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" "
			+ "rel=\"stylesheet\" "
			+ "integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" "
			+ "crossorigin=\"anonymous\"/>");
	
	S setHeaderElements(List<String> headerElements);
	
	S addHeaderElement(String element);
	
	S pageTitle(String pageTitle);
	
	Map<String, String> DEFAULT_HEADER_METAS = Map.of(
		"viewport", "width=device-width, initial-scale=1, shrink-to-fit=no");
	
	S setHeaderMetas(Map<String, String> headerMetas);
	
	S addHeaderMeta(String name, String content);
}
