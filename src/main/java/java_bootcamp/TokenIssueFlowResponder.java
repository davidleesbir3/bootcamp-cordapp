package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import java_examples.ArtTransferFlowInitiator;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;

// `InitiatedBy` means that we will start this flow in response to a
// message from `ArtTransferFlow.Initiator`.
@InitiatedBy(TokenIssueFlow.class)
public class TokenIssueFlowResponder extends FlowLogic<Void> {
    private final FlowSession counterpartySession;

    // Responder flows always have a single constructor argument - a
    // `FlowSession` with the counterparty who initiated the flow.
    public TokenIssueFlowResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {

        subFlow(new ReceiveFinalityFlow(this.counterpartySession));
        return null;
    }
}