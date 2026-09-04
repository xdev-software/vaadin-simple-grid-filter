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

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;


/**
 * Represents a chip badge shown underneath the {@link software.xdev.vaadin.FilterComponent}
 */
@CssImport("./styles/filterComponent.css")
public class ChipBadge<T> extends Composite<Span> implements HasSize, HasStyle
{
	private static final String CHIP_BADGE_CONTAINER_CSS = "chipbadge-container";
	private static final String CHIP_BADGE_LABEL_CSS = "chipbadge-label";
	private static final String CHIP_BADGE_DELETE_BTN_CSS = "chipbadge-delete-btn";
	
	protected T item;
	protected ItemLabelGenerator<T> itemLabelGenerator = Object::toString;
	
	protected final Button btnDelete = new Button(VaadinIcon.CLOSE_CIRCLE.create());
	protected final Span label = new Span();
	
	protected String badgeId;
	
	public ChipBadge(final T item)
	{
		this.item = item;
		
		this.initUI();
	}
	
	private void initUI()
	{
		this.setClassName(CHIP_BADGE_CONTAINER_CSS);
		
		this.label.setClassName(CHIP_BADGE_LABEL_CSS);
		
		this.btnDelete.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY_INLINE);
		this.btnDelete.setClassName(CHIP_BADGE_DELETE_BTN_CSS);
		
		this.label.setSizeUndefined();
		this.btnDelete.setSizeUndefined();
		
		this.getContent().add(this.label, this.btnDelete);
	}
	
	@Override
	protected void onAttach(final AttachEvent attachEvent)
	{
		this.updateTextFromItemLabelGenerator();
	}
	
	public T getItem()
	{
		return this.item;
	}
	
	public String getBadgeId()
	{
		return this.badgeId;
	}
	
	public void setBadgeId(final String badgeId)
	{
		this.badgeId = badgeId;
	}
	
	public void setItem(final T item)
	{
		this.item = item;
	}
	
	public void setItemLabelGenerator(final ItemLabelGenerator<T> itemLabelGenerator)
	{
		this.itemLabelGenerator = itemLabelGenerator;
	}
	
	/**
	 * Updates the text of the {@link Span} from the integrated {@link ItemLabelGenerator}
	 */
	public void updateTextFromItemLabelGenerator()
	{
		this.label.setText(this.itemLabelGenerator.apply(this.item));
	}
	
	public Registration addBtnDeleteClickListener(final ComponentEventListener<ClickEvent<Button>> listener)
	{
		return this.btnDelete.addClickListener(listener);
	}
	
	public void setReadonly(final boolean readOnly)
	{
		this.btnDelete.setEnabled(!readOnly);
	}
}
