/*
 * Copyright © 2023 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
