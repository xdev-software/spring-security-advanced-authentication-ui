package software.xdev.spring.security.web.authentication.ui.extendable;

import java.lang.reflect.Field;


public class FieldAccessor
{
	@SuppressWarnings({"java:S3011"})
	public void setValue(
		final Class<?> fieldContainer,
		final Object object,
		final String fieldName,
		final Object value)
	{
		try
		{
			final Field field = fieldContainer.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		}
		catch(final NoSuchFieldException | IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	@SuppressWarnings({"java:S3011", "unchecked"})
	public <T> T getValue(
		final Class<?> fieldContainer,
		final Object object,
		final String fieldName)
	{
		try
		{
			final Field field = fieldContainer.getDeclaredField(fieldName);
			field.setAccessible(true);
			final Object value = field.get(object);
			return (T)value;
		}
		catch(final NoSuchFieldException | IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
