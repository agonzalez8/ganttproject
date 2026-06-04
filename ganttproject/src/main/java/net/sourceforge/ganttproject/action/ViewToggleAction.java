/*
GanttProject is an opensource project management tool.
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
package net.sourceforge.ganttproject.action;

import biz.ganttproject.app.ViewPaneKt;
import net.sourceforge.ganttproject.gui.view.ViewProvider;
import net.sourceforge.ganttproject.gui.view.GPViewManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;

/**
 * @author bard
 */
public class ViewToggleAction extends GPAction {
  private final ViewProvider myViewProvider;

  private final GPViewManager myViewManager;

  public ViewToggleAction(GPViewManager viewManager, ViewProvider viewProvider) {
    myViewProvider = viewProvider;
    myViewManager = viewManager;
  }

  @Override
  public String getLocalizedDescription() {
    return MessageFormat.format(getI18n("view.toggle.description"), getLocalizedName());
  }

  @Override
  public String getLocalizedName() {
    return myViewProvider == null ? super.getLocalizedName() : ViewPaneKt.getLabel(myViewProvider);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    boolean visible;
    if (e != null && e.getSource() instanceof JMenuItem) {
      visible = ((JMenuItem) e.getSource()).isSelected();
    } else {
      // JavaFX CustomMenuItem invokes actionPerformed(null); checkbox state is in SELECTED_KEY.
      Boolean selected = (Boolean) getValue(Action.SELECTED_KEY);
      if (selected != null) {
        visible = selected;
      } else {
        visible = !myViewManager.getView(myViewProvider.getId()).isVisible();
        putValue(Action.SELECTED_KEY, visible);
      }
    }
    var view = myViewManager.getView(myViewProvider.getId());
    view.setVisible(visible);
    if (visible) {
      view.setActive(true);
    }
  }

  /** Sync checkbox state with whether the view tab is actually open. */
  public void syncFromView() {
    putValue(Action.SELECTED_KEY, myViewManager.getView(myViewProvider.getId()).isVisible());
  }
}
