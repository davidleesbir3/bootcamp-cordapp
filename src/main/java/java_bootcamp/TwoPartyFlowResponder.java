package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatedBy;

@InitiatedBy(TwoPartyFlow.class)
public class TwoPartyFlowResponder extends FlowLogic<Void> {
    private FlowSession counterPartySession;

    public TwoPartyFlowResponder(FlowSession counterPartySession) {
        this.counterPartySession = counterPartySession;
    }

    @Suspendable
    public Void call() throws FlowException {
        int received = counterPartySession.receive(Integer.class).unwrap(it -> {
            if (it > 10) throw new IllegalArgumentException("Number too nigh");
            return it;
        });

        counterPartySession.send(received + 1);

        return null;
    }
}
