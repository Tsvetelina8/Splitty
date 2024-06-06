package client.ui;

import client.scenes.MainCtrl;
import com.google.inject.Inject;
import commons.Event;
import commons.Loan;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class DebtsListCell extends ListCell<Loan> {
    private String eventName;
    @FXML
    private HBox hbox;
    @FXML
    private Text information;

    @FXML
    private Button markReceivedButton;

    private Event event;

    private final MainCtrl mainCtrl;

    /**
     * Constructor for DebtsListCell. Sets the necessary visual constraints:
     * allignment, spacings, etc.
     *
     * @param mainCtrl main controller (injection to allow changing
     * @param eventName name of event
     */
    @Inject
    public DebtsListCell(MainCtrl mainCtrl, String eventName) {
        super();

        this.mainCtrl = mainCtrl;
        this.eventName = eventName;

        hbox = new HBox(10);
        information = new Text();
        markReceivedButton = new Button("Mark Received");

        hbox.getChildren().addAll(information, markReceivedButton);
        HBox.setHgrow(information, Priority.ALWAYS);

        information.setTextAlignment(TextAlignment.LEFT);
        markReceivedButton.setTextAlignment(TextAlignment.RIGHT);

        // TO DO: implement Mark Received button
    }

    @Override
    protected void updateItem(Loan loan, boolean empty) {
        super.updateItem(loan, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            information.setText(String.format("%s gives %.2f€ to %s",
                    loan.borrower.firstName, loan.amount, loan.payer.firstName));
            setGraphic(hbox);
        }
    }

    /**
     * Bind this debt cell to an event it belongs to.
     * Used to correctly switch to the required scene on button presses.
     *
     * @param event event the debt belongs to
     * @return itself
     */
    public DebtsListCell attatchEvent(Event event) {
        this.event = event;
        return this;
    }

}
