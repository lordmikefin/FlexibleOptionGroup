package org.vaadin.hene.flexibleoptiongroup;

import java.util.Iterator;

import com.vaadin.data.HasValue;
import com.vaadin.event.LayoutEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Henri Kerola / Vaadin
 */
public class DefaultView extends VerticalLayout implements View {

    private static final String CAPTION_PROPERTY = "caption";
    private static final String ICON_PROPERTY = "icon";
    private static final String SELECTION_PROPERTY = "selection";

    private static final String[] DOCUMENTS = new String[] { "Word",
                                                             "document-doc.png", "Image", "document-image.png", "PDF",
                                                             "document-pdf.png", "PowerPoint", "document-ppt.png", "Text",
                                                             "document-txt.png", "Web", "document-web.png", "Excel",
                                                             "document-xsl.png" };

    private FlexibleOptionGroupPropertyEditor propertyEditor;

    public DefaultView() {
        setSizeFull();

        Label headerLabel = new Label("FlexibleOptionGroup");
        headerLabel.setStyleName(ValoTheme.LABEL_H1);
        addComponent(headerLabel);

        final TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addComponent(new GridLayoutTab());
        ts.addComponent(new HorizontalOptionGroupTab());
        ts.addComponent(new AbsoluteLayoutTab());
        addComponent(ts);
        setExpandRatio(ts, 1);

        propertyEditor = new FlexibleOptionGroupPropertyEditor();
        propertyEditor.refresh((AbstractTab) ts.getSelectedTab());
        addComponent(propertyEditor);

        //		ts.addSelectedTabChangeListener(new SelectedTabChangeListener() {
        //			public void selectedTabChange(SelectedTabChangeEvent event) {
        //				propertyEditor.refresh((AbstractTab) ts.getSelectedTab());
        //			}
        //		});
    }

    //	private static Container createTestContainer() {
    //		IndexedContainer cont = new IndexedContainer();
    //		cont.addContainerProperty(CAPTION_PROPERTY, String.class, null);
    //		cont.addContainerProperty(ICON_PROPERTY, Resource.class, null);
    //
    //		for (int i = 0; i < DOCUMENTS.length; i++) {
    //			String name = DOCUMENTS[i++];
    //			String id = DOCUMENTS[i];
    //			Item item = cont.addItem(id);
    //			valuateTestContainerItem(item, name, id);
    //
    //		}
    //		return cont;
    //	}

    //	private static void valuateTestContainerItem(Item item, String name,
    //			String iconName) {
    //		item.getItemProperty(CAPTION_PROPERTY).setValue(name);
    //		item.getItemProperty(ICON_PROPERTY).setValue(
    //				new ThemeResource("../runo/icons/16/" + iconName));
    //	}

    public static Label createCaptionLabel(FlexibleCheckBoxGroupItemComponent fog) {
        Label captionLabel = new Label();
        captionLabel.setData(fog);
        captionLabel.setIcon(fog.getIcon());
        captionLabel.setCaption(fog.getCaption());
        captionLabel.setWidth(null);
        return captionLabel;
    }

    @Override
    public void enter(final ViewChangeListener.ViewChangeEvent event) {

    }

    private static abstract class AbstractTab extends VerticalLayout {

        protected FlexibleCheckBoxGroup flexibleOptionGroup;

        protected LayoutEvents.LayoutClickListener layoutClickListener = new LayoutEvents.LayoutClickListener() {

            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                FlexibleCheckBoxGroupItemComponent c = null;
                boolean allowUnselection = false; //flexibleOptionGroup.isMultiSelect();
                if (event.getChildComponent() instanceof FlexibleCheckBoxGroupItemComponent) {
                    c = (FlexibleCheckBoxGroupItemComponent) event
                            .getChildComponent();
                } else if (event.getChildComponent() instanceof AbstractComponent) {
                    Object data = ((AbstractComponent) event
                            .getChildComponent()).getData();
                    if (data instanceof FlexibleCheckBoxGroupItemComponent) {
                        c = (FlexibleCheckBoxGroupItemComponent) data;
                    }
                    if (event.getChildComponent() instanceof HorizontalLayout) {
                        allowUnselection = false;
                    }
                }
                if (c != null) {
                    Object item = c.getItem();
                    if (flexibleOptionGroup.isSelected(item)
                            && allowUnselection) {
                        flexibleOptionGroup.deselect(item);
                    } else {
                        flexibleOptionGroup.select(item);
                    }
                }
            }
        };

        public AbstractTab(String caption) {
            setCaption(caption);
            setMargin(true);
            //			flexibleOptionGroup = new FlexibleOptionGroup(createTestContainer());
            //			flexibleOptionGroup.setItemCaptionPropertyId(CAPTION_PROPERTY);
            //			flexibleOptionGroup.setItemIconPropertyId(ICON_PROPERTY);
        }
    }

    private static class GridLayoutTab extends AbstractTab {

        private GridLayout layout;

        public GridLayoutTab() {
            super("GridLayout");

            //			Item otherItem = flexibleOptionGroup.addItem("other");
            //			valuateTestContainerItem(otherItem, "other", "document.png");

            layout = new GridLayout(2, 1);
            layout.setWidth("100%");
            layout.setColumnExpandRatio(1, 1);
            //			layout.addLayoutClickListener(layoutClickListener);
            addComponent(layout);

            for (Iterator<FlexibleCheckBoxGroupItemComponent> iter = flexibleOptionGroup
                    .getItemComponentIterator(); iter.hasNext();) {
                FlexibleCheckBoxGroupItemComponent c = iter.next();
                layout.addComponent(c);
                if ("other".equals(c.getItem())) {
                    layout.setComponentAlignment(c, Alignment.MIDDLE_CENTER);
                    //					HorizontalLayout otherLayout = createOtherItemLayout(otherItem);
                    //					otherLayout.setData(c);
                    //					layout.addComponent(otherLayout);
                } else {
                    layout.addComponent(createCaptionLabel(c));
                }
                layout.newLine();
            }

        }

        //		private HorizontalLayout createOtherItemLayout(Item otherItem) {
        //			HorizontalLayout otherLayout = new HorizontalLayout();
        //			Label otherIcon = new Label();
        //			otherIcon.setWidth("16px");
        //			otherIcon.setIcon((Resource) otherItem.getItemProperty(
        //					ICON_PROPERTY).getValue());
        //			otherLayout.addComponent(otherIcon);
        //			otherLayout.setComponentAlignment(otherIcon,
        //					Alignment.MIDDLE_CENTER);
        //			TextField otherTextField = new TextField();
        //			otherTextField.setInputPrompt("Other");
        //			otherLayout.addComponent(otherTextField);
        //			return otherLayout;
        //		}

    }

    private static class HorizontalOptionGroupTab extends AbstractTab {

        private HorizontalLayout layout;

        public HorizontalOptionGroupTab() {
            super("HorizontalLayout");

            layout = new HorizontalLayout();
            //			layout.addLayoutClickListener(layoutClickListener);
            addComponent(layout);

            for (Iterator<FlexibleCheckBoxGroupItemComponent> iter = flexibleOptionGroup
                    .getItemComponentIterator(); iter.hasNext();) {
                FlexibleCheckBoxGroupItemComponent c = iter.next();
                layout.addComponent(c);
                layout.addComponent(createCaptionLabel(c));
            }

        }

    }

    private static class AbsoluteLayoutTab extends AbstractTab {

        private AbsoluteLayout layout;

        public AbsoluteLayoutTab() {
            super("AbsoluteLayout");
            setSizeFull();

            layout = new AbsoluteLayout();
            //			layout.addLayoutClickListener(layoutClickListener);
            layout.setSizeFull();
            addComponent(layout);

            int x = 10;
            int y = 10;
            for (Iterator<FlexibleCheckBoxGroupItemComponent> iter = flexibleOptionGroup
                    .getItemComponentIterator(); iter.hasNext();) {
                FlexibleCheckBoxGroupItemComponent c = iter.next();
                layout.addComponent(c, "top: " + y + "; left: " + x);
                layout.addComponent(createCaptionLabel(c), "top: " + (y + 15)
                        + "; left: " + (x + 20));
                x += 20;
                y += 20;
            }
        }

    }

    private static class FlexibleOptionGroupPropertyEditor extends
                                                           VerticalLayout implements HasValue.ValueChangeListener {

        private AbstractTab tab;

        // private CheckBox immediateCheckBox = new CheckBox("Immediate");
        private CheckBox enableCheckBox      = new CheckBox("Enabled");
        private CheckBox readOnlyCheckBox    = new CheckBox("Read-only");
        private CheckBox multiSelectCheckBox = new CheckBox("Multi-select");

        public FlexibleOptionGroupPropertyEditor() {
            // immediateCheckBox.addListener(this);
            // immediateCheckBox.setImmediate(true);
            // addComponent(immediateCheckBox);
            //			enableCheckBox.addValueChangeListener(this);
            addComponent(enableCheckBox);
            //			readOnlyCheckBox.addValueChangeListener(this);
            addComponent(readOnlyCheckBox);
            //			multiSelectCheckBox.addValueChangeListener(this);
            addComponent(multiSelectCheckBox);
        }

        public void refresh(AbstractTab tab) {
            this.tab = tab;
            FlexibleCheckBoxGroup fop = tab.flexibleOptionGroup;
            // immediateCheckBox.setValue(fop.isImmediate());
            enableCheckBox.setValue(fop.isEnabled());
            readOnlyCheckBox.setValue(fop.isReadOnly());
            //			multiSelectCheckBox.setValue(fop.isMultiSelect());
        }

        public void valueChange(HasValue.ValueChangeEvent event) {
            FlexibleCheckBoxGroup fop = tab.flexibleOptionGroup;
            // if (immediateCheckBox == event.getProperty()) {
            // fop.setImmediate(immediateCheckBox.getValue());
            // } else
            if (enableCheckBox == event.getComponent()) {
                fop.setEnabled(enableCheckBox.getValue());
            } else if (readOnlyCheckBox == event.getComponent()) {
                fop.setReadOnly(readOnlyCheckBox.getValue());
            } else if (multiSelectCheckBox == event.getComponent()) {
                //				fop.setMultiSelect(multiSelectCheckBox.getValue());
            }

        }
    }
}
