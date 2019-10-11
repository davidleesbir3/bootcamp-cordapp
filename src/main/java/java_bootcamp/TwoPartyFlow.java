package java_bootcamp;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;

@InitiatingFlow
@StartableByRPC
public class TwoPartyFlow extends FlowLogic<Integer> {
    private Party counterParty;
    private Integer initialNumber;

    public TwoPartyFlow(Party counterParty, Integer initialNumber) {
        this.counterParty = counterParty;
        this.initialNumber = initialNumber;
    }

    @Suspendable
    public Integer call() throws FlowException {
        FlowSession session = initiateFlow(counterParty);
        session.send(initialNumber);
        int incrementedNumber = session.receive(Integer.class).unwrap(it -> it);

        return incrementedNumber;
    }
}
