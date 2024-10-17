package xyz.duncanruns.jingle.eyesee.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.commons.lang3.tuple.Pair;
import xyz.duncanruns.jingle.eyesee.EyeSee;
import xyz.duncanruns.jingle.eyesee.EyeSeeOptions;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EyeSeePluginPanel {
    public JPanel mainPanel;
    private JCheckBox enableBox;
    private JCheckBox autoBox;
    private JTextField projPosXField;
    private JTextField projPosYField;
    private JTextField projPosWField;
    private JTextField projPosHField;
    private JButton applyButton;
    private JPanel customPositionPanel;
    private JTextField projFpsLimitField;
    private JPanel fpsLimitPanel;
    private JPanel positionPanel;

    public EyeSeePluginPanel() {
        EyeSeeOptions options = EyeSee.getOptions();
        enableBox.addActionListener(a -> {
            options.enabled = enableBox.isSelected();
            reloadEnabledComponents();
        });
        enableBox.setSelected(options.enabled);
        autoBox.addActionListener(a -> {
            Rectangle projectorRect = EyeSee.getProjectorRect();
            options.autoPos = autoBox.isSelected();
            if (options.autoPos) {
                projPosXField.setText(null);
                projPosYField.setText(null);
                projPosWField.setText(null);
                projPosHField.setText(null);
            } else {
                projPosXField.setText(String.valueOf(projectorRect.x));
                projPosYField.setText(String.valueOf(projectorRect.y));
                projPosWField.setText(String.valueOf(projectorRect.width));
                projPosHField.setText(String.valueOf(projectorRect.height));
                options.x = projectorRect.x;
                options.y = projectorRect.y;
                options.w = projectorRect.width;
                options.h = projectorRect.height;
            }
            reloadEnabledComponents();
        });
        autoBox.setSelected(options.autoPos);

        if (!options.autoPos) {
            Rectangle projectorRect = EyeSee.getProjectorRect();
            projPosXField.setText(String.valueOf(projectorRect.x));
            projPosYField.setText(String.valueOf(projectorRect.y));
            projPosWField.setText(String.valueOf(projectorRect.width));
            projPosHField.setText(String.valueOf(projectorRect.height));
        }

        projFpsLimitField.setText(String.valueOf(options.fpsLimit));

        applyButton.addActionListener(a -> {
            int x = getIntFromField(projPosXField, 0);
            options.x = x;
            projPosXField.setText(String.valueOf(x));

            int y = getIntFromField(projPosYField, 0);
            options.y = y;
            projPosYField.setText(String.valueOf(y));

            int w = getIntFromField(projPosWField, 0);
            options.w = w;
            projPosWField.setText(String.valueOf(w));

            int h = getIntFromField(projPosHField, 0);
            options.h = h;
            projPosHField.setText(String.valueOf(h));
        });

        Arrays.<Pair<JTextField, IntSupplier>>asList(
                Pair.of(projPosXField, () -> options.x),
                Pair.of(projPosYField, () -> options.y),
                Pair.of(projPosWField, () -> options.w),
                Pair.of(projPosHField, () -> options.h)
        ).forEach(p -> p.getLeft().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                p.getLeft().setText(String.valueOf(p.getRight().getAsInt()));
            }
        }));

        projFpsLimitField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                projFpsLimitField.setText(String.valueOf(EyeSee.getOptions().fpsLimit));
            }
        });

        projFpsLimitField.getDocument().addDocumentListener(new DocumentListener() {
            private void update() {
                EyeSee.getOptions().fpsLimit = clamp(getIntFromField(projFpsLimitField, 30), 5, 240);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        reloadEnabledComponents();
    }

    private static int getIntFromField(JTextField field, int onEmpty) {
        String text = field.getText().trim();
        boolean isNegative = text.startsWith("-");
        String numbers = getNumbersFromString(text);
        return numbers.isEmpty() ? onEmpty : (isNegative ? -1 : 1) * Integer.parseInt(numbers);
    }

    private static String getNumbersFromString(String text) {
        char[] charArray = text.toCharArray();
        return IntStream.range(0, charArray.length).mapToObj(ci -> charArray[ci])
                .filter(Character::isDigit)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static int clamp(int i, int min, int max) {
        return Math.max(min, Math.min(max, i));
    }

    private void reloadEnabledComponents() {
        EyeSeeOptions options = EyeSee.getOptions();
        for (Component component : customPositionPanel.getComponents()) {
            component.setEnabled(options.enabled && !options.autoPos);
        }
        autoBox.setEnabled(options.enabled);
        for (Component component : fpsLimitPanel.getComponents()) {
            component.setEnabled(options.enabled);
        }

        /*
         *  int fps = clamp(getIntFromField(projFpsLimitField), 5, 240); // prevent small/huge fps values
         *  options.fpsLimit = fps;
         *  projFpsLimitField.setText(String.valueOf(fps));
         */
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(6, 1, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        enableBox = new JCheckBox();
        enableBox.setText("Enable EyeSee Measuring Projector");
        mainPanel.add(enableBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("<html> <h2>Warning</h2> The EyeSee plugin provides a measuring projector that works without the use of OBS.<br> It's possible this projector will fail to work for any of the following reasons: <ul> <li>Usage of an AMD GPU</li> <li>Being on a Laptop</li> <li>Certain hardware configurations</li> <li>Not being blessed by the Jingle God</li> </ul> If you can't get the EyeSee projector to work, try using an OBS projector (check out the OBS tab). </html>");
        mainPanel.add(label1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        mainPanel.add(separator1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fpsLimitPanel = new JPanel();
        fpsLimitPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(fpsLimitPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("FPS Limit:");
        fpsLimitPanel.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        projFpsLimitField = new JTextField();
        projFpsLimitField.setText("");
        fpsLimitPanel.add(projFpsLimitField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(60, -1), null, 0, false));
        positionPanel = new JPanel();
        positionPanel.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        mainPanel.add(positionPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        positionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        autoBox = new JCheckBox();
        autoBox.setText("Automatically Position EyeSee Measuring Projector");
        positionPanel.add(autoBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customPositionPanel = new JPanel();
        customPositionPanel.setLayout(new GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        positionPanel.add(customPositionPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        customPositionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label3 = new JLabel();
        label3.setText("Position:");
        customPositionPanel.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        projPosXField = new JTextField();
        projPosXField.setText("");
        customPositionPanel.add(projPosXField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        projPosYField = new JTextField();
        projPosYField.setText("");
        customPositionPanel.add(projPosYField, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Size:");
        customPositionPanel.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        projPosWField = new JTextField();
        projPosWField.setText("");
        customPositionPanel.add(projPosWField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        projPosHField = new JTextField();
        projPosHField.setText("");
        customPositionPanel.add(projPosHField, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), null, 0, false));
        applyButton = new JButton();
        applyButton.setText("Apply");
        customPositionPanel.add(applyButton, new GridConstraints(0, 3, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
