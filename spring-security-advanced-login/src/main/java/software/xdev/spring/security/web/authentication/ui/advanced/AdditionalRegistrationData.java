package software.xdev.spring.security.web.authentication.ui.advanced;

public class AdditionalRegistrationData
{
	private String color;
	
	private String iconSrc;
	
	private boolean invertTextColor;
	
	public String getColor()
	{
		return this.color;
	}
	
	public void setColor(final String color)
	{
		this.color = color;
	}
	
	public String getIconSrc()
	{
		return this.iconSrc;
	}
	
	public void setIconSrc(final String iconSrc)
	{
		this.iconSrc = iconSrc;
	}
	
	public boolean isInvertTextColor()
	{
		return this.invertTextColor;
	}
	
	public void setInvertTextColor(final boolean invertTextColor)
	{
		this.invertTextColor = invertTextColor;
	}
}
