package ui;


import io.PreferenceHelper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


/**
 * UI.ProxyTab.java
 * Used to display and edit HTTP proxy options in the preferences panel.
 */
public final class ProxyTab extends Tab {

    private static final int PANEL_MARGIN = 20;
    private static final int MINIMUM_MARGIN = 8;

    public ProxyTab() {

        // tab header
        setText(" Proxy ");
        // non-closable
        setClosable(false);

        // load initial proxy enable option
        var proxyEnableOption = PreferenceHelper.getProxyEnableOption();
        // load initial proxy config
        var proxyConfig = PreferenceHelper.getProxyConfig();

        // 3 * 2 layout
        var gridPane = new GridPane();
        gridPane.setHgap(3);
        gridPane.setVgap(2);
        gridPane.setPadding(new Insets(PANEL_MARGIN, PANEL_MARGIN + MINIMUM_MARGIN, PANEL_MARGIN, PANEL_MARGIN));

        var proxyEnableOptionCheckBox = new CheckBox("HTTP Proxy");
        var hostnameTextField = new TextField();
        var portTextField = new TextField();

        proxyEnableOptionCheckBox.setSelected(proxyEnableOption);
        hostnameTextField.setDisable(!proxyEnableOption);
        portTextField.setDisable(!proxyEnableOption);

        proxyEnableOptionCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            PreferenceHelper.setProxyEnableOption(newValue);
            hostnameTextField.setDisable(!newValue);
            portTextField.setDisable(!newValue);
        });

        GridPane.setMargin(proxyEnableOptionCheckBox, new Insets(MINIMUM_MARGIN));
        gridPane.add(proxyEnableOptionCheckBox, 0, 0, 2, 1);

        // add "Host:" label
        var hostLabel = new Label("Host:");
        GridPane.setMargin(hostLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(hostLabel, 0, 1);

        // customise hostname TextFiled
        hostnameTextField.setPromptText("Host");
        hostnameTextField.setText(proxyConfig.getHostname());
        hostnameTextField.setPrefWidth(200);
        GridPane.setMargin(hostnameTextField, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));

        // save to Java Preferences API when hostname is changed
        hostnameTextField.textProperty().addListener((observable, oldValue, newValue) -> PreferenceHelper.setProxyHostname(newValue));

        // moves the caret after the last char of the text
        hostnameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(hostnameTextField::end);
            }
        });

        gridPane.add(hostnameTextField, 1, 1);

        // add "Port" label
        var portLabel = new Label("Port:");
        GridPane.setMargin(portLabel, new Insets(MINIMUM_MARGIN));
        gridPane.add(portLabel, 0, 2);

        // customise port TextFiled
        portTextField.setPromptText("Port");
        portTextField.setText(proxyConfig.getPortAsString());
        portTextField.setMaxWidth(80);
        GridPane.setMargin(portTextField, new Insets(MINIMUM_MARGIN, MINIMUM_MARGIN, MINIMUM_MARGIN, 0));

        // save to Java Preferences API when port number is changed
        portTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                portTextField.setText(newValue.replaceAll("\\D+", ""));
            } else {
                PreferenceHelper.setProxyPort(newValue);
            }
        });

        // moves the caret after the last char of the text
        portTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(portTextField::end);
            }
        });

        gridPane.add(portTextField, 1, 2);

        setContent(gridPane);

    }

}
