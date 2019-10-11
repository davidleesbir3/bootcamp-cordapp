package java_bootcamp;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.identity.Party;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.List;

// Not being used
public class ScratchPad {
    public static void main (String[] args) {
        // How to create a new transaction
        StateAndRef<ContractState> inputState = null;
        HouseState outputState = new HouseState("123 Main St", null);
        List<PublicKey> requiredSigners = ImmutableList.of(outputState.getOwner().getOwningKey());
        Party notary = null;

        TransactionBuilder builder = new TransactionBuilder();
        builder.setNotary(notary);
        builder
                .addInputState(inputState)
                .addOutputState(outputState, "java_bootcamp.HouseContract")
                .addCommand(new HouseContract.Register(), requiredSigners);

    }
}
