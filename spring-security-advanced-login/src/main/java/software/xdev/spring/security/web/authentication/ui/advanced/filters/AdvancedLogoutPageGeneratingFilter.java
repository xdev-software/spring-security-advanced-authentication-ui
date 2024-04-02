package software.xdev.spring.security.web.authentication.ui.advanced.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultLogoutPageGeneratingFilter;


public class AdvancedLogoutPageGeneratingFilter
	extends ExtendableDefaultLogoutPageGeneratingFilter
	implements AdvancedSharedPageGeneratingFilter<AdvancedLogoutPageGeneratingFilter>
{
	protected List<String> headerElements = new ArrayList<>(DEFAULT_HEADER_ELEMENTS);
	
	protected String pageTitle = "Confirm Log Out?";
	
	protected Map<String, String> headerMetas = new LinkedHashMap<>(DEFAULT_HEADER_METAS);
	
	@Override
	public AdvancedLogoutPageGeneratingFilter setHeaderElements(final List<String> headerElements)
	{
		this.headerElements = headerElements;
		return this;
	}
	
	@Override
	public AdvancedLogoutPageGeneratingFilter addHeaderElement(final String element)
	{
		this.headerElements.add(element);
		return this;
	}
	
	@Override
	public AdvancedLogoutPageGeneratingFilter pageTitle(final String pageTitle)
	{
		this.pageTitle = pageTitle;
		return this;
	}
	
	@Override
	public AdvancedLogoutPageGeneratingFilter setHeaderMetas(final Map<String, String> headerMetas)
	{
		this.headerMetas = headerMetas;
		return this;
	}
	
	@Override
	public AdvancedLogoutPageGeneratingFilter addHeaderMeta(final String name, final String content)
	{
		this.headerMetas.put(name, content);
		return this;
	}
	
	@Override
	protected void renderLogout(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(this.generateLogoutPageHtml(request, response));
	}
	
	protected String generateLogoutPageHtml(final HttpServletRequest request, final HttpServletResponse response)
	{
		return "<!DOCTYPE html>"
			+ "<html lang='en' style='height:100%'>"
			+ this.generateHeader()
			+ this.generateBody(request)
			+ "</html>";
	}
	
	protected String generateHeader()
	{
		return "  <head>"
			+ "    <meta charset='utf-8'>"
			+ this.headerMetas.entrySet()
			.stream()
			.map(e -> "    <meta name='" + e.getKey() + "' content='" + e.getValue() + "'>")
			.collect(Collectors.joining())
			+ Optional.ofNullable(this.pageTitle).map(t -> "    <title>" + t + "</title>").orElse("")
			+ this.headerElements.stream().map(s -> "    " + s).collect(Collectors.joining())
			+ "  </head>";
	}
	
	protected String generateBody(final HttpServletRequest request)
	{
		return "  <body style='height:100%'>"
			+ "     <div class=\"container\">"
			+ "      <form class=\"w-100 m-auto\" style=\"max-width: 21em; padding: 1rem\""
			+ " method=\"post\" action=\"" + request.getContextPath() + "/logout\">"
			+ "        <h4 class=\"h4 mb-3 fw-normal\">Are you sure you want to log out?</h2>"
			+ this.renderHiddenInputs(request)
			+ "        <button class=\"btn btn-block btn-lg btn-primary w-100\" type=\"submit\">Log Out</button>"
			+ "      </form>"
			+ "    </div>"
			+ "  </body>";
	}
}
