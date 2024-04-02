package software.xdev.spring.security.web.authentication.ui.extendable.filters;

import java.util.Collection;
import java.util.function.Consumer;

import software.xdev.spring.security.web.authentication.ui.extendable.FieldAccessor;


public interface GeneratingFilterFillDataFrom<F>
{
	void fillDataFrom(F source);
	
	default void fillDataFrom(
		final Class<F> fieldContainer,
		final F source,
		final Collection<CopyInfo<?>> copyInfos)
	{
		final FieldAccessor fieldAccessor = new FieldAccessor();
		copyInfos.forEach(c -> c.setter().accept(fieldAccessor.getValue(fieldContainer, source, c.fieldName())));
	}
	
	record CopyInfo<T>(
		String fieldName,
		Consumer<T> setter)
	{
	}
}
