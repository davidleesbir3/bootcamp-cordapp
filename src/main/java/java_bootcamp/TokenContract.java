package java_bootcamp;
import net.corda.core.contracts.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/* Our contract, governing how our state will evolve over time.
 * See src/main/java/examples/ArtContract.java for an example. */
public class TokenContract implements Contract{
    public static String ID = "java_bootcamp.TokenContract";

    public interface Commands extends CommandData {
        class Issue implements Commands { }
    }

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        if (tx.getCommands().size() != 1) {
            throw new IllegalArgumentException("Transaction must have exactly one command");
        }

        CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);
        List<PublicKey> requiredSigners = command.getSigners();
        CommandData commandType = command.getValue();

        System.out.println(command.getValue().toString());
        if (command.getValue() instanceof Commands.Issue) {
            // Shape constraints
            if (tx.getInputs().size() > 0) throw new IllegalArgumentException("Issue must have no input states");
            if (tx.getOutputs().size() != 1) throw new IllegalArgumentException("Issue must have exactly one output state");

            // Content constraints
            ContractState outputState = tx.getOutput(0);
            if (!(outputState instanceof TokenState)) throw new IllegalArgumentException("Output must be TokenState");

            TokenState outputToken = (TokenState) outputState;
            if (outputToken.getAmount() <= 0) throw new IllegalArgumentException("In an Issue, amount must be positive");

            // Required signers constraints
            Party tokenIssuer = outputToken.getIssuer();
            if (!requiredSigners.contains(tokenIssuer.getOwningKey())) throw new IllegalArgumentException("Issuer must sign issue");

        } else {
            throw new IllegalArgumentException("Command type not recognized");
        }
    }

}