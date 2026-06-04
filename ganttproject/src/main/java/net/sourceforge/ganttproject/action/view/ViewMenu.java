/*
GanttProject is an opensource project management tool. License: GPL3
Copyright (C) 2005-2011 GanttProject Team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.sourceforge.ganttproject.action.view;

import biz.ganttproject.core.option.FontOption;
import biz.ganttproject.core.option.IntegerOption;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.action.ViewToggleAction;
import net.sourceforge.ganttproject.gui.UIFacade;
import net.sourceforge.ganttproject.gui.view.GPViewManager;
import net.sourceforge.ganttproject.gui.view.ViewProvider;
import net.sourceforge.ganttproject.plugins.PluginManager;

import javax.swing.*;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Collection of actions present in the view menu
 */
public class ViewMenu extends JMenu {
  public ViewMenu(GPViewManager viewManager, UIFacade uiFacade, IntegerOption dpiOption,
      FontOption chartFontOption, String key) {
    super(GPAction.createVoidAction(key));

    List<ViewProvider> charts = buildViewProviderList(uiFacade);
    if (charts.isEmpty()) {
      setEnabled(false);
    }
    for (ViewProvider viewProvider : charts) {
      var action = new ViewToggleAction(viewManager, viewProvider);
      action.syncFromView();
      action.updateAction();
      add(new JCheckBoxMenuItem(action));
    }
    addMenuListener(new javax.swing.event.MenuListener() {
      @Override
      public void menuSelected(javax.swing.event.MenuEvent e) {
        syncToggleActions();
      }

      @Override
      public void menuDeselected(javax.swing.event.MenuEvent e) {}

      @Override
      public void menuCanceled(javax.swing.event.MenuEvent e) {}
    });
    setToolTipText(null);
  }

  private void syncToggleActions() {
    for (int i = 0; i < getMenuComponentCount(); i++) {
      Component component = getMenuComponent(i);
      if (component instanceof JCheckBoxMenuItem) {
        Action action = ((JCheckBoxMenuItem) component).getAction();
        if (action instanceof ViewToggleAction) {
          ((ViewToggleAction) action).syncFromView();
        }
      }
    }
  }

  private static List<ViewProvider> buildViewProviderList(UIFacade uiFacade) {
    List<ViewProvider> charts = new ArrayList<>();
    Set<String> ids = new HashSet<>();
    ViewProvider gantt = uiFacade.getGanttViewProvider();
    if (gantt != null && ids.add(gantt.getId())) {
      charts.add(gantt);
    }
    ViewProvider resources = uiFacade.getResourceViewProvider();
    if (resources != null && ids.add(resources.getId())) {
      charts.add(resources);
    }
    for (ViewProvider plugin : PluginManager.getViewProviders()) {
      if (plugin != null && ids.add(plugin.getId())) {
        charts.add(plugin);
      }
    }
    return charts;
  }
}
