/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.intx.netbeans.module.extools.actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.NodeAction;
import org.openide.windows.WindowManager;
import pl.intx.netbeans.module.extools.ExternalTools;
import pl.intx.netbeans.module.extools.models.ExternalTool;

@ActionID(
        category = "Tools",
        id = "pl.intx.netbeans.module.extools.ExternalToolsAction"
)
@ActionRegistration(
        displayName = "#CTL_ExternalToolsAction", lazy = false
)
@ActionReferences({
    @ActionReference(path = "UI/ToolActions/Files", position = 2051)
})
@NbBundle.Messages("CTL_ExternalToolsAction=Run External Tool")
public class ExternalToolsRunAction extends NodeAction {

    private Lookup context;
    private ExternalTools externalTools;

    public ExternalToolsRunAction() {
        this(Utilities.actionsGlobalContext());
    }

    private ExternalToolsRunAction(Lookup context) {
        putValue(Action.NAME, NbBundle.getMessage(ExternalToolsRunAction.class, "CTL_ExternalToolsAction"));
        this.context = context;
        this.externalTools = new ExternalTools();
    }

    @Override
    public Action createContextAwareInstance(Lookup context) {
        return new ExternalToolsRunAction(context);
    }

    @Override
    public boolean accept(Object sender) {
        return super.accept(sender);
    }

    @Override
    protected void performAction(Node[] nodes) {

        externalTools.loadTools();
        ExternalTool selectedTool = selectTool();
        if (selectedTool == null) {
            return;
        }

        for (Node node : nodes) {
            Lookup lookup = node.getLookup();
            externalTools.execute(selectedTool, lookup.lookup(FileObject.class));
        }
    }

    private ExternalTool selectTool() {
        class Dialog implements Runnable {

            ExternalTool selectedTool;

            public ExternalTool getTool() {
                return selectedTool;
            }

            @Override
            public void run() {
                ExternalToolsRunActionDialog dialog = new ExternalToolsRunActionDialog(externalTools.getTools());
                dialog.setVisible(true);
                selectedTool = dialog.getSelectedItem();
            }
        }

        Dialog dialog = new Dialog();
        try {
            SwingUtilities.invokeAndWait(dialog);
        } catch (InterruptedException | InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        return dialog.getTool();
    }

    @Override
    protected boolean enable(Node[] nodes) {
        if (nodes.length == 1) {
            Lookup lookup = nodes[0].getLookup();
            if (lookup.lookup(FileObject.class) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(ExternalToolsRunAction.class, "CTL_ExternalToolsAction");
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(DEFAULT);
    }

    public static void showMessage(String message) {
        NotifyDescriptor.Message m = new NotifyDescriptor.Message(message);
        DialogDisplayer.getDefault().notify(m);
    }
}
