/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.intx.netbeans.module.extools.options;

import pl.intx.netbeans.module.extools.models.ExternalTool;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import pl.intx.netbeans.module.extools.ExternalTools;

@OptionsPanelController.SubRegistration(
        displayName = "#AdvancedOption_DisplayName_ExternalTools",
        keywords = "#AdvancedOption_Keywords_ExternalTools",
        keywordsCategory = "Advanced/ExternalTools"
)
@org.openide.util.NbBundle.Messages({"AdvancedOption_DisplayName_ExternalTools=External Tools", "AdvancedOption_Keywords_ExternalTools=external tools"})
public final class ExternalToolsOptionsPanelController extends OptionsPanelController {

    private final DefaultListModel<ExternalTool> toolsListModel = new DefaultListModel<>();
    private final ExternalTools exTools = new ExternalTools();
    private ExternalToolsOptionsPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    ListModel getToolListModel() {
        return toolsListModel;
    }

    void loadListModel() {
        exTools.loadTools();
        toolsListModel.clear();
        toolsListModel.addAll(exTools.getTools());

    }

    void storeListModel() {
        ArrayList<ExternalTool> list = Collections.list(toolsListModel.elements());
        exTools.setTools(list);
        exTools.storeTools();
    }

    void createNewTool() {
        toolsListModel.addElement(new ExternalTool());
    }

    @Override
    public void update() {
        getPanel().load();
        changed = false;
    }

    @Override
    public void applyChanges() {
        SwingUtilities.invokeLater(() -> {
            getPanel().store();
            changed = false;
        });
    }

    @Override
    public void cancel() {
    }

    @Override
    public boolean isValid() {
        return getPanel().valid();
    }

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return getPanel();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }

    private ExternalToolsOptionsPanel getPanel() {
        if (panel == null) {
            panel = new ExternalToolsOptionsPanel(this);
        }
        return panel;
    }

    void changed() {
        if (!changed) {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
        }
        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }

    void removeItem(ExternalTool currentSelectedItem) {
        toolsListModel.removeElement(currentSelectedItem);
    }

}
