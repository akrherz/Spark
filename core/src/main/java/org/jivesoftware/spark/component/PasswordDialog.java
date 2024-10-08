/**
 * Copyright (C) 2004-2011 Jive Software. All rights reserved.
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
package org.jivesoftware.spark.component;

import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.SparkManager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import org.jivesoftware.Spark;
import org.jivesoftware.spark.util.Encryptor;
import org.jivesoftware.spark.util.ResourceUtils;
import org.jivesoftware.spark.util.log.Log;
import org.jivesoftware.sparkimpl.settings.local.LocalPreferences;
import org.jivesoftware.sparkimpl.settings.local.SettingsManager;


/**
 * <code>PasswordDialog</code> class is used to retrieve information from a user.
 *
 * @author Derek DeMoro
 */
public final class PasswordDialog implements PropertyChangeListener {
    private JPasswordField passwordField;
    private JOptionPane optionPane;
    private JDialog dialog;

    private final JCheckBox _savePasswordBox = new JCheckBox();
    private String stringValue;
    private int width = 400;
    private int height = 200;
    private String password;
    public void setPasswordField(String password)
    {
        this.password=password;
    }
    public boolean isCheckboxSelected()
    {
        return _savePasswordBox.isSelected();
    }
    public  void savePassword(String roomName, String password)  
    {
        File sparkProperties = new File(Spark.getSparkUserHome().concat(File.separator).concat(SettingsManager.getSettingsFile().getName()));
        Properties props = new Properties();
        try {
        	props.load(new FileInputStream(sparkProperties));
        	} catch (Exception e) {
        		Log.error("error with file"+ e.getCause());
        	}
        LocalPreferences preferences  = new LocalPreferences(props);
        try {
        	preferences.setGroupChatPassword(roomName,Encryptor.encrypt(password));
        } catch (Exception ex) {
        	Log.error(ex.getCause());
        }
        FileOutputStream fileOut;
        try {
        	fileOut = new FileOutputStream(sparkProperties);
        	props.store(fileOut, "added room");
        	} catch (Exception ex) {
        		Log.error(ex.getCause());
        	}                    
		}

    /**
     * Empty Constructor.
     */
    public PasswordDialog() {
    }

    /**
     * Returns the input from a user.
     *
     * @param title       the title of the dialog.
     * @param description the dialog description.
     * @param icon        the icon to use.
     * @param width       the dialog width
     * @param height      the dialog height
     * @return the users input.
     */
    public String getInput(String title, String description, Icon icon, int width, int height) {
        this.width = width;
        this.height = height;

        return getPassword(title, description, icon, SparkManager.getMainWindow());
    }

    /**
     * Prompt and return input.
     *
     * @param title       the title of the dialog.
     * @param description the dialog description.
     * @param icon        the icon to use.
     * @param parent      the parent to use.
     * @return the user input.
     */
    public String getPassword(String title, String description, Icon icon, Component parent) {
        passwordField = new JPasswordField();
        passwordField.setText(password);

        TitlePanel titlePanel = new TitlePanel(title, description, icon, true);

        // Construct main panel w/ layout.
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        final JPanel passwordPanel = new JPanel(new GridBagLayout());
        JLabel passwordLabel = new JLabel(Res.getString("label.enter.password") + ":");
        passwordPanel.add(passwordLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        passwordPanel.add(passwordField, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

        //user should be able to close this dialog (with an option to save room's password)
        passwordPanel.add(_savePasswordBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        ResourceUtils.resButton(_savePasswordBox, Res.getString("checkbox.save.password"));
        final Object[] options = {Res.getString("ok"), Res.getString("cancel") , };
        optionPane = new JOptionPane(passwordPanel, JOptionPane.PLAIN_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION, null, options, options[0]);

        mainPanel.add(optionPane, BorderLayout.CENTER);

        // Lets make sure that the dialog is modal. Cannot risk people
        // losing this dialog.
        JOptionPane p = new JOptionPane();
        dialog = p.createDialog(parent, title);
        dialog.setModal(true);
        dialog.pack();
        dialog.setSize(width, height);
        dialog.setContentPane(mainPanel);
        dialog.setLocationRelativeTo(parent);
        optionPane.addPropertyChangeListener(this);

        // Add Key Listener to Send Field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
			public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_TAB) {
                    optionPane.requestFocus();
                }
                else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
                    dialog.dispose();
                }
            }
        });

        passwordField.requestFocus();


        dialog.setVisible(true);
        return stringValue;
    }

    /**
     * Move to focus forward action.
     */
    public Action nextFocusAction = new AbstractAction(Res.getString("label.move.focus.forwards")) {
		private static final long serialVersionUID = 6465350147231073505L;

		@Override
		public void actionPerformed(ActionEvent evt) {
            ((Component)evt.getSource()).transferFocus();
        }
    };

    /**
     * Moves the focus backwards in the dialog.
     */
    public Action prevFocusAction = new AbstractAction(Res.getString("label.move.focus.backwards")) {
		private static final long serialVersionUID = -91177056113094990L;

		@Override
		public void actionPerformed(ActionEvent evt) {
            ((Component)evt.getSource()).transferFocusBackward();
        }
    };

    @Override
	public void propertyChange(PropertyChangeEvent e) {
        String value = (String)optionPane.getValue();
        if (Res.getString("cancel").equals(value)) {
            stringValue = null;
            dialog.setVisible(false);
        }
        else if (Res.getString("ok").equals(value)) {
            stringValue = String.valueOf(passwordField.getPassword()).trim();
            if (stringValue.isEmpty()) {
                stringValue = null;
            }
            dialog.setVisible(false);
        }
    }
}
