/*
 * Copyright © 2024 XDEV Software (https://xdev.software)
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
package software.xdev.vaadin.model;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;


public class ChipBadgeExtension<T> extends ChipBadge<T>
{
	public static final String BTN_DELETE_CHIP_COMPONENT = "btnDeleteChipComponent";
	public static final String BTN_EDIT_CHIP_COMPONENT = "btnEditChipComponent";
	
	protected final Button btnEdit = new Button(VaadinIcon.PENCIL.create());
	
	private CustomizationDegree customizationDegree = CustomizationDegree.EVERYTHING;
	
	public ChipBadgeExtension(final T item)
	{
		super(item);
		
		this.initBtnEdit();
	}
	
	private void initBtnEdit()
	{
		this.btnDelete.setEnabled(true);
		this.btnDelete.setVisible(true);
		this.btnDelete.setId(BTN_DELETE_CHIP_COMPONENT);
		
		this.btnEdit.setEnabled(false);
		this.btnEdit.setVisible(false);
		this.btnEdit.setId(BTN_EDIT_CHIP_COMPONENT);
		
		this.btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE);
		this.btnEdit.getStyle().set("font-size", "var(--lumo-font-size-m)");
		this.btnEdit.setSizeUndefined();
		
		this.getContent().add(this.btnEdit);
	}
	
	public void setBtnEditEnabled(final boolean enabled)
	{
		this.btnEdit.setEnabled(enabled);
		this.btnEdit.setVisible(enabled);
	}
	
	public boolean isBtnEditEnabled()
	{
		return this.btnEdit.isEnabled();
	}
	
	public void setBtnDeleteEnabled(final boolean enabled)
	{
		this.btnDelete.setEnabled(enabled);
		this.btnDelete.setVisible(enabled);
	}
	
	public boolean isBtnDeleteEnabled()
	{
		return this.btnDelete.isEnabled();
	}
	
	public CustomizationDegree getCustomizationRating()
	{
		return this.customizationDegree;
	}
	
	public void setCustomizationRating(final CustomizationDegree customizationDegree)
	{
		this.customizationDegree = customizationDegree;
	}
	
	public Registration addBtnEditClickListener(final ComponentEventListener<ClickEvent<Button>> listener)
	{
		return this.btnEdit.addClickListener(listener);
	}
}
